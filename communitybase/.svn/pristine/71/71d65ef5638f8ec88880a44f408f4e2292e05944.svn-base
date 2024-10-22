package se.dosf.communitybase.modules.notifications;

import it.sauronsoftware.cron4j.Scheduler;

import java.io.Writer;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.enums.NotificationFormat;
import se.dosf.communitybase.interfaces.Notification;
import se.dosf.communitybase.interfaces.NotificationFragment;
import se.dosf.communitybase.interfaces.NotificationHandler;
import se.dosf.communitybase.interfaces.NotificationTransformer;
import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.MySQLRowLimiter;
import se.unlogic.standardutils.dao.QueryOperators;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.date.PooledSimpleDateFormat;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;


public class NotificationHandlerModule extends AnnotatedForegroundModule implements NotificationHandler, Runnable{

	public static final PooledSimpleDateFormat DATE_FORMATTER = new PooledSimpleDateFormat("EEEE dd MMMM");

	@ModuleSetting
	@TextFieldSettingDescriptor(name="Notification lifetime", description="How many days notifications should be stored before being deleted", required=true, formatValidator=PositiveStringIntegerValidator.class)
	private Integer notificationLifetime = 31;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Notification delete interval", description = "How often this module should check for old notifications to delete (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String notificationDeleteInterval = "0 * * * *";

	@ModuleSetting
	@TextFieldSettingDescriptor(name="JSON notification count", description="How many notifications to include in JSON requests", required=true, formatValidator=PositiveStringIntegerValidator.class)
	private Integer notificationCount = 3;

	private Scheduler scheduler;

	private AnnotatedDAO<StoredNotification> notificationDAO;
	private QueryParameterFactory<StoredNotification, Timestamp> addedQueryParamFactory;
	private QueryParameterFactory<StoredNotification, Boolean> enabledQueryParamFactory;
	private QueryParameterFactory<StoredNotification, Boolean> readQueryParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> externaIDParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> userQueryParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> sectionQueryParamFactory;
	private QueryParameterFactory<StoredNotification, Integer> sourceModuleQueryParamFactory;
	private QueryParameterFactory<StoredNotification, String> typeQueryParamFactory;

	@Override
	public void init(ForegroundModuleDescriptor descriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(descriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(NotificationHandler.class, this)) {

			throw new RuntimeException("Unable to register " + this.moduleDescriptor + " in global instance handler using key " + NotificationHandler.class.getSimpleName() + ", another instance is already registered using this key.");
		}

		initScheduler();
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		stopScheduler();
		initScheduler();
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();

		systemInterface.getInstanceHandler().removeInstance(NotificationHandler.class, this);

		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, this.getClass().getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		notificationDAO = new SimpleAnnotatedDAOFactory(dataSource).getDAO(StoredNotification.class);

		this.addedQueryParamFactory = notificationDAO.getParamFactory("added", Timestamp.class);
		this.enabledQueryParamFactory = notificationDAO.getParamFactory("enabled", boolean.class);
		this.readQueryParamFactory = notificationDAO.getParamFactory("read", boolean.class);
		this.externaIDParamFactory = notificationDAO.getParamFactory("externalNotificationID", Integer.class);
		this.userQueryParamFactory = notificationDAO.getParamFactory("userID", Integer.class);
		this.sectionQueryParamFactory = notificationDAO.getParamFactory("sectionID", Integer.class);
		this.sourceModuleQueryParamFactory = notificationDAO.getParamFactory("sourceModuleID", Integer.class);
		this.typeQueryParamFactory = notificationDAO.getParamFactory("notificationType", String.class);
	}

	@Override
	public void addNotification(int userID, Integer sectionID, int sourceModuleID, String notificationType, Integer externalNotificationID, Map<String, String> attributes) throws SQLException {

		StoredNotification storedNotification = new StoredNotification();

		storedNotification.setUserID(userID);
		storedNotification.setSectionID(sectionID);
		storedNotification.setSourceModuleID(sourceModuleID);
		storedNotification.setNotificationType(notificationType);
		storedNotification.setExternalNotificationID(externalNotificationID);
		storedNotification.setEnabled(true);
		storedNotification.setAdded(TimeUtils.getCurrentTimestamp());

		if(attributes != null){

			List<StoredNotificationAttribute> attributesList = new ArrayList<StoredNotificationAttribute>(attributes.size());

			for(Entry<String,String> entry : attributes.entrySet()){

				attributesList.add(new StoredNotificationAttribute(entry.getKey(), entry.getValue()));
			}

			storedNotification.setAttributes(attributesList);
		}

		this.notificationDAO.add(storedNotification);
	}

	@Override
	public void deleteNotifications(int userID, Integer sectionID) throws SQLException {

		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();

		query.addParameter(userQueryParamFactory.getParameter(userID));

		if(sectionID != null){

			query.addParameter(sectionQueryParamFactory.getParameter(sectionID));
		}

		this.notificationDAO.delete(query);
	}

	@Override
	public void deleteNotifications(Integer sectionID, int sourceModuleID, int externalNotificationID, String notificationType) throws SQLException {

		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();

		if(sectionID != null){

			query.addParameter(sectionQueryParamFactory.getParameter(sectionID));
		}

		query.addParameter(sourceModuleQueryParamFactory.getParameter(sourceModuleID));
		query.addParameter(externaIDParamFactory.getParameter(externalNotificationID));

		if(notificationType != null){

			query.addParameter(typeQueryParamFactory.getParameter(notificationType));
		}

		this.notificationDAO.delete(query);
	}

	@Override
	public void enableNotifications(Integer sectionID, int sourceModuleID) throws SQLException {

		setEnabled(sectionID, sourceModuleID, true);
	}

	@Override
	public void disableNotifications(Integer sectionID, int sourceModuleID) throws SQLException {

		setEnabled(sectionID, sourceModuleID, false);
	}

	public void setEnabled(Integer sectionID, int sourceModuleID, boolean enabled) throws SQLException{

		LowLevelQuery<StoredNotification> query = new LowLevelQuery<StoredNotification>();

		query.addParameter(enabled);

		if(sectionID == null){

			query.setSql("UPDATE " + notificationDAO.getTableName() + " SET enabled = ? WHERE sourceModuleID = ?");

		}else{

			query.setSql("UPDATE " + notificationDAO.getTableName() + " SET enabled = ? WHERE sectionID = ? AND sourceModuleID = ?");
			query.addParameter(sectionID);
		}

		query.addParameter(sourceModuleID);

		notificationDAO.update(query);
	}

	@Override
	public List<NotificationFragment> getFragments(int userID, Integer count, Timestamp breakpoint, boolean unreadOnly, boolean markAsRead, NotificationFormat format, String fullContextPath) throws SQLException {

		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();

		query.addParameter(userQueryParamFactory.getParameter(userID));
		query.addParameter(enabledQueryParamFactory.getParameter(true));

		if(unreadOnly){

			query.addParameter(readQueryParamFactory.getParameter(false));
		}

		if(breakpoint != null){
			
			query.addParameter(addedQueryParamFactory.getParameter(breakpoint, QueryOperators.BIGGER_THAN));
		}
		
		if(count != null){

			query.setRowLimiter(new MySQLRowLimiter(count));
		}

		List<StoredNotification> storedNotifications = notificationDAO.getAll(query);

		if(storedNotifications == null){

			return null;
		}

		List<NotificationFragment> fragments = new ArrayList<NotificationFragment>(storedNotifications.size());

		for(StoredNotification notification : storedNotifications){

			Entry<ForegroundModuleDescriptor, ForegroundModule> moduleEntry;

			if(notification.getSectionID() != null){

				//Look up source module using attribute in the given section
				SectionInterface sectionInterface = systemInterface.getSectionInterface(notification.getSectionID());

				if(sectionInterface == null){

					log.warn("Unable to get fragment for notification " + notification + ", section is not available from core section map.");
					continue;
				}

				moduleEntry = sectionInterface.getForegroundModuleCache().getModuleEntryByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, notification.getSourceModuleID().toString());

				if (moduleEntry == null) {
					//	Look up module using true module ID
					moduleEntry = sectionInterface.getForegroundModuleCache().getEntry(notification.getSourceModuleID());
				}

				if(moduleEntry == null){

					log.warn("Unable to get fragment for notification " + notification + ", module not found in foreground module cache of section " + sectionInterface.getSectionDescriptor());
					continue;
				}

			}else{

				//Look up module in root section using true module ID
				moduleEntry = systemInterface.getRootSection().getForegroundModuleCache().getEntry(notification.getSourceModuleID());

				if(moduleEntry == null){

					log.warn("Unable to get fragment for notification " + notification + ", module not found in foreground module cache of root section.");
					continue;
				}
			}

			if(!(moduleEntry.getValue() instanceof NotificationTransformer)){

				log.warn("Unable to get fragment for notification " + notification + ", module " + moduleEntry.getKey() + " doesn't implement " + NotificationTransformer.class.getSimpleName() + " interface.");
				continue;
			}

			try{
				ViewFragment viewFragment = ((NotificationTransformer)moduleEntry.getValue()).getFragment(notification, format, fullContextPath);

				if(viewFragment == null){

					log.warn("Got no fragment for notification " + notification + " from module " + moduleEntry.getKey());
					continue;
				}

				fragments.add(new SimpleNotificationFragment(notification, viewFragment));

			}catch(Exception e){

				log.error("Error getting fragment for notification " + notification + " from module " + moduleEntry.getKey(), e);
				continue;
			}
		}

		if(markAsRead){

			markAsRead(storedNotifications);
		}

		if(fragments.isEmpty()){

			return null;
		}

		return fragments;
	}

	private void markAsRead(List<StoredNotification> storedNotifications) throws SQLException {

		LowLevelQuery<StoredNotification> query = new LowLevelQuery<StoredNotification>();

		query.setSql("UPDATE " + notificationDAO.getTableName() + " SET isRead = true WHERE notificationID IN (?" + StringUtils.repeatString(",?", storedNotifications.size() - 1) + ")");

		for(StoredNotification notification : storedNotifications){

			query.addParameter(notification.getNotificationID());
		}

		notificationDAO.update(query);
	}

	@Override
	public Integer getUnreadCount(int userID) throws SQLException {

		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();

		query.addParameter(userQueryParamFactory.getParameter(userID));
		query.addParameter(enabledQueryParamFactory.getParameter(true));
		query.addParameter(readQueryParamFactory.getParameter(false));

		return notificationDAO.getCount(query);
	}

	@Override
	public void run() {

		log.info("Deleting notifications older than " + notificationLifetime + " days...");

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -notificationLifetime);

		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();

		query.addParameter(addedQueryParamFactory.getParameter(new Timestamp(calendar.getTimeInMillis()), QueryOperators.SMALLER_THAN));

		try{
			Integer deleteCount = notificationDAO.delete(query);
			
			if(deleteCount > 0){
				
				log.info("Deleted " + deleteCount + " notifications");
			}

		}catch(SQLException e){

			log.error("Error deleting old notifications", e);
		}
	}

	protected synchronized void initScheduler() {

		scheduler = new Scheduler();

		scheduler.schedule(this.notificationDeleteInterval, this);
		scheduler.start();
	}

	protected synchronized void stopScheduler(){

		try {

			if(scheduler != null){

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}
	}

	@Override
	public Notification getNotification(int userID, Integer sectionID, int sourceModuleID, Integer externalNotificationID, String notificationType, Boolean readStatus) throws SQLException {

		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();

		query.addParameter(userQueryParamFactory.getParameter(userID));

		if(sectionID != null){

			query.addParameter(sectionQueryParamFactory.getParameter(sectionID));
		}

		query.addParameter(sourceModuleQueryParamFactory.getParameter(sourceModuleID));

		if(externalNotificationID != null){

			query.addParameter(externaIDParamFactory.getParameter(externalNotificationID));
		}

		if(notificationType != null){

			query.addParameter(typeQueryParamFactory.getParameter(notificationType));
		}

		if(readStatus != null){

			query.addParameter(readQueryParamFactory.getParameter(readStatus));
		}

		return notificationDAO.get(query);
	}

	@Override
	public List<StoredNotification> getNotifications(Integer sectionID, int sourceModuleID, Integer externalNotificationID, String notificationType, Boolean readStatus) throws SQLException {

		HighLevelQuery<StoredNotification> query = new HighLevelQuery<StoredNotification>();

		if(sectionID != null){

			query.addParameter(sectionQueryParamFactory.getParameter(sectionID));
		}

		query.addParameter(sourceModuleQueryParamFactory.getParameter(sourceModuleID));

		if(externalNotificationID != null){

			query.addParameter(externaIDParamFactory.getParameter(externalNotificationID));
		}

		if(notificationType != null){

			query.addParameter(typeQueryParamFactory.getParameter(notificationType));
		}

		if(readStatus != null){

			query.addParameter(readQueryParamFactory.getParameter(readStatus));
		}

		return notificationDAO.getAll(query);
	}

	@Override
	public String getUrl(HttpServletRequest req) {

		return req.getContextPath() + this.getFullAlias();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " listing all notifications");

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		List<NotificationFragment> fragments = getFragments(user.getUserID(), null, null, false, true, NotificationFormat.LIST, RequestUtils.getFullContextPathURL(req));

		if(fragments != null){

			String currentDay = null;
			Element dayElement = null;

			for(NotificationFragment fragment : fragments){

				String dayName = DATE_FORMATTER.format(fragment.getNotification().getAdded());

				if(dayElement == null || !dayName.equals(currentDay)){

					dayElement = doc.createElement("Day");
					documentElement.appendChild(dayElement);

					currentDay = dayName;

					XMLUtils.appendNewElement(doc, dayElement, "Name", currentDay);

					dayElement.appendChild(fragment.toXML(doc));

				}else{

					dayElement.appendChild(fragment.toXML(doc));
				}
			}
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	@WebPublic(alias="latest")
	public ForegroundModuleResponse getLatestNotifications(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		List<NotificationFragment> fragments = getFragments(user.getUserID(), notificationCount, null, false, true, NotificationFormat.POPUP, RequestUtils.getFullContextPathURL(req));

		res.setContentType("text/html");

		Writer writer = res.getWriter();

		if(fragments == null){

			writer.write("");

		}else{

			for(NotificationFragment fragment : fragments){

				writer.write(fragment.getHTML());
			}
		}

		res.getWriter().flush();
		res.getWriter().close();

		return null;
	}
}
