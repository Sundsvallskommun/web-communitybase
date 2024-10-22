package se.dosf.communitybase.modules.userremover;

import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

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

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.unlogic.emailutils.framework.EmailHandler;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.validation.StringEmailValidator;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.timer.RunnableTimerTask;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class UserRemover extends AnnotatedForegroundModule implements Runnable{

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();

	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("emailSubject", "Subject", "The text displayed in the subject of sent email", true, "User deleted notification email from CommunityBase", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderName", "Sender name", "The name displayed in the sender field of sent e-mail", true, "CommunityBase", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderAddress", "Sender address", "The sender address", true, "not.set@not.set.com", new StringEmailValidator()));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("systemURL", "Base URL", "The full URL to this site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityURL", "Municipality URL", "The full URL to the municipality site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityName", "Municipality name", "The name of the municipality which uses communitybase", true, "Not set", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("emailContentType", "Content type", "The content type of generated e-mails", true, "text/html", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("starttime", "Start time", "Tells when module should start checking users (default is 3 am)", true, "3", new StringIntegerValidator(1,24)));
	}

	private CommunityUserDAO userDAO;
	private CommunityGroupDAO groupDAO;
	private CommunitySchoolDAO schoolDAO;

	@ModuleSetting
	protected String senderName = "CommunityBase";

	@ModuleSetting
	protected String senderAddress = "not.set@not.set.com";

	@ModuleSetting
	protected String systemURL = "http://not.set.com";

	@ModuleSetting
	protected String municipalityURL = "http://not.set.com";

	@ModuleSetting
	protected String municipalityName = "Not set";

	@ModuleSetting
	protected String emailSubject = "User deleted notification email from CommunityBase";

	@ModuleSetting
	protected int starttime = 3;

	private long interval = 86400000;

	private Timer timer;
	private RunnableTimerTask timerTask;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor,	SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		this.createDAOs(dataSource);

		//Create task and timer
		this.initTimer();

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.createDAOs(dataSource);

		//Cancel task and timer
		this.timerTask.cancel();
		this.initTimer();

	}

	@Override
	protected void createDAOs(DataSource dataSource) {

		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);

		this.userDAO = new CommunityUserDAO(dataSource);
		this.userDAO.setGroupDao(groupDAO);
		this.userDAO.setSchoolDAO(schoolDAO);

	}

	private void initTimer(){

		this.timerTask = new RunnableTimerTask(this);
		this.timer = new Timer(true);
		int difference = (this.starttime - TimeUtils.getHour(System.currentTimeMillis()));
		long starttime = difference < 0 ? ((difference + 24) * 3600 * 1000) : (difference * 3600 * 1000);
		this.timer.scheduleAtFixedRate(timerTask, starttime, interval);

	}

	@Override
	public void unload() throws Exception {

		//Cancel task and timer
		this.timerTask.cancel();
		this.timer.cancel();
		super.unload();
	}



	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req,	HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		res.getWriter().write("This module does not have a frontend, check the log instead.");

		return null;

	}

	@Override
	public void run() {

		log.info("Checking users access...");

		Transformer transformer = null;

		try {
			transformer = this.sectionInterface.getForegroundModuleXSLTCache().getModuleTranformer(this.moduleDescriptor);
		} catch (TransformerConfigurationException e1) {

			log.error("Unable to get transformer from ModuleXSLTCache, aborting", e1);
			return;
		}

		if(transformer == null){

			log.error("Found no cached stylesheet in ModuleXSLTCache, aborting");
			return;
		}

		EmailHandler emailHandler = this.sectionInterface.getSystemInterface().getEmailHandler();

		if(!emailHandler.hasSenders()){

			log.error("No e-mail senders registered in email handler, aborting");

			return;
		}

		try{

			ArrayList<CommunityUser> users = this.userDAO.getAll(true, true);

			int removed = 0;

			if(users != null){

				for(CommunityUser user : users){

					log.debug("Checking user " + user + "...");

					if(!user.isAdmin() && user.getGroups() == null && user.getSchools() == null){

						this.userDAO.delete(user);

						removed++;

						log.info("User " + user + " was deleted because user has no access left");

						// send email to user
						this.sendNotificationEmail(user, transformer, emailHandler);

					}

				}

				log.info(users.size() + " users was checked where " + removed + " was removed.");

			}

		}catch(SQLException e){

			log.error("Error getting users from database");

		}catch (InvalidEmailAddressException e) {

			log.error("Error processing user deleted notification email",e);
		}

	}

	private void sendNotificationEmail(CommunityUser user, Transformer transformer, EmailHandler emailHandler) throws InvalidEmailAddressException {

		Document doc = XMLUtils.createDomDocument();

		Element documentElement = doc.createElement("document");
		doc.appendChild(documentElement);

		documentElement.appendChild(XMLUtils.createCDATAElement("systemURL", this.systemURL, doc));
		documentElement.appendChild(XMLUtils.createCDATAElement("municipalityName", this.municipalityName, doc));
		documentElement.appendChild(XMLUtils.createCDATAElement("municipalityURL", this.municipalityURL, doc));

		Element notificationEmailElement = doc.createElement("UserDeletedNotification");
		documentElement.appendChild(notificationEmailElement);

		notificationEmailElement.appendChild(user.toXML(doc));

		StringWriter messageWriter = new StringWriter();

		try {

			transformer.transform(new DOMSource(doc), new StreamResult(messageWriter));

		} catch (TransformerException e) {

			log.error("Unable to transform notification email for user " + user,e);
			return;
		}

		SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());

		email.setSubject(this.emailSubject);
		email.setMessage(messageWriter.toString());
		email.setSenderAddress(this.senderAddress);
		email.setSenderName(this.senderName);
		email.addRecipient(user.getEmail());
		email.setMessageContentType(SimpleEmail.HTML);

		log.info("Sending user deleted notification email to user " + user);

		try {
			emailHandler.send(email);

		} catch (NoEmailSendersFoundException e) {

			log.error("Unable to send user deleted notification email to user " + user,e);

		} catch (UnableToProcessEmailException e) {

			log.error("Unable to send user deleted notification email to user " + user,e);
		}

	}

	@Override
	public List<SettingDescriptor> getSettings() {

		List<? extends SettingDescriptor> superSettings = super.getSettings();

		if (superSettings != null) {
			ArrayList<SettingDescriptor> combinedSettings = new ArrayList<SettingDescriptor>();

			combinedSettings.addAll(superSettings);
			combinedSettings.addAll(SETTINGDESCRIPTORS);

			return combinedSettings;
		} else {
			return SETTINGDESCRIPTORS;
		}
	}

}
