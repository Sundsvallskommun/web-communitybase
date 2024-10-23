package se.dosf.communitybase.modules.templatedistribution;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.beans.SimpleSectionType;
import se.dosf.communitybase.interfaces.CBInterface;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.BaseVisibleModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.daos.interfaces.ModuleDAO;
import se.unlogic.hierarchy.core.enums.CRUDAction;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.FilterModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.webutils.http.URIParser;

public class TemplateDistributionModule extends AnnotatedForegroundModule {

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Autostart new modules", description = "Wheather to start new modules after copy or not")
	private boolean autostartModules = true;

	@InstanceManagerDependency(required = true)
	protected CBInterface cbInterface;

	@WebPublic
	public ForegroundModuleResponse foreground(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer templateModuleID;
		Integer sectionTypeID;

		if (uriParser.size() == 4 && (templateModuleID = uriParser.getInt(2)) != null && (sectionTypeID = uriParser.getInt(3)) != null) {

			SimpleForegroundModuleDescriptor templateModule = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModule(templateModuleID);

			if (templateModule == null) {

				log.warn("Foreground TemplateModule with ID " + templateModuleID + " not found");
				return new SimpleForegroundModuleResponse("Foreground TemplateModule with ID " + templateModuleID + " not found", moduleDescriptor.getName(), this.getDefaultBreadcrumb());
			}

			SimpleSectionType sectionType = cbInterface.getSectionType(sectionTypeID);

			if (sectionType == null) {

				log.warn("SectionType with ID " + sectionTypeID + " not found");
				return new SimpleForegroundModuleResponse("SectionType with ID " + sectionTypeID + " not found", moduleDescriptor.getName(), this.getDefaultBreadcrumb());
			}

			return distribute(templateModule, sectionType, systemInterface.getCoreDaoFactory().getForegroundModuleDAO());
		}

		throw new URINotFoundException(uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse background(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Integer templateModuleID;
		Integer sectionTypeID;

		if (uriParser.size() == 4 && (templateModuleID = uriParser.getInt(2)) != null && (sectionTypeID = uriParser.getInt(3)) != null) {

			SimpleBackgroundModuleDescriptor templateModule = systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().getModule(templateModuleID);

			if (templateModule == null) {

				log.warn("Background TemplateModule with ID " + templateModuleID + " not found");
				return new SimpleForegroundModuleResponse("Background TemplateModule with ID " + templateModuleID + " not found", moduleDescriptor.getName(), this.getDefaultBreadcrumb());
			}

			SimpleSectionType sectionType = cbInterface.getSectionType(sectionTypeID);

			if (sectionType == null) {

				log.warn("SectionType with ID " + sectionTypeID + " not found");
				return new SimpleForegroundModuleResponse("SectionType with ID " + sectionTypeID + " not found", moduleDescriptor.getName(), this.getDefaultBreadcrumb());
			}

			return distribute(templateModule, sectionType, systemInterface.getCoreDaoFactory().getBackgroundModuleDAO());
		}

		throw new URINotFoundException(uriParser);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ForegroundModuleResponse distribute(BaseVisibleModuleDescriptor templateModule, SimpleSectionType sectionType, ModuleDAO moduleDAO) throws Throwable {

		List<SimpleSectionDescriptor> sections = systemInterface.getCoreDaoFactory().getSectionDAO().getSectionsByAttribute(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID, sectionType.getSectionTypeID().toString(), false);

		if (!CollectionUtils.isEmpty(sections)) {

			templateModule.getAttributeHandler().setAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, templateModule.getModuleID().toString());

			for (SimpleSectionDescriptor section : sections) {

				SectionInterface sectionI = systemInterface.getSectionInterface(section.getSectionID());

				if (sectionI != null) {

					if (sectionI.getForegroundModuleCache().getModuleEntryByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, templateModule.getAttributeHandler().getString(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE)) == null) {

						if (templateModule instanceof ForegroundModuleDescriptor) {

							ForegroundModuleDescriptor foregroundTemplate = (ForegroundModuleDescriptor) templateModule;

							ForegroundModuleDescriptor conflictingDescriptor = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModule(section.getSectionID(), foregroundTemplate.getAlias());

							if (conflictingDescriptor != null) {

								log.warn("DuplicateModuleAlias " + foregroundTemplate.getAlias() + " in section " + section + " with module " + conflictingDescriptor);
								continue;
							}
						}

						templateModule.setModuleID(null);
						templateModule.setSectionID(section.getSectionID());

						moduleDAO.add(templateModule);
						
						if (moduleDescriptor instanceof ForegroundModuleDescriptor) {

							systemInterface.getEventHandler().sendEvent(ForegroundModuleDescriptor.class, new CRUDEvent<ForegroundModuleDescriptor>(CRUDAction.ADD, (ForegroundModuleDescriptor) moduleDescriptor), EventTarget.ALL);

						} else if (moduleDescriptor instanceof BackgroundModuleDescriptor) {

							systemInterface.getEventHandler().sendEvent(BackgroundModuleDescriptor.class, new CRUDEvent<BackgroundModuleDescriptor>(CRUDAction.ADD, (BackgroundModuleDescriptor) moduleDescriptor), EventTarget.ALL);

						} else if (moduleDescriptor instanceof FilterModuleDescriptor) {

							systemInterface.getEventHandler().sendEvent(FilterModuleDescriptor.class, new CRUDEvent<FilterModuleDescriptor>(CRUDAction.ADD, (FilterModuleDescriptor) moduleDescriptor), EventTarget.ALL);

						} else {

							log.warn("Unrecognized moduleDescriptor " + moduleDescriptor + ", sending generic event");
							systemInterface.getEventHandler().sendEvent(ModuleDescriptor.class, new CRUDEvent<ModuleDescriptor>(CRUDAction.ADD, moduleDescriptor), EventTarget.ALL);
						}

						if (autostartModules) {
							
							ModuleDescriptor templateModuleDBCopy = moduleDAO.getModule(templateModule.getModuleID());

							if (templateModule instanceof ForegroundModuleDescriptor) {

								sectionI.getForegroundModuleCache().cache((ForegroundModuleDescriptor) templateModuleDBCopy);

							} else {

								sectionI.getBackgroundModuleCache().cache((BackgroundModuleDescriptor) templateModuleDBCopy);
							}
						}

					} else {

						log.warn("Section " + section + " already has a module with " + CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE + "= " + templateModule.getAttributeHandler().getString(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE));
					}

				} else {

					log.warn("Section " + section + " not loaded");
				}
			}

			return new SimpleForegroundModuleResponse("Completed", moduleDescriptor.getName(), this.getDefaultBreadcrumb());

		} else {

			log.warn("Found no sections with sectionType " + sectionType);
		}

		return new SimpleForegroundModuleResponse("Error, see log", moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}
}
