package se.dosf.communitybase.modules.pictureGallery.migration;

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

public class PictureGalleryDataMigrator {

	protected final DataSource ds;

	protected final Map<Integer, Integer> pictureGalleryGroupGalleries = new HashMap<Integer, Integer>();
	protected final Map<Integer, Integer> pictureGallerySchoolGalleries = new HashMap<Integer, Integer>();
	protected final Map<Integer, Integer> pictureGalleryGroupPictures = new HashMap<Integer, Integer>();
	protected final Map<Integer, Integer> pictureGallerySchoolPictures = new HashMap<Integer, Integer>();

	private Logger logger;

	public PictureGalleryDataMigrator(DataSource ds, Logger logger) {
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
		int count;
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
			 *  Note: The other tables has constraints on picturegallery_galleries,
			 *  hence it's sufficient to only check for data in picturegallery_galleries
			 * 
			 */
			// Prepare read statement
			PreparedStatement pstmt1 = connection.prepareStatement("SELECT galleryID FROM picturegallery_galleries");

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
			 * STEP 1 - Copy galleries from group galleries
			 * 
			 */
			this.log("STEP 1 - Copying galleries from group galleries..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt2 = connection.prepareStatement("SELECT sectionID, name, description, posted FROM picturegallerygroupsections");

			// Execute
			ResultSet rs2 = pstmt2.executeQuery();
			rs2.last();
			this.log("STEP 1 - " + rs2.getRow() + " rows were read");
			rs2.beforeFirst();

			count = 0;

			while(rs2.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO picturegallery_galleries (name, description, posted) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

				// Set name, description posted from old gallery
				pstmt.setString(1, rs2.getString("name"));
				pstmt.setString(2, rs2.getString("description"));
				pstmt.setTimestamp(3, rs2.getTimestamp("posted"));

				// Execute
				count += pstmt.executeUpdate();

				// Create mapping, old gallery id -> new gallery id
				ResultSet keys = pstmt.getGeneratedKeys();
				if(keys != null && keys.next()) {
					this.pictureGalleryGroupGalleries.put(rs2.getInt("sectionID"), keys.getInt(1));
				} else {
					throw new Exception("Error mapping primary keys!");
				}
				
				DBUtils.closeResultSet(keys);
				DBUtils.closePreparedStatement(pstmt);
			}
			
			DBUtils.closeResultSet(rs2);
			DBUtils.closePreparedStatement(pstmt2);
			
			this.log("STEP 1 - " + count + " rows were written");
			this.log("STEP 1 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 2 - Copy galleries from school galleries
			 * 
			 */
			this.log("STEP 2 - Copying galleries from school galleries..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt3 = connection.prepareStatement("SELECT sectionID, name, description, posted FROM picturegalleryschoolsections");

			// Execute
			ResultSet rs3 = pstmt3.executeQuery();
			rs3.last();
			this.log("STEP 2 - " + rs3.getRow() + " rows were read");
			rs3.beforeFirst();

			count = 0;

			while(rs3.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO picturegallery_galleries (name, description, posted) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

				// Set name from old gallery
				pstmt.setString(1, rs3.getString("name"));
				pstmt.setString(2, rs3.getString("description"));
				pstmt.setTimestamp(3, rs3.getTimestamp("posted"));

				// Execute
				count += pstmt.executeUpdate();

				// Create mapping, old gallery id -> new gallery id
				ResultSet keys = pstmt.getGeneratedKeys();
				if(keys != null && keys.next()) {
					this.pictureGallerySchoolGalleries.put(rs3.getInt("sectionID"), keys.getInt(1));
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
			 * STEP 3 - Determine and set global flag on galleries
			 * 
			 */
			this.log("STEP 3 - Determining and setting global flag on galleries..");

			memento = System.currentTimeMillis();

			// Prepare read/write statement
			String sql = "UPDATE picturegallery_galleries SET `global` = 1 " +
					"WHERE picturegallery_galleries.galleryID IN " +
					"(SELECT sectionID FROM " +
					"(SELECT sectionID, count(schoolID) as nr FROM " +
					"picturegalleryschoolsections GROUP BY sectionID) as a " +
					"WHERE a.nr = (SELECT count(schoolID) " +
					"FROM schools)" +
					")";
			PreparedStatement pstmt4 = connection.prepareStatement(sql);

			// Execute
			count = pstmt4.executeUpdate();

			DBUtils.closePreparedStatement(pstmt4);
			
			this.log("STEP 3 - " + count + " flags were set");
			this.log("STEP 3 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 4 - Copy school mappings (only for non global galleries)
			 * 
			 */
			this.log("STEP 4 - Copying school mappings (only for non global galleries)..");

			memento = System.currentTimeMillis();

			// Prepare first read statement (get new keys for non global galleries)
			PreparedStatement pstmt5 = connection.prepareStatement("SELECT galleryID FROM picturegallery_galleries WHERE global = 0");

			// Execute
			ResultSet rs5 = pstmt5.executeQuery();

			List<Integer> oldKeysForNonGlobalGalleries = new ArrayList<Integer>();
			while(rs5.next()) {
				for(Entry<Integer, Integer> entry : this.pictureGallerySchoolGalleries.entrySet()) {
					if(entry.getValue().equals(rs5.getInt("galleryID"))) {
						oldKeysForNonGlobalGalleries.add(entry.getKey());
					}
				}
			}
			
			DBUtils.closeResultSet(rs5);
			DBUtils.closePreparedStatement(pstmt5);

			count = 0;
			
			// Prepare second read statement (Get mappings for non global galleries)
			if(!oldKeysForNonGlobalGalleries.isEmpty()) {
				
				sql = "SELECT schoolID, sectionID FROM picturegalleryschoolsections " +
						"WHERE sectionID IN (" + StringUtils.toCommaSeparatedString(oldKeysForNonGlobalGalleries) + ") ";
			
				PreparedStatement pstmt6 = connection.prepareStatement(sql);
	
				// Execute
				ResultSet rs6 = pstmt6.executeQuery();
				rs6.last();
				this.log("STEP 4 - " + rs6.getRow() + " rows were read");
				rs6.beforeFirst();
	
				while(rs6.next()) {
	
					// Prepare write statement
					PreparedStatement pstmt = connection.prepareStatement("INSERT INTO picturegallery_galleryschools (schoolID, galleryID) VALUES (?,?)");
	
					// Set school id from old mapping
					pstmt.setInt(1, rs6.getInt("schoolID"));
	
					// Set new gallery id
					pstmt.setInt(2, this.pictureGallerySchoolGalleries.get(rs6.getInt("sectionID")));
	
					// Execute
					count += pstmt.executeUpdate();
					
					DBUtils.closePreparedStatement(pstmt);
				}
	
				DBUtils.closeResultSet(rs6);
				DBUtils.closePreparedStatement(pstmt6);
			
			} else {
				this.log("STEP 4 - 0 rows were read");
			}
			
			this.log("STEP 4 - " + count + " rows were written");
			this.log("STEP 4 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 5 - Copy group mappings (should not exist if gallery is global)
			 * 
			 */
			this.log("STEP 5 - Copying group mappings (should not exist if gallery is global)..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt7 = connection.prepareStatement("SELECT groupID, sectionID FROM picturegallerygroupsections");

			// Execute
			ResultSet rs7 = pstmt7.executeQuery();
			rs7.last();
			this.log("STEP 5 - " + rs7.getRow() + " rows were read");
			rs7.beforeFirst();

			count = 0;

			while(rs7.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO picturegallery_gallerygroups (groupID, galleryID) VALUES (?,?)");

				// Set group id from old mapping
				pstmt.setInt(1, rs7.getInt("groupID"));

				// Set new gallery id
				pstmt.setInt(2, this.pictureGalleryGroupGalleries.get(rs7.getInt("sectionID")));

				// Execute
				count += pstmt.executeUpdate();
				
				DBUtils.closePreparedStatement(pstmt);
			}

			DBUtils.closeResultSet(rs7);
			DBUtils.closePreparedStatement(pstmt7);
			
			this.log("STEP 5 - " + count + " rows were written");
			this.log("STEP 5 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 6 - Copy pictures from group pictures
			 * 
			 */
			this.log("STEP 6 - Copying pictures from group pictures..");

			memento = System.currentTimeMillis();

			// Prepare index read statement
			PreparedStatement pstmt8 = connection.prepareStatement("SELECT fileID FROM picturegallerygrouppictures");

			// Execute
			ResultSet rs8 = pstmt8.executeQuery();
			rs8.last();
			this.log("STEP 6 - " + rs8.getRow() + " rows were read");
			rs8.beforeFirst();

			count = 0;

			// Loop each index
			while(rs8.next()) {

				// Prepare a full read statement (per index)
				PreparedStatement pstmt = connection.prepareStatement("SELECT fileID, sectionID, picSmall, picMedium, picLarge, filename, posted, postedBy FROM picturegallerygrouppictures WHERE fileID = ?");
				pstmt.setInt(1, rs8.getInt("fileID"));

				// Execute
				ResultSet rs = pstmt.executeQuery();

				if(rs.next()) {

					// Prepare write statement
					PreparedStatement innerPstmt = connection.prepareStatement("INSERT INTO picturegallery_pictures (galleryID, picSmall, picMedium, picLarge, filename, posted, postedBy) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

					// Set new gallery id
					innerPstmt.setInt(1, this.pictureGalleryGroupGalleries.get(rs.getInt("sectionID")));

					// Set the rest from old picture
					innerPstmt.setBlob(2, rs.getBlob("picSmall"));
					innerPstmt.setBlob(3, rs.getBlob("picMedium"));
					innerPstmt.setBlob(4, rs.getBlob("picLarge"));
					innerPstmt.setString(5, rs.getString("filename"));
					innerPstmt.setTimestamp(6, rs.getTimestamp("posted"));
					innerPstmt.setInt(7, rs.getInt("postedBy"));

					// Execute
					count += innerPstmt.executeUpdate();

					// Create mapping, old picture id -> new picture id
					ResultSet keys = innerPstmt.getGeneratedKeys();
					if(keys != null && keys.next()) {
						this.pictureGalleryGroupPictures.put(rs.getInt("fileID"), keys.getInt(1));
					} else {
						throw new Exception("Error mapping primary keys!");
					}
					
					DBUtils.closeResultSet(keys);
					DBUtils.closePreparedStatement(innerPstmt);
					
				}
				
				DBUtils.closeResultSet(rs);
				DBUtils.closePreparedStatement(pstmt);
			}
			
			DBUtils.closeResultSet(rs8);
			DBUtils.closePreparedStatement(pstmt8);

			this.log("STEP 6 - " + count + " rows were written");
			this.log("STEP 6 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 7 - Copy pictures from school pictures
			 * 
			 */
			this.log("STEP 7 - Copying pictures from school pictures..");

			memento = System.currentTimeMillis();

			// Prepare index read statement
			PreparedStatement pstmt9 = connection.prepareStatement("SELECT fileID FROM picturegalleryschoolpictures");

			// Execute
			ResultSet rs9 = pstmt9.executeQuery();
			rs9.last();
			this.log("STEP 7 - " + rs9.getRow() + " rows were read");
			rs9.beforeFirst();

			count = 0;

			// Loop each index
			while(rs9.next()) {

				// Prepare a full read statement (per index)
				PreparedStatement pstmt = connection.prepareStatement("SELECT fileID, sectionID, picSmall, picMedium, picLarge, filename, posted, postedBy FROM picturegalleryschoolpictures WHERE fileID = ?");
				pstmt.setInt(1, rs9.getInt("fileID"));

				// Execute
				ResultSet rs = pstmt.executeQuery();

				while(rs.next()) {

					// Prepare write statement
					PreparedStatement innerPstmt = connection.prepareStatement("INSERT INTO picturegallery_pictures (galleryID, picSmall, picMedium, picLarge, filename, posted, postedBy) " +
							"VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

					// Set new gallery id
					innerPstmt.setInt(1, this.pictureGallerySchoolGalleries.get(rs.getInt("sectionID")));

					// Set the rest from old picture
					innerPstmt.setBlob(2, rs.getBlob("picSmall"));
					innerPstmt.setBlob(3, rs.getBlob("picMedium"));
					innerPstmt.setBlob(4, rs.getBlob("picLarge"));
					innerPstmt.setString(5, rs.getString("filename"));
					innerPstmt.setTimestamp(6, rs.getTimestamp("posted"));
					innerPstmt.setInt(7, rs.getInt("postedBy"));

					// Execute
					count += innerPstmt.executeUpdate();

					// Create mapping, old picture id -> new picture id
					ResultSet keys = innerPstmt.getGeneratedKeys();
					if(keys != null && keys.next()) {
						this.pictureGallerySchoolPictures.put(rs.getInt("fileID"), keys.getInt(1));
					} else {
						throw new Exception("Error mapping primary keys!");
					}
					
					DBUtils.closeResultSet(keys);
					DBUtils.closePreparedStatement(innerPstmt);
				}
				
				DBUtils.closeResultSet(rs);
				DBUtils.closePreparedStatement(pstmt);
				
			}
			
			DBUtils.closeResultSet(rs9);
			DBUtils.closePreparedStatement(pstmt9);

			this.log("STEP 7 - " + count + " rows were written");
			this.log("STEP 7 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 8 - Copy comments from group picture comments
			 * 
			 */
			this.log("STEP 8 - Copying comments from group picture comments..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt10 = connection.prepareStatement("SELECT fileID, comment, date, userID FROM grouppicturecomments");

			// Execute
			ResultSet rs10 = pstmt10.executeQuery();
			rs10.last();
			this.log("STEP 8 - " + rs10.getRow() + " rows were read");
			rs10.beforeFirst();

			count = 0;

			while(rs10.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO picturegallery_comments (pictureID, comment, posted, postedBy) " +
						"VALUES (?, ?, ?, ?)");

				// Set new picture id
				pstmt.setInt(1, this.pictureGalleryGroupPictures.get(rs10.getInt("fileID")));

				// Set the rest from old comment
				pstmt.setString(2, rs10.getString("comment"));
				pstmt.setTimestamp(3, rs10.getTimestamp("date"));
				pstmt.setInt(4, rs10.getInt("userID"));

				// Execute
				count += pstmt.executeUpdate();
				
				DBUtils.closePreparedStatement(pstmt);
			}
			
			DBUtils.closeResultSet(rs10);
			DBUtils.closePreparedStatement(pstmt10);

			this.log("STEP 8 - " + count + " rows were written");
			this.log("STEP 8 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


			/**
			 * 
			 * STEP 9 - Copy comments from school picture comments
			 * 
			 */
			this.log("STEP 9 - Copying comments from school picture comments..");

			memento = System.currentTimeMillis();

			// Prepare read statement
			PreparedStatement pstmt11 = connection.prepareStatement("SELECT fileID, comment, date, userID FROM schoolpicturecomments");

			// Execute
			ResultSet rs11 = pstmt11.executeQuery();
			rs11.last();
			this.log("STEP 9 - " + rs11.getRow() + " rows were read");
			rs11.beforeFirst();

			count = 0;

			while(rs11.next()) {

				// Prepare write statement
				PreparedStatement pstmt = connection.prepareStatement("INSERT INTO picturegallery_comments (pictureID, comment, posted, postedBy) " +
						"VALUES (?, ?, ?, ?)");

				// Set new picture id
				pstmt.setInt(1, this.pictureGallerySchoolPictures.get(rs11.getInt("fileID")));

				// Set the rest from old comment
				pstmt.setString(2, rs11.getString("comment"));
				pstmt.setTimestamp(3, rs11.getTimestamp("date"));
				pstmt.setInt(4, rs11.getInt("userID"));

				// Execute
				count += pstmt.executeUpdate();
				
				DBUtils.closePreparedStatement(pstmt);
			}
			
			DBUtils.closeResultSet(rs11);
			DBUtils.closePreparedStatement(pstmt11);

			this.log("STEP 9 - " + count + " rows were written");
			this.log("STEP 9 - Completed in " + TimeUtils.millisecondsToString(System.currentTimeMillis() - memento) + " ms");


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


		} catch(Exception e) {
			this.log("Error during migration. Executing rollback!");
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

		PictureGalleryDataMigrator migrator = new PictureGalleryDataMigrator(ds, null);

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
