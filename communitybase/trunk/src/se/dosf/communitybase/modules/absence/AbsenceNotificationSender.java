package se.dosf.communitybase.modules.absence;

import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityGroupComparator;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.absence.beans.Absence;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.unlogic.emailutils.framework.EmailHandler;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.dao.enums.Order;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.timer.RunnableTimerTask;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class AbsenceNotificationSender extends AnnotatedForegroundModule implements Runnable, SystemStartupListener {

	protected QueryParameterFactory<Absence, CommunityGroup> groupParamFactory;
	protected QueryParameterFactory<Absence, Timestamp> startTimeParamFactory;
	protected QueryParameterFactory<Absence, Timestamp> endTimeParamFactory;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Start time", description = "Tells when module should start checking for absences (default is 8 am)", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected int startTime = 8;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Sender name", description = "The name displayed in the sender field of sent e-mail", required = true)
	protected String senderName = "Not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Sender address", description = "The sender address", required = true)
	protected String senderAddress = "not.set@not.set.com";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Subject", description = "The text displayed in the subject of sent email", required = true)
	protected String emailSubject = "Not set";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Base URL", description = "The full URL to this site", required = true)
	protected String baseURL = "http://not.set.com";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Absence adminmodule alias", description = "Alias of absence adminmodule relative from contextpath", required = true)
	protected String adminModuleAlias = "/notset";

	@ModuleSetting(allowsNull = true)
	@EnumMultiListSettingDescriptor(name = "Roles to notify", description = "Roles to send notifications to", required = false)
	protected List<GroupAccessLevel> accessLevels;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Notify schooladmins", description = "Send notifications to schooladmins")
	protected boolean sendToSchoolAdmins = true;

	protected CommunitySchoolDAO schoolDAO;

	protected CommunityGroupDAO groupDAO;

	protected CommunityUserDAO userDAO;

	protected AnnotatedDAOWrapper<Absence, Integer> absenceCRUDDAO;

	protected final Timer timer = new Timer(true);

	protected TimerTask timerTask;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if(systemInterface.getSystemStatus() == SystemStatus.Started){

			this.initTimer();

		}else{

			this.systemInterface.addStartupListener(this);
		}

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.stopTimerTask();

		this.initTimer();
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, this.sectionInterface.getSystemInterface().getUserHandler());

		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO.setUserDao(userDAO);
		this.userDAO.setGroupDao(groupDAO);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);
		this.userDAO.setSchoolDAO(schoolDAO);
		this.schoolDAO.setGroupDAO(groupDAO);

		AnnotatedDAO<Absence> absenceDAO = daoFactory.getDAO(Absence.class);
		this.absenceCRUDDAO = absenceDAO.getWrapper("absenceID", Integer.class);

		this.groupParamFactory = absenceDAO.getParamFactory("group", CommunityGroup.class);
		this.startTimeParamFactory = absenceDAO.getParamFactory("startTime", Timestamp.class);
		this.endTimeParamFactory = absenceDAO.getParamFactory("endTime", Timestamp.class);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return new SimpleForegroundModuleResponse("<div class=\"contentitem\">This module does not have a frontend, check the log instead.</div>", this.getDefaultBreadcrumb());
	}

	@WebPublic
	public ForegroundModuleResponse trigger(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		log.info("Manually triggering sending of absence notifications at " + TimeUtils.getCurrentTimestamp());

		this.run();

		return new SimpleForegroundModuleResponse("<div class=\"contentitem\">Notifications sent!</div>", this.getDefaultBreadcrumb());
	}

	public void run() {

		if (this.systemInterface.getSystemStatus() == SystemStatus.Started) {

			log.info("Generating absence notifications for users");

			Transformer transformer = null;

			try {
				transformer = this.sectionInterface.getModuleXSLTCache().getModuleTranformer(this.moduleDescriptor);
			} catch (TransformerConfigurationException e) {
				log.error("Unable to get transformer from ModuleXSLTCache, aborting", e);
				return;
			}

			if (transformer == null) {
				log.error("Found no cached stylesheet in ModuleXSLTCache, aborting");
				return;
			}

			EmailHandler emailHandler = this.sectionInterface.getSystemInterface().getEmailHandler();

			if (!emailHandler.hasSenders()) {
				log.error("No e-mail senders registered in email handler, aborting");
				return;
			}

			try {

				List<CommunityUser> users = this.userDAO.getUsers(true, true, sendToSchoolAdmins, accessLevels);

				if (!CollectionUtils.isEmpty(users)) {

					Calendar calendar = Calendar.getInstance();
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);

					Timestamp date = new Timestamp(calendar.getTimeInMillis());

					for (CommunityUser user : users) {

						Document doc = XMLUtils.createDomDocument();
						Element document = doc.createElement("Document");
						doc.appendChild(document);

						if(user.isEnabled()) {

							Collection<CommunityGroup> userGroups = user.getCommunityGroups();

							TreeMap<CommunityGroup, Element> addedGroups = new TreeMap<CommunityGroup, Element>(CommunityGroupComparator.getInstance(Order.ASC));

							if (!CollectionUtils.isEmpty(userGroups)) {

								for (CommunityGroup group : userGroups) {

									if (AccessUtils.checkAccess(user, group, accessLevels.toArray(new GroupAccessLevel[accessLevels.size()]))) {

										List<Absence> absences = this.getAbsences(date, group);

										if (!CollectionUtils.isEmpty(absences)) {

											Element groupElement = group.toXML(doc);

											XMLUtils.append(doc, groupElement, absences);

											addedGroups.put(group, groupElement);

										}

									}

								}

							}

							if (sendToSchoolAdmins) {

								Collection<School> schools = user.getSchools();

								if (!CollectionUtils.isEmpty(schools)) {

									for (School school : schools) {

										Collection<CommunityGroup> schoolGroups = school.getGroups();

										if (!CollectionUtils.isEmpty(schoolGroups)) {

											for (CommunityGroup group : schoolGroups) {

												school.setGroups(null);
												group.setSchool(school);

												if (!addedGroups.containsKey(group)) {

													List<Absence> absences = this.getAbsences(date, group);

													if (!CollectionUtils.isEmpty(absences)) {

														Element groupElement = group.toXML(doc);

														XMLUtils.append(doc, groupElement, absences);

														addedGroups.put(group, groupElement);

													}

												}

											}

										}

									}

								}

							}

							if (!addedGroups.isEmpty()) {

								for (CommunityGroup group : addedGroups.keySet()) {

									document.appendChild(addedGroups.get(group));

								}

								this.sendNotificationEmail(user, doc, transformer, emailHandler);

							}

						}

					}

				}

			} catch (SQLException e) {

				log.error("Unable to get absences when generating absencenotifications", e);

			}

		}

	}

	private void sendNotificationEmail(User user, Document doc, Transformer transformer, EmailHandler emailHandler) {

		String date = DateUtils.DATE_FORMATTER.format(System.currentTimeMillis());

		Element documentElement = doc.getDocumentElement();

		XMLUtils.appendNewElement(doc, documentElement, "baseURL", this.baseURL);
		XMLUtils.appendNewElement(doc, documentElement, "adminModuleAlias", adminModuleAlias);
		XMLUtils.appendNewElement(doc, documentElement, "date", date);
		documentElement.appendChild(user.toXML(doc));

		StringWriter messageWriter = new StringWriter();

		try {

			transformer.transform(new DOMSource(doc), new StreamResult(messageWriter));

		} catch (TransformerException e) {

			log.error("Unable to transform absence notification email for user " + user, e);
			return;
		}

		try {

			SimpleEmail email = new SimpleEmail();

			email.setSubject(this.emailSubject + " " + date);
			email.setMessage(messageWriter.toString());
			email.setSenderAddress(this.senderAddress);
			email.setSenderName(this.senderName);
			email.addRecipient(user.getEmail());
			email.setMessageContentType("text/html");

			log.info("Sending absence notification email to user " + user);

			emailHandler.send(email);

		} catch (NoEmailSendersFoundException e) {

			log.error("Unable to send absence notification email to user " + user, e);

		} catch (UnableToProcessEmailException e) {

			log.error("Unable to send absence notification email to user " + user, e);
		} catch (InvalidEmailAddressException e) {

			log.error("Unable to create absence notification email to user " + user, e);
		}

	}

	private List<Absence> getAbsences(Timestamp date, CommunityGroup group) throws SQLException {

		LowLevelQuery<Absence> query = new LowLevelQuery<Absence>();

		query.setSql("SELECT * FROM " + this.absenceCRUDDAO.getAnnotatedDAO().getTableName() + " WHERE groupID = ? AND (? BETWEEN startTime and endTime OR startTime >= ? AND endTime < ?)");
		query.addParameter(group.getGroupID());
		query.addParameter(date);
		query.addParameter(date);
		query.addParameter(new Timestamp(date.getTime() + MillisecondTimeUnits.DAY));

		return this.absenceCRUDDAO.getAnnotatedDAO().getAll(query);

	}

	public void systemStarted() {

		this.initTimer();

	}

	private void initTimer() {

		this.timerTask = new RunnableTimerTask(this);
		int difference = (this.startTime - TimeUtils.getHour(System.currentTimeMillis()));
		long starttime = difference < 0 ? ((difference + 24) * 3600 * 1000) : (difference * 3600 * 1000);
		this.timer.scheduleAtFixedRate(timerTask, starttime, MillisecondTimeUnits.DAY);

	}

	private void stopTimerTask() {

		if(this.timerTask != null) {

			this.timerTask.cancel();

		}

	}

	@Override
	public void unload() throws Exception {

		this.stopTimerTask();

		if(this.timer != null) {
			this.timer.cancel();
		}

		super.unload();

	}

}
