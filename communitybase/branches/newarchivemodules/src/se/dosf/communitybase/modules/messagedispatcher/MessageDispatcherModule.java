package se.dosf.communitybase.modules.messagedispatcher;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GenericAdminMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityGroupComparator;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.beans.SchoolComparator;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedAdminModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.messagedispatcher.beans.DispatchMessage;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.AccessibleFactory;
import se.unlogic.emailutils.framework.EmailHandler;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSMS;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.instances.InstanceListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.sms.SMSSender;
import se.unlogic.standardutils.bool.BooleanUtils;
import se.unlogic.standardutils.db.DBUtils;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.SwedishPhoneNumberValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class MessageDispatcherModule extends AnnotatedAdminModule<CommunityGroup> implements CommunityModule, InstanceListener<SMSSender> {

	private CommunityUserDAO userDAO;
	private CommunitySchoolDAO schoolDAO;
	private CommunityGroupDAO groupDAO;

	protected AccessibleFactory accessFactory;

	@ModuleSetting(allowsNull=true)
	@HTMLEditorSettingDescriptor(name="Welcome message", description="Welcome message displayed above the message form if set.")
	protected String welcomeMessage = null;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "SMS sender name", description = "The name set as sender on generated SMS messages", required = true)
	protected String smsSenderName = "John Doe";
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "E-mail sender name", description = "The name set as sender on generated email messages", required = true)
	protected String emailSenderName = "John Doe";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "E-mail sender address", description = "The adress set as sender on generated email messages", required = true, formatValidator = EmailPopulator.class)
	protected String emailSenderAddress = "";
	
	protected SMSSender smsSender;

	AnnotatedRequestPopulator<DispatchMessage> messagePopulator = new AnnotatedRequestPopulator<DispatchMessage>(DispatchMessage.class);
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.createDAOs(dataSource);
		systemInterface.getInstanceHandler().addInstanceListener(SMSSender.class, this);
	}

	@Override
	protected void createDAOs(DataSource dataSource) {
		userDAO = new CommunityUserDAO(dataSource);
		groupDAO = new CommunityGroupDAO(dataSource);
		schoolDAO = new CommunitySchoolDAO(dataSource);

		accessFactory = new AccessibleFactory(groupDAO, schoolDAO);

		groupDAO.setUserDao(userDAO);
		userDAO.setGroupDao(groupDAO);
		userDAO.setSchoolDAO(schoolDAO);
		schoolDAO.setGroupDAO(groupDAO);
		schoolDAO.setUserDAO(userDAO);
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		systemInterface.getInstanceHandler().removeInstanceListener(SMSSender.class, this);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		if (dataSource != this.dataSource) {
			this.createDAOs(dataSource);
		}

		super.update(moduleDescriptor, dataSource);
	}

	@Override
	protected boolean checkAccess(CommunityGroup group, CommunityUser user) {

		return AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN);
	}

	@Override
	protected Class<? extends CommunityGroup> getGenericClass() {

		return CommunityGroup.class;
	}

	@Override
	protected CommunityGroup getRequestedBean(Integer groupID) throws SQLException {

		return this.groupDAO.getGroup(groupID, true);
	}

	public Integer getModuleID() {

		return this.moduleDescriptor.getModuleID();
	}

	public String getModuleName() {
		return this.moduleDescriptor.getName();
	}

	@Override
	public List<Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		return null;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		// This method is not supported by this module
		throw new URINotFoundException(uriParser);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return defaultMethod(req, res, user, uriParser, group, null);
	}

	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, ValidationException validationException) throws Exception {

		Collection<ValidationError> validationErrors = new ArrayList<ValidationError>();

		if (req.getMethod().equalsIgnoreCase("POST")) {

			DispatchMessage message = populateMessage(req, user, group, validationErrors);

			if (validationErrors.isEmpty() && req.getParameter("backToWrite") == null) {

				return previewMessage(req, res, user, uriParser, group, message, validationErrors);
			}
		}

		return writeMessage(req, res, user, uriParser, group, validationErrors);
	}

	private ForegroundModuleResponse writeMessage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, Collection<ValidationError> validationErrors) throws DOMException, AccessDeniedException, SQLException {

		log.info("User " + user + " requesting form for message dispatcher");

		Document doc = createDocument(req, uriParser, group, user);

		Element writeMessageElement = doc.createElement("WriteMessage");
		doc.getDocumentElement().appendChild(writeMessageElement);

		XMLUtils.appendNewElement(doc, writeMessageElement, "WelcomeMessage", welcomeMessage);
		
		writeMessageElement.appendChild(accessFactory.getAccessibleXML(user, doc, GroupAccessLevel.ADMIN));

		if (!validationErrors.isEmpty()) {
			XMLUtils.append(doc, writeMessageElement, "validationException", validationErrors);
		}

		//Message sent
		String userCount = req.getParameter("u");
		String smsUserCount = req.getParameter("s");

		if (!StringUtils.isEmpty(userCount)) {
			XMLUtils.appendNewElement(doc, writeMessageElement, "UserCount", userCount);
		}

		if (!StringUtils.isEmpty(smsUserCount)) {
			XMLUtils.appendNewElement(doc, writeMessageElement, "SMSUserCount", smsUserCount);
		}

		writeMessageElement.appendChild(RequestUtils.getRequestParameters(req, doc));

		return new SimpleForegroundModuleResponse(doc);
	}

	private DispatchMessage populateMessage(HttpServletRequest req, CommunityUser user, CommunityGroup group, Collection<ValidationError> validationErrors) throws SQLException {

		try {
			DispatchMessage message = messagePopulator.populate(req);

			if (message != null) {

				if (!BooleanUtils.valueOf(message.getSendEmail()) && !BooleanUtils.valueOf(message.getSendSMS())) {
					validationErrors.add(new ValidationError("MessageType", ValidationErrorType.RequiredField));
				}

				if (BooleanUtils.valueOf(message.getSendEmail()) && StringUtils.isEmpty(message.getEmailText())) {
					validationErrors.add(new ValidationError("emailText", ValidationErrorType.RequiredField));
				}

				if (BooleanUtils.valueOf(message.getSendEmail()) && StringUtils.isEmpty(message.getEmailSubject())) {
					validationErrors.add(new ValidationError("emailSubject", ValidationErrorType.RequiredField));
				}

				if (BooleanUtils.valueOf(message.getSendSMS()) && StringUtils.isEmpty(message.getSMSText())) {
					validationErrors.add(new ValidationError("smsText", ValidationErrorType.RequiredField));
				}

				{
					Collection<GroupAccessLevel> accessLevels = new ArrayList<GroupAccessLevel>();

					if (BooleanUtils.valueOf(message.getSendToMembers())) {
						accessLevels.add(GroupAccessLevel.MEMBER);
					}

					if (BooleanUtils.valueOf(message.getSendToPublishers())) {
						accessLevels.add(GroupAccessLevel.PUBLISHER);
						accessLevels.add(GroupAccessLevel.ADMIN);
					}

					if (accessLevels.isEmpty()) {

						validationErrors.add(new ValidationError("memberType", ValidationErrorType.RequiredField));

					} else {

						Connection connection = null;

						try {
							connection = dataSource.getConnection();

							IntegerPopulator integerPopulator = IntegerPopulator.getPopulator();

							List<Integer> groupIDs = new ArrayList<Integer>();
							List<Integer> schoolIDs = new ArrayList<Integer>();

							@SuppressWarnings("unchecked")
							Enumeration<String> parameterNames = req.getParameterNames();

							while (parameterNames.hasMoreElements()) {

								String name = parameterNames.nextElement();

								if (name.startsWith("groupschool")) {

									String[] groups = req.getParameterValues(name);

									for (String groupPair : groups) {

										String[] schoolAndGroup = groupPair.split(":");

										groupIDs.add(integerPopulator.getValue(schoolAndGroup[1]));
									}

								} else if (name.startsWith("schoolbase")) {

									String[] schools = req.getParameterValues(name);

									for (String school : schools) {

										schoolIDs.add(integerPopulator.getValue(school));
									}
								}
							}

							List<Integer> userIDsFromGroupAccess = userDAO.getUserIdsFromGroupsByAccessLevel(groupIDs, accessLevels.toArray(new GroupAccessLevel[] {}));
							List<Integer> userIDsFromSchoolAccess = accessLevels.contains(GroupAccessLevel.ADMIN) ? userDAO.getAdminUserIdsFromSchools(schoolIDs) : null;

							HashSet<Integer> userIDs = new HashSet<Integer>();

							if (userIDsFromGroupAccess != null) {
								userIDs.addAll(userIDsFromGroupAccess);
							}

							if (userIDsFromSchoolAccess != null) {
								userIDs.addAll(userIDsFromSchoolAccess);
							}

							if (!userIDs.isEmpty()) {
								List<Integer> userIDs2 = Arrays.asList(userIDs.toArray(new Integer[userIDs.size()]));

								List<CommunityUser> users = userDAO.getUsers(userIDs2, false, false, connection);

								if (users != null) {

									message.setRecipients(users);
								}
							}

							if (!groupIDs.isEmpty()) {
								List<CommunityGroup> selectedGroups = groupDAO.getGroups(groupIDs, true);

								if (selectedGroups != null) {
									List<School> selectedSchools = new ArrayList<School>();

									for (CommunityGroup communityGroup : selectedGroups) {
										School school = communityGroup.getSchool();
										int index = selectedSchools.indexOf(school);

										if (index != -1) {

											selectedSchools.get(index).getGroups().add(communityGroup);

										} else {

											school = new School(school.getSchoolID(), school.getName());
											school.setGroups(new ArrayList<CommunityGroup>());
											school.getGroups().add(communityGroup);
											selectedSchools.add(school);
										}
									}

									for (School school : selectedSchools) {
										Collections.sort(school.getGroups(), CommunityGroupComparator.getInstance(Order.ASC));
									}

									Collections.sort(selectedSchools, SchoolComparator.getInstance(Order.ASC));

									message.setSelectedSchoolsAndGroups(selectedSchools);
								}
							}

						} finally {
							DBUtils.closeConnection(connection);
						}
					}
				}

				if (message.getRecipients() == null || message.getRecipients().isEmpty()) {
					validationErrors.add(new ValidationError("recipient", ValidationErrorType.RequiredField));
				}
			}

			return message;
		} catch (ValidationException e) {
			validationErrors.addAll(e.getErrors());
		}

		return null;
	}

	private ForegroundModuleResponse previewMessage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group, DispatchMessage message, Collection<ValidationError> validationErrors) {

		log.info("User " + user + " previewing dispatch message");

		Document doc = createDocument(req, uriParser, group, user);

		Element previewMessageElement = doc.createElement("PreviewMessage");
		doc.getDocumentElement().appendChild(previewMessageElement);

		previewMessageElement.appendChild(message.toXML(doc));

		XMLUtils.appendNewElement(doc, previewMessageElement, "UserCount", message.getRecipients() == null ? 0 : message.getRecipients().size());

		XMLUtils.append(doc, previewMessageElement, "validationException", validationErrors);
		previewMessageElement.appendChild(RequestUtils.getRequestParameters(req, doc));

		return new SimpleForegroundModuleResponse(doc);
	}

	@GenericAdminMethod
	public ForegroundModuleResponse send(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, AccessDeniedException, IOException, URINotFoundException {

		if (req.getMethod().equalsIgnoreCase("POST")) {

			Collection<ValidationError> validationErrors = new ArrayList<ValidationError>();

			DispatchMessage message = populateMessage(req, user, group, validationErrors);

			if (!validationErrors.isEmpty()) {
				return writeMessage(req, res, user, uriParser, group, validationErrors);
			}
			
			boolean sendEmail = BooleanUtils.valueOf(message.getSendEmail());
			boolean sendSms = BooleanUtils.valueOf(message.getSendSMS());
			
			if (sendEmail) {

				log.info("User " + user + " sending e-mail message to " + message.getRecipients().size() + " users.");
					
				EmailHandler emailHandler = systemInterface.getEmailHandler();

				for (CommunityUser communityUser : message.getRecipients()) {

					try {
						if (!StringUtils.isEmpty(communityUser.getEmail())) {

							SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());
							email.setSenderName(emailSenderName);
							email.setSenderAddress(emailSenderAddress);
							email.setSubject(message.getEmailSubject());
							email.setMessage(message.getEmailText());
							email.setMessageContentType(SimpleEmail.HTML);
							email.addRecipient(communityUser.getEmail());

							emailHandler.send(email);
						}

					} catch (InvalidEmailAddressException e) {

						log.warn("Error sending e-mail message to user " + user, e);

					} catch (NoEmailSendersFoundException e) {

						log.warn("Error sending e-mail message to user " + user, e);

					} catch (UnableToProcessEmailException e) {

						log.warn("Error sending e-mail message to user " + user, e);

					}
				}
			}

			int smsUserCount = 0;

			if (sendSms) {
				log.info("User " + user + " sending SMS message to " + message.getRecipients().size() + " users.");

				for (CommunityUser communityUser : message.getRecipients()) {

					String phoneNumber = communityUser.getPhoneMobile();

					if (!StringUtils.isEmpty(phoneNumber)) {

						SimpleSMS sms = new SimpleSMS();
						sms.setMessage(message.getSMSText());
						sms.setSenderName(smsSenderName);

						if (phoneNumber.contains("/")) {
							phoneNumber = phoneNumber.split("/")[0]; // Multiple alternative numbers specified, use the first one
						}

						if (SwedishPhoneNumberValidator.getValidator().validateFormat(phoneNumber)) {

							//See http://en.wikipedia.org/wiki/List_of_international_call_prefixes if you are calling from a country using non-standard international call prefix
							phoneNumber = phoneNumber.replaceAll("\\+", "00").replaceAll("[^0-9]+", "");

							sms.setRecipients(Collections.singletonList(phoneNumber));

							smsUserCount++;

							if (!smsSender.send(sms)) {
								log.warn("Error sending sms message to " + phoneNumber);
							}
						}
					}
				}
			}

			int userCount = message.getRecipients() == null ? 0 : message.getRecipients().size();
			
			String params = "";
			
			if(sendEmail & sendSms){
				params = "?u=" + userCount + "&s=" + smsUserCount;
			} else if (sendEmail) {
				params = "?u=" + userCount;
			} else if (sendSms) {
				params = "?s=" + smsUserCount;
			}

			res.sendRedirect(this.getModuleURI(req) + "/" + group.getGroupID() + "/" + params);

			return null;
		}

		log.info("User " + user + " attempted GET from message dispatch send, redirecting");

		redirectToDefaultMethod(req, res, group);

		return null;
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		XMLUtils.appendNewElement(doc, document, "groupID", group.getGroupID());

		if (AccessUtils.checkAccess(user, group.getSchool())) {
			document.appendChild(doc.createElement("isSchoolAdmin"));
		}

		if (user.isAdmin()) {
			document.appendChild(doc.createElement("isSysAdmin"));
		}

		doc.appendChild(document);

		return doc;
	}

	public Breadcrumb getBreadcrumb(String name, String description, String method, CommunityGroup group) {

		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + method, URLType.RELATIVE_FROM_CONTEXTPATH);
	}
	
	@Override
	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (smsSender == null) {
			throw new ModuleConfigurationException("Missing required dependency " + SMSSender.class.getSimpleName());
		}

		return super.processRequest(req, res, user, uriParser);
	}

	@Override
	public <InstanceType extends SMSSender> void instanceAdded(Class<SMSSender> key, InstanceType instance) {

		smsSender = instance;
	}

	@Override
	public <InstanceType extends SMSSender> void instanceRemoved(Class<SMSSender> key, InstanceType instance) {

		smsSender = null;

	}

}
