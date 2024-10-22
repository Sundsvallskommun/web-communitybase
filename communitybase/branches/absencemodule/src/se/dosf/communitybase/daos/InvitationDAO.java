package se.dosf.communitybase.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.beans.Invitation;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.populators.CommunitySchoolPopulator;
import se.dosf.communitybase.populators.GroupRelationPopulator;
import se.dosf.communitybase.populators.InvitationPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.BooleanQuery;
import se.unlogic.standardutils.dao.querys.HashMapQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.db.DBUtils;

public class InvitationDAO extends BaseDAO {

	public static final InvitationPopulator INVITATION_POPULATOR = new InvitationPopulator();
	public static final CommunitySchoolPopulator COMMUNITY_SCHOOL_POPULATOR = new CommunitySchoolPopulator();
	public static final GroupRelationPopulator GROUP_RELATION_POPULATOR = new GroupRelationPopulator();

	public InvitationDAO(DataSource dataSource) {
		super(dataSource);
	}

	public ArrayList<Invitation> getInvitations(CommunityGroup group, boolean groups, boolean schools) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayListQuery<Invitation> query = new ArrayListQuery<Invitation>(connection, false, "SELECT invitations.* FROM groupinvitations INNER JOIN invitations ON (groupinvitations.invitationID=invitations.invitationID) WHERE  (groupinvitations.groupID = ?) ORDER BY invitations.email, invitations.expires", INVITATION_POPULATOR);

			query.setInt(1, group.getGroupID());

			ArrayList<Invitation> invitations = query.executeQuery();

			if(invitations != null){

				if(groups){
					getGroups(invitations, connection);
				}

				if(schools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	private void getSchools(ArrayList<Invitation> invitations, Connection connection) throws SQLException {

		for(Invitation invitation : invitations){
			getSchools(invitation,connection);
		}
	}

	private void getSchools(Invitation invitation, Connection connection) throws SQLException {

		ArrayListQuery<School> query = new ArrayListQuery<School>(connection, false, "SELECT schools.* FROM schools INNER JOIN schoolinvitations ON (schools.schoolID=schoolinvitations.schoolID) WHERE (schoolinvitations.invitationID = ?)", COMMUNITY_SCHOOL_POPULATOR);

		query.setInt(1, invitation.getInvitationID());

		invitation.setSchools(query.executeQuery());
	}

	private void getGroups(ArrayList<Invitation> invitations, Connection connection) throws SQLException {
		for(Invitation invitation : invitations){
			getGroups(invitation,connection);
		}
	}

	private void getGroups(Invitation invitation, Connection connection) throws SQLException {

		HashMapQuery<CommunityGroup, GroupRelation> query = new HashMapQuery<CommunityGroup, GroupRelation>(connection, false, "SELECT groups.*, groupinvitations.`comment`, groupinvitations.accessLevel, schools.schoolID, schools.name as schoolname FROM groupinvitations INNER JOIN groups ON (groupinvitations.groupID=groups.groupID) INNER JOIN schools ON (groups.schoolID=schools.schoolID) WHERE (groupinvitations.invitationID = ?)", GROUP_RELATION_POPULATOR);

		query.setInt(1, invitation.getInvitationID());

		invitation.setGroups(query.executeQuery());
	}

	public Invitation getInvitation(Integer invitationID, boolean groups, boolean schools) throws SQLException {

		Connection connection = null;

		try{
			connection = dataSource.getConnection();

			ObjectQuery<Invitation> query = new ObjectQuery<Invitation>(connection, false, "SELECT * FROM invitations WHERE invitationID = ?", INVITATION_POPULATOR);

			query.setInt(1, invitationID);

			Invitation invitation = query.executeQuery();

			if(invitation != null){

				if(schools){
					this.getSchools(invitation, connection);
				}

				if(groups){
					this.getGroups(invitation, connection);
				}
			}

			return invitation;
		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public Invitation getInvitation(String email, boolean groups, boolean schools) throws SQLException {

		Connection connection = null;

		try{
			connection = dataSource.getConnection();

			ObjectQuery<Invitation> query = new ObjectQuery<Invitation>(connection, false, "SELECT * FROM invitations WHERE email = ?", INVITATION_POPULATOR);

			query.setString(1, email);

			Invitation invitation = query.executeQuery();

			if(invitation != null){

				if(schools){
					this.getSchools(invitation, connection);
				}

				if(groups){
					this.getGroups(invitation, connection);
				}
			}

			return invitation;
		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public void addGroupInvitation(String email, CommunityGroup group, GroupRelation groupRelation, Timestamp expires, boolean useGroupEmail) throws SQLException {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = new TransactionHandler(dataSource);

			Invitation invitation = this.getInvitation(email, true, false, transactionHandler);

			if(invitation != null){

				if(invitation.getGroups() == null){
					invitation.setGroups(new HashMap<CommunityGroup, GroupRelation>());
				}

				invitation.getGroups().put(group, groupRelation);
				invitation.setExpires(expires);
				invitation.setUpdated(new Timestamp(System.currentTimeMillis()));

				this.update(invitation, false, true, transactionHandler);

			}else{

				invitation = new Invitation();

				invitation.setEmail(email);
				if(useGroupEmail){
					invitation.setSenderEmail(group.getEmail());
				}
				invitation.setExpires(expires);
				invitation.setGroups(new HashMap<CommunityGroup, GroupRelation>(1));
				invitation.getGroups().put(group, groupRelation);
				invitation.setLinkID(UUID.randomUUID().toString());

				this.add(invitation,transactionHandler);
			}

			transactionHandler.commit();

		}finally{
			TransactionHandler.autoClose(transactionHandler);
		}
	}

	private Invitation getInvitation(String email, boolean groups, boolean schools, TransactionHandler transactionHandler) throws SQLException {

		ObjectQuery<Invitation> query = transactionHandler.getObjectQuery("SELECT * FROM invitations WHERE email = ?", INVITATION_POPULATOR);

		query.setString(1, email);

		Invitation invitation = query.executeQuery();

		if(invitation != null){

			if(schools){
				this.getSchools(invitation, transactionHandler);
			}

			if(groups){
				this.getGroups(invitation, transactionHandler);
			}
		}

		return invitation;
	}

	private void getGroups(Invitation invitation, TransactionHandler transactionHandler) throws SQLException {

		HashMapQuery<CommunityGroup, GroupRelation> query = transactionHandler.getHashMapQuery("SELECT groups.*, groupinvitations.`comment`, groupinvitations.accessLevel, schools.schoolID, schools.name as schoolname FROM groupinvitations INNER JOIN groups ON (groupinvitations.groupID=groups.groupID) INNER JOIN schools ON (groups.schoolID=schools.schoolID) WHERE (groupinvitations.invitationID = ?)", GROUP_RELATION_POPULATOR);

		query.setInt(1, invitation.getInvitationID());

		invitation.setGroups(query.executeQuery());
	}

	private void getSchools(Invitation invitation,	TransactionHandler transactionHandler) throws SQLException {

		ArrayListQuery<School> query = transactionHandler.getArrayListQuery("SELECT schools.* FROM schools INNER JOIN schoolinvitations ON (schools.schoolID=schoolinvitations.schoolID) WHERE (schoolinvitations.invitationID = ?)", COMMUNITY_SCHOOL_POPULATOR);

		query.setInt(1, invitation.getInvitationID());

		invitation.setSchools(query.executeQuery());
	}

	private void add(Invitation invitation,	TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("INSERT INTO invitations VALUES (null,?,?,?,?,?,?,?,?)");

		query.setString(1, invitation.getEmail());
		query.setTimestamp(2, invitation.getExpires());
		query.setTimestamp(3, invitation.getUpdated());
		query.setTimestamp(4, invitation.getLastSent());
		query.setString(5, invitation.getLinkID());
		query.setBoolean(6, invitation.isAdmin());
		query.setBoolean(7, invitation.isResend());
		if(invitation.getSenderEmail() != null){
			query.setString(8, invitation.getSenderEmail());
		}else{
			query.setNull(8, 0);
		}

        IntegerKeyCollector keyCollector = new IntegerKeyCollector();
        
        query.executeUpdate(keyCollector);
		
		invitation.setInvitationID(keyCollector.getKeyValue());

		if(invitation.getGroups() != null){
			this.addGroups(invitation,transactionHandler);
		}

		if(invitation.getSchools() != null){
			this.addSchools(invitation,transactionHandler);
		}
	}

	private void addSchools(Invitation invitation, TransactionHandler transactionHandler) throws SQLException {

		if(invitation.getSchools() != null){
			for(School school: invitation.getSchools()){

				UpdateQuery query = transactionHandler.getUpdateQuery("INSERT INTO schoolinvitations VALUES (?,?)");

				query.setInt(1, invitation.getInvitationID());
				query.setInt(2, school.getSchoolID());

				query.executeUpdate();
			}
		}
	}

	private void clearSchools(Invitation invitation, TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("DELETE FROM schoolinvitations WHERE invitationID = ?");

		query.setInt(1, invitation.getInvitationID());

		query.executeUpdate();
	}

	private void addGroups(Invitation invitation, TransactionHandler transactionHandler) throws SQLException {

		if(invitation.getGroups() != null){
			for(Entry<CommunityGroup, GroupRelation> entry : invitation.getGroups().entrySet()){

				UpdateQuery query = transactionHandler.getUpdateQuery("INSERT INTO groupinvitations VALUES (?,?,?,?)");

				query.setInt(1, invitation.getInvitationID());
				query.setInt(2, entry.getKey().getGroupID());
				query.setString(3, entry.getValue().getAccessLevel().toString());
				query.setString(4, entry.getValue().getComment());

				query.executeUpdate();
			}
		}
	}

	private void clearGroups(Invitation invitation, TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("DELETE FROM groupinvitations WHERE invitationID = ?");

		query.setInt(1, invitation.getInvitationID());

		query.executeUpdate();
	}

	public void update(Invitation invitation, boolean schools, boolean groups, TransactionHandler transactionHandler) throws SQLException {

		UpdateQuery query = transactionHandler.getUpdateQuery("UPDATE invitations SET email = ?, expires = ?, updated = ?, lastSent = ?, linkID = ?, admin = ?, resend = ?, senderEmail = ? WHERE invitationID = ?");

		query.setString(1, invitation.getEmail());
		query.setTimestamp(2, invitation.getExpires());
		query.setTimestamp(3, invitation.getUpdated());
		query.setTimestamp(4, invitation.getLastSent());
		query.setString(5, invitation.getLinkID());
		query.setBoolean(6, invitation.isAdmin());
		query.setBoolean(7, invitation.isResend());
		if(invitation.getSenderEmail() != null){
			query.setString(8, invitation.getSenderEmail());
		}else{
			query.setNull(8, 0);
		}
		query.setInt(9,invitation.getInvitationID());
		

		query.executeUpdate();

		if(schools){
			this.clearSchools(invitation,transactionHandler);
			this.addSchools(invitation, transactionHandler);
		}

		if(groups){
			this.clearGroups(invitation, transactionHandler);
			this.addGroups(invitation, transactionHandler);
		}
	}



	public void update(Invitation invitation, boolean groups, boolean schools) throws SQLException {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = new TransactionHandler(dataSource);

			//Check if the invitation exists in db
			BooleanQuery booleanQuery = transactionHandler.getBooleanQuery("SELECT invitationID FROM invitations WHERE invitationID = ?");

			booleanQuery.setInt(1, invitation.getInvitationID());

			if(booleanQuery.executeQuery()){
				this.update(invitation, schools, groups, transactionHandler);
				transactionHandler.commit();
			}else{
				transactionHandler.abort();
			}

		}finally{
			TransactionHandler.autoClose(transactionHandler);
		}

	}

	public void delete(Invitation invitation) throws SQLException {

		UpdateQuery query = new UpdateQuery(dataSource, true, "DELETE FROM invitations WHERE invitationID = ?");

		query.setInt(1, invitation.getInvitationID());

		query.executeUpdate();
	}

	public List<Invitation> getInvitations(School school, boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayListQuery<Invitation> query = new ArrayListQuery<Invitation>(connection, false, "SELECT invitations.* FROM schoolinvitations INNER JOIN invitations ON (schoolinvitations.invitationID=invitations.invitationID) WHERE  (schoolinvitations.schoolID = ?) ORDER BY invitations.email, invitations.expires", INVITATION_POPULATOR);

			query.setInt(1, school.getSchoolID());

			ArrayList<Invitation> invitations = query.executeQuery();

			if(invitations != null){

				if(getGroups){
					getGroups(invitations, connection);
				}

				if(getSchools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public List<Invitation> getAdminInvitations(boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayList<Invitation> invitations = new ArrayListQuery<Invitation>(connection, false, "SELECT * FROM invitations  WHERE admin = true ORDER BY email", INVITATION_POPULATOR).executeQuery();

			if(invitations != null){

				if(getGroups){
					getGroups(invitations, connection);
				}

				if(getSchools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public void addAdminInvitation(String email, Timestamp expires) throws SQLException {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = new TransactionHandler(dataSource);

			Invitation invitation = this.getInvitation(email, false, false, transactionHandler);

			if(invitation != null){

				if(!invitation.isAdmin()){

					invitation.setAdmin(true);
					invitation.setExpires(expires);
					invitation.setUpdated(new Timestamp(System.currentTimeMillis()));

					this.update(invitation, false, false, transactionHandler);

				}else{
					transactionHandler.abort();
					return;
				}

			}else{

				invitation = new Invitation();
				invitation.setAdmin(true);
				invitation.setEmail(email);
				invitation.setExpires(expires);
				invitation.setLinkID(UUID.randomUUID().toString());

				this.add(invitation,transactionHandler);
			}

			transactionHandler.commit();

		}finally{
			TransactionHandler.autoClose(transactionHandler);
		}
	}

	public void addSchoolInvitation(String email, School school, Timestamp expires) throws SQLException {

		TransactionHandler transactionHandler = null;

		try{
			transactionHandler = new TransactionHandler(dataSource);

			Invitation invitation = this.getInvitation(email, false, true, transactionHandler);

			if(invitation != null){

				if(invitation.getSchools() == null){

					invitation.setSchools(new ArrayList<School>());

				}else if(invitation.getSchools().contains(school)){

					transactionHandler.abort();
					return;
				}

				invitation.getSchools().add(school);
				invitation.setExpires(expires);
				invitation.setUpdated(new Timestamp(System.currentTimeMillis()));

				this.update(invitation, true, false, transactionHandler);

			}else{

				invitation = new Invitation();

				invitation.setEmail(email);
				invitation.setExpires(expires);
				invitation.setSchools(new ArrayList<School>());
				invitation.getSchools().add(school);
				invitation.setLinkID(UUID.randomUUID().toString());

				this.add(invitation,transactionHandler);
			}

			transactionHandler.commit();

		}finally{
			TransactionHandler.autoClose(transactionHandler);
		}
	}

	public List<Invitation> getUnsentInvitations(boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayList<Invitation> invitations = new ArrayListQuery<Invitation>(connection, false, "SELECT * FROM invitations WHERE lastSent IS NULL", INVITATION_POPULATOR).executeQuery();

			if(invitations != null){

				if(getGroups){
					getGroups(invitations, connection);
				}

				if(getSchools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public List<Invitation> getUpdatedInvitations(boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayList<Invitation> invitations = new ArrayListQuery<Invitation>(connection, false, "SELECT * FROM invitations WHERE updated > lastSent", INVITATION_POPULATOR).executeQuery();

			if(invitations != null){

				if(getGroups){
					getGroups(invitations, connection);
				}

				if(getSchools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public List<Invitation> getResendInvitations(boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayList<Invitation> invitations = new ArrayListQuery<Invitation>(connection, false, "SELECT * FROM invitations WHERE resend = true", INVITATION_POPULATOR).executeQuery();

			if(invitations != null){

				if(getGroups){
					getGroups(invitations, connection);
				}

				if(getSchools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public List<Invitation> getExpiredInvitations(boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayList<Invitation> invitations = new ArrayListQuery<Invitation>(connection, false, "SELECT * FROM invitations WHERE expires < CURRENT_TIMESTAMP", INVITATION_POPULATOR).executeQuery();

			if(invitations != null){

				if(getGroups){
					getGroups(invitations, connection);
				}

				if(getSchools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public List<Invitation> getRecalledInvitations(boolean getSchools, boolean getGroups) throws SQLException {

		Connection connection = null;

		try{

			connection = this.dataSource.getConnection();

			ArrayList<Invitation> invitations = new ArrayListQuery<Invitation>(connection, false, "SELECT invitations.* FROM invitations WHERE invitations.admin = false AND invitations.invitationID NOT IN (SELECT groupinvitations.invitationID FROM groupinvitations) AND invitations.invitationID NOT IN (SELECT schoolinvitations.invitationID FROM schoolinvitations);", INVITATION_POPULATOR).executeQuery();

			if(invitations != null){

				if(getGroups){
					getGroups(invitations, connection);
				}

				if(getSchools){
					getSchools(invitations, connection);
				}
			}

			return invitations;

		}finally{
			DBUtils.closeConnection(connection);
		}
	}

	public Invitation getInvitation(Integer invitationID, String linkID, boolean schools, boolean groups) throws SQLException {

		Connection connection = null;

		try{
			connection = dataSource.getConnection();

			ObjectQuery<Invitation> query = new ObjectQuery<Invitation>(connection, false, "SELECT * FROM invitations WHERE invitationID = ? AND linkID = ?", INVITATION_POPULATOR);

			query.setInt(1, invitationID);
			query.setString(2, linkID);

			Invitation invitation = query.executeQuery();

			if(invitation != null){

				if(schools){
					this.getSchools(invitation, connection);
				}

				if(groups){
					this.getGroups(invitation, connection);
				}
			}

			return invitation;
		}finally{
			DBUtils.closeConnection(connection);
		}
	}
}
