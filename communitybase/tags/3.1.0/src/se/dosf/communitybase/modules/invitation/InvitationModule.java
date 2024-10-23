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
import se.dosf.communitybase.modules.invitation.beans.Invitation;
import se.dosf.communitybase.modules.invitation.beans.SectionInvitation;
import se.dosf.communitybase.modules.userprofile.UserProfileProvider;
import se.unlogic.emailutils.framework.InvalidEmailAddressException;
import se.unlogic.emailutils.framework.NoEmailSendersFoundException;
import se.unlogic.emailutils.framework.SimpleEmail;
import se.unlogic.emailutils.framework.UnableToProcessEmailException;
import se.unlogic.emailutils.validation.StringEmailValidator;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.io.BinarySizeFormater;
import se.unlogic.standardutils.io.BinarySizes;
import se.unlogic.standardutils.string.MapTagSource;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.string.TagReplacer;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.url.URLRewriter;

public class InvitationModule extends AnnotatedForegroundModule {

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

	@InstanceManagerDependency(required = true)
	private CBInterface cbInterface;

	@InstanceManagerDependency(required = true)
	UserProfileProvider userProfileProvider;

	private QueryParameterFactory<Invitation, String> invitationEmailParamFactory;

	private QueryParameterFactory<Invitation, Integer> invitationIDParamFactory;

	private QueryParameterFactory<Invitation, UUID> invitationLinkIDParamFactory;

	protected CBDAOFactory daoFactory;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(InvitationModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + InvitationModule.class.getName());
		}
	}
	
	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(InvitationModule.class, this);

		super.unload();
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
	public ForegroundModuleResponse register(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer invitationID = uriParser.getInt(2);

		if (invitationID != null && uriParser.get(3) != null && StringUtils.isValidUUID(uriParser.get(3))) {

			Invitation invitation = getInvitation(invitationID, UUID.fromString(uriParser.get(3)));

			if (invitation != null) {

				List<ValidationError> errors = new ArrayList<ValidationError>();

				if (req.getMethod().equalsIgnoreCase("POST")) {

					try {

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

						systemInterface.getLoginHandler().processLoginRequest(req, res, uriParser, false);

						return null;

					} catch (ValidationException validationException) {

						if(req.getAttribute("multipartRequest") != null) {
							
							req = (MultipartRequest) req.getAttribute("multipartRequest");
						}
						
						errors.addAll(validationException.getErrors());

					}

				}

				log.info("User " + user + " accessing invitation " + invitation + " from " + req.getRemoteAddr());

				Document doc = this.createDocument(req, uriParser, user);

				Element registerElement = doc.createElement("Register");
				doc.getFirstChild().appendChild(registerElement);

				XMLUtils.appendNewElement(doc, registerElement, "RegistrationText", URLRewriter.setAbsoluteLinkUrls(registrationText, req));
				XMLUtils.appendNewElement(doc, registerElement, "MaxAllowedFileSize", BinarySizeFormater.getFormatedSize(userProfileProvider.getMaximumFileSize() * BinarySizes.MegaByte));

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

	public boolean sendInvitation(Invitation invitation, HttpServletRequest req) throws SQLException {

		try {

			log.info("Sending invitation " + invitation);

			SimpleEmail email = new SimpleEmail();
			email.setMessageContentType(SimpleEmail.HTML);

			email.setSenderName(emailSender);
			email.setSenderAddress(emailSenderAddress);
			email.addRecipient(invitation.getEmail());
			email.setSubject(emailSubject);

			MapTagSource mapTagSource = new MapTagSource();

			mapTagSource.addTag(INVITATION_LINK_TAG, RequestUtils.getFullContextPathURL(req) + this.getFullAlias() + "/register/" + invitation.getInvitationID() + "/" + invitation.getLinkID());

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

	public List<Invitation> getInvitations(Integer sectionID) throws SQLException {

		LowLevelQuery<Invitation> query = new LowLevelQuery<Invitation>();

		query.setSql("SELECT DISTINCT i.* FROM " + daoFactory.getInvitationDAO().getTableName() + " AS i LEFT JOIN " + SectionInvitation.SECTION_INVITATIONS_TABLE + " AS si ON (i.invitationID = si.invitationID) WHERE si.sectionID = ?");

		query.addParameter(sectionID);

		return daoFactory.getInvitationDAO().getAll(query);
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

}
