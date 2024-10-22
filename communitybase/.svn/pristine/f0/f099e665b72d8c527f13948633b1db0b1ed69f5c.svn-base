package se.dosf.communitybase.modules.calendar;

import java.io.StringWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.beans.Receipt;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.RelationType;
import se.dosf.communitybase.interfaces.EmailResumeProvider;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.calendar.beans.CalendarPost;
import se.dosf.communitybase.modules.calendar.daos.CalendarModuleDAO;
import se.dosf.communitybase.modules.calendar.populators.CalendarPopulator;
import se.dosf.communitybase.modules.dbcleanup.DBCleaner;
import se.dosf.communitybase.modules.oldcontentremover.beans.OldContent;
import se.dosf.communitybase.modules.oldcontentremover.interfaces.OldContentRemover;
import se.dosf.communitybase.utils.AccessUtils;
import se.dosf.communitybase.utils.AccessibleFactory;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
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
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import flexjson.JSONSerializer;

public class CalendarModule extends AnnotatedCommunityModule implements CommunityModule, EmailResumeProvider, DBCleaner, OldContentRemover {

	private CalendarModuleDAO calendarDAO;
	
	private UserHandler userHandler;
	private static CalendarPopulator CalendarPopulator = new CalendarPopulator();

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="Editor CSS",description="Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)",required=false)
	protected String cssPath;
	
	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name="Full URL to CombinedCalendarModule",description="The full URL to the CombinedCalendarModule",required=false)
	protected String combinedCalendarURL;
	
	@XSLVariable
	protected String updatePostBreadCrumb = "Update post";

	@XSLVariable
	protected String addPostBreadCrumb = "Add post";

	@XSLVariable
	protected String readReceiptBreadCrumb = "Readreceipt";

	@XSLVariable
	protected String newCalendarPostTest = "New event: ";

	private AccessibleFactory publishingFactory;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.userHandler = this.sectionInterface.getSystemInterface().getUserHandler();

		this.createDAOs(this.dataSource);
		this.publishingFactory = new AccessibleFactory(getGroupDAO(), getSchoolDAO());
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);

		this.userHandler = this.sectionInterface.getSystemInterface().getUserHandler();

		this.createDAOs(this.dataSource);
		this.publishingFactory = new AccessibleFactory(getGroupDAO(), getSchoolDAO());
	}
	
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		super.createDAOs(dataSource);
		
		this.calendarDAO = new CalendarModuleDAO(dataSource, this.userHandler);
	}

	@Override
	@GroupMethod
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		log.info("User " + user + " requesting calendar for group " + group);

		Document doc = this.createDocument(req, uriParser, group, user);

		Element calendarElement = this.getDefaultXML(doc);

		XMLUtils.appendNewElement(doc, calendarElement, "startMonth", new Date().getTime());

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group),  getModuleBreadcrumb(group));
	}

	@GroupMethod
	public ForegroundModuleResponse showMonth(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		Document doc = this.createDocument(req, uriParser, group, user);

		Element calendarElement = this.getDefaultXML(doc);

		if(uriParser.size() == 5 && NumberUtils.isInt(uriParser.get(3)) && NumberUtils.isInt(uriParser.get(4))){

			Calendar cal = Calendar.getInstance();

			cal.set(Integer.valueOf(uriParser.get(3)), (Integer.valueOf(uriParser.get(4))-1), 1);

			XMLUtils.appendNewElement(doc, calendarElement, "startMonth", cal.getTimeInMillis());

		}else{

			XMLUtils.appendNewElement(doc, calendarElement, "startMonth", new Date().getTime());

		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getModuleBreadcrumb(group));

	}

	@GroupMethod
	public ForegroundModuleResponse getPosts(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {


		if (uriParser.size() != 5 || !NumberUtils.isInt(uriParser.get(3)) || !NumberUtils.isInt(uriParser.get(4))) {

			throw new URINotFoundException(uriParser);

		} else {

			Calendar calendar = Calendar.getInstance();

			// get all calendarposts related to the current user from the given month
			//calendar.set(Integer.valueOf(uriParser.get(3)), Integer.valueOf(uriParser.get(4))-1, 1, 0, 0);
			
			// get all calendarposts related to the current user from the given month +- 1 month.
			calendar.set(Integer.valueOf(uriParser.get(3)), Integer.valueOf(uriParser.get(4))-2, 1, 0, 0);

			Date startDate = calendar.getTime();

			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+2);

			calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));

			Date endDate = calendar.getTime();

			ArrayList<CalendarPost> posts = calendarDAO.getCalenderPosts(group, startDate, endDate);

			String json = this.createJSONObjects(posts, group, uriParser);

			res.setHeader("Cache-Control", "no-cache");

			HTTPUtils.sendReponse(json.toString(), "text/html;charset=ISO-8859-1", res);

			return null;

		}

	}

	@GroupMethod
	public ForegroundModuleResponse showPost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		CalendarPost post = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

			throw new URINotFoundException(uriParser);

		} else {

			if(!AccessUtils.checkGroupAccess(post, group)){
				
				throw new AccessDeniedException("User does not have access to calendar post " + post + " via group " + group);
			}
			
			log.info("User " + user + " requesting calendar post " + post + " for group " + group);

			this.checkCalendarPostReadStatus(user, post);

			Document doc = this.createDocument(req, uriParser, group, user);

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
			
			if(AccessUtils.checkAccess(user, group, GroupAccessLevel.PUBLISHER)) {
				XMLUtils.appendNewElement(doc, showPostElement, "hasReceiptAccess");
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(post.getStartTime());

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(post.getStartTime()), group), new Breadcrumb(post.getDescription(), "", "#"));

		}
		
	}

	@GroupMethod
	public ForegroundModuleResponse addPost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		
		ValidationException validationException = null;

		if(req.getMethod().equalsIgnoreCase("POST")){

			try{
				
				RelationType type = RelationType.valueOf(req.getParameter("posttype"));
				
				this.checkAdminAccess(user, group, type);
				
				CalendarPost post = CalendarPopulator.populate(req);

				post.setPosterID(user.getUserID());

				log.info("User " + user + " adding calendar post " + post + " to group " + group);

				this.calendarDAO.add(post, type, group);

				Calendar cal = Calendar.getInstance();

				cal.setTime(post.getStartTime());

				res.sendRedirect(this.getModuleURI(req) + "/" + group.getGroupID() + "/showMonth/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1));

				return null;

			}catch (ValidationException e) {
				validationException = e;
			}

		}

		this.checkAdminAccess(user, group);
		
		Document doc = this.createDocument(req, uriParser, group, user);

		Element addPostElement = doc.createElement("addPost");
		doc.getFirstChild().appendChild(addPostElement);

		Calendar cal = Calendar.getInstance();

		if(uriParser.size() == 6 && NumberUtils.isInt(uriParser.get(3)) && NumberUtils.isInt(uriParser.get(4)) && NumberUtils.isInt(uriParser.get(5))){

			cal = Calendar.getInstance();
			
			cal.set(Integer.valueOf(uriParser.get(3)), (Integer.valueOf(uriParser.get(4))-1), Integer.valueOf(uriParser.get(5)));

			String date = DateUtils.DATE_FORMATTER.format(cal.getTime());

			XMLUtils.appendNewElement(doc, addPostElement, "date", date);

			XMLUtils.appendNewElement(doc, addPostElement, "month", (cal.get(Calendar.MONTH)+1));

			XMLUtils.appendNewElement(doc, addPostElement, "year", cal.get(Calendar.YEAR));

		}

		if(validationException != null){
			addPostElement.appendChild(validationException.toXML(doc));
			addPostElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(new Timestamp(cal.getTimeInMillis())), group), new Breadcrumb(addPostBreadCrumb,addPostBreadCrumb,"#"));

	}

	@GroupMethod
	public ForegroundModuleResponse updatePost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		ValidationException validationException = null;

		CalendarPost post = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

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

					log.info("User " + user + " updating calendar post " + post + " in group " + group);

					this.calendarDAO.update(post);

					res.sendRedirect(this.getModuleURI(req) + "/" + group.getGroupID() + "/showPost/" + post.getCalendarID());

					return null;
					
				}catch (ValidationException e) {
					validationException = e;
				}

			}

			Document doc = this.createDocument(req, uriParser, group, user);

			Element updatePostElement = doc.createElement("updatePost");

			doc.getFirstChild().appendChild(updatePostElement);

			updatePostElement.appendChild(post.toXML(doc));

			updatePostElement.appendChild(user.toXML(doc));

			Element publishingXML = this.publishingFactory.getAccessibleXML(user, doc);

			if (publishingXML == null) {
				throw new AccessDeniedException("Permission denied for user " + user);
			}

			updatePostElement.appendChild(publishingXML);

			if(validationException != null){
				updatePostElement.appendChild(validationException.toXML(doc));
				updatePostElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(post.getStartTime()), group), getMethodBreadcrumb(oldDescription, "", "showPost/" + post.getCalendarID(), group), new Breadcrumb(updatePostBreadCrumb, updatePostBreadCrumb, "#"));
		}
		
	}

	@GroupMethod
	public ForegroundModuleResponse deletePost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		CalendarPost post = null;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

			throw new URINotFoundException(uriParser);

		} else {

			this.checkAdminAccess(post, user);
			
			log.info("User " + user + " deleting calendar post " + post + " from group " + group);

			this.calendarDAO.delete(post);

			Calendar cal = Calendar.getInstance();

			cal.setTime(post.getStartTime());

			res.sendRedirect(this.getModuleURI(req) + "/" + group.getGroupID() + "/showMonth/" + cal.get(Calendar.YEAR) + "/" + (cal.get(Calendar.MONTH)+1));

			return null;
		}
		
	}

	@GroupMethod
	public ForegroundModuleResponse showReadReceipt(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		CalendarPost post;

		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (post = this.calendarDAO.get(Integer.valueOf(uriParser.get(3)))) == null) {

			throw new URINotFoundException(uriParser);

		} else {

			if(!AccessUtils.checkAccess(user, group, GroupAccessLevel.PUBLISHER) && AccessUtils.checkGroupAccess(post, group)){
				
				throw new AccessDeniedException("User does not have access to read receipts for calendar post " + post + " via group " + group);
			}

			log.info("User " + user + " listing calendar read receipts for " + post + " in group " + group);
			
			Document doc = this.createDocument(req, uriParser, group, user);

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

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), getMethodBreadcrumb(this.moduleDescriptor.getName(), "", getShowCalenderURL(post.getStartTime()), group), getMethodBreadcrumb(post.getDescription(), "", "showPost/" + post.getCalendarID(), group), new Breadcrumb(readReceiptBreadCrumb, readReceiptBreadCrumb, "#"));

		}

	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityGroup group, CommunityUser user){

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));
		if(this.cssPath != null) {
			XMLUtils.appendNewElement(doc, document, "cssPath", this.cssPath);
		}
		
		document.appendChild(XMLUtils.createElement("isSchoolAdmin", String.valueOf(AccessUtils.checkAccess(user, group.getSchool())), doc));
		document.appendChild(XMLUtils.createElement("isGroupAdmin", String.valueOf(AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)), doc));
		document.appendChild(XMLUtils.createElement("isSysAdmin", String.valueOf(user.isAdmin()), doc));
		doc.appendChild(document);

		return doc;
		
	}

	private String getShowCalenderURL(Timestamp timestamp){

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(timestamp);

		return "showMonth/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH)+1);

	}

	private Breadcrumb getMethodBreadcrumb(String name, String description, String methodUrl, CommunityGroup group) {
		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + methodUrl, URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	private void checkCalendarPostReadStatus(CommunityUser user, CalendarPost post) throws SQLException{
		this.calendarDAO.checkCalendarPostReadStatus(user, post);
	}

	private Element getDefaultXML(Document doc){

		Element calendarElement = doc.createElement("calendarmodule");
		doc.getFirstChild().appendChild(calendarElement);

		return calendarElement;

	}

	private String createJSONObjects(ArrayList<CalendarPost> posts, CommunityGroup group, URIParser uriParser){

		StringBuilder json = new StringBuilder("{\"posts\": [");

		JSONSerializer serializer = new JSONSerializer();

		if(posts != null){
			for(CalendarPost post : posts){

				post.setUrl(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + (group != null ? "/" + group.getGroupID() : "") + "/showPost/" + post.getCalendarID());
				json.append(serializer.exclude("details", "posterID", "posted", "groupName", "schoolName", "*.class", "*.school").deepSerialize(post) + ",");

			}
			json.deleteCharAt(json.length() - 1);
		}

		json.append("] }");

		return json.toString();

	}
	
	public boolean hasUpdateAccess(CalendarPost post, CommunityUser user) {
		
		return AccessUtils.checkAdminAccess(post, user, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER);
		
	}
	
	public void checkAdminAccess(CalendarPost post, CommunityUser user) throws AccessDeniedException {
		
		if(!this.hasUpdateAccess(post, user)) {
			throw new AccessDeniedException("Permission denied for user " + user);
		}
		
	}

	public List<Event> getSchoolResume(CommunityGroup group, CommunityUser user)	throws Exception {
		return null;
	}

	@Override
	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		List<IdentifiedEvent> calendarEvents = this.calendarDAO.getCalendarResume(group, startStamp);

		if(calendarEvents != null){

			for(IdentifiedEvent event : calendarEvents){
				event.setTitle(this.newCalendarPostTest + StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(event.getTitle());
				event.setFullAlias(this.getFullAlias(group) + "/showPost/" + event.getId());
			}
		}

		return calendarEvents;
	}
	
	private void checkAdminAccess(CommunityUser user, CommunityGroup group, RelationType type) throws AccessDeniedException {
		
		if(type.equals(RelationType.GLOBAL)) {
			
			this.checkAdminAccess(user);
		
		} else if(type.equals(RelationType.SCHOOL)) {
			
			this.checkAdminAccess(user, group.getSchool());
		
		} else {
			
			this.checkAdminAccess(user, group);
		
		}
		
	}

	@Override
	public String getGlobalEmailResumeHTML(CommunityUser user) throws Exception {

		if(!StringUtils.isEmpty(combinedCalendarURL)) {
		
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, 1);
	
			Date startDate = DateUtils.setTimeToMidnight(calendar.getTime());
	
			Date endDate = DateUtils.setTimeToMaximum(calendar.getTime());
	
			List<CalendarPost> posts = this.calendarDAO.getCalenderPosts(user, startDate, endDate, true);
	
			if(!CollectionUtils.isEmpty(posts)) {
			
				Document doc = XMLUtils.createDomDocument();
		
				Element documentElement = doc.createElement("GlobalResume");
				doc.appendChild(documentElement);
		
				XMLUtils.appendNewElement(doc, documentElement, "date", DateUtils.DATE_FORMATTER.format(startDate));
				XMLUtils.appendNewElement(doc, documentElement, "combinedCalendarURL", combinedCalendarURL);
				XMLUtils.append(doc, documentElement, posts);
		
				Transformer transformer = null;
		
				try {
					transformer = this.sectionInterface.getForegroundModuleXSLTCache().getModuleTranformer(this.moduleDescriptor);
				} catch (TransformerConfigurationException e) {
					log.error("Unable to get transformer from ModuleXSLTCache, aborting", e);
					return null;
				}
		
				if (transformer == null) {
					log.error("Found no cached stylesheet in ModuleXSLTCache, aborting");
				}
		
				StringWriter messageWriter = new StringWriter();
		
				try {
					transformer.transform(new DOMSource(doc), new StreamResult(messageWriter));
				} catch (TransformerException e) {
					log.error("Unable to transform e-mail resume for user " + user, e);
				}
				
				return messageWriter.toString();
			
			}
		
		}

		return null;
	}
	
	private List<Integer> getOrphanedCalendarsIDs() throws SQLException {

		String sql = "SELECT DISTINCT c.calendarID FROM calendar c LEFT OUTER JOIN groupcalendar g ON c.calendarID = g.calendarID LEFT OUTER JOIN schoolcalendar s ON c.calendarID = s.calendarID LEFT OUTER JOIN globalcalendar gc ON gc.calendarID = c.calendarID WHERE groupID IS null AND schoolID IS null AND gc.calendarID IS null";

		ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(this.dataSource, sql, new IntegerPopulator());

		return query.executeQuery();
	}

	@Override
	public void cleanDB() throws SQLException {

		List<Integer> orphans = getOrphanedCalendarsIDs();

		if(orphans != null && orphans.size() > 0){
			log.warn("There are " + orphans.size() + " orphaned calendars.");

			for(Integer id : orphans){

				log.info("Deleting calendar with id " + id);

				calendarDAO.delete(id);
			}
		}

	}

	@Override
	public int getOldContentCount(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException {

		ObjectQuery<Integer> query = new ObjectQuery<Integer>(dataSource, "SELECT COUNT(n.calendarID) FROM calendar as n " +
				"LEFT OUTER JOIN groupcalendar as gn ON n.calendarID = gn.calendarID " +
				"LEFT OUTER JOIN globalcalendar as gg ON n.calendarID = gg.calendarID " +
				"WHERE gn.groupID = ? AND gg.calendarID IS NULL AND n.endTime <= ? ",
				IntegerPopulator.getPopulator());

		query.setInt(1, group.getGroupID());
		query.setObject(2, new Timestamp(endDate.getTime()));

		return query.executeQuery();
	}

	@Override
	public Collection<OldContent> getOldContent(CommunityUser user, CommunityGroup group, Date endDate) throws SQLException {

		String sql = "SELECT DISTINCT calendar.*" +
				"FROM calendar " +
				"LEFT JOIN groupcalendar " +
				"ON calendar.calendarID = groupcalendar.calendarID " +
				"LEFT JOIN globalcalendar " +
				"ON calendar.calendarID = globalcalendar.calendarID " +
				"WHERE groupcalendar.groupID = ? " +
				"AND globalcalendar.calendarID IS NULL " +
				"AND endTime <= ? " +
				"ORDER BY endTime ASC;";

		ArrayListQuery<CalendarPost> query = new ArrayListQuery<CalendarPost>(dataSource.getConnection(), true, sql, new CalendarPopulator());

		query.setInt(1, group.getGroupID());
		query.setTimestamp(2, new Timestamp(endDate.getTime()));

		ArrayList<CalendarPost> posts = query.executeQuery();

		if (posts != null) {

			for (CalendarPost post : posts) {
				post.setGroups(calendarDAO.getGroups(post, null));
				post.setSchools(calendarDAO.getSchools(post, null));
			}

			Collection<OldContent> content = new ArrayList<OldContent>();

			for (CalendarPost calendarPost : posts) {
				try{
					if (!calendarPost.isGlobal()) {
						this.checkAdminAccess(calendarPost, user);
						content.add(new OldContent(calendarPost.getCalendarID(), calendarPost.getDescription(), getFullAlias(group) + "/showPost/" + calendarPost.getCalendarID(), calendarPost.getEndTime(), CalendarPost.class.getName().toString()));
					}
				} catch (AccessDeniedException ignore) {}
			}

			return content;
		}

		return null;
	}

	@Override
	public int deleteOldContent(Collection<OldContent> oldContents, CommunityUser user, CommunityGroup group) throws SQLException {

		int deletedCount = 0;

		for (OldContent oldContent : oldContents) {

			CalendarPost calendarPost = calendarDAO.get(oldContent.getID());

			if (calendarPost == null) {
				continue;
			}

			if (!calendarPost.isGlobal()) {

				try {
					this.checkAdminAccess(calendarPost, user);

					if (calendarPost.getSchools() == null && calendarPost.getGroups().size() == 1 && calendarPost.getGroups().get(0).getGroupID().equals(group.getGroupID())) {

						//Our group is the last one, delete
						log.info("Deleting calendar post " + calendarPost.getCalendarID());
						calendarDAO.delete(calendarPost);

					} else {

						//Our group is not the last one, remove ourselves
						for (Iterator<CommunityGroup> it = calendarPost.getGroups().iterator(); it.hasNext();) {

							CommunityGroup communityGroup = it.next();
							if (communityGroup.getGroupID().equals(group.getGroupID())) {
								it.remove();
								break;
							}
						}

						calendarDAO.update(calendarPost);
					}

					deletedCount++;

				} catch (AccessDeniedException ignore) {}
			}
		}

		return deletedCount;
	}
}