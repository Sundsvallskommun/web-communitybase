package se.dosf.communitybase.modules.filearchive;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.SimpleEvent;
import se.dosf.communitybase.modules.filearchive.beans.File;
import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.dosf.communitybase.modules.filearchive.populators.FilePopulator;
import se.dosf.communitybase.modules.filearchive.populators.SectionPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;

public class FilearchiveDAO extends BaseDAO {

	private static final AnnotatedResultSetPopulator<SimpleEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<SimpleEvent>(SimpleEvent.class);
	private FilePopulator POPULATOR = null;
	private final SectionPopulator SECTIONPOPULATOR = new SectionPopulator();

	public FilearchiveDAO(DataSource ds, UserHandler userHandler) {
		super(ds);
		this.POPULATOR = new FilePopulator(userHandler);
	}

	public ArrayList<Section> getGroupSectionName(Integer groupID) throws SQLException {

		String sql = "SELECT filearchivegroupsections.sectionID, filearchivegroupsections.name, filearchivegroupsections.groupID as id " +
		"FROM filearchivegroupsections " +
		"WHERE groupID = ? " +
		"ORDER BY filearchivegroupsections.name;";

		ArrayListQuery<Section> query = new ArrayListQuery<Section>(this.dataSource.getConnection(), true, sql, SECTIONPOPULATOR);

		query.setInt(1, groupID);

		ArrayList<Section> sections = query.executeQuery();

		return sections;
	}

	public ArrayList<File> getGroupFiles(Integer groupID, Integer sectionID) throws SQLException{

		String 	sql = "SELECT filearchivegroupsections.name, filearchivegroupfiles.fileID, filearchivegroupfiles.sectionID, filearchivegroupfiles.description, filearchivegroupfiles.filename, filearchivegroupfiles.contentType, filearchivegroupfiles.fileSize, filearchivegroupfiles.postedBy, filearchivegroupfiles.posted " +
						"FROM filearchivegroupfiles, filearchivegroupsections " +
						"WHERE filearchivegroupsections.groupID = ? " +
						"AND filearchivegroupsections.sectionID = ? " +
						"AND filearchivegroupfiles.sectionID = filearchivegroupsections.sectionID " +
						"ORDER BY filearchivegroupsections.name;";

		ArrayListQuery<File> query = new ArrayListQuery<File>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, groupID);
		query.setInt(2, sectionID);

		ArrayList<File> files = query.executeQuery();

		return files;
	}

	public ArrayList<Section> getSchoolSectionName(Integer schoolID) throws SQLException {

		String sql = "SELECT filearchiveschoolsections.sectionID, filearchiveschoolsections.name, filearchiveschoolsections.schoolID as id " +
		"FROM filearchiveschoolsections " +
		"WHERE schoolID = ? " +
		"ORDER BY filearchiveschoolsections.name;";

		ArrayListQuery<Section> query = new ArrayListQuery<Section>(this.dataSource.getConnection(), true, sql, SECTIONPOPULATOR);

		query.setInt(1, schoolID);

		ArrayList<Section> sections = query.executeQuery();

		return sections;
	}

	public ArrayList<File> getSchoolFiles(Integer schoolID, Integer sectionID)throws SQLException{

		String sql = "SELECT filearchiveschoolsections.name, filearchiveschoolfiles.fileID, filearchiveschoolfiles.sectionID, filearchiveschoolfiles.description, filearchiveschoolfiles.filename, filearchiveschoolfiles.contentType, filearchiveschoolfiles.postedBy, filearchiveschoolfiles.fileSize,  filearchiveschoolfiles.posted " +
		"FROM filearchiveschoolfiles, filearchiveschoolsections " +
		"WHERE filearchiveschoolsections.schoolID = ? " +
		"AND filearchiveschoolsections.sectionID = ? " +
		"AND filearchiveschoolfiles.sectionID = filearchiveschoolsections.sectionID " +
		"ORDER BY filearchiveschoolsections.name;";

		ArrayListQuery<File> query = new ArrayListQuery<File>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, schoolID);
		query.setInt(2, sectionID);

		ArrayList<File> files = query.executeQuery();

		return files;

	}

	public ArrayList<File> getGlobalFiles()throws SQLException{

		return null;
	}

	public void addGroupSection(CommunityGroup group, Section section) throws SQLException{

		String sql = "INSERT INTO filearchivegroupsections VALUES(null, ?, ?); ";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, group.getGroupID());
		query.setString(2, section.getName());

		query.executeUpdate();
	}

	public void addGroupFile(CommunityUser user, File file) throws SQLException{

		String sql = "INSERT INTO filearchivegroupfiles VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?); ";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, file.getSectionID());
		query.setString(2, file.getDescription());
		query.setBlob(3, file.getBlob());
		query.setString(4, file.getFileName());
		query.setString(5, file.getContentType());
		query.setInt(6, user.getUserID());
		query.setLong(7, file.getFileSize());
		query.setTimestamp(8, file.getDate());

		query.executeUpdate();
	}

	public void updateGroupSection(Section section) throws SQLException {

		String sql = "UPDATE filearchivegroupsections SET name = ? WHERE sectionID = ?;";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, section.getName());
		query.setInt(2, section.getSectionID());

		query.executeUpdate();
	}

	public Section getSection(Integer sectionID) throws SQLException {
		String sql = "SELECT sectionID, name, groupId AS id  FROM filearchivegroupsections WHERE sectionID = ?";

		ObjectQuery<Section> query = new ObjectQuery<Section>(this.dataSource.getConnection(), true, sql, SECTIONPOPULATOR);

		query.setInt(1, sectionID);

		Section section = query.executeQuery();

		return section;
	}

	public void deleteGroupSection(Integer sectionID) throws SQLException {
		String sql = "DELETE FROM filearchivegroupsections WHERE sectionID = ?;";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, sectionID);

		query.executeUpdate();

	}

	public void addSchoolFile(CommunityUser user, File file)  throws SQLException{

		String sql = "INSERT INTO filearchiveschoolfiles VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?); ";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, file.getSectionID());
		query.setString(2, file.getDescription());
		query.setBlob(3, file.getBlob());
		query.setString(4, file.getFileName());
		query.setString(5, file.getContentType());
		query.setInt(6, user.getUserID());
		query.setLong(7, file.getFileSize());
		query.setTimestamp(8, file.getDate());

		query.executeUpdate();
	}

	public Section getSchoolSection(Integer sectionID, Integer schoolID) throws SQLException {
		String sql = "SELECT sectionID, name, schoolID AS id FROM filearchiveschoolsections WHERE sectionID = ? AND schoolID = ?";

		ObjectQuery<Section> query = new ObjectQuery<Section>(this.dataSource.getConnection(), true, sql, SECTIONPOPULATOR);

		query.setInt(1, sectionID);
		query.setInt(2, schoolID);

		Section section = query.executeQuery();

		return section;
	}

	public File getGroupFile(int fileID, Integer groupID, boolean blob) throws SQLException {

		String queryString =  null;

		if(blob){

			queryString =  "SELECT filearchivegroupfiles.*, filearchivegroupsections.name " +
			"FROM filearchivegroupfiles,filearchivegroupsections WHERE fileID = ? " +
			"AND filearchivegroupsections.sectionID = filearchivegroupfiles.sectionID AND filearchivegroupsections.groupID = ?";

		}else{

			queryString =  "SELECT filearchivegroupfiles.fileID, filearchivegroupfiles.sectionID, filearchivegroupfiles.description, filearchivegroupfiles.filename, filearchivegroupfiles.contentType, filearchivegroupfiles.fileSize, filearchivegroupfiles.postedBy, filearchivegroupfiles.posted, filearchivegroupsections.name " +
			"FROM filearchivegroupfiles,filearchivegroupsections WHERE fileID = ? " +
			"AND filearchivegroupsections.sectionID = filearchivegroupfiles.sectionID AND filearchivegroupsections.groupID = ?";
		}

		ObjectQuery<File> query = new ObjectQuery<File>(this.dataSource.getConnection(),true,queryString,POPULATOR);

		query.setInt(1, fileID);
		query.setInt(2, groupID);

		File file = query.executeQuery();


		return file;
	}

	public File getSchoolFile(int fileID, Integer schoolID, boolean blob) throws SQLException {

		String queryString = null;

		if(blob){

			queryString =  "SELECT filearchiveschoolfiles.* , filearchiveschoolsections.name " +
			"FROM filearchiveschoolfiles,filearchiveschoolsections WHERE fileID = ? " +
			"AND filearchiveschoolsections.sectionID = filearchiveschoolfiles.sectionID AND filearchiveschoolsections.schoolID = ?";

		}else{

			queryString =  "SELECT filearchiveschoolfiles.fileID, filearchiveschoolfiles.sectionID, filearchiveschoolfiles.description, filearchiveschoolfiles.filename, filearchiveschoolfiles.contentType, filearchiveschoolfiles.postedBy, filearchiveschoolfiles.fileSize,  filearchiveschoolfiles.posted, filearchiveschoolsections.name " +
			"FROM filearchiveschoolfiles,filearchiveschoolsections WHERE fileID = ? " +
			"AND filearchiveschoolsections.sectionID = filearchiveschoolfiles.sectionID AND filearchiveschoolsections.schoolID = ?";
		}

		ObjectQuery<File> query = new ObjectQuery<File>(this.dataSource.getConnection(),true,queryString,POPULATOR);

		query.setInt(1, fileID);
		query.setInt(2, schoolID);

		File file = query.executeQuery();


		return file;
	}

	public void addSchoolSection(CommunityGroup group, Section section) throws SQLException{

		String sql = "INSERT INTO filearchiveschoolsections VALUES(null, ?, ?); ";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, group.getSchool().getSchoolID());
		query.setString(2, section.getName());

		query.executeUpdate();

	}

	public void updateSchoolSection(Section section) throws SQLException {
		String sql = "UPDATE filearchiveschoolsections SET name = ? WHERE sectionID = ?;";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, section.getName());
		query.setInt(2, section.getSectionID());

		query.executeUpdate();

	}

	public void deleteSchoolSection(Integer sectionID) throws SQLException{
		String sql = "DELETE FROM filearchiveschoolsections WHERE sectionID = ?;";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, sectionID);

		query.executeUpdate();

	}

	public void updateGroupFile(File file) throws SQLException {

		String sql = "UPDATE filearchivegroupfiles SET description = ?, sectionID = ? WHERE fileID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, file.getDescription());
		query.setInt(2, file.getSectionID());
		query.setInt(3, file.getFileID());

		query.executeUpdate();

	}

	public void updateSchoolFile(File file) throws SQLException {

		String sql = "UPDATE filearchiveschoolfiles SET description = ?, sectionID = ? WHERE fileID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setString(1, file.getDescription());
		query.setInt(2, file.getSectionID());
		query.setInt(3, file.getFileID());

		query.executeUpdate();

	}
	public ArrayList<Section> getGroupSections(Integer groupID) throws SQLException {

		String sql = "SELECT sectionID, name, groupID as id " +
		"FROM filearchivegroupsections " +
		"WHERE filearchivegroupsections.groupID = ? ";

		ArrayListQuery<Section> query = new ArrayListQuery<Section>(this.dataSource.getConnection(), true, sql, SECTIONPOPULATOR);

		query.setInt(1, groupID);

		ArrayList<Section> sections = query.executeQuery();

		return sections;
	}

	public ArrayList<Section> getSchoolSections(Integer schoolID) throws SQLException{

		String sql = "SELECT sectionID, name, schoolID as id " +
		"FROM filearchiveschoolsections " +
		"WHERE filearchiveschoolsections.schoolID = ? ORDER BY name";

		ArrayListQuery<Section> query = new ArrayListQuery<Section>(this.dataSource.getConnection(), true, sql, SECTIONPOPULATOR);

		query.setInt(1, schoolID);

		ArrayList<Section> sections = query.executeQuery();

		return sections;
	}

	public void deleteGroupFile(Integer fileID) throws SQLException {
		String sql = "DELETE FROM filearchivegroupfiles WHERE fileID = ?;";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, fileID);

		query.executeUpdate();
	}

	public void deleteSchoolFile(Integer fileID) throws SQLException {
		String sql = "DELETE FROM filearchiveschoolfiles WHERE fileID = ?;";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, fileID);

		query.executeUpdate();
	}

	public List<SimpleEvent> getEvents(CommunityGroup group, Timestamp lastLogin) throws SQLException {

		Connection connection = null;

		try{
			connection = this.dataSource.getConnection();

			String groupSQL = "SELECT \r\n" +
					"  filearchivegroupfiles.filename as title,\r\n" +
					"  filearchivegroupfiles.description,\r\n" +
					"  filearchivegroupfiles.posted as added\r\n" +
					"FROM\r\n" +
					" filearchivegroupfiles\r\n" +
					" INNER JOIN filearchivegroupsections ON (filearchivegroupfiles.sectionID=filearchivegroupsections.sectionID)\r\n" +
					"WHERE\r\n" +
					"  (filearchivegroupsections.groupID = ?) AND \r\n" +
					"  (filearchivegroupfiles.posted > ?)\r\n" +
					"ORDER BY\r\n" +
					"  filearchivegroupfiles.filename,\r\n" +
					"  filearchivegroupfiles.posted";

			ArrayListQuery<SimpleEvent> groupQuery = new ArrayListQuery<SimpleEvent>(connection, false, groupSQL, EVENT_POPULATOR);

			groupQuery.setInt(1, group.getGroupID());
			groupQuery.setTimestamp(2, lastLogin);

			List<SimpleEvent> groupEvents = groupQuery.executeQuery();

			String schoolSQL = "SELECT \r\n" +
					"  filearchiveschoolfiles.filename as title,\r\n" +
					"  filearchiveschoolfiles.description,\r\n" +
					"  filearchiveschoolfiles.posted as added\r\n" +
					"FROM\r\n" +
					" filearchiveschoolfiles\r\n" +
					" INNER JOIN filearchiveschoolsections ON (filearchiveschoolfiles.sectionID=filearchiveschoolsections.sectionID)\r\n" +
					"WHERE\r\n" +
					"  (filearchiveschoolsections.schoolID = ?) AND \r\n" +
					"  (filearchiveschoolfiles.posted > ?)\r\n" +
					"ORDER BY\r\n" +
					"  filearchiveschoolfiles.filename,\r\n" +
					"  filearchiveschoolfiles.description";

			ArrayListQuery<SimpleEvent> schoolQuery = new ArrayListQuery<SimpleEvent>(connection, false, schoolSQL, EVENT_POPULATOR);

			schoolQuery.setInt(1, group.getSchool().getSchoolID());
			schoolQuery.setTimestamp(2, lastLogin);

			List<SimpleEvent> schoolEvents = schoolQuery.executeQuery();

			if(schoolEvents == null && groupEvents == null){
				return null;
			}

			ArrayList<SimpleEvent> fileEvents = new ArrayList<SimpleEvent>();

			if(groupEvents != null){
				fileEvents.addAll(groupEvents);
			}

			if(schoolEvents != null){
				fileEvents.addAll(schoolEvents);
			}

			return fileEvents;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}
}