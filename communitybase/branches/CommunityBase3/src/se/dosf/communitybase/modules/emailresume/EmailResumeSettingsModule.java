package se.dosf.communitybase.modules.emailresume;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.NotificationHandler;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.attributes.MutableAttributeHandler;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.HourStringFormatValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class EmailResumeSettingsModule extends AnnotatedForegroundModule {
	
	@InstanceManagerDependency(required = true)
	private CBInterface cbInterface;

	@ModuleSetting(allowsNull = true)
	@TextAreaSettingDescriptor(name = "Supported module notifications", description = "Supported module notificationses specified as [sourceModuleID:name]")
	private List<String> supportedModuleNotifications = null;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Default resume hour", description = "Default resume hour for users which have no attribute set.", formatValidator = HourStringFormatValidator.class, required = true)
	private Integer defaultResumeHour = 16;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Redirect alias", description = "The full alias to redirect to on cancel or submit")
	private String redirectAlias;

	@InstanceManagerDependency(required = true)
	protected NotificationHandler notificationHandler;

	private List<ModuleNotification> moduleNotifications;

	@Override
	protected void moduleConfigured() throws Exception {

		if (supportedModuleNotifications != null) {

			List<ModuleNotification> moduleNotifications = new ArrayList<ModuleNotification>(supportedModuleNotifications.size());

			for (String moduleNotificationString : supportedModuleNotifications) {

				if (StringUtils.countOccurrences(moduleNotificationString, ":") != 1 || moduleNotificationString.startsWith(":") || moduleNotificationString.endsWith(":")) {

					log.warn("Notification type \"" + moduleNotificationString + "\" has an invalid format, ignoring.");
					continue;
				}

				String[] splitString = moduleNotificationString.split(":");

				moduleNotifications.add(new ModuleNotification(splitString[0], splitString[1]));
			}

			if (!moduleNotifications.isEmpty()) {

				this.moduleNotifications = moduleNotifications;
				return;
			}
		}

		this.moduleNotifications = null;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		MutableAttributeHandler attributeHandler = getAttributeHandler(user);

		if (attributeHandler == null) {

			//This user does not have a mutable attribute handler
			XMLUtils.appendNewElement(doc, documentElement, "UnsupportedAttributeHandler");

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
		}

		List<Integer> userSections = CBAccessUtils.getUserSections(user, cbInterface);

		if (req.getMethod().equalsIgnoreCase("POST")) {

			log.info("User " + user + " updating resume settings");

			String resume = req.getParameter("resume");

			if (resume == null) {

				attributeHandler.setAttribute(CBConstants.USER_RESUME_HOUR_ATTRIBUTE, "off");

			} else {

				attributeHandler.setAttribute(CBConstants.USER_RESUME_HOUR_ATTRIBUTE, NumberUtils.toInt(req.getParameter("resumeHour")));
			}

			//Parse notification types
			if (moduleNotifications != null) {

				for (ModuleNotification moduleNotification : moduleNotifications) {

					if (req.getParameter("module" + moduleNotification.getId()) == null) {

						attributeHandler.setAttribute(CBConstants.USER_RESUME_EXCLUDED_MODULE_NOTIFICATIONS_ATTRIBUTE_PREFIX + moduleNotification.getId(), true);

					} else {

						attributeHandler.removeAttribute(CBConstants.USER_RESUME_EXCLUDED_MODULE_NOTIFICATIONS_ATTRIBUTE_PREFIX + moduleNotification.getId());
					}
				}
			}

			//Parse sections
			if (userSections != null) {

				for (Integer sectionID : userSections) {

					if (req.getParameter("section" + sectionID) == null) {

						attributeHandler.setAttribute(CBConstants.USER_RESUME_EXCLUDED_SECTION_ATTRIBUTE_PREFIX + sectionID, true);

					} else {

						attributeHandler.removeAttribute(CBConstants.USER_RESUME_EXCLUDED_SECTION_ATTRIBUTE_PREFIX + sectionID);
					}
				}
			}

			systemInterface.getUserHandler().updateUser(user, false, false, true);

			if (redirectAlias != null) {

				res.sendRedirect(req.getContextPath() + redirectAlias);
				return null;
			}

			XMLUtils.appendNewElement(doc, documentElement, "Updated");
		}

		log.info("User " + user + " requesting e-mail resume settings form");

		XMLUtils.appendNewElement(doc, documentElement, "CurrentURI", req.getContextPath() + getFullAlias());

		String hourAttribute = attributeHandler.getString(CBConstants.USER_RESUME_HOUR_ATTRIBUTE);

		if (hourAttribute == null) {

			XMLUtils.appendNewElement(doc, documentElement, "Hour", defaultResumeHour);

		} else {

			XMLUtils.appendNewElement(doc, documentElement, "Hour", NumberUtils.toInt(hourAttribute));
		}

		XMLUtils.appendNewElement(doc, documentElement, "DefaultHour", defaultResumeHour);

		if (redirectAlias != null) {

			XMLUtils.appendNewElement(doc, documentElement, "RedirectURI", req.getContextPath() + redirectAlias);
		}

		if (moduleNotifications != null) {

			Element moduleNotificationsElement = doc.createElement("ModuleNotifications");
			documentElement.appendChild(moduleNotificationsElement);

			for (ModuleNotification moduleNotification : moduleNotifications) {

				Element notificationTypeElement = moduleNotification.toXML(doc);
				moduleNotificationsElement.appendChild(notificationTypeElement);

				XMLUtils.appendNewElement(doc, notificationTypeElement, "Checked", !attributeHandler.getPrimitiveBoolean(CBConstants.USER_RESUME_EXCLUDED_MODULE_NOTIFICATIONS_ATTRIBUTE_PREFIX + moduleNotification.getId()));
			}
		}

		if (userSections != null) {

			Element sectionsElement = doc.createElement("Sections");

			for (Integer sectionID : userSections) {

				SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

				if (sectionInterface != null) {

					Element sectionElement = sectionInterface.getSectionDescriptor().toXML(doc);
					sectionsElement.appendChild(sectionElement);

					XMLUtils.appendNewElement(doc, sectionElement, "Checked", !attributeHandler.getPrimitiveBoolean(CBConstants.USER_RESUME_EXCLUDED_SECTION_ATTRIBUTE_PREFIX + sectionID));
				}
			}

			if (sectionsElement.hasChildNodes()) {

				documentElement.appendChild(sectionsElement);
			}
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	private MutableAttributeHandler getAttributeHandler(User user) {

		AttributeHandler attributeHandler = user.getAttributeHandler();

		if (attributeHandler != null && attributeHandler instanceof MutableAttributeHandler) {

			return (MutableAttributeHandler) attributeHandler;
		}

		return null;
	}
}