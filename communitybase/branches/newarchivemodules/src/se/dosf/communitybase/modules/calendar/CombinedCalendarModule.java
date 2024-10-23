package se.dosf.communitybase.modules.calendar;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import flexjson.JSONSerializer;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.Receipt;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.AnnotatedGlobalModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.calendar.beans.CalendarPost;
import se.dosf.communitybase.modules.calendar.daos.CalendarModuleDAO;
import se.dosf.communitybase.modules.calendar.populators.CalendarPopulator;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.AccessibleFactory;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class CombinedCalendarModule extends AnnotatedGlobalModule implements CommunityModule {

	private CalendarModuleDAO calendarDAO;
	private CommunitySchoolDAO schoolDAO;
	private CommunityGroupDAO communityGroupDAO;
	private UserHandler userHandler;
	private CalendarPopulator CalendarPopulator = new CalendarPopulator();

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="Editor CSS",description="Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)",required=false)
	protected String cssPath;

	private AccessibleFactory publishingFactory;

	@XSLVariable
	protected String updatePostBreadCrumb = "Update post";

	@XSLVariable
	protected String addPostBreadCrumb = "Add post";

	@XSLVariable
	protected String readReceiptBreadCrumb = "Readreceipt";

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptor, sectionInterface, dataSource);

		this.userHandler = this.sectionInterface.getSystemInterface().getUserHandler();

		this.calendarDAO = new CalendarModuleDAO(dataSource, this.userHandler);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);
		this.communityGroupDAO = new CommunityGroupDAO(dataSource);
		this.schoolDAO.setGroupDAO(this.communityGroupDAO);
		this.publishingFactory = new AccessibleFactory(this.communityGroupDAO, this.schoolDAO);

	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);

		this.userHandler = this.sectionInterface.getSystemInterface().getUserHandler();

		this.calendarDAO = new CalendarModuleDAO(dataSource, this.userHandler);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);
		this.communityGroupDAO = new CommunityGroupDAO(dataSource);
		this.schoolDAO.setGroupDAO(this.communityGroupDAO);
		this.publishingFactory = new AccessibleFactory(this.communityGroupDAO, this.schoolDAO);
	}

	@Override
	@WebPublic
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		log.info("User " + user + " requesting combined calendar");

		Document doc = this.createDocument(req, uriParser, user);

		Element calendarElement = this.getDefaultXML(doc);

		XMLUtils.appendNewElement(doc, calendarElement, "startMonth", new Date().getTime());

		return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getModuleBreadcrumb());

	}

	@WebPublic
	public ForegroundModuleResponse showMonth(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		Document doc = this.createDocument(req, uriParser, user);

		Element calendarElement = this.getDefaultXML(doc);

		if(uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(2)) && NumberUtils.isInt(uriParser.get(3))){

			Calendar cal = Calendar.getInstance();

			cal.set(Integer.valueOf(uriParser.get(2)), (Integer.valueOf(uriParser.get(3))-1), 1);

			XMLUtils.appendNewElement(doc, calendarElement, "startMonth", cal.getTimeInMillis());

		}else{

			XMLUtils.appendNewElement(doc, calendarElement, "startMonth", new Date().getTime());

		}

		return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getModuleBreadcrumb());

	}

	@WebPublic
	public ForegroundModuleResponse getPosts(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(2)) || !NumberUtils.isInt(uriParser.get(3))) {

			throw new URINotFoundException(uriParser);

		} else {

			Calendar calendar = Calendar.getInstance();

			// get all calendarposts related to the current user from the given month
			//calendar.set(Integer.valueOf(uriParser.get(2)), Integer.valueOf(uriParser.get(3))-1 , 1, 0, 0);

			// get all calendarposts related to the current user from the given month +- 1 month.
			calendar.set(Integer.valueOf(uriParser.get(2)), Integer.valueOf(uriParser.get(3))-2, 1, 0, 0);

			Date startDate = calendar.getTime();

			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+2);

			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

			Date endDate = calendar.getTime();

			ArrayList<CalendarPost> posts = calendarDAO.getCalenderPosts(user, startDate, endDate, false);

			String json = this.createJSONObjects(posts, uriParser);

			res.setHeader("Cache-Control", "no-cache");

			HTTPUtils.sendReponse(json.toString(), "text/html;charset=ISO-8859-1", res);

			return null;

		}

	}

	@WebPublic
	public ForegroundModuleResponse addPost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		ValidationException validationException = null;

		if(req.getMethod().equalsIgnoreCase("POST")) {

			try{

				CalendarPost post = CalendarPopulator.populate(req);

				List<ValidationError> validationErrors = new ArrayList<ValidationError>();

				publishingFactory.validateAndSetAccess(req, post, validationErrors, user);

				if (!validationErrors.isEmpty()) {
					throw new ValidationException(validationErrors);
				}

				post.setPosterID(user.getUserID());

				log.info("User " + user + " adding calendar post " + post + " using combined calendar");

				this.calendarDAO.add(post);

				Calendar cal = Calendar.getInstance();

				cal.setTime(post.getStartTime());

				res.sendRedirect(this.getModuleURI(req) + "/showMonth/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1));

				return null;

			}catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, user);

		Element addPostElement = doc.createElement("addPost");
		doc.getFirstChild().appendChild(addPostElement);

		addPostElement.appendChild(user.toXML(doc));

		Element publishingXML = this.publishingFactory.getAccessibleXML(user, doc);

		if(publishingXML == null) {
			throw new AccessDeniedException("Permission denied for user " + user);
		}

		addPostElement.appendChild(publishingXML);

		Calendar cal = Calendar.getInstance();

		if(uriParser.size() == 5 && NumberUtils.isInt(uriParser.get(2)) && NumberUtils.isInt(uriParser.get(3)) && NumberUtils.isInt(uriParser.get(4))){

			cal = Calendar.getInstance();

			cal.set(Integer.valueOf(uriParser.get(2)), (Integer.valueOf(uriParser.get(3))-1), Integer.valueOf(uriParser.get(4)));

			String date = DateUtils.DATE_FORMATTER.format(cal.getTime());

			XMLUtils.appendNewElement(doc, addPostElement, "date", date);

			XMLUtils.appendNewElement(doc, addPostElement, "month", (cal.get(Calendar.MONTH)+1));

			XMLUtils.appendNewElement(doc, addPostElement, "year", cal.get(Calendar.YEAR));

		}

		if(validationException != null){
			addPostElement.appendChild(validationException.toXML(doc));
			addPostElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(new Timestamp(cal.getTimeInMillis()))), new Breadcrumb(addPostBreadCrumb,addPostBreadCrumb,"#"));

	}

	@WebPublic
	public ForegroundModuleResponse showPost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		CalendarPost post = null;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(2)))) == null) {

			throw new URINotFoundException(uriParser);

		} else if(!AccessUtils.checkReadAccess(post, user, GroupAccessLevel.MEMBER)){

			throw new AccessDeniedException("Access denied to calendar post " + post);
		}

		log.info("User " + user + " requesting calendar post " + post + " in combined calendar");

		this.checkCalendarPostReadStatus(user, post);

		Document doc = this.createDocument(req, uriParser, user);

		Element showPostElement = doc.createElement("showPost");

		doc.getFirstChild().appendChild(showPostElement);

		post.setFullDate(this.getUserLanguage(user));

		showPostElement.appendChild(post.toXML(doc));

		CommunityUser postedUser = (CommunityUser)this.userHandler.getUser(post.getPosterID(), false, false);

		if(postedUser != null){

			Element postedUserElement = doc.createElement("postedUser");

			showPostElement.appendChild(postedUserElement);

			postedUserElement.appendChild(postedUser.toXML(doc));

		}

		if(this.hasUpdateAccess(post, user)) {
			XMLUtils.appendNewElement(doc, showPostElement, "hasUpdateAccess", "true");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(post.getStartTime());

		return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(post.getStartTime())), new Breadcrumb(post.getDescription(), "", "#"));
	}

	@WebPublic
	public ForegroundModuleResponse updatePost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		ValidationException validationException = null;

		CalendarPost post = null;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(2)))) == null) {

			throw new URINotFoundException(uriParser);

		} else {

			this.checkAdminAccess(post, user);

			String oldDescription = post.getDescription();

			if(req.getMethod().equalsIgnoreCase("POST")){

				try{

					post = CalendarPopulator.populate(post, req);

					List<ValidationError> validationErrors = new ArrayList<ValidationError>();

					publishingFactory.validateAndSetAccess(req, post, validationErrors, user);

					if (!validationErrors.isEmpty()) {
						throw new ValidationException(validationErrors);
					}

					post.setPosterID(user.getUserID());

					log.info("User " + user + " updating calendar post " + post + " using combined calendar");

					this.calendarDAO.update(post);

					res.sendRedirect(this.getModuleURI(req) + "/showPost/" + post.getCalendarID());

					return null;

				}catch (ValidationException e) {
					validationException = e;
				}

			}

			Document doc = this.createDocument(req, uriParser, user);

			Element updatePostElement = doc.createElement("updatePost");
			doc.getFirstChild().appendChild(updatePostElement);

			updatePostElement.appendChild(post.toXML(doc));

			updatePostElement.appendChild(user.toXML(doc));

			Element publishingXML = this.publishingFactory.getAccessibleXML(user, doc);

			if(publishingXML == null) {
				throw new AccessDeniedException("Permission denied for user " + user);
			}

			updatePostElement.appendChild(publishingXML);

			if(validationException != null){
				updatePostElement.appendChild(validationException.toXML(doc));
				updatePostElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(post.getStartTime())), getMethodBreadcrumb(oldDescription, "", "showPost/" + post.getCalendarID()), new Breadcrumb(updatePostBreadCrumb, updatePostBreadCrumb, "#"));
		}

	}

	@WebPublic
	public ForegroundModuleResponse deletePost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		CalendarPost post = null;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(2)))) == null) {

			throw new URINotFoundException(uriParser);

		} else {

			this.checkAdminAccess(post, user);

			log.info("User " + user + " deleting calendar post " + post);

			this.calendarDAO.delete(post);

			Calendar cal = Calendar.getInstance();

			cal.setTime(post.getStartTime());

			res.sendRedirect(this.getModuleURI(req) + "/showMonth/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1));

			return null;

		}

	}

	@WebPublic
	public ForegroundModuleResponse showReadReceipt(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		CalendarPost post;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(2)))) == null) {

			throw new URINotFoundException(uriParser);

		} else {

			this.checkAdminAccess(post, user);

			Document doc = this.createDocument(req, uriParser, user);

			Element showReceiptElement = doc.createElement("showReadReceipt");

			doc.getFirstChild().appendChild(showReceiptElement);

			showReceiptElement.appendChild(post.toXML(doc));

			ArrayList<Receipt> receipts = this.calendarDAO.getCalendarPostReceipt(post);
			
			int hiddenReceipts = 0;
			
			for(Iterator<Receipt> it = receipts.iterator(); it.hasNext();){
				Receipt receipt = it.next();
				
				if(!AccessUtils.checkReceiptAccess(receipt, user, post)){
					
					it.remove();
					hiddenReceipts++;
				}
			}

			XMLUtils.appendNewElement(doc, showReceiptElement, "HiddenReceipts", hiddenReceipts);

			XMLUtils.append(doc, showReceiptElement, receipts);

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getModuleBreadcrumb(), getMethodBreadcrumb(post.getDescription(), "", "showPost/" + post.getCalendarID()), new Breadcrumb(readReceiptBreadCrumb, readReceiptBreadCrumb, "#"));

		}

	}

	private String createJSONObjects(ArrayList<CalendarPost> posts, URIParser uriParser){

		StringBuilder json = new StringBuilder("{\"posts\": [");

		JSONSerializer serializer = new JSONSerializer();

		if(posts != null){
			for(CalendarPost post : posts){

				post.setUrl(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/showPost/" + post.getCalendarID());
				json.append(serializer.exclude("details", "posterID", "posted", "groupName", "schoolName", "*.class", "*.school").deepSerialize(post) + ",");

			}
			json.deleteCharAt(json.length() - 1);
		}

		json.append("] }");

		return json.toString();

	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user){

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		if(this.cssPath != null) {
			XMLUtils.appendNewElement(doc, document, "cssPath", this.cssPath);
		}

		boolean isAdmin = false;

		if(!CollectionUtils.isEmpty(user.getSchools())) {
			isAdmin = true;
		} else if(user.getGroups() != null) {

			for(CommunityGroup group : user.getCommunityGroups()){

				if(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)){

					isAdmin = true;
					break;

				}

			}

		}

		document.appendChild(XMLUtils.createElement("isAdmin", (user.isAdmin() ? "true" : String.valueOf(isAdmin)), doc));
		document.appendChild(XMLUtils.createElement("isSysAdmin", String.valueOf(user.isAdmin()), doc));
		doc.appendChild(document);

		return doc;

	}

	private Element getDefaultXML(Document doc){

		Element calendarElement = doc.createElement("calendarmodule");
		doc.getFirstChild().appendChild(calendarElement);

		return calendarElement;

	}

	private Breadcrumb getMethodBreadcrumb(String name, String description, String methodUrl) {
		return new Breadcrumb(name, description, this.getFullAlias() + "/" + methodUrl, URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	private String getShowCalenderURL(Timestamp timestamp){

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);

		return "showMonth/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1);

	}

	private void checkCalendarPostReadStatus(CommunityUser user, CalendarPost post) throws SQLException{
		this.calendarDAO.checkCalendarPostReadStatus(user, post);
	}

	public boolean hasUpdateAccess(CalendarPost post, CommunityUser user) {

		return AccessUtils.checkAdminAccess(post, user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);

	}

	public void checkAdminAccess(CalendarPost post, CommunityUser user) throws AccessDeniedException {

		if(!this.hasUpdateAccess(post, user)) {
			throw new AccessDeniedException("Permission denied for user " + user);
		}

	}

	@Override
	public ModuleType getModuleType() {
		return ModuleType.Administration;
	}

	@Override
	public String getFullAlias(CommunityGroup group) {
		return null;
	}

	@Override
	public List<Event> getGroupResume(CommunityGroup group, CommunityUser user) throws Exception {
		return null;
	}

	@Override
	public List<? extends se.dosf.communitybase.beans.Event> getGroupResume(
			CommunityGroup group, CommunityUser user, Timestamp startStamp)
					throws Exception {
		return null;
	}

}
