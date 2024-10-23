package se.dosf.communitybase.modules.calendar;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.beans.Receipt;
import se.dosf.communitybase.beans.School;
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
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.ModuleSetting;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import flexjson.JSONSerializer;

public class CombinedCalendarModule extends AnnotatedGlobalModule implements CommunityModule {

	private CalendarModuleDAO calendarDAO;
	private CommunitySchoolDAO schoolDAO;
	private CommunityGroupDAO communityGroupDAO;
	private UserHandler userHandler;
	private CalendarPopulator CalendarPopulator = new CalendarPopulator();

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="Editor CSS",description="Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)",required=false)
	protected String cssPath;
	
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
		
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);
		
		this.userHandler = this.sectionInterface.getSystemInterface().getUserHandler();
		
		this.calendarDAO = new CalendarModuleDAO(dataSource, this.userHandler);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);
		this.communityGroupDAO = new CommunityGroupDAO(dataSource);
		this.schoolDAO.setGroupDAO(this.communityGroupDAO);
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

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

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

			ArrayList<CalendarPost> posts = calendarDAO.getCalenderPosts(user, startDate, endDate);
			
			String json = this.createJSONObjects(posts, uriParser);

			res.setHeader("Cache-Control", "no-cache");

			HTTPUtils.sendReponse(json.toString(), "text/html;charset=ISO-8859-1", res);

			return null;

		}

	}
	
	@WebPublic
	public ForegroundModuleResponse addPost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		ValidationException validationException = null;
		
		if(req.getMethod().equalsIgnoreCase("POST")){

			try{

				ArrayList<String> chosenGroups = new ArrayList<String>();
				ArrayList<String> chosenSchools = new ArrayList<String>();
				
				CalendarPost post = CalendarPopulator.populate(req, chosenGroups, chosenSchools);

				post.setPosterID(user.getUserID());

				log.info("User " + user + " adding calendar post " + post + " using combined calendar");

				this.calendarDAO.add(post, chosenGroups, chosenSchools);

				Calendar cal = Calendar.getInstance();

				cal.setTime(post.getStartTime());

				res.sendRedirect(this.getURI(req) + "/showMonth/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1));

				return null;

			}catch (ValidationException e) {
				validationException = e;
			}

		}

		Document doc = this.createDocument(req, uriParser, user);

		Element addPostElement = doc.createElement("addPost");
		doc.getFirstChild().appendChild(addPostElement);
		
		addPostElement.appendChild(user.toXML(doc));
		
		addPostElement.appendChild(this.getPublisherXML(user, doc));
		
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

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {
			
			log.info("User " + user + " requesting calendar post " + post + " in combined calendar");

			this.checkCalendarPostReadStatus(user, post);

			Document doc = this.createDocument(req, uriParser, user);

			Element showPostElement = doc.createElement("showPost");

			doc.getFirstChild().appendChild(showPostElement);

			showPostElement.appendChild(post.toXML(doc));

			CommunityUser postedUser = (CommunityUser)this.userHandler.getUser(post.getPosterID(), false);

			if(postedUser != null){

				Element postedUserElement = doc.createElement("postedUser");

				showPostElement.appendChild(postedUserElement);

				postedUserElement.appendChild(postedUser.toXML(doc));

			}

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(post.getStartTime());

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(post.getStartTime())), new Breadcrumb(post.getDescription(), "", "#"));

		}
		
	}
	
	@GroupMethod
	public ForegroundModuleResponse showReadReceipt(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		CalendarPost post;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			Document doc = this.createDocument(req, uriParser, user);

			Element showReceiptElement = doc.createElement("showReadReceipt");

			doc.getFirstChild().appendChild(showReceiptElement);

			showReceiptElement.appendChild(post.toXML(doc));

			ArrayList<Receipt> receipts = this.calendarDAO.getCalendarPostReceipt(post);

			XMLUtils.append(doc, showReceiptElement, receipts);

			return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getModuleBreadcrumb(), getMethodBreadcrumb(post.getDescription(), "", "showPost/" + post.getCalendarID()), new Breadcrumb(readReceiptBreadCrumb, readReceiptBreadCrumb, "#"));

		}

	}
	
	
	@WebPublic
	public ForegroundModuleResponse updatePost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		ValidationException validationException = null;

		CalendarPost post = null;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(2)))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			this.checkAccess(post, user);
			
			String oldDescription = post.getDescription();
			
			if(req.getMethod().equalsIgnoreCase("POST")){

				try{

					ArrayList<String> chosenGroups = new ArrayList<String>();
					ArrayList<String> chosenSchools = new ArrayList<String>();
					
					post = CalendarPopulator.populate(post, req, chosenGroups, chosenSchools);

					post.setPosterID(user.getUserID());

					log.info("User " + user + " updating calendar post " + post + " using combined calendar");

					this.calendarDAO.update(post, chosenGroups, chosenSchools);

					res.sendRedirect(this.getURI(req) + "/showPost/" + post.getCalendarID());

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
			
			updatePostElement.appendChild(this.getPublisherXML(user, doc));			

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

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			this.checkAccess(post, user);
			
			log.info("User " + user + " deleting calendar post " + post);

			this.calendarDAO.delete(post);

			Calendar cal = Calendar.getInstance();

			cal.setTime(post.getStartTime());

			res.sendRedirect(this.getURI(req) + "/showMonth/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1));

			return null;
			
		}
		
	}

	@WebPublic
	public ForegroundModuleResponse showReadReceipt(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {

		CalendarPost post;

		if (uriParser.size() != 3 || !NumberUtils.isInt(uriParser.get(2)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(2)))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, uriParser);

		} else {

			Document doc = this.createDocument(req, uriParser, user);

			Element showReceiptElement = doc.createElement("showReadReceipt");

			doc.getFirstChild().appendChild(showReceiptElement);

			showReceiptElement.appendChild(post.toXML(doc));

			ArrayList<Receipt> receipts = this.calendarDAO.getCalendarPostReceipt(post);

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
				json.append(serializer.exclude("details", "posterID", "posted", "groupName", "schoolName", "class").serialize(post) + ",");

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
		
		if(user.getGroups() != null){
		
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
	
	private Element getPublisherXML(CommunityUser user, Document doc) throws SQLException, AccessDeniedException{
		
		Element schoolsElement = doc.createElement("schools");
		
		if(!user.isAdmin()){
			
			Map<CommunityGroup, GroupRelation> groups = user.getGroupMap();
			
			if (groups != null && !groups.isEmpty()) {
				
				HashMap<School, Element> addedSchools = new HashMap<School, Element>();
				
				List<School> schoolAdmins = user.getSchools() != null ? user.getSchools() : new ArrayList<School>();
				
				for (Entry<CommunityGroup, GroupRelation> entry : groups.entrySet()) {
	
					School school = entry.getKey().getSchool();
					
					if(!schoolAdmins.contains(school) && AccessUtils.checkAccess(user, entry.getKey(), GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)){
					
						if(addedSchools.containsKey(school)){
							
							addedSchools.get(school).appendChild(entry.getKey().toXML(doc));
							
						}else{
							
							Element groupsElement = doc.createElement("groups");
							
							groupsElement.appendChild(entry.getKey().toXML(doc));
							
							addedSchools.put(school, groupsElement);
							
						}
					
					}else{

						Element groupsElement = doc.createElement("groups");
						
						XMLUtils.append(doc, groupsElement, this.communityGroupDAO.getGroups(school));
						
						addedSchools.put(school, groupsElement);
						
					}
					
				}
				
				for(School schoolAdmin : schoolAdmins) {
					
					if(!addedSchools.containsKey(schoolAdmin)) {
						
						Element groupsElement = doc.createElement("groups");
						
						XMLUtils.append(doc, groupsElement, this.communityGroupDAO.getGroups(schoolAdmin));
						
						addedSchools.put(schoolAdmin, groupsElement);
					}
					
				}
				
				if(!addedSchools.isEmpty()){
				
					for(School school : addedSchools.keySet()){
						
						Element schoolElement = school.toXML(doc);
						
						schoolElement.appendChild(addedSchools.get(school));
						
						schoolsElement.appendChild(schoolElement);
						
					}
				
				}else{
					throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied for user " + user);	
				}
				
			}
			
		}else{
			
			List<School> schools = this.schoolDAO.getSchools(false, true);
			
			if(schools != null){
				XMLUtils.append(doc, schoolsElement, schools);
			}
			
		}
	
		return schoolsElement;
		
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
	
	private void checkAccess(CalendarPost post, CommunityUser user) throws AccessDeniedException {
		
		if(post.isGlobal()){
			this.checkSysAdminAccess(user);
		}else{
		
			if(post.getGroups() != null){
				for(CommunityGroup group : post.getGroups()){
					try{
						this.checkAdminAccess(user, group);
					}catch(AccessDeniedException e){
						this.checkAdminAccess(user, group.getSchool());
					}
				}
			}
			
			if(post.getSchools()!= null){
				for(School school : post.getSchools()){
					this.checkAdminAccess(user, school);
				}
			}
			
		}		
		
	}
	
	@Override
	public ModuleType getModuleType() {
		return ModuleType.Administration;
	}

	public String getFullAlias(CommunityGroup group) {
		return null;
	}

	public List<Event> getGroupResume(CommunityGroup group, CommunityUser user) throws Exception {
		return null;
	}

	public List<? extends se.dosf.communitybase.beans.Event> getGroupResume(
			CommunityGroup group, CommunityUser user, Timestamp startStamp)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
