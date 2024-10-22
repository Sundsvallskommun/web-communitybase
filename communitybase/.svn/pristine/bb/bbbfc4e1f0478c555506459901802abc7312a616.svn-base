package se.dosf.communitybase.modules.linkarchive;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.daos.EventDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.linkarchive.beans.Link;
import se.dosf.communitybase.modules.linkarchive.cruds.LinkCRUD;
import se.dosf.communitybase.modules.linkarchive.daos.LinkDAO;
import se.dosf.communitybase.populators.CommunityGroupQueryPopulator;
import se.dosf.communitybase.populators.CommunityGroupTypePopulator;
import se.dosf.communitybase.populators.CommunityUserQueryPopulator;
import se.dosf.communitybase.populators.CommunityUserTypePopulator;
import se.dosf.communitybase.populators.SchoolQueryPopulator;
import se.dosf.communitybase.populators.SchoolTypePopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.CommunityBaseAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class LinkArchiveModule extends AnnotatedCommunityModule implements CommunityModule, CRUDCallback<CommunityUser> {

	public static final Map<String, Order> SORT_COLUMNS = new HashMap<String, Order>();

	static{
		SORT_COLUMNS.put("posted", Order.DESC);
		SORT_COLUMNS.put("url", Order.ASC);
		SORT_COLUMNS.put("description", Order.ASC);
	}

	public static boolean isValidSortColumn(String sortBy) {

		return SORT_COLUMNS.containsKey(sortBy);
	}

	public static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);

	/* Breadcrumbs */
	@XSLVariable
	protected String newLinkText = "New link: ";

	@XSLVariable
	protected String addLinkBreadcrumb = "Add link";

	@XSLVariable
	protected String updateLinkBreadcrumb = "Update link";

	private EventDAO eventDAO;

	private LinkDAO linkDAO;
	private LinkCRUD linkCRUD;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptorBean, sectionInterface, dataSource);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);

		String tableGroupName = this.getClass().getPackage().getName();
		XMLDBScriptProvider scriptProvider = new XMLDBScriptProvider(this.getClass().getResourceAsStream("MySQL DB Script.xml"));
		Integer currentVersion = TableVersionHandler.getTableGroupVersion(dataSource, tableGroupName);

		if(currentVersion == null){

			// Create new tables (notice the max version argument at the end)
			UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider, null, 1);

			if(upgradeResult.isUpgrade()){
				log.info(upgradeResult.toString());
			}
		}

		TransactionHandler transactionHandler = null;

		try{
			if(currentVersion == null || currentVersion == 1){

				// Migrate data
				transactionHandler = new TransactionHandler(dataSource);

				LinkArchiveDataMigrator migrator = new LinkArchiveDataMigrator(dataSource, this.log);
				migrator.run(true);

				transactionHandler.commit();
			}

		}finally{

			TransactionHandler.autoClose(transactionHandler);
		}

		// Run normal upgrade to upgrade to version 2 or later
		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider);

		if(upgradeResult.isUpgrade()){
			log.info(upgradeResult.toString());
		}

		CommunityBaseAnnotatedDAOFactory daoFactory = new CommunityBaseAnnotatedDAOFactory(dataSource, systemInterface.getUserHandler(), systemInterface.getGroupHandler(), getSchoolDAO());

		List<QueryParameterPopulator<?>> queryParameterPopulators = new ArrayList<QueryParameterPopulator<?>>();

		queryParameterPopulators.add(CommunityUserQueryPopulator.POPULATOR);
		queryParameterPopulators.add(CommunityGroupQueryPopulator.POPULATOR);
		queryParameterPopulators.add(SchoolQueryPopulator.POPULATOR);

		List<BeanStringPopulator<?>> typePopulators = new ArrayList<BeanStringPopulator<?>>();

		typePopulators.add((new CommunityUserTypePopulator(this.systemInterface.getUserHandler(), false)));
		typePopulators.add(new CommunityGroupTypePopulator(this.systemInterface.getGroupHandler()));
		typePopulators.add(new EnumPopulator<Language>(Language.class));
		typePopulators.add(new SchoolTypePopulator(this.getSchoolDAO(), false, false));

		linkDAO = new LinkDAO(dataSource, daoFactory, daoFactory.getQueryParameterPopulators(), daoFactory.getBeanStringPopulators());
		daoFactory.addDAO(Link.class, linkDAO);
		AnnotatedDAOWrapper<Link, Integer> linkCRUDDAO = linkDAO.getWrapper("linkID", Integer.class);
		this.linkCRUD = new LinkCRUD(linkCRUDDAO, new AnnotatedRequestPopulator<Link>(Link.class), "Link", "link", "", this);

		this.eventDAO = new EventDAO(dataSource, linkDAO.getTableName(), "linkarchive_linkgroups", "linkarchive_linkschools", "linkID", "description", "posted");
	}

	private void setStickyPreferences(HttpServletRequest req, String criteria, boolean reverse) {

		req.getSession().setAttribute("linkArchive.sortingPreferences.criteria", criteria);
		req.getSession().setAttribute("linkArchive.sortingPreferences.reverse", Boolean.toString(reverse));
	}

	private void propagatePreferences(HttpServletRequest req, String criteria, boolean reverse, Order order) {

		req.setAttribute("linkArchive.sortingPreferences.criteria", criteria);
		req.setAttribute("linkArchive.sortingPreferences.reverse", reverse);
		req.setAttribute("linkArchive.sortingPreferences.order", order);
	}

	/**
	 * Determines and sets user sorting preferences for listing links. Preferences from request take precedence over "sticky" preferences from session
	 * Preferences are set to session and propagated with request to subsequent handlers
	 * 
	 * @param req
	 * @param getStickyPreferences
	 * @param setStickyPreferences
	 * @throws URINotFoundException
	 */
	private void setSortingPreferences(HttpServletRequest req, boolean getStickyPreferences, boolean setStickyPreferences) throws URINotFoundException {

		HttpSession session;
		String sortingCriteria = null;
		boolean reverse = false;
		Order sortOrder = null;

		// Get preferences from query string
		if(req.getQueryString() != null){
			sortingCriteria = req.getParameter("orderby");
			reverse = Boolean.parseBoolean(req.getParameter("reverse"));
		}

		// Get "sticky" preferences from session
		else if(getStickyPreferences && (session = req.getSession(false)) != null){
			sortingCriteria = (String)session.getAttribute("linkArchive.sortingPreferences.criteria");
			reverse = Boolean.parseBoolean((String)session.getAttribute("linkArchive.sortingPreferences.reverse"));
		}

		// Get default preferences
		if(sortingCriteria == null){
			sortingCriteria = "url";
		}

		// Set "sticky" preferences to session
		if(setStickyPreferences){
			this.setStickyPreferences(req, sortingCriteria, reverse);
		}

		if(isValidSortColumn(sortingCriteria)){
			if(reverse){
				sortOrder = LinkArchiveModule.SORT_COLUMNS.get(sortingCriteria).equals(Order.ASC) ? Order.DESC : Order.ASC;
			}else{
				sortOrder = LinkArchiveModule.SORT_COLUMNS.get(sortingCriteria);
			}
			this.propagatePreferences(req, sortingCriteria, reverse, sortOrder);
			return;
		}

		throw new URINotFoundException(req.getRequestURI());
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.setSortingPreferences(req, true, true);
		return this.linkCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse links(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		this.setSortingPreferences(req, true, true);
		return this.linkCRUD.list(req, res, user, uriParser, null);
	}

	@GroupMethod
	public ForegroundModuleResponse addLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.linkCRUD.add(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse updateLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.linkCRUD.update(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteLink(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.linkCRUD.delete(req, res, user, uriParser);
	}

	@Override
	public List<? extends Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		// Get new links
		List<IdentifiedEvent> linkEvents = this.eventDAO.getEvents(group, startStamp);

		if(linkEvents != null){
			for(IdentifiedEvent event : linkEvents){
				event.setTitle(this.newLinkText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(this.getFullAlias(group));
			}
		}

		return linkEvents;
	}

	public boolean checkAccess(Link link, CommunityGroup group) {

		if(link != null && ((link.getGroups() != null && link.getGroups().contains(group)) || (link.getSchools() != null && link.getSchools().contains(group.getSchool())) || link.isGlobal())){
			return true;
		}
		return false;
	}

	public boolean hasUpdateAccess(Link link, CommunityUser user) {

		return AccessUtils.checkAdminAccess(link, user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
	}

	public void appendGroupAndAccess(Document doc, CommunityGroup group, CommunityUser user) {

		Element document = doc.getDocumentElement();
		if(group != null){
			document.appendChild(group.toXML(doc));
		}
		if(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)){
			document.appendChild(XMLUtils.createElement("isAdmin", "true", doc));
		}
		
		if(user.isAdmin()) {
			document.appendChild(XMLUtils.createElement("isSysAdmin", "true", doc));
		}
	}

	public Breadcrumb getAddLinkBreadCrumb(CommunityGroup group) {

		return this.getMethodBreadcrumb(this.addLinkBreadcrumb, "addLink", group);
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, Link link) {

		return this.getMethodBreadcrumb(this.updateLinkBreadcrumb, "updateLink" + "/" + link.getLinkID(), group);
	}

	private Breadcrumb getMethodBreadcrumb(String name, String methodUri, CommunityGroup group) {

		return new Breadcrumb(name, this.getFullAlias() + "/" + group.getGroupID() + "/" + methodUri);
	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		doc.appendChild(document);

		return doc;
	}

	@Override
	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	public LinkDAO getLinkDAO() {

		return linkDAO;
	}
}