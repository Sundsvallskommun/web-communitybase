package se.dosf.communitybase.modules.linkarchive;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.time.TimeUtils;

public class LinkArchiveDataMigrator {

	protected final DataSource ds;

	protected final Map<Integer, Integer> linkArchiveSchoolLinks = new HashMap<Integer, Integer>();
	protected final Map<Integer, Integer> linkArchiveSchoolSections = new HashMap<Integer, Integer>();

	private Logger logger;

	public LinkArchiveDataMigrator(DataSource ds, Logger logger) {
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
			 *  Note: The other tables has constraints on linkarchive_links,
			 *  hence it's sufficient to only check for data in linkarchive_links
			 * 
			 */
			// Prepare read statement
			PreparedStatement pstmt1 = connection.prepareStatement("SELECT linkID FROM linkarchive_links");

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
			 * STEP 1 - Copy global links
			 * 
			 */
			this.log("STEP 1 - Copying global links..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt2 = connection.prepareStatement("SELECT links.* FROM links, globallinks WHERE links.linkID = globallinks.linkID");

			// Execute
			ResultSet rs2 = pstmt2.executeQuery();
			rs2.last();
			this.log("STEP 1 - " + rs2.getRow() + " rows were read");
			rs2.beforeFirst();

			count = 0;

			while(rs2.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO linkarchive_links (url, description, posted, global, poster) VALUES (?, ?, ?, ?, ?)");

				// Set values from old link
				pstmt.setString(1, rs2.getString("url"));
				pstmt.setString(2, rs2.getString("description"));
				pstmt.setTimestamp(3, rs2.getTimestamp("posted"));
				pstmt.setInt(4, 1);
				pstmt.setInt(5, rs2.getInt("posterID"));

				// Execute
				count += pstmt.executeUpdate();

				DBUtils.closePreparedStatement(pstmt);
			}

			DBUtils.closeResultSet(rs2);
			DBUtils.closePreparedStatement(pstmt2);

			this.log("STEP 1 - " + count + " rows were written");
			this.log("STEP 1 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");

			
			/**
			 * 
			 * STEP 2 - Copy school links
			 * 
			 */
			this.log("STEP 2 - Copying school links..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt3 = connection.prepareStatement("SELECT links.*, schoollinks.schoolID FROM links, schoollinks WHERE links.linkID = schoollinks.linkID");

			// Execute
			ResultSet rs3 = pstmt3.executeQuery();
			rs3.last();
			this.log("STEP 2 - " + rs3.getRow() + " rows were read");
			rs3.beforeFirst();

			count = 0;

			while(rs3.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO linkarchive_links (url, description, posted, poster) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

				// Set values from old link
				pstmt.setString(1, rs3.getString("url"));
				pstmt.setString(2, rs3.getString("description"));
				pstmt.setTimestamp(3, rs3.getTimestamp("posted"));
				pstmt.setInt(4, rs3.getInt("posterID"));

				// Execute
				count += pstmt.executeUpdate();

				ResultSet keys = pstmt.getGeneratedKeys();
				
				// Write mapping
				if(keys != null && keys.next()) {
					
					// Prepare write statement
					PreparedStatement innerPstmt = connection.prepareStatement("INSERT INTO linkarchive_linkschools (schoolID, linkID) VALUES (?, ?)");
					
					innerPstmt.setInt(1, rs3.getInt("schoolID"));
					innerPstmt.setInt(2, keys.getInt(1));
					
					innerPstmt.executeUpdate();
					
					DBUtils.closePreparedStatement(innerPstmt);
					
				} else {
					throw new Exception("Error mapping primary keys!");
				}

				DBUtils.closeResultSet(keys);
				DBUtils.closePreparedStatement(pstmt);
			}

			DBUtils.closeResultSet(rs3);
			DBUtils.closePreparedStatement(pstmt3);

			this.log("STEP 2 - " + count + " rows were written");
			this.log("STEP 2 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");
			
			
			
			/**
			 * 
			 * STEP 3 - Copy group links
			 * 
			 */
			this.log("STEP 3 - Copying group links..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt4 = connection.prepareStatement("SELECT links.*, linkgroups.groupID FROM links, linkgroups WHERE links.linkID = linkgroups.linkID");

			// Execute
			ResultSet rs4 = pstmt4.executeQuery();
			rs4.last();
			this.log("STEP 3 - " + rs4.getRow() + " rows were read");
			rs4.beforeFirst();

			count = 0;

			while(rs4.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO linkarchive_links (url, description, posted, poster) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

				// Set values from old link
				pstmt.setString(1, rs4.getString("url"));
				pstmt.setString(2, rs4.getString("description"));
				pstmt.setTimestamp(3, rs4.getTimestamp("posted"));
				pstmt.setInt(4, rs4.getInt("posterID"));

				// Execute
				count += pstmt.executeUpdate();

				ResultSet keys = pstmt.getGeneratedKeys();
				
				// Write mapping
				if(keys != null && keys.next()) {
					
					// Prepare write statement
					PreparedStatement innerPstmt = connection.prepareStatement("INSERT INTO linkarchive_linkgroups (groupID, linkID) VALUES (?, ?)");
					
					innerPstmt.setInt(1, rs4.getInt("groupID"));
					innerPstmt.setInt(2, keys.getInt(1));
					
					innerPstmt.executeUpdate();
					
					DBUtils.closePreparedStatement(innerPstmt);
					
				} else {
					throw new Exception("Error mapping primary keys!");
				}

				DBUtils.closeResultSet(keys);
				DBUtils.closePreparedStatement(pstmt);
			}

			DBUtils.closeResultSet(rs4);
			DBUtils.closePreparedStatement(pstmt4);

			this.log("STEP 3 - " + count + " rows were written");
			this.log("STEP 3 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");
			
			
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

		LinkArchiveDataMigrator migrator = new LinkArchiveDataMigrator(ds, null);

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
