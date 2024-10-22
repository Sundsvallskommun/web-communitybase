package se.dosf.communitybase.modules.invitation.sender;

import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import se.dosf.communitybase.beans.Invitation;
import se.dosf.communitybase.daos.InvitationDAO;
import se.dosf.communitybase.utils.ModuleUtils;
import se.unlogic.emailutils.framework.EmailHandler;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.validation.StringEmailValidator;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.modules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.modules.ModuleSetting;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.timer.RunnableTimerTask;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XSLTCacher;
import se.unlogic.standardutils.xml.XSLVariableReader;
import se.unlogic.webutils.http.URIParser;

public class InvitationSenderModule extends AnnotatedForegroundModule implements Runnable{

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();

	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderName", "Sender name", "The name displayed in the sender field of sent e-mail resumes", true, "CommunityBase", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderAddress", "Sender address", "The sender address", true, "not.set@not.set.com", new StringEmailValidator()));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("systemURL", "Base URL", "The full URL to this site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityURL", "Municipality URL", "The full URL to the municipality site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityName", "Municipality name", "The name of the municipality which uses communitybase", true, "Not set", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("registrationURL", "Registration URL", "The full URL to the registration module", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("emailContentType", "Content type", "The content type of generated e-mails", true, "text/html", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("interval", "Poll interval", "Controls how often this module should check for invitations to send (default is 1200000 ms)", true, "120000", new StringIntegerValidator(1000,null)));
	}

	@XSLVariable
	protected String newInvitationSubject = "Invitation to CommunityBase";

	@XSLVariable
	protected String updatedInvitationSubject = "Updated invitation to CommunityBase";

	@XSLVariable
	protected String resentInvitationSubject = "Reminder of invitation to CommunityBase";

	@XSLVariable
	protected String expiredInvitationSubject = "Invitation to CommunityBase deleted";

	@XSLVariable
	protected String recalledInvitationSubject = "Invitation to CommunityBase recalled";

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
	protected String registrationURL = "http://not.set.com/registration";

	@ModuleSetting
	protected String emailContentType = "text/html";

	private InvitationDAO invitationDAO;

	private Timer timer;
	private RunnableTimerTask timerTask;

	@ModuleSetting
	protected long interval = 120000;

	@ModuleSetting(allowsNull=false)
	@CheckboxSettingDescriptor(name="i18n support",description="Internationalization support, i.e. multiple languages")
	private boolean i18n = false;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Registration Module Alias",description= "Registration module alias",required=false/*,formatValidator=TODO*/)
	protected String registrationModuleAlias = "Not set";

	@ModuleSetting
	@TextAreaSettingDescriptor(name="Language Stylesheets",description="Language specific style sheets",required=false/*,formatValidator=TODO*/)
	private String languageXSLsAsString;

	private Map<Language, XSLTCacher> languageXSLs = new HashMap<Language, XSLTCacher>();

	private Map<Language, XSLVariableReader> languageXSLVariableReaders = new HashMap<Language, XSLVariableReader>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor,	SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptor, sectionInterface, dataSource);

		// Update language style sheets and language variable readers
		ModuleUtils.cacheLanguageXSLs(this.languageXSLs, this.languageXSLsAsString, this.moduleDescriptor, this.log);
		ModuleUtils.cacheLanguageXSLVariableReaders(this.languageXSLVariableReaders, this.languageXSLsAsString, this.moduleDescriptor, this.log);

		this.timerTask = new RunnableTimerTask(this);
		this.timer = new Timer(true);
		this.timer.scheduleAtFixedRate(timerTask, 10000, interval);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		// Update language style sheets and language variable readers
		ModuleUtils.cacheLanguageXSLs(this.languageXSLs, this.languageXSLsAsString, this.moduleDescriptor, this.log);
		ModuleUtils.cacheLanguageXSLVariableReaders(this.languageXSLVariableReaders, this.languageXSLsAsString, this.moduleDescriptor, this.log);

		this.timerTask.cancel();
		this.timerTask = new RunnableTimerTask(this);
		this.timer.scheduleAtFixedRate(timerTask, 10000, interval);
	}

	@Override
	public void unload() {

		this.timerTask.cancel();
		this.timer.cancel();
		super.unload();
	}

	@Override
	protected void createDAOs(DataSource dataSource) {

		this.invitationDAO = new InvitationDAO(dataSource);
	}


	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req,	HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		res.getWriter().write("This module does not have a frontend, check the log instead.");

		return null;
	}

	public void run() {

		if(this.sectionInterface.getSystemInterface().getSystemStatus() == SystemStatus.Started){

			log.debug("Checking invitations...");

			Transformer transformer = null;

			try {
				transformer = this.sectionInterface.getModuleXSLTCache().getModuleTranformer(this.moduleDescriptor);
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


			try {
				//Get unsent invitations
				List<Invitation> unsentInvitations = this.invitationDAO.getUnsentInvitations(true, true);

				if(unsentInvitations != null){

					for(Invitation invitation : unsentInvitations){

						if(!invitation.isAdmin() && invitation.getGroups() == null && invitation.getSchools() == null){

							//Invitation has no access
							log.debug("Invitation " + invitation + " has no access left, skipping...");
							continue;
						}


						this.sendInvitation(invitation,this.newInvitationSubject,"newInvitationSubject","newInvitation",transformer,emailHandler,"new");
					}
				}

				//Get updated invitations
				List<Invitation> updatedInvitations = this.invitationDAO.getUpdatedInvitations(true, true);

				if(updatedInvitations != null){

					for(Invitation invitation : updatedInvitations){

						if(!invitation.isAdmin() && invitation.getGroups() == null && invitation.getSchools() == null){

							//Invitation has no access
							log.debug("Invitation " + invitation + " has no access left, skipping...");
							continue;
						}


						this.sendInvitation(invitation,this.updatedInvitationSubject,"updatedInvitationSubject","updatedInvitation",transformer,emailHandler,"updated");
					}
				}

				//Get invitations marked for resending
				List<Invitation> resendInvitations = this.invitationDAO.getResendInvitations(true, true);

				if(resendInvitations != null){

					for(Invitation invitation : resendInvitations){

						if(!invitation.isAdmin() && invitation.getGroups() == null && invitation.getSchools() == null){

							//Invitation has no access
							log.debug("Invitation " + invitation + " has no access left, skipping...");
							continue;
						}


						this.sendInvitation(invitation,this.resentInvitationSubject,"resentInvitationSubject","resendInvitation",transformer,emailHandler,"another copy of");
					}
				}

				//Get expired invitations
				List<Invitation> expiredInvitations = this.invitationDAO.getExpiredInvitations(false, false);

				if(expiredInvitations != null){

					for(Invitation invitation : expiredInvitations){

						this.sendInvitation(invitation,this.expiredInvitationSubject,"expiredInvitationSubject","expiredInvitation",transformer,emailHandler,"notice of expiry of");

						log.info("Deleting expired invitation " + invitation);

						this.invitationDAO.delete(invitation);
					}
				}

				//Get recalled invitations
				List<Invitation> recalledInvitations = this.invitationDAO.getRecalledInvitations(false, false);

				if(recalledInvitations != null){

					for(Invitation invitation : recalledInvitations){

						this.sendInvitation(invitation,this.recalledInvitationSubject,"recalledInvitationSubject","recalledInvitation",transformer,emailHandler,"notice of recall of");

						log.info("Deleting recalled invitation " + invitation);

						this.invitationDAO.delete(invitation);
					}
				}
			} catch (SQLException e) {

				log.error("Error processing invitations",e);

			} catch (InvalidEmailAddressException e) {

				log.error("Error processing invitations",e);
			}
		}else{

			log.info("System status is " + this.sectionInterface.getSystemInterface().getSystemStatus() + ", aborting");
		}

	}

//	private void sendInvitation(Invitation invitation, String subject, String elementName, Transformer transformer, EmailHandler emailHandler, String logText) throws InvalidEmailAddressException, SQLException {
//		this.sendInvitation(invitation, subject, null, elementName, transformer, emailHandler, logText);
//	}

	private void sendInvitation(Invitation invitation, String subject, String subjectFieldName, String elementName, Transformer transformer, EmailHandler emailHandler, String logText) throws InvalidEmailAddressException, SQLException {

		boolean useDefaultTransformer = true;
		boolean useDefaultSubject = true;
		Language language = null;
		XSLTCacher xsltCacher;
		XSLVariableReader xslVariableReader;
		Transformer languageSpecificTransformer = null;
		String languageSpecificSubject = null;

		// If invitation has a language set and a corresponding transformer is cached => use that language specific transformer instead of default transformer
		try {
			if(this.i18n && (language = invitation.getLanguage()) != null) {
				if((xsltCacher = this.languageXSLs.get(language)) != null && (languageSpecificTransformer = xsltCacher.getTransformer()) != null) {
					useDefaultTransformer = false;
				} else {
					log.warn("Invitation message was created for an unsupported language " + language + ". Falling back on default transformer");
				}
			}
		} catch(TransformerConfigurationException e) {
			log.error("No transformer could be retreived from language XSL. Falling back on default transformer", e);
		}

		// If invitation has a language set and a corresponding XSL variable reader is cached => use that language specific reader instead of default xsl variable
		if(this.i18n && (language = invitation.getLanguage()) != null) {
			if((xslVariableReader = this.languageXSLVariableReaders.get(language)) != null && (languageSpecificSubject = xslVariableReader.getValue(subjectFieldName)) != null) {
				useDefaultSubject = false;
			} else {
				log.warn("Invitation subject was created for an unsupported language " + language + ". Falling back on default XSL variable reader");
			}
		}

		Document doc = XMLUtils.createDomDocument();

		Element documentElement = doc.createElement("document");
		doc.appendChild(documentElement);

		documentElement.appendChild(XMLUtils.createCDATAElement("systemURL", this.systemURL, doc));
		if(useDefaultTransformer) {
			documentElement.appendChild(XMLUtils.createCDATAElement("registrationURL", this.registrationURL, doc));
		} else {
			documentElement.appendChild(XMLUtils.createCDATAElement("registrationURL", this.systemURL + "/" + language.getLanguageCode() + "/" + this.registrationModuleAlias, doc));
		}
		documentElement.appendChild(XMLUtils.createCDATAElement("municipalityURL", this.municipalityURL, doc));
		documentElement.appendChild(XMLUtils.createCDATAElement("municipalityName", this.municipalityName, doc));

		Element newInvitationElement = doc.createElement(elementName);
		documentElement.appendChild(newInvitationElement);

		newInvitationElement.appendChild(invitation.toXML(doc));

		StringWriter messageWriter = new StringWriter();

		try {

			if(useDefaultTransformer) {
				transformer.transform(new DOMSource(doc), new StreamResult(messageWriter));
			} else {
				languageSpecificTransformer.transform(new DOMSource(doc), new StreamResult(messageWriter));
			}

		} catch (TransformerException e) {

			log.error("Unable to transform invitation " + invitation,e);
			return;
		}

		SimpleEmail email = new SimpleEmail();

		email.setSubject(useDefaultSubject ? subject : languageSpecificSubject);
		email.setMessage(messageWriter.toString());
		if(invitation.getSenderEmail() != null) {
			email.setSenderAddress(invitation.getSenderEmail());
		} else {
			email.setSenderAddress(this.senderAddress);
		}
		email.setSenderName(this.senderName);
		email.addRecipient(invitation.getEmail());
		email.setMessageContentType(emailContentType);

		log.info("Sending " + logText + " invitation " + invitation);

		try {
			emailHandler.send(email);

			invitation.setLastSent(new Timestamp(System.currentTimeMillis()));
			invitation.setResend(false);

			this.invitationDAO.update(invitation, false, false);

		} catch (NoEmailSendersFoundException e) {

			log.error("Unable to send invitation " + invitation,e);

		} catch (UnableToProcessEmailException e) {

			log.error("Unable to send invitation " + invitation,e);
		}
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {

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
