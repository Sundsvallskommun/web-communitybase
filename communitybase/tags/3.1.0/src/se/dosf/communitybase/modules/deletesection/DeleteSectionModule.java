package se.dosf.communitybase.modules.deletesection;

import it.sauronsoftware.cron4j.Scheduler;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.enums.NotificationFormat;
import se.dosf.communitybase.events.CBSectionPreDeleteEvent;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.Notification;
import se.dosf.communitybase.interfaces.NotificationHandler;
import se.dosf.communitybase.interfaces.NotificationTransformer;
import se.dosf.communitybase.interfaces.Role;
import se.dosf.communitybase.modules.userprofile.UserProfileProvider;
import se.dosf.communitybase.utils.CBSectionAttributeHelper;
import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.utils.SimpleViewFragmentTransformer;
import se.unlogic.hierarchy.core.utils.ViewFragmentTransformer;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.systemadmin.SectionUpdater;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;


public class DeleteSectionModule extends AnnotatedForegroundModule implements Runnable, NotificationTransformer {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Room delete interval", description = "How often to check the database for rooms to delete", required = true, formatValidator = CronStringValidator.class)
	private String sectionDeleteInterval = "0 * * * *";

	@ModuleSetting
	@TextFieldSettingDescriptor(name="Delete delay", description="The number of days rooms marked to be deleted are kept before being deleted", formatValidator=PositiveStringIntegerValidator.class, required = true)
	private Integer deleteDelay = 31;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name="Delete section message", description="Delete message to displayed on the warning/confirmation page. The $section.name tag is supported.", required = true)
	private String message = "not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Notification stylesheet", description = "The stylesheet used to transform notifications")
	private String notificationStylesheet = "DeleteSectionNotification.sv.xsl";

	@InstanceManagerDependency(required = true)
	protected CBInterface cbInterface;

	private Scheduler scheduler;

	@InstanceManagerDependency(required = false)
	protected NotificationHandler notificationHandler;

	private SimpleViewFragmentTransformer notificationFragmentTransformer;

	@InstanceManagerDependency(required = false)
	protected UserProfileProvider userProfileProvider;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(DeleteSectionModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + DeleteSectionModule.class.getName());
		}

		initScheduler();
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		stopScheduler();
		initScheduler();
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();

		systemInterface.getInstanceHandler().removeInstance(DeleteSectionModule.class, this);

		super.unload();
	}

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		if (notificationStylesheet == null) {

			log.warn("No stylesheet set for notification transformations");
			notificationFragmentTransformer = null;

		} else {

			try {
				notificationFragmentTransformer = new SimpleViewFragmentTransformer(notificationStylesheet, systemInterface.getEncoding(), this.getClass(), moduleDescriptor, sectionInterface);

			} catch (Exception e) {

				log.error("Error parsing stylesheet for notification transformations", e);
				notificationFragmentTransformer = null;
			}
		}
	}

	@WebPublic(alias="warning")
	public ForegroundModuleResponse showWarning(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		SimpleSectionDescriptor sectionDescriptor = getSection(uriParser, user);

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req));
		documentElement.appendChild(sectionDescriptor.toXML(doc));
		XMLUtils.appendNewElement(doc, documentElement, "FullAlias", getFullAlias());

		XMLUtils.appendNewElement(doc, documentElement, "Message", message.replace("$section.name", sectionDescriptor.getName()));

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	@WebPublic(alias="delete")
	public ForegroundModuleResponse deleteRoom(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		SimpleSectionDescriptor sectionDescriptor = getSection(uriParser, user);

		if (sectionDescriptor != null && CBSectionAttributeHelper.getDeleted(sectionDescriptor) == null) {

			log.info("User " + user + " marking section " + sectionDescriptor + " for deletion");

			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionDescriptor.getSectionID());

			//Stop the section if it is started
			if(sectionInterface != null){

				try{
					sectionInterface.getParentSectionInterface().getSectionCache().unload(sectionDescriptor);
				}catch(KeyNotCachedException e){}
			}

			sectionDescriptor.setEnabled(false);

			CBSectionAttributeHelper.setDeleted(sectionDescriptor, TimeUtils.getCurrentTimestamp());

			systemInterface.getCoreDaoFactory().getSectionDAO().update(sectionDescriptor);

			if (notificationHandler != null) {

				List<Integer> userIDs = cbInterface.getSectionMembers(sectionDescriptor.getSectionID());

				if (!CollectionUtils.isEmpty(userIDs)) {

					for (Integer userID : userIDs) {

						notificationHandler.addNotification(userID, null, this.moduleDescriptor.getModuleID(), sectionDescriptor.getName(), user.getUserID(), null);
					}
				}
			}

			this.systemInterface.getEventHandler().sendEvent(SimpleSectionDescriptor.class, new CBSectionPreDeleteEvent(sectionDescriptor.getSectionID()), EventTarget.ALL);
		}

		res.sendRedirect(req.getContextPath() + "/");

		return null;
	}

	public String getDeleteSectionAlias(int sectionID) {

		return this.getFullAlias() + "/warning/" + sectionID;
	}

	public String getRestoreSectionAlias(int sectionID) {

		return this.getFullAlias() + "/restore/" + sectionID;
	}

	private SimpleSectionDescriptor getSection(URIParser uriParser, User user) throws URINotFoundException, SQLException, AccessDeniedException {

		Integer sectionID = uriParser.getInt(2);

		if(sectionID == null){

			throw new URINotFoundException(uriParser);
		}

		SimpleSectionDescriptor sectionDescriptor = systemInterface.getCoreDaoFactory().getSectionDAO().getSection(sectionID, false);

		if (sectionDescriptor == null) {

			throw new URINotFoundException(uriParser);
		}

		Role role = cbInterface.getRole(sectionID, user);

		if(role == null || !role.hasDeleteRoomAccess()){

			throw new AccessDeniedException("User is not allowed to delete section ID" + sectionID);
		}

		return sectionDescriptor;
	}

	protected synchronized void initScheduler() {

		scheduler = new Scheduler();

		scheduler.schedule(this.sectionDeleteInterval, this);
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

	private void updateSection(SimpleSectionDescriptor sectionDescriptor, User user) throws SQLException {

		systemInterface.getCoreDaoFactory().getSectionDAO().update(sectionDescriptor);

		SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionDescriptor.getSectionID());

		if (sectionInterface != null) {

			SectionUpdater sectionUpdater = new SectionUpdater(sectionInterface.getParentSectionInterface().getSectionCache(), sectionDescriptor, user);
			sectionUpdater.isDaemon();
			sectionUpdater.start();
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (user.isAdmin()) {

			run();
		}

		return null;
	}

	@Override
	public void run() {

		try{
			log.info("Checking for sections to delete...");

			List<SimpleSectionDescriptor> sections = systemInterface.getCoreDaoFactory().getSectionDAO().getSectionsByAttribute(CBConstants.SECTION_ATTRIBUTE_DELETED, false);
			
			if(sections == null){
				log.debug("No sections to delete found");
				return;
			}

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DATE, -this.deleteDelay);

			Timestamp deletionTime = new Timestamp(calendar.getTimeInMillis());

			for (SimpleSectionDescriptor sectionDescriptor : sections) {

				//Check if the section is enabled
				if(sectionDescriptor.isEnabled()){

					log.info("Section " + sectionDescriptor + " is enabled in DB even though it's marked to be deleted, removing deleted flag.");

					CBSectionAttributeHelper.setDeleted(sectionDescriptor, null);

					updateSection(sectionDescriptor, null);

					continue;
				}

				//Check if section is started
				if (systemInterface.getSectionInterface(sectionDescriptor.getSectionID()) != null) {

					log.info("Section " + sectionDescriptor + " is started even though it's marked to be deleted, removing deleted flag.");

					CBSectionAttributeHelper.setDeleted(sectionDescriptor, null);

					updateSection(sectionDescriptor, null);

					continue;
				}

				//Check if section has passed deletion date
				if (deletionTime.after(CBSectionAttributeHelper.getDeleted(sectionDescriptor))) {

					log.info("Deleting section " + sectionDescriptor);

					systemInterface.getCoreDaoFactory().getSectionDAO().delete(sectionDescriptor);

					cbInterface.deleteSectionLogo(sectionDescriptor.getSectionID());

					this.systemInterface.getEventHandler().sendEvent(SimpleSectionDescriptor.class, new CRUDEvent<SimpleSectionDescriptor>(CRUDAction.DELETE, sectionDescriptor), EventTarget.ALL);
				}
			}

		}catch(Throwable t){

			log.error("Error checking for sections to delete", t);
		}

	}

	@Override
	public ViewFragment getFragment(Notification notification, NotificationFormat format, String fullContextPath) throws Exception {

		ViewFragmentTransformer transformer = this.notificationFragmentTransformer;

		if (transformer == null) {

			log.warn("No event fragment transformer available, unable to transform notification " + notification);
			return null;
		}

		if (log.isDebugEnabled()) {

			log.debug("Transforming notification " + notification);
		}

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		XMLUtils.appendNewElement(doc, documentElement, "ContextPath", fullContextPath);

		if (userProfileProvider != null) {

			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ProfileImageAlias", userProfileProvider.getProfileImageAlias());
			XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ShowProfileAlias", userProfileProvider.getShowProfileAlias());
		}

		User poster = systemInterface.getUserHandler().getUser(notification.getExternalNotificationID(), false, false);

		if (poster != null) {

			documentElement.appendChild(poster.toXML(doc));
		}
		
		XMLUtils.appendNewElement(doc, documentElement, "Format", format);
		XMLUtils.appendNewElement(doc, documentElement, "Posted", DateUtils.DATE_TIME_FORMATTER.format(notification.getAdded()));
		XMLUtils.appendNewElement(doc, documentElement, "ModuleName", moduleDescriptor.getName());
		XMLUtils.appendNewElement(doc, documentElement, "SectionName", notification.getNotificationType());

		if (!notification.isRead()) {

			XMLUtils.appendNewElement(doc, documentElement, "Unread");
		}

		return transformer.createViewFragment(doc);
	}

	public Integer getDaysRemainingBeforePermantentDeletion(SectionDescriptor sectionDescriptor) {

		Timestamp deletionTime = CBSectionAttributeHelper.getDeleted(sectionDescriptor);

		if (deletionTime == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -this.deleteDelay);

		Timestamp permanentDeletionTime = new Timestamp(calendar.getTimeInMillis());
		
		long days = DateUtils.daysBetween(permanentDeletionTime, deletionTime);

		if (days < 0) {
			return 0;
		}

		return (int) days;
	}

}
