package se.dosf.communitybase.interfaces;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.enums.NotificationFormat;


public interface NotificationHandler {

	public void addNotification(int userID, Integer sectionID, int sourceModuleID, String notificationType, Integer externalNotificationID, Map<String,String> attributes) throws SQLException;
	
	public void addNotifications(List<Integer> userIDs, Integer sectionID, int sourceModuleID, String notificationType, Integer externalNotificationID, Map<String,String> attributes) throws SQLException;

	//User deleted from section
	public void deleteNotifications(int userID, Integer sectionID) throws SQLException;

	//Source content deleted
	public void deleteNotifications(Integer sectionID, int sourceModuleID, int externalNotificationID, String notificationType, Integer userID) throws SQLException;
	
	//Source module added
	public void enableNotifications(Integer sectionID, int sourceModuleID) throws SQLException;

	//Source module removed
	public void disableNotifications(Integer sectionID, int sourceModuleID) throws SQLException;

	//Get notifications
	public List<NotificationFragment> getFragments(int userID, Integer count, Timestamp breakpoint, boolean unreadOnly, boolean markAsRead, NotificationFormat format, String fullContextPath) throws SQLException;

	//Get unread count
	public Integer getUnreadCount(int userID) throws SQLException;

	public Notification getNotification(int userID, Integer sectionID, int sourceModuleID, Integer externalNotificationID, String notificationType, Boolean readStatus) throws SQLException;

	public List<? extends Notification> getNotifications(Integer sectionID, int sourceModuleID, Integer externalNotificationID, String notificationType, Boolean readStatus) throws SQLException;
	
	public String getUrl(HttpServletRequest req);
}
