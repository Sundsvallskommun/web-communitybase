package se.dosf.communitybase.modules.sectionaccess;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.Role;
import se.dosf.communitybase.modules.userprofile.UserProfileProvider;
import se.dosf.communitybase.utils.CBSectionAttributeHelper;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionAccessDeniedHandler;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.validation.NonNegativeStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class SectionAccessDeniedHandlerModule extends AnnotatedForegroundModule implements SectionAccessDeniedHandler {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Priority", description = "The priority of this section access handler compared to other handlers. A low value means a higher priority. Valid values are 0 - " + Integer.MAX_VALUE + ".", required = true, formatValidator = NonNegativeStringIntegerValidator.class)
	protected int priority = 0;

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Managers found message", description = "The message displayed when managers have been found in the requested section (tag $section.name is supported)", required = true)
	protected String managersFoundMessage = "Not set";

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Managers NOT found message", description = "The message displayed when no managers have been found in the requested section (tag $section.name is supported)", required = true)
	protected String managersNotFoundMessage = "Not set";

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Allow access request", description = "If access requests should be allowed directly in the module")
	protected boolean allowAccessRequest = false;

	@InstanceManagerDependency(required = true)
	protected CBInterface cbInterface;

	@InstanceManagerDependency(required = true)
	protected UserProfileProvider userProfileProvider;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		systemInterface.getRootSection().addSectionAccessDeniedHandler(this);
	}

	@Override
	public void unload() throws Exception {

		if (!systemInterface.getRootSection().removeSectionAccessDeniedHandler(this)) {

			log.warn("Unable to unregister section access denied listener");
		}

		super.unload();
	}

	@Override
	public int getPriority() {

		return priority;
	}

	@Override
	public boolean supportsRequest(HttpServletRequest req, User user, URIParser uriParser, SectionDescriptor sectionDescriptor) throws Throwable {

		dependencyReadLock.lock();

		try {
			if (user == null) {

				return false;
			}

			if (cbInterface == null) {

				return false;
			}

			SectionAccessMode accessMode = CBSectionAttributeHelper.getAccessMode(sectionDescriptor);

			if (accessMode != null && accessMode != SectionAccessMode.HIDDEN) {

				return true;
			}

		} finally {

			dependencyReadLock.unlock();
		}

		return false;
	}

	@Override
	public void handleRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, SectionDescriptor sectionDescriptor) throws Throwable {

		redirectToMethod(req, res, "/" + sectionDescriptor.getSectionID());
	}

	@Override
	protected ForegroundModuleResponse processForegroundRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer sectionID = uriParser.getInt(1);

		if (sectionID == null) {

			throw new URINotFoundException(uriParser);
		}

		SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

		if (sectionInterface == null) {

			throw new URINotFoundException(uriParser);
		}

		SectionDescriptor sectionDescriptor = sectionInterface.getSectionDescriptor();

		if (CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) == null) {

			throw new URINotFoundException(uriParser);
		}

		if (CBSectionAttributeHelper.getAccessMode(sectionDescriptor) == SectionAccessMode.HIDDEN) {

			throw new URINotFoundException(uriParser);
		}

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		List<User> managers = getManagers(sectionID);

		if (managers != null) {

			XMLUtils.appendNewElement(doc, documentElement, "Message", managersFoundMessage.replace("$section.name", sectionInterface.getSectionDescriptor().getName()));

			XMLUtils.appendNewElement(doc, documentElement, "ProfileImageAlias", userProfileProvider.getProfileImageAlias());
			XMLUtils.appendNewElement(doc, documentElement, "ShowProfileAlias", userProfileProvider.getShowProfileAlias());
			XMLUtils.appendNewElement(doc, documentElement, "SectionID", sectionID);
			
			if (allowAccessRequest) {
				XMLUtils.appendNewElement(doc, documentElement, "AllowsAccessRequest");
				
				if (cbInterface.getAccessRequest(user, sectionID) != null) {
					XMLUtils.appendNewElement(doc, documentElement, "HasAccessRequest");
				}
				else if (req.getMethod().equals("POST")) {
					String comment = req.getParameter("requestComment");
					
					if (StringUtils.isEmpty(comment)) {
						XMLUtils.appendNewElement(doc, documentElement, "EmptyComment");
					}
					else if (comment.length() > 1024) {
						documentElement.appendChild(RequestUtils.getRequestParameters(req, doc));
						
						XMLUtils.appendNewElement(doc, documentElement, "CommentTooLong");
					}
					else {
						cbInterface.addAccessRequest(user, sectionID, comment);
						
						XMLUtils.appendNewElement(doc, documentElement, "RequestAdded");
					}
				}
			}

			Element managersElement = doc.createElement("MemberManagers");
			documentElement.appendChild(managersElement);

			for (User manager : managers) {

				Element userElement = manager.toXML(doc);
				managersElement.appendChild(userElement);

				AttributeHandler attributeHandler = manager.getAttributeHandler();

				if (attributeHandler != null) {

					userElement.appendChild(attributeHandler.toXML(doc));
				}
			}

		} else {

			XMLUtils.appendNewElement(doc, documentElement, "Message", managersNotFoundMessage.replace("$section.name", sectionInterface.getSectionDescriptor().getName()));
		}

		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	private List<User> getManagers(Integer sectionID) {

		List<Role> roles = cbInterface.getRoles(sectionID);

		if (roles != null) {

			List<Integer> userManagerRoleGroups = new ArrayList<Integer>(roles.size());

			for (Role role : roles) {

				if (role.hasManageMembersAccess()) {

					Group group = cbInterface.getGroup(role.getRoleID(), sectionID, false);

					if (group != null) {

						userManagerRoleGroups.add(group.getGroupID());
					}
				}
			}

			if (!userManagerRoleGroups.isEmpty()) {

				return systemInterface.getUserHandler().getUsersByGroups(userManagerRoleGroups, true);
			}
		}

		return null;
	}
}
