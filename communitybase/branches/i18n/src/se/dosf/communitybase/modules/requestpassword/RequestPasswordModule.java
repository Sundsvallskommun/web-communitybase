package se.dosf.communitybase.modules.requestpassword;

import java.io.StringWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.AnotatedGlobalModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.unlogic.emailutils.framework.EmailHandler;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.validation.StringEmailValidator;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.modules.ModuleSetting;
import se.unlogic.standardutils.random.RandomUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;


public class RequestPasswordModule extends AnotatedGlobalModule implements CommunityModule {

	private static final ArrayList<SettingDescriptor> SETTINGDESCRIPTORS = new ArrayList<SettingDescriptor>();
	
	static {
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("emailSubject", "Subject", "The text displayed in the subject of sent email", true, "New password from CommunityBase", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderName", "Sender name", "The name displayed in the sender field of sent e-mail", true, "CommunityBase", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("senderAddress", "Sender address", "The sender address", true, "not.set@not.set.com", new StringEmailValidator()));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("systemURL", "Base URL", "The full URL to this site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityURL", "Municipality URL", "The full URL to the municipality site", true, "http://not.set.com", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("municipalityName", "Municipality name", "The name of the municipality which uses communitybase", true, "Not set", null));
		SETTINGDESCRIPTORS.add(SettingDescriptor.createTextFieldSetting("emailContentType", "Content type", "The content type of generated e-mails", true, "text/html", null));
	}
	
	private CommunityUserDAO userDAO;

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
	protected String emailSubject = "New password from CommunityBase";
	
	@ModuleSetting
	protected String emailContentType = "text/html";
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
	
		super.init(moduleDescriptor, sectionInterface, dataSource);
		
		this.userDAO = new CommunityUserDAO(dataSource);
		
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		
		super.update(moduleDescriptor, dataSource);
		
		this.userDAO = new CommunityUserDAO(dataSource);
		
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		
		ValidationException validationException = null;
		
		if(req.getMethod().equalsIgnoreCase("POST")){
			
			String email = req.getParameter("email");
			
			try{
			
				if(!StringUtils.isEmpty(email)){
								
					if(!EmailUtils.isValidEmailAddress(email)){
						throw new ValidationException(new ValidationError("email", ValidationErrorType.InvalidFormat));
					}
					
					CommunityUser requestedUser = this.userDAO.getUser(email, false, false);
					
					if(requestedUser == null){
						throw new ValidationException(new ValidationError("UserNotFound"));
					}
					
					String newPassword = RandomUtils.getRandomString(8, 10);
					
					requestedUser.setPassword(newPassword);
					
					this.userDAO.update(requestedUser, true, false, false);
					
					log.info("User " + requestedUser + " requested new password");
					
					// send email to user
					this.sendNewPassword(requestedUser);
					
					return this.showSentMessageForm(req, res, user, uriParser);
					
				}else{
					throw new ValidationException(new ValidationError("email", ValidationErrorType.RequiredField));
				}
			
			}catch(ValidationException e){
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, user);
		
		Element newPasswordElement = doc.createElement("RequestNewPassword");
		
		doc.getFirstChild().appendChild(newPasswordElement);
		
		if(validationException != null){
			newPasswordElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			newPasswordElement.appendChild(validationException.toXML(doc));
		}
		
		return new SimpleForegroundModuleResponse(doc, this.getModuleBreadcrumb());
		
	}
	
	private ForegroundModuleResponse showSentMessageForm(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser){
		
		Document doc = this.createDocument(req, uriParser, user);
		
		Element sendMessageElement = doc.createElement("NewPasswordSent");
		
		doc.getFirstChild().appendChild(sendMessageElement);
		
		return new SimpleForegroundModuleResponse(doc);
		
	}
	
	private void sendNewPassword(CommunityUser user) throws InvalidEmailAddressException{
		
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
		
		Document doc = XMLUtils.createDomDocument();

		Element documentElement = doc.createElement("document");
		doc.appendChild(documentElement);

		documentElement.appendChild(XMLUtils.createCDATAElement("systemURL", this.systemURL, doc));
		documentElement.appendChild(XMLUtils.createCDATAElement("municipalityName", this.municipalityName, doc));
		documentElement.appendChild(XMLUtils.createCDATAElement("municipalityURL", this.municipalityURL, doc));

		Element notificationEmailElement = doc.createElement("RequestNewPasswordEmail");
		documentElement.appendChild(notificationEmailElement);

		notificationEmailElement.appendChild(user.toXML(doc));

		StringWriter messageWriter = new StringWriter();

		try {

			transformer.transform(new DOMSource(doc), new StreamResult(messageWriter));

		} catch (TransformerException e) {

			log.error("Unable to transform new password email for user " + user,e);
			return;
		}

		SimpleEmail email = new SimpleEmail();

		email.setSubject(this.emailSubject);
		email.setMessage(messageWriter.toString());
		email.setSenderAddress(this.senderAddress);
		email.setSenderName(this.senderName);
		email.addRecipient(user.getEmail());
		email.setMessageContentType(this.emailContentType);

		log.info("Sending new password email to user " + user);
		
		try {
			emailHandler.send(email);

		} catch (NoEmailSendersFoundException e) {

			log.error("Unable to send new password email to user " + user,e);

		} catch (UnableToProcessEmailException e) {

			log.error("Unable to send new password email to user " + user,e);
		}
		
		
	}
	
	@Override
	public ModuleType getModuleType() {
		return ModuleType.Administration;
	}

	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {
		// No resume in MysettingsModule
		return null;
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user){

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		doc.appendChild(document);

		return doc;
		
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
