package se.dosf.communitybase.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.populators.CommunitySchoolPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;

public class CommunitySchoolDAO extends BaseDAO {

	private static CommunitySchoolPopulator POPULATOR = new CommunitySchoolPopulator();
	private CommunityUserDAO userDAO;
	private CommunityGroupDAO groupDAO;

	public CommunitySchoolDAO(DataSource ds) {
		super(ds);
	}

	public void setGroupDAO(CommunityGroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setUserDAO(CommunityUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public School getSchool(CommunityGroup group) throws SQLException{

		Connection connection = this.dataSource.getConnection();

		ObjectQuery<School> query = new ObjectQuery<School>(connection, true, "SELECT * FROM schools WHERE schoolID = ?", POPULATOR);

		query.setInt(1, group.getSchool().getSchoolID());

		return query.executeQuery();


	}

	//TODO share connection
	public School getSchool(int schoolID, boolean getAdmins, boolean getGroups) throws SQLException {

		ObjectQuery<School> query = new ObjectQuery<School>(this.dataSource.getConnection(), true, "SELECT * FROM schools WHERE schoolID = ?", POPULATOR);

		query.setInt(1, schoolID);

		School school = query.executeQuery();

		if(school != null && (getAdmins || getGroups)){

			if(getAdmins){
				school.setAdmins(this.userDAO.getUsers(school));
			}

			if(getGroups){
				school.setGroups(this.groupDAO.getGroups(school));
			}
		}

		return school;
	}

	//TODO share connection
	public List<School> getSchools(boolean getAdmins, boolean getGroups) throws SQLException {

		ArrayListQuery<School> query = new ArrayListQuery<School>(dataSource, true, "SELECT * FROM schools ORDER BY name", POPULATOR);

		ArrayList<School> schools = query.executeQuery();

		if(schools != null && (getAdmins || getGroups)){

			for(School school : schools){

				if(getAdmins){
					school.setAdmins(this.userDAO.getUsers(school));
				}

				if(getGroups){
					school.setGroups(this.groupDAO.getGroups(school));
				}
			}
		}

		return schools;
	}

	//TODO share connection
	public List<School> getSchools(CommunityUser user,boolean getAdmins, boolean getGroups) throws SQLException {

		ArrayListQuery<School> query = new ArrayListQuery<School>(dataSource, true, "SELECT schools.* FROM schooladmins INNER JOIN schools ON (schooladmins.schoolID=schools.schoolID) WHERE (schooladmins.userID = ?) ORDER BY schools.name", POPULATOR);

		query.setInt(1, user.getUserID());

		ArrayList<School> schools = query.executeQuery();

		if(schools != null && (getAdmins || getGroups)){

			for(School school : schools){

				if(getAdmins){
					school.setAdmins(this.userDAO.getUsers(school));
				}

				if(getGroups){
					school.setGroups(this.groupDAO.getGroups(school));
				}
			}
		}

		return schools;
	}

	public void add(School school) throws SQLException {

		UpdateQuery query = new UpdateQuery(dataSource, true, "INSERT INTO schools VALUES (null,?)");

		query.setString(1, school.getName());

        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
        
        query.executeUpdate(keyCollector);
		
		school.setSchoolID(keyCollector.getKeyValue());
	}

	public void update(School school) throws SQLException {

		UpdateQuery query = new UpdateQuery(dataSource, true, "UPDATE schools SET name = ? WHERE schoolID = ?");

		query.setString(1, school.getName());
		query.setInt(2, school.getSchoolID());

		query.executeUpdate();
	}

	public void delete(School school) throws SQLException {

		UpdateQuery query = new UpdateQuery(dataSource, true, "DELETE FROM schools WHERE schoolID = ?");

		query.setInt(1, school.getSchoolID());

		query.executeUpdate();
	}

}
