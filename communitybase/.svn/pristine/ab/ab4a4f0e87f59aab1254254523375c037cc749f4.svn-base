package se.dosf.communitybase.modules.newsletter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.EventDAO;
import se.dosf.communitybase.daos.ReceiptDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.newsletter.beans.NewsLetter;
import se.dosf.communitybase.modules.newsletter.beans.NewsLetterReceipt;
import se.dosf.communitybase.modules.newsletter.cruds.NewsLetterCRUD;
import se.dosf.communitybase.populators.CommunityGroupQueryPopulator;
import se.dosf.communitybase.populators.CommunityGroupTypePopulator;
import se.dosf.communitybase.populators.CommunityUserQueryPopulator;
import se.dosf.communitybase.populators.CommunityUserTypePopulator;
import se.dosf.communitybase.populators.SchoolQueryPopulator;
import se.dosf.communitybase.populators.SchoolTypePopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.SimpleAnnotatedDAOFactory;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.io.FileUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.EnumPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class NewsLetterModule extends AnnotatedCommunityModule implements CommunityModule, CRUDCallback<CommunityUser> {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max upload size", description = "Maxmium upload size in megabytes allowed in a single post request", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer diskThreshold = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "RAM threshold", description = "How many megabytes of RAM to use as buffer during file uploads. If the threshold is exceeded the files are written to disk instead.", required = true, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 20;

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="Editor CSS",description="Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)",required=false)
	protected String cssPath;

	@XSLVariable
	protected String newNewsLetterText = "New newsletter: ";

	@XSLVariable
	protected String updateNewsletterBreadCrumb = "Update newsletter";

	@XSLVariable
	protected String addNewsletterBreadCrumb = "Add newsletter";

	@XSLVariable
	protected String readReceiptBreadCrumb = "Readreceipt";

	private EventDAO eventDAO;

	private AnnotatedDAOWrapper<NewsLetter, Integer> newsletterCRUDDAO;

	private NewsLetterCRUD newsletterCRUD;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.createDAOs(this.dataSource);

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		this.createDAOs(this.dataSource);

	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception{

		super.createDAOs(dataSource);

		SimpleAnnotatedDAOFactory daoFactory = new SimpleAnnotatedDAOFactory(dataSource);

		List<QueryParameterPopulator<?>> queryParameterPopulators = new ArrayList<QueryParameterPopulator<?>>();

		queryParameterPopulators.add(CommunityUserQueryPopulator.POPULATOR);
		queryParameterPopulators.add(CommunityGroupQueryPopulator.POPULATOR);
		queryParameterPopulators.add(SchoolQueryPopulator.POPULATOR);

		List<BeanStringPopulator<?>> typePopulators = new ArrayList<BeanStringPopulator<?>>();

		typePopulators.add((new CommunityUserTypePopulator(this.systemInterface.getUserHandler(), false)));
		typePopulators.add(new CommunityGroupTypePopulator(this.systemInterface.getUserHandler()));
		typePopulators.add(new EnumPopulator<Language>(Language.class));
		typePopulators.add(new SchoolTypePopulator(this.getSchoolDAO(), false, false));

		daoFactory.getDAO(School.class, queryParameterPopulators, typePopulators);
		daoFactory.getDAO(NewsLetterReceipt.class, queryParameterPopulators, typePopulators);

		ReceiptDAO<NewsLetterReceipt> receiptDAO = new ReceiptDAO<NewsLetterReceipt>(dataSource, NewsLetterReceipt.class, daoFactory, queryParameterPopulators, typePopulators);

		AnnotatedDAO<NewsLetter> newsLetterDAO = daoFactory.getDAO(NewsLetter.class, queryParameterPopulators, typePopulators);
		this.newsletterCRUDDAO = new AnnotatedDAOWrapper<NewsLetter, Integer>(newsLetterDAO, "newsletterID", Integer.class);
		this.newsletterCRUD = new NewsLetterCRUD(newsletterCRUDDAO, new AnnotatedRequestPopulator<NewsLetter>(NewsLetter.class), "NewsLetter", "newsletter", "/show", this, receiptDAO);

		this.eventDAO = new EventDAO(dataSource, newsLetterDAO.getTableName(), "weekLetterID", "title", "date");

	}


	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.show(req, res, user, uriParser, group);

	}

	@GroupMethod
	public ForegroundModuleResponse show(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.newsletterCRUD.list(req, res, user, uriParser, group);

	}

	@GroupMethod
	public ForegroundModuleResponse addNewsletter(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.newsletterCRUD.add(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse updateNewsletter(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.newsletterCRUD.update(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse deleteNewsletter(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.newsletterCRUD.delete(req, res, user, uriParser);
	}

	@GroupMethod
	public ForegroundModuleResponse showReadReceipt(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		return this.newsletterCRUD.showReadReceipt(req, res, user, uriParser, group);
	}

	@GroupMethod
	public ForegroundModuleResponse getImage(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		NewsLetter newsLetter = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (newsLetter = this.newsletterCRUDDAO.get(Integer.valueOf(uriParser.get(3)))) == null || newsLetter.getImageLocation() == null || newsLetter.getImage() == null) {
			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);
		}

		try {
			writePicture(newsLetter.getImage(), newsLetter.getTitle() + ".jpg", res);
		} catch (IOException e) {
			log.info("Caught exception " + e + " while sending picture in newsletter " + newsLetter + " requested by user " + user);
		}

		return null;

	}

	public static void writePicture(Blob blob, String filename, HttpServletResponse res) throws SQLException, IOException {

		if (blob != null) {
		
			HTTPUtils.setContentLength(blob.length(), res);
	
			res.setContentType("image/jpeg");
			res.setHeader("Content-Disposition", "inline; filename=" + FileUtils.toValidHttpFilename(filename));
	
			InputStream in = null;
			OutputStream out = null;
	
			try{
				in = blob.getBinaryStream();
				out = res.getOutputStream();
	
				StreamUtils.transfer(in, out);
	
			} finally{
	
				StreamUtils.closeStream(in);
				StreamUtils.closeStream(out);
			}
		
		}

	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("Document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		if(this.cssPath != null) {
			XMLUtils.appendNewElement(doc, document, "cssPath", this.cssPath);
		}

		doc.appendChild(document);

		return doc;
	}

	public void appendGroupAndAccess(Document doc, CommunityGroup group, CommunityUser user) {

		Element document = doc.getDocumentElement();

		if(group != null) {
			document.appendChild(group.toXML(doc));
		}

		if (AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			document.appendChild(XMLUtils.createElement("isAdmin", "true", doc));
		}

		document.appendChild(XMLUtils.createElement("isSysAdmin", String.valueOf(user.isAdmin()), doc));

	}

	public boolean checkAccess(NewsLetter newsLetter, CommunityGroup group) {

		if(newsLetter != null && ((newsLetter.getGroups() != null && newsLetter.getGroups().contains(group)) || (newsLetter.getSchools() != null && newsLetter.getSchools().contains(group.getSchool())) || newsLetter.isGlobal())) {

			return true;
		}

		return false;
	}

	public boolean hasUpdateAccess(NewsLetter newsLetter, CommunityUser user) {

		return AccessUtils.checkAdminAccess(newsLetter, user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);

	}

	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		List<IdentifiedEvent> events = this.eventDAO.getEvents(group, startStamp);

		if(events != null){

			for(IdentifiedEvent event : events){
				event.setTitle(this.newNewsLetterText + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(this.getFullAlias(group) + "/show/" + event.getId());
			}
		}

		return events;
	}

	public String getTitlePrefix() {

		return this.moduleDescriptor.getName();
	}

	public Breadcrumb getReadReceiptBreadCrumb() {
		return new Breadcrumb(readReceiptBreadCrumb, readReceiptBreadCrumb, "#");
	}

	public Breadcrumb getAddBreadCrumb(CommunityGroup group) {
		return this.getMethodBreadcrumb(this.addNewsletterBreadCrumb, "addNewsletter", group);
	}

	public Breadcrumb getUpdateBreadCrumb(CommunityGroup group, NewsLetter newsLetter) {
		return this.getMethodBreadcrumb(this.updateNewsletterBreadCrumb, "updateNewsletter" + "/" + newsLetter.getNewsletterID(), group);
	}

	private Breadcrumb getMethodBreadcrumb(String name, String methodUri, CommunityGroup group) {
		return new Breadcrumb(name, this.getFullAlias() + "/" + group.getGroupID() + "/" + methodUri);
	}

	public Integer getRamThreshold() {

		return this.ramThreshold;
	}

	public Integer getDiskThreshold() {

		return this.diskThreshold;
	}

}
