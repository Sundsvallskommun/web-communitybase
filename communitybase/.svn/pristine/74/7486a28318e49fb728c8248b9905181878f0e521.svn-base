package se.dosf.communitybase.modules.oldcontentremover;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.firstpage.FirstpageModule;
import se.dosf.communitybase.modules.oldcontentremover.beans.OldContent;
import se.dosf.communitybase.modules.oldcontentremover.interfaces.OldContentRemover;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.populators.DatePopulator;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.validation.ValidationUtils;

public class OldContentRemovalModule extends AnnotatedForegroundModule implements CRUDCallback<User> {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Minimum content age (months)", description = "The minimum age required for content to be removed in months(30 days)", required = true, formatValidator = StringIntegerValidator.class)
	private Integer minimumAgeInMonthsForContentRemoval = 1;

	private CommunityGroupDAO communityGroupDAO;
	private CommunityUserDAO communityUserDAO;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(OldContentRemovalModule.class, this)) {

			log.warn("Another instance has already been registered in instance handler for class " + OldContentRemovalModule.class.getName());
		}
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		communityGroupDAO = new CommunityGroupDAO(dataSource);
		communityUserDAO = new CommunityUserDAO(dataSource);
		communityGroupDAO.setUserDao(this.communityUserDAO);
		communityUserDAO.setGroupDao(this.communityGroupDAO);
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		systemInterface.getInstanceHandler().removeInstance(OldContentRemovalModule.class, this);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(documentElement);
		return doc;
	}

	private void validateEndDate(Date endDate, Collection<ValidationError> validationErrors) {

		if (endDate != null && new Date().before(endDate)) {
			validationErrors.add(new ValidationError("EndDateIsInTheFuture"));
		}

		if (endDate != null && DateUtils.getTodaysDatePlusMinusDays(-30 * minimumAgeInMonthsForContentRemoval).before(endDate)) {
			validationErrors.add(new ValidationError("" + minimumAgeInMonthsForContentRemoval, ValidationErrorType.Other, "EndDateIsTooRecent"));
		}
	}

	@WebPublic
	public ForegroundModuleResponse select(HttpServletRequest req, HttpServletResponse res, User user2, URIParser uriParser) throws Throwable {

		CommunityUser user = (CommunityUser) user2;
		CommunityGroup group = null;

		try {
			group = this.communityGroupDAO.getGroup(Integer.valueOf(uriParser.get(2)), false);

		} catch (NumberFormatException ignore) {} catch (NullPointerException ignore) {}

		if (group == null || !AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN)) {

			throw new AccessDeniedException("User does not have admin access to group " + group);
		}

		Collection<CommunityModule> oldContentRemovers = getAllOldContentRemovers(user, group);

		Collection<ValidationError> validationErrors = new ArrayList<ValidationError>();

		if (req.getMethod().equalsIgnoreCase("POST")) {

			Date endDate = ValidationUtils.validateParameter("endDate", req, true, new DatePopulator(), validationErrors);

			validateEndDate(endDate, validationErrors);

			Collection<Integer> moduleIDs = ValidationUtils.validateParameters("module", req, true, IntegerPopulator.getPopulator(), validationErrors);

			if (validationErrors.isEmpty()) {
				for (Iterator<CommunityModule> it = oldContentRemovers.iterator(); it.hasNext();) {
					CommunityModule communityModule = it.next();

					if (!moduleIDs.contains(communityModule.getModuleDescriptor().getModuleID())) {
						it.remove();
					}
				}

				log.info("User " + user + " selected " + oldContentRemovers.size() + " modules to remove old content from");

				return showDeleteForm(req, res, user, group, uriParser, validationErrors, endDate, oldContentRemovers);
			}
		}

		log.info("User " + user + " requesting old content module selection for group " + group);

		Document doc = createDocument(req, uriParser, user);

		Element selectModulesElement = doc.createElement("SelectModules");
		doc.getDocumentElement().appendChild(selectModulesElement);

		if (oldContentRemovers != null) {
			Element modules = doc.createElement("Modules");
			selectModulesElement.appendChild(modules);

			for (CommunityModule oldContentRemover : oldContentRemovers) {
				Element module = doc.createElement("Module");
				XMLUtils.appendNewElement(doc, module, "Name", oldContentRemover.getModuleDescriptor().getName());
				XMLUtils.appendNewElement(doc, module, "ID", oldContentRemover.getModuleDescriptor().getModuleID());
				modules.appendChild(module);
			}
		}

		selectModulesElement.appendChild(group.toXML(doc));
		XMLUtils.appendNewElement(doc, selectModulesElement, "MinimumAge", minimumAgeInMonthsForContentRemoval);

		if (!validationErrors.isEmpty()) {
			XMLUtils.append(doc, selectModulesElement, "validationException", validationErrors);
			selectModulesElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	private ForegroundModuleResponse showDeleteForm(HttpServletRequest req, HttpServletResponse res, CommunityUser user, CommunityGroup group, URIParser uriParser, Collection<ValidationError> validationErrors, Date endDate, Collection<CommunityModule> oldContentRemoverModules) throws SQLException {

		log.info("User " + user + " requesting delete old content form for group " + group);

		Document doc = createDocument(req, uriParser, user);

		Element selectOldContentElement = doc.createElement("SelectContent");
		doc.getDocumentElement().appendChild(selectOldContentElement);

		Element oldContentsElement = doc.createElement("Contents");
		selectOldContentElement.appendChild(oldContentsElement);

		for (CommunityModule module : oldContentRemoverModules) {
			Element moduleElement = doc.createElement("Module");
			XMLUtils.appendNewElement(doc, moduleElement, "Name", module.getModuleDescriptor().getName());
			XMLUtils.appendNewElement(doc, moduleElement, "ID", module.getModuleDescriptor().getModuleID());
			oldContentsElement.appendChild(moduleElement);

			OldContentRemover oldContentRemover = (OldContentRemover) module;
			Collection<OldContent> content = oldContentRemover.getOldContent(user, group, endDate);

			if (content != null) {
				for (OldContent archivalContent : content) {
					moduleElement.appendChild(archivalContent.toXML(doc));
				}
			}
		}

		selectOldContentElement.appendChild(group.toXML(doc));
		XMLUtils.appendNewElement(doc, selectOldContentElement, "EndDate", DateUtils.DATE_FORMATTER.format(endDate));

		if (!validationErrors.isEmpty()) {
			XMLUtils.append(doc, selectOldContentElement, "validationException", validationErrors);
			selectOldContentElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc);
	}

	@WebPublic
	public ForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, User user2, URIParser uriParser) throws Throwable {

		CommunityUser user = (CommunityUser) user2;
		CommunityGroup group = null;

		try {
			group = this.communityGroupDAO.getGroup(Integer.valueOf(req.getParameter("groupID")), false);

		} catch (NumberFormatException ignore) {} catch (NullPointerException ignore) {}

		if (group == null || !AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN)) {

			throw new AccessDeniedException("User does not have admin access to group " + group);
		}

		Collection<CommunityModule> modules = null;

		String[] moduleIDs = req.getParameterValues("module");

		if (moduleIDs != null) {

			modules = new ArrayList<CommunityModule>();
			Collection<CommunityModule> oldContentRemovers = getAllOldContentRemovers(user, group);

			for (String idS : moduleIDs) {
				Entry<ForegroundModuleDescriptor, ForegroundModule> entry = sectionInterface.getForegroundModuleCache().getEntry(Integer.valueOf(idS));

				if (entry != null) {
					CommunityModule communityModule = (CommunityModule) entry.getValue();

					if (oldContentRemovers.contains(communityModule)) {
						modules.add(communityModule);

					} else {
						throw new AccessDeniedException("User tried to remove content from a module not in the selected group " + group);
					}

				} else {
					throw new AccessDeniedException("User tried to remove content from a non-existant module");
				}
			}
		}

		Date endDate = DateUtils.getDate(DateUtils.DATE_FORMATTER, req.getParameter("endDate"));

		if (modules == null || endDate == null) {
			throw new InvalidParameterException();
		}

		Collection<ValidationError> validationErrors = new ArrayList<ValidationError>();

		validateEndDate(endDate, validationErrors);

		if (req.getMethod().equalsIgnoreCase("POST")) {

			Collection<EradicationContent> eradicationContents = new ArrayList<OldContentRemovalModule.EradicationContent>();

			for (CommunityModule module : modules) {
				EradicationContent eradicationContent = new EradicationContent(module);

				OldContentRemover oldContentRemover = (OldContentRemover) module;
				Collection<OldContent> oldContents = oldContentRemover.getOldContent(user, group, endDate);

				if (oldContents != null) {
					Collection<Integer> contentIDs = ValidationUtils.validateParameters("contentFromModule-" + module.getModuleDescriptor().getModuleID(), req, false, IntegerPopulator.getPopulator(), validationErrors);

					if (contentIDs != null) {
						for (OldContent oldContent : oldContents) {
							if (contentIDs.contains(oldContent.getID())) {
								eradicationContent.content.add(oldContent);
							}
						}
					}
				}

				if (!eradicationContent.content.isEmpty()) {
					eradicationContents.add(eradicationContent);
				}
			}

			if (validationErrors.isEmpty()) {
				int deletedContentCount = 0;

				for (EradicationContent content : eradicationContents) {
					OldContentRemover oldContentRemover = (OldContentRemover) content.module;
					deletedContentCount += oldContentRemover.deleteOldContent(content.content, user, group);
				}

				log.info("User " + user + " deleted " + deletedContentCount + " old content from group " + group);

				Document doc = createDocument(req, uriParser, user);
				Element contentDeletedContentElement = doc.createElement("ContentDeleted");
				doc.getDocumentElement().appendChild(contentDeletedContentElement);

				XMLUtils.appendNewElement(doc, contentDeletedContentElement, "DeletedCount", deletedContentCount);

				return new SimpleForegroundModuleResponse(doc);
			}
		}

		return showDeleteForm(req, res, user, group, uriParser, validationErrors, endDate, modules);
	}

	private Collection<CommunityModule> getAllOldContentRemovers(CommunityUser user, CommunityGroup group) {

		FirstpageModule firstpageModule = se.dosf.communitybase.utils.ModuleUtils.getCachedModule(sectionInterface, FirstpageModule.class);

		if (firstpageModule == null) {

			log.warn("Module " + moduleDescriptor + " unable to find FirstpageModule!");

		} else {

			Map<ForegroundModuleDescriptor, CommunityModule> moduleMap = firstpageModule.getPublicModuleMap();

			if (!moduleMap.isEmpty()) {

				Collection<CommunityModule> oldContentRemovers = new ArrayList<CommunityModule>();

				for (Entry<ForegroundModuleDescriptor, CommunityModule> moduleEntry : firstpageModule.getPublicModuleMap().entrySet()) {

					CommunityModule module = moduleEntry.getValue();

					if (AccessUtils.checkAccess(module.getModuleDescriptor(), group) && OldContentRemover.class.isAssignableFrom(module.getClass())) {

						oldContentRemovers.add(module);
					}
				}

				if (oldContentRemovers.size() > 0) {
					return oldContentRemovers;
				}
			}
		}

		return null;
	}

	public int getOldContentCount(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException {

		int contentCount = 0;

		Collection<CommunityModule> modules = getAllOldContentRemovers(user, group);

		for (CommunityModule module : modules) {

			OldContentRemover oldContentRemover = (OldContentRemover) module;
			contentCount += oldContentRemover.getOldContentCount(user, group, endDate);
		}

		return contentCount;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	private class EradicationContent {

		public CommunityModule module;
		public Collection<OldContent> content = new ArrayList<OldContent>();

		public EradicationContent(CommunityModule module2) {

			module = module2;
		}
	}

}
