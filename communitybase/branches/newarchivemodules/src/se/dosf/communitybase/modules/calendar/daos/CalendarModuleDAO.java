package se.dosf.communitybase.modules.calendar.daos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.beans.Receipt;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.modules.calendar.beans.CalendarPost;
import se.dosf.communitybase.modules.calendar.beans.CalendarResume;
import se.dosf.communitybase.modules.calendar.populators.CalendarPopulator;
import se.dosf.communitybase.populators.CommunityGroupPopulator;
import se.dosf.communitybase.populators.CommunitySchoolPopulator;
import se.dosf.communitybase.populators.ReceiptItemPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.date.PooledSimpleDateFormat;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;
import se.unlogic.standardutils.string.StringUtils;

public class CalendarModuleDAO extends BaseDAO {

	private static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);
	private static final PooledSimpleDateFormat DATE_FORMATTER = new PooledSimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private CalendarPopulator POPULATOR = new CalendarPopulator();
	private static CommunityGroupPopulator GROUPPOPULATOR = new CommunityGroupPopulator();
	private static CommunitySchoolPopulator SCHOOLPOPULATOR = new CommunitySchoolPopulator();
	private ReceiptItemPopulator RECEIPTPOPULATOR = null;

	public CalendarModuleDAO(DataSource ds) {
		super(ds);

	}

	public CalendarModuleDAO(DataSource ds, UserHandler userHandler) {
		super(ds);

		RECEIPTPOPULATOR = new ReceiptItemPopulator(userHandler);
	}

	public ArrayList<CalendarPost> getCalenderPosts(CommunityGroup group, Date startDate, Date endDate) throws SQLException{


		String sql = "SELECT DISTINCT calendar.*" +
				"FROM calendar " +
				"LEFT JOIN groupcalendar " +
				"ON calendar.calendarID = groupcalendar.calendarID " +
				"LEFT JOIN groups " +
				"ON groupcalendar.groupID = groups.groupID " +
				"LEFT JOIN schoolcalendar " +
				"ON calendar.calendarID = schoolcalendar.calendarID " +
				"LEFT JOIN schools " +
				"ON schoolcalendar.schoolID = schools.schoolID " +
				"LEFT JOIN globalcalendar " +
				"ON calendar.calendarID = globalcalendar.calendarID " +
				"WHERE (schoolcalendar.schoolID = ? " +
				"OR groupcalendar.groupID = ? " +
				"OR globalcalendar.calendarID = calendar.calendarID) " +
				"AND startTime >= ? AND endTime <= ? " +
				"ORDER BY startTime asc;";

		ArrayListQuery<CalendarPost> query = new ArrayListQuery<CalendarPost>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, group.getSchool().getSchoolID());
		query.setInt(2, group.getGroupID());
		query.setTimestamp(3, new Timestamp(startDate.getTime()));
		query.setTimestamp(4, new Timestamp(endDate.getTime()));

		ArrayList<CalendarPost> posts = query.executeQuery();

		if(posts != null){

			for(CalendarPost post : posts){
				post.setGroups(this.getGroups(post,null));
				post.setSchools(this.getSchools(post,null));
			}

		}


		return posts;

	}



	public ArrayList<CalendarPost> getCalenderPosts(CommunityUser user, Date startDate, Date endDate, boolean onlyWithReminder) throws SQLException{

		StringBuilder groupStr = new StringBuilder("");

		StringBuilder schoolStr = new StringBuilder("");

		if(user.getGroups() != null){

			for(CommunityGroup group : user.getCommunityGroups()){

				groupStr.append("," + group.getGroupID());

				schoolStr.append("," + group.getSchool().getSchoolID());

			}

		}

		if(user.getSchools() != null){

			for(School school : user.getSchools()){

				schoolStr.append("," + school.getSchoolID());

			}

		}

		String schools = schoolStr.length() > 0 ? schoolStr.substring(1,schoolStr.length()) : "null";
		String groups = groupStr.length() > 0 ? groupStr.substring(1,groupStr.length()) : "null";

		String sql = "SELECT DISTINCT calendar.*, groups.*, schools.schoolID AS 'schoolsID', schools.name AS 'schoolname' " +
				"FROM calendar " +
				"LEFT JOIN groupcalendar " +
				"ON calendar.calendarID = groupcalendar.calendarID " +
				"LEFT JOIN groups " +
				"ON groupcalendar.groupID = groups.groupID " +
				"LEFT JOIN schoolcalendar " +
				"ON calendar.calendarID = schoolcalendar.calendarID " +
				"LEFT JOIN schools " +
				"ON schoolcalendar.schoolID = schools.schoolID " +
				"LEFT JOIN globalcalendar " +
				"ON calendar.calendarID = globalcalendar.calendarID " +
				"WHERE (schoolcalendar.schoolID IN (" + schools + ") " +
				"OR groupcalendar.groupID IN (" + groups + ") " +
				"OR globalcalendar.calendarID = calendar.calendarID) " +
				"AND startTime >= ? AND endTime <= ? " +
				(onlyWithReminder == true ? " AND sendReminder = 1 " : "") +
				"GROUP BY calendar.calendarID " +
				"ORDER BY startTime asc;";

		ArrayListQuery<CalendarPost> query = new ArrayListQuery<CalendarPost>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setTimestamp(1, new Timestamp(startDate.getTime()));
		query.setTimestamp(2, new Timestamp(endDate.getTime()));
		
		ArrayList<CalendarPost> calendarPosts = query.executeQuery();
		
		if(!CollectionUtils.isEmpty(calendarPosts)) {
			
			for(CalendarPost post : calendarPosts) {
			
				post.setGroups(this.getGroups(post,null));
				post.setSchools(this.getSchools(post,null));
			
			}
			
		}
		
		return calendarPosts;

	}

	public Integer add(CalendarPost post, RelationType type, CommunityGroup group) throws SQLException{

		TransactionHandler transactionHandler = null;

		Integer calendarID = null;

		try{

			transactionHandler = new TransactionHandler(this.dataSource);

			String sql = "INSERT INTO calendar VALUES(null,?,?,?,?,?,?,?)";

			UpdateQuery query = transactionHandler.getUpdateQuery(sql);

			query.setString(1, post.getDescription());
			query.setString(2, post.getDetails());
			query.setTimestamp(3, post.getStartTime());
			query.setTimestamp(4, post.getEndTime());
			query.setInt(5, post.getPosterID());
			query.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			query.setBoolean(7, post.isSendReminder());

			IntegerKeyCollector keyCollector = new IntegerKeyCollector();

			query.executeUpdate(keyCollector);

			calendarID = keyCollector.getKeyValue();

			Integer relationID = type.equals(RelationType.GROUP) ? group.getGroupID() : group.getSchool().getSchoolID();

			this.addCalendarRelation(calendarID, type, relationID, transactionHandler);

			transactionHandler.commit();

		}finally {
			if (transactionHandler != null && !transactionHandler.isClosed()) {
				transactionHandler.abort();
			}
		}

		return calendarID;

	}

	public Integer add(CalendarPost post) throws SQLException {

		TransactionHandler transactionHandler = null;

		Integer calendarID = null;

		try{

			transactionHandler = new TransactionHandler(this.dataSource);

			String sql = "INSERT INTO calendar VALUES(null,?,?,?,?,?,?,?)";

			UpdateQuery query = transactionHandler.getUpdateQuery(sql);

			query.setString(1, post.getDescription());
			query.setString(2, post.getDetails());
			query.setTimestamp(3, post.getStartTime());
			query.setTimestamp(4, post.getEndTime());
			query.setInt(5, post.getPosterID());
			query.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			query.setBoolean(7, post.isSendReminder());

			IntegerKeyCollector keyCollector = new IntegerKeyCollector();

			query.executeUpdate(keyCollector);

			calendarID = keyCollector.getKeyValue();

			if(post.isGlobal()){

				this.addCalendarRelation(calendarID, RelationType.GLOBAL, null, transactionHandler);

			}else{

				if (post.getGroups() != null) {

					for (CommunityGroup group : post.getGroups()) {

						this.addCalendarRelation(calendarID, RelationType.GROUP, group.getGroupID(), transactionHandler);

					}

				}

				if (post.getSchools() != null) {

					for (School school : post.getSchools()) {

						this.addCalendarRelation(calendarID, RelationType.SCHOOL, school.getSchoolID(), transactionHandler);

					}

				}

			}

			transactionHandler.commit();

		}finally {
			if (transactionHandler != null && !transactionHandler.isClosed()) {
				transactionHandler.abort();
			}
		}

		return calendarID;

	}

	public CalendarPost get(Integer calendarID) throws SQLException{

		String sql = "SELECT * FROM calendar WHERE calendarID = ?";

		ObjectQuery<CalendarPost> query = new ObjectQuery<CalendarPost>(this.dataSource.getConnection(), true, sql, POPULATOR);

		query.setInt(1, calendarID);

		CalendarPost post = query.executeQuery();

		if(post != null){

			post.setGroups(this.getGroups(post,null));
			post.setSchools(this.getSchools(post,null));


			if(post.getGroups() == null && post.getSchools() == null){
				post.setGlobal(true);
			}

		}

		return post;

	}

	public void update(CalendarPost post) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {

			transactionHandler = new TransactionHandler(this.dataSource);

			String sql = "UPDATE calendar SET title = ?, details = ?, startTime = ?, endTime = ?, posterID = ?, posted = ?, sendReminder = ? WHERE calendarID = ?";

			UpdateQuery query = transactionHandler.getUpdateQuery(sql);

			query.setString(1, post.getDescription());
			query.setString(2, post.getDetails());
			query.setTimestamp(3, post.getStartTime());
			query.setTimestamp(4, post.getEndTime());
			query.setObject(5, post.getPosterID());
			query.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
			query.setBoolean(7, post.isSendReminder());
			query.setInt(8, post.getCalendarID());

			query.executeUpdate();

			this.deleteCalendarRelation(post, RelationType.GLOBAL, transactionHandler);
			this.deleteCalendarRelation(post, RelationType.GROUP, transactionHandler);
			this.deleteCalendarRelation(post, RelationType.SCHOOL, transactionHandler);

			if (post.isGlobal()) {

				this.addCalendarRelation(post.getCalendarID(), RelationType.GLOBAL, null, transactionHandler);

			} else {

				if (post.getGroups() != null) {

					for (CommunityGroup communityGroup : post.getGroups()) {

						this.addCalendarRelation(post.getCalendarID(), RelationType.GROUP, communityGroup.getGroupID(), transactionHandler);

					}

				}

				if (post.getSchools() != null) {

					for (School school : post.getSchools()) {

						this.addCalendarRelation(post.getCalendarID(), RelationType.SCHOOL, school.getSchoolID(), transactionHandler);

					}

				}

			}

			transactionHandler.commit();

		} finally {

			if (transactionHandler != null && !transactionHandler.isClosed()) {
				transactionHandler.abort();
			}

		}

	}

	public void delete(CalendarPost post) throws SQLException{
		
		delete(post.getCalendarID());

	}
	
	public void delete(Integer calendarID) throws SQLException{

		String sql = "DELETE FROM calendar WHERE calendarID = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		query.setInt(1, calendarID);

		query.executeUpdate();

	}

	public ArrayList<IdentifiedEvent> getCalendarResume(CommunityGroup group, Timestamp startStamp) throws SQLException{

		String sql = "SELECT DISTINCT\r\n" +
				"  calendar.calendarID as id,\r\n" +
				"  calendar.title,\r\n" +
				"  calendar.details as description,\r\n" +
				"  calendar.startTime as added\r\n" +
				"FROM\r\n" +
				"  calendar\r\n" +
				"  LEFT OUTER JOIN globalcalendar ON (globalcalendar.calendarID=calendar.calendarID)\r\n" +
				"  LEFT OUTER JOIN groupcalendar ON (groupcalendar.calendarID=calendar.calendarID)\r\n" +
				"  LEFT OUTER JOIN schoolcalendar ON (schoolcalendar.calendarID=calendar.calendarID)\r\n" +
				"WHERE\r\n" +
				"  posted > ? AND\r\n" +
				"  ((calendar.calendarID = globalcalendar.calendarID) OR\r\n" +
				"  (groupcalendar.groupID = ?) OR\r\n" +
				"  (schoolcalendar.schoolID = ?))\r\n" +
				"ORDER BY\r\n" +
				"  calendar.posted DESC";

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(dataSource, sql, EVENT_POPULATOR);

		query.setTimestamp(1, startStamp);
		query.setInt(2, group.getGroupID());
		query.setInt(3, group.getSchool().getSchoolID());

		return query.executeQuery();
	}

	public CalendarResume getUserCalendarResume(CommunityUser user, Integer limit) throws SQLException {

		TransactionHandler transactionHandler = new TransactionHandler(dataSource);

		try{
			Connection connection = null;
			connection = this.dataSource.getConnection();

			Date today = new Date();

			ArrayList<Integer> groupIDList = new ArrayList<Integer>();
			TreeSet<Integer> schoolIDList = new TreeSet<Integer>();

			if(user.getGroups() != null){
				for(CommunityGroup group : user.getCommunityGroups()){
					groupIDList.add(group.getGroupID());
					schoolIDList.add(group.getSchool().getSchoolID());
				}
			}
			if(user.getSchools() != null){
				for(School communitySchool : user.getSchools()){
					schoolIDList.add(communitySchool.getSchoolID());
				}
			}

			String sql = null;

			if(groupIDList != null && !groupIDList.isEmpty() && schoolIDList != null && !schoolIDList.isEmpty()){
				sql = "SELECT DISTINCT calendar.*, groups.name as 'groupname', schools.name as 'schoolname' FROM calendar " +
						"LEFT JOIN groupcalendar ON calendar.calendarID = groupcalendar.calendarID " +
						"LEFT JOIN groups ON groupcalendar.groupID = groups.groupID " +
						"LEFT JOIN schoolcalendar ON calendar.calendarID = schoolcalendar.calendarID " +
						"LEFT JOIN schools ON schoolcalendar.schoolID = schools.schoolID " +
						"LEFT JOIN globalcalendar ON calendar.calendarID = globalcalendar.calendarID " +
						"WHERE calendar.startTime >= ? " +
						"AND (groupcalendar.groupID IN (" + StringUtils.toCommaSeparatedString(groupIDList) + ")  " +
						"OR schoolcalendar.schoolID IN (" + StringUtils.toCommaSeparatedString(schoolIDList) + ")  " +
						"OR calendar.calendarID = globalcalendar.calendarID) ORDER BY starttime, startTime LIMIT ?;";
			}else if(groupIDList.isEmpty() && schoolIDList != null && !schoolIDList.isEmpty()){
				sql = "SELECT DISTINCT calendar.*, schools.name as 'schoolname' FROM calendar " +
						"LEFT JOIN schoolcalendar ON calendar.calendarID = schoolcalendar.calendarID " +
						"LEFT JOIN schools ON schoolcalendar.schoolID = schools.schoolID " +
						"LEFT JOIN globalcalendar ON calendar.calendarID = globalcalendar.calendarID " +
						"WHERE calendar.startTime >= ? " +
						"AND (schoolcalendar.schoolID IN (" + StringUtils.toCommaSeparatedString(schoolIDList) + ")  " +
						"OR calendar.calendarID = globalcalendar.calendarID) ORDER BY starttime, startTime LIMIT ?;";
			}else if(schoolIDList != null && schoolIDList.isEmpty() && groupIDList != null && !groupIDList.isEmpty()){
				sql = "SELECT DISTINCT calendar.*, groups.name as 'groupname' FROM calendar " +
						"LEFT JOIN groupcalendar ON calendar.calendarID = groupcalendar.calendarID " +
						"LEFT JOIN groups ON groupcalendar.groupID = groups.groupID" +
						"LEFT JOIN globalcalendar ON calendar.calendarID = globalcalendar.calendarID " +
						"WHERE calendar.startTime >= ? " +
						"AND (groupcalendar.groupID IN (" + StringUtils.toCommaSeparatedString(groupIDList) + ")  " +
						"OR calendar.calendarID = globalcalendar.calendarID) ORDER BY starttime LIMIT ?;";
			}else{
				sql = "SELECT DISTINCT calendar.* FROM calendar " +
						"LEFT JOIN globalcalendar ON calendar.calendarID = globalcalendar.calendarID " +
						"WHERE calendar.startTime >= ? " +
						"AND calendar.calendarID = globalcalendar.calendarID ORDER BY starttime LIMIT ?;";
			}

			ArrayListQuery<CalendarPost> query = new ArrayListQuery<CalendarPost>(connection, true, sql, POPULATOR);

			query.setString(1, DATE_FORMATTER.format(today).toString());
			query.setInt(2, limit);

			CalendarResume calendarResume = new CalendarResume();

			ArrayList<CalendarPost> posts = query.executeQuery();

			if(posts != null){

				for(CalendarPost post : posts) {
					post.setGroups(this.getGroups(post, transactionHandler));
					post.setSchools(this.getSchools(post, transactionHandler));
					if(post.getGroups() == null && post.getSchools() == null){
						post.setGlobal(true);
					}
				}

				calendarResume.getCalendarPosts().addAll(posts);
			}

			transactionHandler.commit();

			return calendarResume;

		}finally{
			transactionHandler.isClosed();
		}

	}

	private void addCalendarRelation(Integer calendarID, RelationType type, Integer relationID, TransactionHandler transactionHandler) throws SQLException{

		String sql = null;
		UpdateQuery query = null;

		if(!type.equals(RelationType.GLOBAL)){

			sql = "INSERT INTO " + this.getCalendarRelationTable(type) + " VALUES(?,?)";

			query = transactionHandler.getUpdateQuery(sql);

			query.setInt(1, relationID);
			query.setInt(2, calendarID);

		}else{

			sql = "INSERT INTO globalcalendar" + " VALUES(?)";

			query = transactionHandler.getUpdateQuery(sql);

			query.setInt(1, calendarID);
		}

		query.executeUpdate();

	}

	public void checkCalendarPostReadStatus(CommunityUser user, CalendarPost post) throws SQLException{

		String sql = "INSERT INTO calendarreceipts VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE lastReadTime = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		Timestamp currentTime =  new Timestamp(System.currentTimeMillis());

		query.setInt(1, user.getUserID());
		query.setInt(2, post.getCalendarID());
		query.setTimestamp(3, currentTime);
		query.setTimestamp(4, currentTime);
		query.setTimestamp(5, currentTime);

		query.executeUpdate();

	}

	public ArrayList<Receipt> getCalendarPostReceipt(CalendarPost post) throws SQLException{

		ArrayListQuery<Receipt> query = new ArrayListQuery<Receipt>(this.dataSource.getConnection(), true, "SELECT * FROM calendarreceipts WHERE calendarID = ?", RECEIPTPOPULATOR);

		query.setInt(1, post.getCalendarID());

		return query.executeQuery();

	}

	private void deleteCalendarRelation(CalendarPost post, RelationType type, TransactionHandler transactionHandler) throws SQLException{

		String sql = "DELETE FROM " + this.getCalendarRelationTable(type) + " WHERE calendarID = ?";

		UpdateQuery query = transactionHandler.getUpdateQuery(sql);

		query.setInt(1, post.getCalendarID());

		query.executeUpdate();

	}

	public ArrayList<School> getSchools(CalendarPost post, TransactionHandler transactionHandler) throws SQLException {

		String sql = "SELECT schools.* FROM schoolcalendar " +
				"LEFT JOIN schools ON schoolcalendar.schoolID = schools.schoolID " +
				"WHERE calendarID = ?";

		ArrayListQuery<School> query = null;

		if(transactionHandler != null) {
			query = transactionHandler.getArrayListQuery(sql, SCHOOLPOPULATOR);
		} else {
			query = new ArrayListQuery<School>(this.dataSource, sql, SCHOOLPOPULATOR);
		}

		query.setInt(1, post.getCalendarID());

		return query.executeQuery();
	}

	public ArrayList<CommunityGroup> getGroups(CalendarPost post, TransactionHandler transactionHandler) throws SQLException {

		String sql = "SELECT groups.groupID, groups.name, groups.email, schools.schoolID, schools.name AS schoolname FROM groupcalendar " +
				"LEFT JOIN groups ON groupcalendar.groupID = groups.groupID " +
				"LEFT JOIN schools ON groups.schoolID = schools.schoolID " +
				"WHERE calendarID = ?";

		ArrayListQuery<CommunityGroup> query = null;

		if(transactionHandler != null) {
			query = transactionHandler.getArrayListQuery(sql, GROUPPOPULATOR);
		} else {
			query = new ArrayListQuery<CommunityGroup>(this.dataSource, sql, GROUPPOPULATOR);
		}

		query.setInt(1, post.getCalendarID());

		return query.executeQuery();
	}

	private String getCalendarRelationTable(RelationType type){

		return type.equals(RelationType.GROUP) ? "groupcalendar" : type.equals(RelationType.SCHOOL) ? "schoolcalendar" : "globalcalendar";
	}
}