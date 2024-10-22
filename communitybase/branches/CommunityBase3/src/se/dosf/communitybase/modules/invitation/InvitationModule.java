package se.dosf.communitybase.modules.invitation;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.dao.CBDAOFactory;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.Role;
import se.dosf.communitybase.modules.emailresume.EmailResumeModule;
import se.dosf.communitybase.modules.invitation.beans.Invitation;
import se.dosf.communitybase.modules.invitation.beans.SectionInvitation;
import se.dosf.communitybase.modules.mobilephonevalidation.MobilePhoneValidationModule;
import se.dosf.communitybase.modules.userprofile.UserProfileProvider;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.validation.StringEmailValidator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.SystemStartupListener;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.UserRegisteredEvent;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.MapTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.url.URLRewriter;

import it.sauronsoftware.cron4j.Scheduler;

public class InvitationModule extends AnnotatedForegroundModule implements Runnable {

	private static final String INVITATION_LINK_TAG = "$invitation-link";
	private static final String ROLE_LIST_TAG = "$role-list";
	private static final String SECTION_NAME_TAG = "$section.name";
	private static final String ROLE_NAME_TAG = "$role.name";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email sender name", description = "Email sender name used for invitaion email", required = false)
	private String emailSender;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email sender address", description = "Email sender address used for invitation emails", required = false, formatValidator = StringEmailValidator.class)
	private String emailSenderAddress;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Email subject", description = "Email subject for invitation emails", required = false)
	private String emailSubject;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Email message", description = "Email message used for invitation emails. Tags: $invitation-link, $role-list", required = false)
	private String emailMessage;

	@XSLVariable(prefix = "java.")
	private String sectionInvitationMessage = SECTION_NAME_TAG + " with role " + ROLE_NAME_TAG;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Registration text", description = "Registration text shown above registration form", required = false)
	private String registrationText;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Use registration approval text", description = "Use registration approval text")
	private boolean useRegistrationApproval;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Registration approval label", description = "Registration approval label", required = false)
	private String registrationApprovalLabel;

	@ModuleSetting(allowsNull = true)
	@HTMLEditorSettingDescriptor(name = "Registration approval text", description = "Registration approval text, requires confirmation", required = false)
	private String registrationApprovalText;
	
	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Delete old invitations", description = "Delete old invitations")
	private boolean deleteOldInvitations = true;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Days before invitation is old", description = "Days before an invitation is marked as old", required = true, formatValidator = PositiveStringIntegerValidator.class)
	private Long oldInvitationDays = 90l;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Delete old invitations cron", description = "Cron expression for when to delete old invitations", required = true)
	protected String cronExp = "0 5 * * *";

	@InstanceManagerDependency(required = true)
	private CBInterface cbInterface;

	@InstanceManagerDependency(required = false)
	protected EmailResumeModule emailResumeModule;

	@InstanceManagerDependency(required = true)
	UserProfileProvider userProfileProvider;

	@InstanceManagerDependency
	private MobilePhoneValidationModule phoneValidationModule;

	private QueryParameterFactory<Invitation, String> invitationEmailParamFactory;

	private QueryParameterFactory<Invitation, Integer> invitationIDParamFactory;

	private QueryParameterFactory<Invitation, UUID> invitationLinkIDParamFactory;

	protected CBDAOFactory daoFactory;
	
	protected Scheduler scheduler;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(InvitationModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + InvitationModule.class.getName());
		}
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

		systemInterface.getInstanceHandler().removeInstance(InvitationModule.class, this);

		super.unload();
	}

	@SystemStartupListener
	private void systemStarted() {
		
		initScheduler();
	}
	
	protected synchronized void initScheduler() {

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		
		scheduler.setDaemon(true);
		
		scheduler.schedule(cronExp, this);
		scheduler.start();
	}

	protected synchronized void stopScheduler() {

		try {

			if (scheduler != null) {

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {

			log.error("Error stopping scheduler", e);
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		daoFactory = new CBDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler());

		invitationEmailParamFactory = daoFactory.getInvitationDAO().getParamFactory("email", String.class);

		invitationIDParamFactory = daoFactory.getInvitationDAO().getParamFactory("invitationID", Integer.class);

		invitationLinkIDParamFactory = daoFactory.getInvitationDAO().getParamFactory("linkID", UUID.class);

	}

	@WebPublic
	public synchronized ForegroundModuleResponse register(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer invitationID = uriParser.getInt(2);

		if (invitationID != null && uriParser.get(3) != null && StringUtils.isValidUUID(uriParser.get(3))) {

			Invitation invitation = getInvitation(invitationID, UUID.fromString(uriParser.get(3)));

			if (invitation != null) {

				List<ValidationError> errors = new ArrayList<ValidationError>();

				if (req.getMethod().equalsIgnoreCase("POST")) {

					try {

						req = userProfileProvider.parseRequest(req);

						if (useRegistrationApproval && req.getParameter("approveRegistrationText") == null) {
							throw new ValidationException(new ValidationError("RegistrationTextNotApproved"));
						}

						User newUser = userProfileProvider.addExternalUser(req, invitation.getEmail());

						log.info("User " + user + " registered new user " + newUser + " using " + invitation + " from " + req.getRemoteAddr());

						List<SectionInvitation> sectionInvitations = invitation.getSectionInvitations();

						if (sectionInvitations != null) {

							for (SectionInvitation sectionInvitation : sectionInvitations) {

								if (cbInterface.setUserRole(newUser.getUserID(), sectionInvitation.getSectionID(), sectionInvitation.getRoleID())) {

									log.info("Registered user " + newUser + " added to section " + sectionInvitation.getSectionID() + " with role " + sectionInvitation.getRoleID());

								} else {

									log.warn("Unable to add registered user " + newUser + " to section " + sectionInvitation.getSectionID() + " with role " + sectionInvitation.getRoleID());

								}
							}

						}

						daoFactory.getInvitationDAO().delete(invitation);

						systemInterface.getEventHandler().sendEvent(User.class, new UserRegisteredEvent(newUser), EventTarget.ALL);

						systemInterface.getLoginHandler().processLoginRequest(req, res, uriParser, false);

						return null;

					} catch (ValidationException validationException) {

						if (req.getAttribute("multipartRequest") != null) {

							req = (MultipartRequest) req.getAttribute("multipartRequest");
						}

						errors.addAll(validationException.getErrors());

					}

				}

				log.info("User " + user + " accessing invitation " + invitation + " from " + req.getRemoteAddr());

				Document doc = this.createDocument(req, uriParser, user);

				Element registerElement = doc.createElement("Register");
				doc.getFirstChild().appendChild(registerElement);

				if (phoneValidationModule != null) {
					XMLUtils.appendNewElement(doc, registerElement, "PhoneValidationSupported");
				}

				XMLUtils.appendNewElement(doc, registerElement, "RegistrationText", URLRewriter.setAbsoluteLinkUrls(registrationText, req));
				XMLUtils.appendNewElement(doc, registerElement, "MaxAllowedFileSize", BinarySizeFormater.getFormatedSize(userProfileProvider.getMaximumFileSize() * BinarySizes.MegaByte));
				XMLUtils.appendNewElement(doc, registerElement, "PhoneRequired", userProfileProvider.isRequirePhone());

				if (useRegistrationApproval) {
					XMLUtils.appendNewElement(doc, registerElement, "RegistrationApprovalLabel", registrationApprovalLabel);
					XMLUtils.appendNewElement(doc, registerElement, "RegistrationApprovalText", registrationApprovalText);
				}

				registerElement.appendChild(invitation.toXML(doc));

				if (!errors.isEmpty()) {

					registerElement.appendChild(new ValidationException(errors).toXML(doc));
					registerElement.appendChild(RequestUtils.getRequestParameters(req, doc));
				}

				return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());

			}

			systemInterface.getLoginHandler().processLoginRequest(req, res, uriParser, false);

			return null;
		}

		throw new URINotFoundException(uriParser);

	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse sendVerification(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		String phoneNumber = null;

		if (phoneValidationModule != null && (phoneNumber = req.getParameter("mobilePhone")) != null) {
			JsonObject jsonObject = new JsonObject(1);

			if (phoneValidationModule.validateNumber(phoneNumber)) {
				if (phoneValidationModule.sendSMSCode(phoneNumber, user, req)) {
					jsonObject.putField("Success", "true");
				} else {
					jsonObject.putField("UnknownError", "true");
				}
			} else {
				jsonObject.putField("InvalidPhoneNumber", "true");
			}

			HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

			return null;
		}

		throw new URINotFoundException(uriParser);
	}

	public boolean addInvitation(HttpServletRequest req, String email, Integer sectionID, Integer roleID, boolean sendInvitation) throws SQLException {

		Invitation invitation = getInvitation(email);

		if (invitation != null) {

			List<SectionInvitation> sectionInvitations = invitation.getSectionInvitations();

			if (sectionInvitations != null) {

				for (SectionInvitation sectionInvitation : sectionInvitations) {

					if (sectionInvitation.getSectionID().equals(sectionID)) {

						return true;
					}

				}

			} else {

				invitation.setSectionInvitations(new ArrayList<SectionInvitation>(1));
			}

		} else {

			invitation = new Invitation();
			invitation.setEmail(email);
			invitation.setLinkID(UUID.randomUUID());
			invitation.setSectionInvitations(new ArrayList<SectionInvitation>(1));
		}

		Role role = cbInterface.getSectionRole(sectionID, roleID);

		if (role != null) {

			SectionInvitation sectionInvitation = new SectionInvitation();

			sectionInvitation.setSectionID(sectionID);
			sectionInvitation.setRoleID(roleID);

			invitation.getSectionInvitations().add(sectionInvitation);

			if (invitation.getInvitationID() == null) {

				daoFactory.getInvitationDAO().add(invitation);

				if (sendInvitation) {

					return sendInvitation(invitation, req);
				}

			} else {

				daoFactory.getInvitationDAO().update(invitation);

				return true;
			}

		}

		return false;
	}

	public boolean updateInvitation(Integer invitationID, Integer sectionID, Integer roleID) throws SQLException {

		Invitation invitation = getInvitation(invitationID);

		if (invitation != null) {

			List<SectionInvitation> sectionInvitations = invitation.getSectionInvitations();

			if (sectionInvitations != null) {

				for (SectionInvitation sectionInvitation : sectionInvitations) {

					if (sectionInvitation.getSectionID().equals(sectionID)) {

						Role role = cbInterface.getSectionRole(sectionID, roleID);

						if (role != null) {

							sectionInvitation.setRoleID(roleID);

							daoFactory.getInvitationDAO().update(invitation);

							return true;
						}

					}

				}

			}

		}

		return false;
	}

	public boolean removeInvitation(Integer invitationID, Integer sectionID) throws SQLException {

		Invitation invitation = getInvitation(invitationID);

		if (invitation != null) {

			List<SectionInvitation> sectionInvitations = invitation.getSectionInvitations();

			if (sectionInvitations != null) {

				Iterator<SectionInvitation> iterator = sectionInvitations.iterator();

				while (iterator.hasNext()) {

					SectionInvitation sectionInvitation = iterator.next();

					if (sectionInvitation.getSectionID().equals(sectionID)) {

						iterator.remove();

						break;
					}

				}

			}

			if (CollectionUtils.isEmpty(sectionInvitations)) {

				daoFactory.getInvitationDAO().delete(invitation);

			} else {

				daoFactory.getInvitationDAO().update(invitation);
			}

			return true;
		}

		return false;

	}

	public void removeInvitation(Invitation invitation) throws SQLException {

		if (invitation != null) {
			daoFactory.getInvitationDAO().delete(invitation);
		}
	}

	public boolean sendInvitation(Invitation invitation, HttpServletRequest req) throws SQLException {

		if (emailResumeModule == null) {
			log.warn("Cannot send invitation " + invitation + " to user because no emailresumemodule is present");

			return false;
		}

		try {

			log.info("Sending invitation " + invitation);

			SimpleEmail email = new SimpleEmail(systemInterface.getEncoding());
			email.setMessageContentType(SimpleEmail.HTML);

			email.setSenderName(emailSender);
			email.setSenderAddress(emailSenderAddress);
			email.addRecipient(invitation.getEmail());
			email.setSubject(emailSubject);

			MapTagSource mapTagSource = new MapTagSource();

			mapTagSource.addTag(INVITATION_LINK_TAG, getRegistrationBaseURL() + invitation.getInvitationID() + "/" + invitation.getLinkID());

			StringBuilder tag = new StringBuilder("<ul>");

			for (SectionInvitation sectionInvitation : invitation.getSectionInvitations()) {

				Entry<SectionDescriptor, Section> sectionEntry = sectionInterface.getSectionCache().getEntry(sectionInvitation.getSectionID());

				Role role = cbInterface.getSectionRole(sectionInvitation.getSectionID(), sectionInvitation.getRoleID());

				// TODO how to handle if section in stopped?

				if (sectionEntry != null && role != null) {

					tag.append("<li>" + sectionInvitationMessage.replace(SECTION_NAME_TAG, sectionEntry.getKey().getName()).replace(ROLE_NAME_TAG, role.getName()) + "</li>");

				}

			}

			tag.append("</ul>");

			mapTagSource.addTag(ROLE_LIST_TAG, tag.toString());

			TagReplacer tagReplacer = new TagReplacer(mapTagSource);

			email.setMessage(tagReplacer.replace(emailMessage));

			systemInterface.getEmailHandler().send(email);

			invitation.setLastSent(new Timestamp(System.currentTimeMillis()));
			invitation.setSendCount(invitation.getSendCount() + 1);

			daoFactory.getInvitationDAO().update(invitation);

			return true;

		} catch (NoEmailSendersFoundException e) {

			log.error("Unable to send invitation " + invitation + ", no email senders found!", e);

		} catch (UnableToProcessEmailException e) {

			log.error("Unable to send invitation " + invitation + ", unable to process generated email!", e);

		} catch (InvalidEmailAddressException e) {

			log.error("Unable to send invitation " + invitation + ", " + e.getAddress() + " is not a valid email address!", e);
		}

		return false;
	}

	public String getRegistrationBaseURL() {
		
		return emailResumeModule.getFullContextPath() + this.getFullAlias() + "/register/";
	}

	public List<Invitation> getInvitations(Integer sectionID) throws SQLException {

		LowLevelQuery<Invitation> query = new LowLevelQuery<Invitation>();

		query.setSql("SELECT DISTINCT i.* FROM " + daoFactory.getInvitationDAO().getTableName() + " AS i LEFT JOIN " + SectionInvitation.SECTION_INVITATIONS_TABLE + " AS si ON (i.invitationID = si.invitationID) WHERE si.sectionID = ?");

		query.addParameter(sectionID);

		return daoFactory.getInvitationDAO().getAll(query);
	}

	public List<Invitation> getAllInvitations() throws SQLException {

		return daoFactory.getInvitationDAO().getAll();
	}

	public Invitation getInvitation(Integer invitationID) throws SQLException {

		HighLevelQuery<Invitation> query = new HighLevelQuery<Invitation>();

		query.addParameter(invitationIDParamFactory.getParameter(invitationID));

		return daoFactory.getInvitationDAO().get(query);

	}

	public Invitation getInvitation(String email) throws SQLException {

		HighLevelQuery<Invitation> query = new HighLevelQuery<Invitation>();

		query.addParameter(invitationEmailParamFactory.getParameter(email));

		return daoFactory.getInvitationDAO().get(query);

	}

	private Invitation getInvitation(Integer invitationID, UUID invitationLinkID) throws SQLException {

		HighLevelQuery<Invitation> query = new HighLevelQuery<Invitation>();

		query.addParameter(invitationIDParamFactory.getParameter(invitationID));
		query.addParameter(invitationLinkIDParamFactory.getParameter(invitationLinkID));

		return daoFactory.getInvitationDAO().get(query);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();

		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		if (user != null) {
			document.appendChild(user.toXML(doc));
		}

		doc.appendChild(document);

		return doc;
	}

	@Override
	public void run() {
		
		if (deleteOldInvitations) {
			try {
				log.info("Checking for old invitations to delete");
				
				int deleted = 0;
				
				List<Invitation> invitations = getAllInvitations();
				
				long deleteDate = TimeUtils.getCurrentTimestamp().getTime() - (oldInvitationDays * MillisecondTimeUnits.DAY);
				
				if (!CollectionUtils.isEmpty(invitations)) {
					for (Invitation invitation : invitations) {
						if (invitation.getLastSent() != null && invitation.getLastSent().getTime() < deleteDate) {
							log.info("Deleting inactive invitation " + invitation);
							
							daoFactory.getInvitationDAO().delete(invitation);
							
							deleted++;
						}
					}
				}
				
				log.info("Deleted " + deleted + " old invitations");
			} catch (SQLException e) {
				log.error("Unable to delete old invitations", e);
			}
		}
	}
}
