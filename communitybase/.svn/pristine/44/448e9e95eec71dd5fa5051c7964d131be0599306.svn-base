package se.dosf.communitybase.modules.emailresume;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.sauronsoftware.cron4j.Scheduler;
import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.beans.SectionEvent;
import se.dosf.communitybase.enums.EventFormat;
import se.dosf.communitybase.enums.NotificationFormat;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.NotificationFragment;
import se.dosf.communitybase.interfaces.NotificationHandler;
import se.dosf.communitybase.modules.sectionevents.CBSectionEventHandler;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.SystemStartupListener;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.string.AnnotatedBeanTagSourceFactory;
import se.unlogic.standardutils.string.SingleTagSource;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.HourStringFormatValidator;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.webutils.http.URIParser;

public class EmailResumeModule extends AnnotatedForegroundModule implements Runnable {

	private static final AnnotatedBeanTagSourceFactory<User> USER_TAG_SOURCE_FACTORY = new AnnotatedBeanTagSourceFactory<User>(User.class, "$user.");

	@InstanceManagerDependency(required = true)
	private CBInterface cbInterface;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Full context path URL", description = "The full URL to the contextpath of this site", required = true)
	private String fullContextPath = "not set";

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Default resume hour", description = "At this hour (0-23) users which has no resume hour attribute set get the e-mail resume. Leave this field empty to disable the default resume hour.", formatValidator = HourStringFormatValidator.class)
	private Integer defaultResumeHour = 16;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Section event limit", description = "The maximum number of section events to include.", formatValidator = PositiveStringIntegerValidator.class)
	private int sectionEventCount = 10;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Notification settings module alias", description = "The full alias to the notification settings module", required = true)
	protected String notificationSettingsModuleAlias = "not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Sender address", description = "E-mail sender address", required = true)
	protected String emailSenderAddress = "not@set.com";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Sender name", description = "E-mail sender name", required = true)
	protected String emailSenderName = "John Doe";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "E-mail subject", description = "E-mail subject", required = true)
	protected String emailSubject = "Not set";

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Message header", description = "The header of generated messages", required = true)
	private String mailHeader = "Not set";

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Message notification section", description = "The template for the notification section of generated messages", required = true)
	private String mailNotificationSection = "Not set";

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Message events section", description = "The template for the events section of generated messages", required = true)
	private String mailEventsSection = "Not set";

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Message footer", description = "The footer of generated messages", required = true)
	private String mailFooter = "Not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "UL tag style attribute", description = "The value of the style attribute to be set on autogenerated UL tags")
	protected String ulStyle = "list-style-type: none;";

	@InstanceManagerDependency
	protected CBSectionEventHandler sectionEventHandler;

	@InstanceManagerDependency
	protected NotificationHandler notificationHandler;

	private Scheduler scheduler;

	private boolean abort = false;

	private boolean running = false;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(EmailResumeModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + EmailResumeModule.class.getName());
		}
	}
	
	@SystemStartupListener
	public void systemStarted() {
		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		
		scheduler.setDaemon(true);

		scheduler.schedule("0 * * * *", this);
		scheduler.start();
	}

	@Override
	public void unload() throws Exception {

		abort = true;

		try {
			if (scheduler != null) {

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}

		super.unload();

		systemInterface.getInstanceHandler().removeInstance(EmailResumeModule.class, this);
	}

	@WebPublic
	public ForegroundModuleResponse trigger(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer hour = uriParser.getInt(2);

		if (hour == null || hour < 0 || hour > 23) {

			throw new URINotFoundException(uriParser);
		}

		if (running) {

			log.info("Resumer already running, ignoring request from user " + user + " to manually trigger resumé for " + hour + " a clock");

		} else {

			log.info("User " + user + " manually triggering resumé for " + hour + " a clock");

			sendResume(hour);
		}

		return null;
	}

	@Override
	public void run() {

		int currentHour = TimeUtils.getHour(System.currentTimeMillis());

		sendResume(currentHour);
	}

	protected void sendResume(int currentHour) {

		log.info("Generating e-mail resume for users that have selected to get e-mail resume at " + currentHour);

		try {
			running = true;

			//TODO check and lock dependencies

			List<User> users = systemInterface.getUserHandler().getUsersByAttribute(CBConstants.USER_RESUME_HOUR_ATTRIBUTE, Integer.toString(currentHour), true, true);

			if (defaultResumeHour != null && defaultResumeHour == currentHour) {

				users = CollectionUtils.combine(users, systemInterface.getUserHandler().getUsersWithoutAttribute(CBConstants.USER_RESUME_HOUR_ATTRIBUTE, true, true));
			}

			if (users == null) {

				log.info("No users found.");
				return;
			}

			if (isAbort()) {
				log.info("Module shutdown detected aborting user synchronization");
				return;
			}

			for (User user : users) {

				if (log.isDebugEnabled()) {

					log.debug("Generating e-mail resumé for user " + user);
				}

				if (user.getEmail() == null || !EmailUtils.isValidEmailAddress(user.getEmail())) {

					log.debug("User " + user + " has no valid e-mail address set, skipping.");
					continue;
				}

				AttributeHandler attributeHandler = user.getAttributeHandler();

				Timestamp breakpoint = getBreakPoint(user, attributeHandler);

				//This can be made more efficient by first getting notifications and filtering them first before getting the fragments
				List<NotificationFragment> notificationFragments = notificationHandler.getFragments(user.getUserID(), null, breakpoint, false, false, NotificationFormat.EMAIL, fullContextPath);

				if (notificationFragments != null && attributeHandler != null) {

					Iterator<NotificationFragment> iterator = notificationFragments.iterator();

					while (iterator.hasNext()) {

						NotificationFragment notificationFragment = iterator.next();

						if (attributeHandler.getPrimitiveBoolean(CBConstants.USER_RESUME_EXCLUDED_MODULE_NOTIFICATIONS_ATTRIBUTE_PREFIX + notificationFragment.getNotification().getSourceModuleID())) {

							iterator.remove();
						}
					}
				}

				Map<SectionDescriptor, List<SectionEvent>> sectionEvents = getSectionEvents(user, attributeHandler, breakpoint);

				if (CollectionUtils.isEmpty(notificationFragments) && sectionEvents == null) {

					if (log.isDebugEnabled()) {

						log.debug("No new notifications or events found (after filtering) for user " + user);
					}

					continue;
				}

				//Generate email
				try {
					SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

					email.setSenderName(emailSenderName);
					email.setSenderAddress(emailSenderAddress);

					TagReplacer tagReplacer = new TagReplacer();
					tagReplacer.addTagSource(USER_TAG_SOURCE_FACTORY.getTagSource(user));
					tagReplacer.addTagSource(new SingleTagSource("$notificationSettings", fullContextPath + notificationSettingsModuleAlias));
					tagReplacer.addTagSource(new SingleTagSource("$siteURL", fullContextPath));

					email.setSubject(tagReplacer.replace(emailSubject));

					StringBuilder messageBuilder = new StringBuilder();

					messageBuilder.append(tagReplacer.replace(mailHeader));

					if (!CollectionUtils.isEmpty(notificationFragments)) {

						StringBuilder notificationSectionBuilder = new StringBuilder();

						for (NotificationFragment notificationFragment : notificationFragments) {

							notificationSectionBuilder.append(notificationFragment.getHTML());
						}

						String notificationString = mailNotificationSection.replace("$notifications", "<ul style=\"" + getUlStyle() + "\">" + notificationSectionBuilder.toString() + "</ul>");

						messageBuilder.append(notificationString);
					}

					if (sectionEvents != null) {

						for (Entry<SectionDescriptor, List<SectionEvent>> sectionEntry : sectionEvents.entrySet()) {

							StringBuilder eventSectionBuilder = new StringBuilder();

							for (SectionEvent sectionEvent : sectionEntry.getValue()) {

								eventSectionBuilder.append(sectionEvent.getFragment(fullContextPath, EventFormat.EMAIL).getHTML());
							}

							String eventsSection = this.mailEventsSection.replace("$section.name", sectionEntry.getKey().getName()).replace("$events", "<ul style=\"" + getUlStyle() + "\">" + eventSectionBuilder.toString() + "</ul>");

							messageBuilder.append(eventsSection);
						}
					}

					messageBuilder.append(tagReplacer.replace(mailFooter));

					email.setMessage(messageBuilder.toString());

					email.addRecipient(user.getEmail());
					email.setMessageContentType(SimpleEmail.HTML);

					this.systemInterface.getEmailHandler().send(email);

					log.info("E-mail resumé for user " + user + " successfully generated and sent.");

					if (attributeHandler instanceof MutableAttributeHandler) {

						((MutableAttributeHandler) attributeHandler).setAttribute(CBConstants.USER_RESUME_LAST_SENT_ATTRIBUTE, System.currentTimeMillis());

						systemInterface.getUserHandler().updateUser(user, false, false, true);

					} else {

						log.warn("User " + user + " does not have a mutable attribute handler, last resume attribute will not be set.");
					}

				} catch (Exception e) {

					log.error("Error sending e-mail resume to user " + user, e);
				}

				if (isAbort()) {
					log.info("Module shutdown detected aborting user synchronization");
					return;
				}
			}

		} catch (Exception e) {

			log.error("Error generating e-mail resumé at " + currentHour + ", resume aborted.", e);

		} finally {

			running = false;
		}
	}

	private String getUlStyle() {

		if (ulStyle == null) {

			return "";
		}

		return ulStyle;
	}

	private Map<SectionDescriptor, List<SectionEvent>> getSectionEvents(User user, AttributeHandler attributeHandler, Timestamp breakpoint) {

		List<Integer> userSections = CBAccessUtils.getUserSections(user, cbInterface);

		if (userSections == null) {

			return null;
		}

		Map<SectionDescriptor, List<SectionEvent>> eventMap = new HashMap<SectionDescriptor, List<SectionEvent>>();

		for (Integer sectionID : userSections) {

			if (attributeHandler != null && attributeHandler.getPrimitiveBoolean(CBConstants.USER_RESUME_EXCLUDED_SECTION_ATTRIBUTE_PREFIX + sectionID)) {

				continue;
			}

			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface == null) {

				continue;
			}

			List<SectionEvent> sectionEvents = sectionEventHandler.getEvents(sectionID, 0, sectionEventCount);

			if (CollectionUtils.isEmpty(sectionEvents)) {

				continue;
			}

			if (breakpoint != null) {

				Iterator<SectionEvent> iterator = sectionEvents.iterator();

				while (iterator.hasNext()) {

					SectionEvent event = iterator.next();

					if (event.getTimestamp().before(breakpoint)) {

						iterator.remove();
					}
				}

				if (sectionEvents.isEmpty()) {

					continue;
				}
			}

			eventMap.put(sectionInterface.getSectionDescriptor(), sectionEvents);
		}

		if (eventMap.isEmpty()) {

			return null;
		}

		return eventMap;
	}

	private Timestamp getBreakPoint(User user, AttributeHandler attributeHandler) {

		if (attributeHandler == null) {

			return user.getLastLogin();
		}

		Long lastResumeAttribute = attributeHandler.getLong(CBConstants.USER_RESUME_LAST_SENT_ATTRIBUTE);

		if (lastResumeAttribute == null) {

			return user.getLastLogin();
		}

		Timestamp lastResume = new Timestamp(lastResumeAttribute);

		if (user.getLastLogin() == null || lastResume.after(user.getLastLogin())) {

			return lastResume;

		}

		return user.getLastLogin();
	}

	private boolean isAbort() {

		if (abort || systemInterface.getSystemStatus() != SystemStatus.STARTED) {

			return true;
		}

		return false;
	}

	public String getFullContextPath() {

		return fullContextPath;
	}

	public String getEmailSenderAddress() {

		return emailSenderAddress;
	}

	public String getEmailSenderName() {

		return emailSenderName;
	}
}
