package se.dosf.communitybase.modules.filearchive.migration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;

public class FileArchiveDataMigrator {

	protected final DataSource ds;

	protected final Map<Integer, Integer> fileArchiveGroupSections = new HashMap<Integer, Integer>();
	protected final Map<Integer, Integer> fileArchiveSchoolSections = new HashMap<Integer, Integer>();

	private Logger logger;

	public FileArchiveDataMigrator(DataSource ds, Logger logger) {
		this.ds = ds;
		this.logger = logger;
	}

	private void log(String message) {
		this.log(Level.INFO, message, null);
	}

	private void log(String message, Throwable t) {
		this.log(Level.INFO, message, t);
	}

	private void log(Level level, String message, Throwable t) {
		if(this.logger != null) {
			if(t != null){
				this.logger.log(level, message, t);
			}
			this.logger.log(level, message);
		} else {
			System.out.println(message);
			if(t != null){
				t.printStackTrace();
			}
		}
	}

	public void run(boolean commit) throws Exception {

		Connection connection = null;
		int count = 0;
		long memento;
		long startTime = System.currentTimeMillis();

		try {


			/**
			 * 
			 * Start Migration
			 * 
			 */
			this.log("Data migration started. Retreiving connection");
			connection = ds.getConnection();
			connection.setAutoCommit(false);

			if(commit) {
				this.log("Note: This is NOT a test. Any changes will be committed!");
			} else {
				this.log("Note: This is a test run. Any changes will NOT be committed!");
			}


			/**
			 * 
			 *  Validate empty tables
			 * 
			 *  Note: The other tables has constraints on filearchive_sections,
			 *  hence it's sufficient to only check for data in filearchive_sections
			 * 
			 */
			// Prepare read statement
			PreparedStatement pstmt1 = connection.prepareStatement("SELECT sectionID FROM filearchive_sections");

			// Execute
			ResultSet rs1 = pstmt1.executeQuery();
			rs1.last();
			if(rs1.getRow() != 0) {
				throw new Exception("Target tables are not empty! Data migration has already been run?");
			}

			DBUtils.closeResultSet(rs1);
			DBUtils.closePreparedStatement(pstmt1);

			/**
			 * 
			 * STEP 1 - Copy sections from group sections
			 * 
			 */
			this.log("STEP 1 - Copying sections from group sections..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt2 = connection.prepareStatement("SELECT sectionID, name FROM filearchivegroupsections");

			// Execute
			ResultSet rs2 = pstmt2.executeQuery();
			rs2.last();
			this.log("STEP 1 - " + rs2.getRow() + " rows were read");
			rs2.beforeFirst();

			count = 0;

			while(rs2.next()) {

				// Prepare write statement
				PreparedStatement nestedPstmt = connection.prepareStatement("INSERT INTO filearchive_sections (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

				// Set name from old section
				nestedPstmt.setString(1, rs2.getString("name"));

				// Execute
				count += nestedPstmt.executeUpdate();

				// Create mapping, old section id -> new section id
				ResultSet keys = nestedPstmt.getGeneratedKeys();
				if(keys != null && keys.next()) {
					this.fileArchiveGroupSections.put(rs2.getInt("sectionID"), keys.getInt(1));
				} else {
					throw new Exception("Error mapping primary keys!");
				}

				DBUtils.closeResultSet(keys);
				DBUtils.closePreparedStatement(nestedPstmt);
			}

			DBUtils.closeResultSet(rs2);
			DBUtils.closePreparedStatement(pstmt2);
			rs2 = null;
			pstmt2 = null;

			this.log("STEP 1 - " + count + " rows were written");
			this.log("STEP 1 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 2 - Copy sections from school sections
			 * 
			 */
			this.log("STEP 2 - Copying sections from school sections..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt3 = connection.prepareStatement("SELECT sectionID, name FROM filearchiveschoolsections");

			// Execute
			ResultSet rs3 = pstmt3.executeQuery();
			rs3.last();
			this.log("STEP 2 - " + rs3.getRow() + " rows were read");
			rs3.beforeFirst();

			count = 0;

			while(rs3.next()) {

				// Prepare write statement
				PreparedStatement nestedPstmt = connection.prepareStatement("INSERT INTO filearchive_sections (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

				// Set name from old section
				nestedPstmt.setString(1, rs3.getString("name"));

				// Execute
				count += nestedPstmt.executeUpdate();

				// Create mapping, old section id -> new section id
				ResultSet keys = nestedPstmt.getGeneratedKeys();
				if(keys != null && keys.next()) {
					this.fileArchiveSchoolSections.put(rs3.getInt("sectionID"), keys.getInt(1));
				} else {
					throw new Exception("Error mapping primary keys!");
				}

				DBUtils.closeResultSet(keys);
				DBUtils.closePreparedStatement(nestedPstmt);
			}

			DBUtils.closeResultSet(rs3);
			DBUtils.closePreparedStatement(pstmt3);
			rs3 = null;
			pstmt3 = null;

			this.log("STEP 2 - " + count + " rows were written");
			this.log("STEP 2 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 3 - Determine and set global flag on sections
			 * 
			 */
			this.log("STEP 3 - Determining and setting global flag on sections..");

			memento = System.currentTimeMillis();

			// Prepare read/write statement
			String sql4 = "UPDATE filearchive_sections SET `global` = 1 " +
					"WHERE filearchive_sections.sectionID IN " +
					"(SELECT sectionID FROM " +
					"(SELECT sectionID, count(schoolID) as nr FROM " +
					"filearchiveschoolsections GROUP BY sectionID) as a " +
					"WHERE a.nr = (SELECT count(schoolID) " +
					"FROM schools)" +
					")";
			PreparedStatement pstmt4 = connection.prepareStatement(sql4);

			// Execute
			count = pstmt4.executeUpdate();

			DBUtils.closePreparedStatement(pstmt4);

			this.log("STEP 3 - " + count + " flags were set");
			this.log("STEP 3 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 4 - Copy school mappings (only for non global sections)
			 * 
			 */
			this.log("STEP 4 - Copying school mappings (only for non global sections)..");

			memento = System.currentTimeMillis();

			// Prepare first read statement (get new keys for non global sections)
			PreparedStatement pstmt5 = connection.prepareStatement("SELECT sectionID FROM filearchive_sections WHERE global = 0");

			// Execute
			ResultSet rs5 = pstmt5.executeQuery();

			List<Integer> oldKeysForNonGlobalSections = new ArrayList<Integer>();
			while(rs5.next()) {
				for(Entry<Integer, Integer> entry : this.fileArchiveSchoolSections.entrySet()) {
					if(entry.getValue().equals(rs5.getInt("sectionID"))) {
						oldKeysForNonGlobalSections.add(entry.getKey());
					}
				}
			}

			DBUtils.closeResultSet(rs5);
			DBUtils.closePreparedStatement(pstmt5);

			rs5 = null;
			pstmt5 = null;

			// Prepare second read statement (Get mappings for non global sections)
			if(!oldKeysForNonGlobalSections.isEmpty()) {
			
				String sql6 = "SELECT schoolID, sectionID FROM filearchiveschoolsections " +
						"WHERE sectionID IN (" + StringUtils.toCommaSeparatedString(oldKeysForNonGlobalSections)  + ")";
				PreparedStatement pstmt6 = connection.prepareStatement(sql6);
	
				// Execute
				ResultSet rs6 = pstmt6.executeQuery();
				rs6.last();
				this.log("STEP 4 - " + rs6.getRow() + " rows were read");
				rs6.beforeFirst();
	
				count = 0;
	
				while(rs6.next()) {
	
					// Prepare write statement
					PreparedStatement nestedPstmt = connection.prepareStatement("INSERT INTO filearchive_sectionschools (schoolID, sectionID) VALUES (?,?)");
	
					// Set school id from old mapping
					nestedPstmt.setInt(1, rs6.getInt("filearchiveschoolsections.schoolID"));
	
					// Set new section id
					nestedPstmt.setInt(2, this.fileArchiveSchoolSections.get(rs6.getInt("filearchiveschoolsections.sectionID")));
	
					// Execute
					count += nestedPstmt.executeUpdate();
	
					DBUtils.closePreparedStatement(nestedPstmt);
				}
	
				DBUtils.closeResultSet(rs6);
				DBUtils.closePreparedStatement(pstmt6);
	
				rs6 = null;
				pstmt6 = null;
			
			} else {
				this.log("STEP 4 - 0 rows were read");
			}

			this.log("STEP 4 - " + count + " rows were written");
			this.log("STEP 4 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 5 - Copy group mappings (should not exist if section is global)
			 * 
			 */
			this.log("STEP 5 - Copying group mappings (should not exist if section is global)..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt7 = connection.prepareStatement("SELECT groupID, sectionID FROM filearchivegroupsections");

			// Execute
			ResultSet rs7 = pstmt7.executeQuery();
			rs7.last();
			this.log("STEP 5 - " + rs7.getRow() + " rows were read");
			rs7.beforeFirst();

			count = 0;

			while(rs7.next()) {

				// Prepare write statement
				PreparedStatement nestedPstmt = connection.prepareStatement("INSERT INTO filearchive_sectiongroups (groupID, sectionID) VALUES (?,?)");

				// Set group id from old mapping
				nestedPstmt.setInt(1, rs7.getInt("groupID"));

				// Set new section id
				nestedPstmt.setInt(2, this.fileArchiveGroupSections.get(rs7.getInt("sectionID")));

				// Execute
				count += nestedPstmt.executeUpdate();

				DBUtils.closePreparedStatement(nestedPstmt);
			}

			DBUtils.closeResultSet(rs7);
			DBUtils.closePreparedStatement(pstmt7);

			rs7 = null;
			pstmt7 = null;

			this.log("STEP 5 - " + count + " rows were written");
			this.log("STEP 5 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 6 - Copy files from group files
			 * 
			 */
			this.log("STEP 6 - Copying files from group files..");

			memento = System.currentTimeMillis();

			// Prepare index read statement
			PreparedStatement pstmt8 = connection.prepareStatement("SELECT fileID FROM filearchivegroupfiles");

			// Execute
			ResultSet rs8 = pstmt8.executeQuery();
			rs8.last();
			this.log("STEP 6 - " + rs8.getRow() + " rows were read");
			rs8.beforeFirst();

			count = 0;

			// Loop each index
			while(rs8.next()) {

				// Prepare a full read statement (per index)
				PreparedStatement nestedPstmt = connection.prepareStatement("SELECT fileID, sectionID, description, file, filename, postedBy, fileSize, posted FROM filearchivegroupfiles WHERE fileID = ?");
				nestedPstmt.setInt(1, rs8.getInt("fileID"));

				// Execute
				ResultSet nestedRS = nestedPstmt.executeQuery();

				if(nestedRS.next()) {

					// Prepare write statement
					PreparedStatement nestedNestedPstmt = connection.prepareStatement("INSERT INTO filearchive_files (sectionID, description, file, filename, postedBy, fileSize, posted) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

					// Set new section id
					nestedNestedPstmt.setInt(1, this.fileArchiveGroupSections.get(nestedRS.getInt("sectionID")));

					// Set the rest from old file
					nestedNestedPstmt.setString(2, nestedRS.getString("description"));
					nestedNestedPstmt.setBlob(3, nestedRS.getBlob("file"));
					nestedNestedPstmt.setString(4, nestedRS.getString("filename"));
					nestedNestedPstmt.setInt(5, nestedRS.getInt("postedBy"));
					nestedNestedPstmt.setInt(6, nestedRS.getInt("fileSize"));
					nestedNestedPstmt.setTimestamp(7, nestedRS.getTimestamp("posted"));

					// Execute
					count += nestedNestedPstmt.executeUpdate();

					DBUtils.closePreparedStatement(nestedNestedPstmt);
				}

				DBUtils.closeResultSet(nestedRS);
				DBUtils.closePreparedStatement(nestedPstmt);
			}

			DBUtils.closeResultSet(rs8);
			DBUtils.closePreparedStatement(pstmt8);

			rs8 = null;
			pstmt8 = null;

			this.log("STEP 6 - " + count + " rows were written");
			this.log("STEP 6 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 7 - Copy files from school files
			 * 
			 */
			this.log("STEP 7 - Copying files from school files..");

			memento = System.currentTimeMillis();

			// Prepare index read statement
			PreparedStatement pstmt9 = connection.prepareStatement("SELECT fileID FROM filearchiveschoolfiles");

			// Execute
			ResultSet rs9 = pstmt9.executeQuery();
			rs9.last();
			this.log("STEP 7 - " + rs9.getRow() + " rows were read");
			rs9.beforeFirst();

			count = 0;

			// Loop each index
			while(rs9.next()) {

				// Prepare a full read statement (per index)
				PreparedStatement nestedPstmt = connection.prepareStatement("SELECT fileID, sectionID, description, file, filename, postedBy, fileSize, posted FROM filearchiveschoolfiles WHERE fileID = ?");
				nestedPstmt.setInt(1, rs9.getInt("fileID"));

				// Execute
				ResultSet nestedRS = nestedPstmt.executeQuery();

				if(nestedRS.next()) {

					// Prepare write statement
					PreparedStatement nestedNestedPstmt = connection.prepareStatement("INSERT INTO filearchive_files (sectionID, description, file, filename, postedBy, fileSize, posted) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

					// Set new section id
					nestedNestedPstmt.setInt(1, this.fileArchiveSchoolSections.get(nestedRS.getInt("sectionID")));

					// Set the rest from old file
					nestedNestedPstmt.setString(2, nestedRS.getString("description"));
					nestedNestedPstmt.setBlob(3, nestedRS.getBlob("file"));
					nestedNestedPstmt.setString(4, nestedRS.getString("filename"));
					nestedNestedPstmt.setInt(5, nestedRS.getInt("postedBy"));
					nestedNestedPstmt.setInt(6, nestedRS.getInt("fileSize"));
					nestedNestedPstmt.setTimestamp(7, nestedRS.getTimestamp("posted"));

					// Execute
					count += nestedNestedPstmt.executeUpdate();

					DBUtils.closePreparedStatement(nestedNestedPstmt);
				}

				DBUtils.closeResultSet(nestedRS);
				DBUtils.closePreparedStatement(nestedPstmt);
			}

			DBUtils.closeResultSet(rs9);
			DBUtils.closePreparedStatement(pstmt9);

			this.log("STEP 7 - " + count + " rows were written");
			this.log("STEP 7 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");

			rs9 = null;
			pstmt9 = null;


			/**
			 * 
			 * Commit Migration
			 * 
			 */
			this.log("COMPLETE - All steps completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - startTime) + " ms");
			if(commit) {
				this.log("Committing changes!");
				connection.commit();
			}


		} catch(Throwable e) {
			this.log("Error during migration. Executing rollback! (count " + count + ")");
			try {
				connection.rollback();
			} catch (SQLException e1) {
				this.log("Error during rollback!!", e1);
			}

			DBUtils.closeConnection(connection);

			throw new Exception(e);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Setup datasource
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUsername("root");
		ds.setPassword("root");
		ds.setUrl("jdbc:mysql://127.0.0.1:3306/communitybase_test");
		ds.setLogAbandoned(true);
		ds.setRemoveAbandoned(true);
		ds.setRemoveAbandonedTimeout(30);
		ds.setTestOnBorrow(true);
		ds.setValidationQuery("SELECT 1");
		ds.setMaxActive(20);
		ds.setMaxIdle(10);
		ds.setMaxWait(10);
		ds.setMinIdle(0);

		FileArchiveDataMigrator migrator = new FileArchiveDataMigrator(ds, null);

		try {
			if(args.length > 0) {
				migrator.run(Boolean.parseBoolean(args[0]));
			} else {
				migrator.run(false);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
}
