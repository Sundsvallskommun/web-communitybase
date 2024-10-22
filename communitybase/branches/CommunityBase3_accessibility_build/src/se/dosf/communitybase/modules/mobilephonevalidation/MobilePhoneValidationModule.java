package se.dosf.communitybase.modules.mobilephonevalidation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.UserMultiListSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleFilterModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.sms.SMSSender;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.random.RandomUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.SessionUtils;
import se.unlogic.webutils.http.URIParser;

import com.nordicpeak.persistingsmssender.PersistedSMS;

public class MobilePhoneValidationModule extends AnnotatedForegroundModule {
	
	private static final Pattern NUMBER_PATTERN = Pattern.compile("^07\\d\\d\\d\\d\\d\\d\\d\\d");
	
	private static final String VERIFICATION_NUMBER_ATTRIBUTE = "MobilePhoneVerificationNumber";
	private static final String VERIFICATION_CODE_ATTRIBUTE = "MobilePhoneVerificationCode";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Length of code", description = "Length of code sent in SMS", formatValidator = PositiveStringIntegerValidator.class)
	private Integer codeLength = 6;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Sender name", description = "Name of sender shown in SMS")
	private String senderName = "Not set";
	
	@ModuleSetting(allowsNull = true)
	@UserMultiListSettingDescriptor(name = "Excluded users", description = "Users excluded from phone validation")
	private List<Integer> excludedUserIDs;

	@InstanceManagerDependency(required = true)
	private SMSSender smsSender;
	
	private MobilePhoneNotSetRedirectFilterModule filterModule;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(MobilePhoneValidationModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + MobilePhoneValidationModule.class.getName());
		}
	}

	@Override
	public void unload() throws Exception {

		super.unload();
		
		if (systemInterface.getSystemStatus() != SystemStatus.STOPPING && filterModule != null) {
			try {
				systemInterface.getFilterModuleCache().unload(filterModule.getDescriptor());

			} catch (KeyNotCachedException e) {

				log.debug("Unable to unload filter module, module not cached", e);

			} catch (Exception e) {

				log.error("Error unloading filter module", e);
			}
		}

		systemInterface.getInstanceHandler().removeInstance(MobilePhoneValidationModule.class, this);
	}
	
	@Override
	protected void moduleConfigured() throws Exception {

		if (filterModule == null) {
			log.info("Adding virtual filter module");
			
			SimpleFilterModuleDescriptor descriptor = new SimpleFilterModuleDescriptor();
			descriptor.setUserAccess(true);
			
			descriptor.setAliases(getAliases());
			descriptor.setEnabled(true);
			descriptor.setClassname(MobilePhoneNotSetRedirectFilterModule.class.getName());
			descriptor.setName(moduleDescriptor.getName() + " (virtual)");
			
			filterModule = (MobilePhoneNotSetRedirectFilterModule) systemInterface.getFilterModuleCache().cache(descriptor);
			
			filterModule.setParentModule(this);
		}
		else {
			SimpleFilterModuleDescriptor descriptor = (SimpleFilterModuleDescriptor) filterModule.getDescriptor();
			
			log.info("Updating virtual filter module " + descriptor);
			
			descriptor.setName(moduleDescriptor.getName() + " (virtual)");
			descriptor.setAliases(getAliases());
			
			systemInterface.getFilterModuleCache().update(descriptor);
		}
	}
	
	private List<String> getAliases() {

		// TODO move to module setting?
		List<String> aliases = new ArrayList<String>();
		aliases.add("exclude:static*");
		aliases.add("exclude:administration*");
		aliases.add("exclude:userprofile/profileimage*");
		aliases.add("exclude:" + moduleDescriptor.getAlias() + "*");
		aliases.add("*");
		
		return aliases;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		if (!user.getAttributeHandler().isSet(CBConstants.USER_MOBILE_PHONE_ATTRIBUTE) && CBAccessUtils.isExternalUser(user)) {
			Document doc = createDocument(req, uriParser, user);
			
			if (req.getMethod().equals("POST")) {
				String phoneNumber = req.getParameter("mobilePhone");
				String code = req.getParameter("verificationCode");
				
				List<ValidationError> errors = new ArrayList<ValidationError>();
				
				if (StringUtils.isEmpty(phoneNumber)) {
					errors.add(new ValidationError("mobilePhone", ValidationErrorType.RequiredField));
				}
				else if (!validateNumber(phoneNumber)) {
					errors.add(new ValidationError("mobilePhone", ValidationErrorType.InvalidFormat));
				}
				
				if (StringUtils.isEmpty(code)) {
					errors.add(new ValidationError("verificationCode", ValidationErrorType.RequiredField));
				}
				
				if (errors.isEmpty() && !validateCode(phoneNumber, code, user, req)) {
					errors.add(new ValidationError("WrongVerificationCode"));
				}
				
				if (errors.isEmpty()) {
					MutableAttributeHandler attributeHandler = (MutableAttributeHandler) user.getAttributeHandler();
					
					attributeHandler.setAttribute(CBConstants.USER_MOBILE_PHONE_ATTRIBUTE, phoneNumber);
					attributeHandler.setAttribute(CBConstants.USER_MOBILE_PHONE_VALIDATED_ATTRIBUTE, DateUtils.DATE_TIME_FORMATTER.format(new Date()));
					
					log.info("Adding mobilePhone attribute to user " + user);
					
					systemInterface.getUserHandler().updateUser(user, false, false, true);
					
					res.sendRedirect(uriParser.getContextPath() + "/");
					
					return null;
				}
				else {
					doc.getDocumentElement().appendChild(new ValidationException(errors).toXML(doc));
					doc.getDocumentElement().appendChild(RequestUtils.getRequestParameters(req, doc));
				}
 			}
			
			return new SimpleForegroundModuleResponse(doc, getDefaultBreadcrumb());
		}
		
		throw new URINotFoundException(uriParser);
	}
	
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse sendVerification(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {
		
		String phoneNumber = null;
		
		if ((phoneNumber = req.getParameter("mobilePhone")) != null) {
			JsonObject jsonObject = new JsonObject(1);
			
			if (validateNumber(phoneNumber)) {
				if (sendSMSCode(phoneNumber, user, req)) {
					jsonObject.putField("Success", "true");
				}
				else {
					jsonObject.putField("UnknownError", "true");
				}
			}
			else {
				jsonObject.putField("InvalidPhoneNumber", "true");
			}
			
			HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);
			
			return null;
		}
		
		throw new URINotFoundException(uriParser);
	}

	public boolean validateNumber(String number) {
		
		if (StringUtils.isEmpty(number)) {
			return false;
		}
		
		return NUMBER_PATTERN.matcher(number).matches();
	}
	
	public boolean validateCode(String number, String code, User user, HttpServletRequest req) {
		
		String sessionCode = (String) SessionUtils.getAttribute(VERIFICATION_CODE_ATTRIBUTE, req);
		String sessionPhoneNumber = (String) SessionUtils.getAttribute(VERIFICATION_NUMBER_ATTRIBUTE, req);
		
		if (code.equals(sessionCode) && number.equals(sessionPhoneNumber)) {
			return true;
		}
		
		return false;
	}
	
	public boolean sendSMSCode(String number, User user, HttpServletRequest req) {
		
		if (smsSender == null) {
			throw new RuntimeException("No SMS sender found");
		}
		
		String code = (String) SessionUtils.getAttribute(VERIFICATION_CODE_ATTRIBUTE, req);
		
		if (StringUtils.isEmpty(code) || !number.equals(SessionUtils.getAttribute(VERIFICATION_NUMBER_ATTRIBUTE, req))) {
			code = RandomUtils.getRandomString(codeLength, RandomUtils.DIGITS);
			
			SessionUtils.setAttribute(VERIFICATION_CODE_ATTRIBUTE, code, req);
			SessionUtils.setAttribute(VERIFICATION_NUMBER_ATTRIBUTE, number, req);
		}
		
		PersistedSMS persistedSMS = null;

		try {
			persistedSMS = new PersistedSMS();
			persistedSMS.setSenderName(senderName);
			persistedSMS.setMessage(code);
			persistedSMS.addRecipient(number);
			persistedSMS.addAttribute("useFlash", "true");

			return smsSender.send(persistedSMS);
		}
		catch (Exception e) {

			log.warn("Error generating/sending sms " + persistedSMS, e);
		}
		
		return false;
	}
	
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();

		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		doc.appendChild(document);

		return doc;
	}

	public boolean isExcludedUser(User user) {

		return CollectionUtils.contains(excludedUserIDs, user.getUserID());
	}
}