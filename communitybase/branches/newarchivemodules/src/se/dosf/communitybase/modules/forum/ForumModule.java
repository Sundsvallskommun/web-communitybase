package se.dosf.communitybase.modules.forum;

import java.io.IOException;
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
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.forum.beans.Forum;
import se.dosf.communitybase.modules.forum.beans.Post;
import se.dosf.communitybase.modules.forum.beans.Thread;
import se.dosf.communitybase.utils.AccessUtils;
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
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class ForumModule extends AnnotatedCommunityModule implements CommunityModule {

	private static final AnnotatedRequestPopulator<Forum> FORUM_POPULATOR = new AnnotatedRequestPopulator<Forum>(Forum.class);
	private static final AnnotatedRequestPopulator<Thread> THREAD_POPULATOR = new AnnotatedRequestPopulator<Thread>(Thread.class);
	private static final AnnotatedRequestPopulator<Post> POST_POPULATOR = new AnnotatedRequestPopulator<Post>(Post.class);
	private ForumDAO forumDAO;
	private CommunityGroupDAO groupDAO;
	private CommunityUserDAO userDAO;

	private UserHandler userHandler;

	@XSLVariable
	protected String addForumBreadcrumb = "Add forum";
	
	@XSLVariable
	protected String updateForumBreadcrumb = "Update forum";
	
	@XSLVariable
	protected String updateThreadBreadcrumb = "Update thread";
	
	@XSLVariable
	protected String updateForumPostBreadcrumb = "Update forumpost";
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.createDAOs(dataSource);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		if (dataSource != this.dataSource) {
			this.createDAOs(dataSource);
		}

		super.update(moduleDescriptor, dataSource);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		super.createDAOs(dataSource);
		
		this.userHandler = sectionInterface.getSystemInterface().getUserHandler();

		this.forumDAO = new ForumDAO(dataSource, userHandler);
		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO.setUserDao(this.userDAO);
		this.userDAO.setGroupDao(this.groupDAO);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException {

		log.info("User " + user + " listing forums in group " + group);

		Document doc = this.createDocument(req, uriParser, user, group);
		Element communityElement = doc.createElement("communityModule");
		doc.getFirstChild().appendChild(communityElement);

		if (AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			communityElement.appendChild(XMLUtils.createElement("admin", "true", doc));
		}

		ArrayList<Forum> forums = this.forumDAO.showCommunity(group, true);

		if (forums != null) {
			for (Forum forum : forums) {
				communityElement.appendChild(forum.toXML(doc));
			}
		} else {
			communityElement.appendChild(XMLUtils.createElement("noCommunities", "", doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group));
	}

	@GroupMethod
	public ForegroundModuleResponse addCommunity(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		ValidationException validationException = null;

		Forum forum = null;
		if (req.getMethod().equalsIgnoreCase("POST")) {
			try {
				forum = FORUM_POPULATOR.populate(req);

				log.info("User " + user + " adding forum " + forum + " to group " + group);

				this.forumDAO.addCommunity(group, forum);

				this.redirectToDefaultMethod(req, res, group);

			} catch (ValidationException e) {
				validationException = e;
			}
		}

		Document doc = this.createDocument(req, uriParser, user, group);
		Element addCommunityElement = doc.createElement("addCommunity");
		addCommunityElement.appendChild(XMLUtils.createElement("groupID", group.getGroupID().toString(), doc));
		doc.getFirstChild().appendChild(addCommunityElement);

		if (validationException != null) {
			addCommunityElement.appendChild(validationException.toXML(doc));
			addCommunityElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(addForumBreadcrumb, "", "addCommunity", group));
	}


	@GroupMethod
	public ForegroundModuleResponse updateCommunity(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		Forum forum = null;
		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((forum = this.forumDAO.getCommunity(Integer.valueOf(uriParser.get(3)))) != null)) {

			if(!forum.getGroupID().equals(group.getGroupID())){
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor);
			}

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {
					forum = FORUM_POPULATOR.populate(forum, req);

					log.info("User " + user + " updating forum " + forum + " in group " + group);

					this.forumDAO.updateCommunity(forum);

					this.redirectToDefaultMethod(req, res, group);

					return null;

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			Document doc = this.createDocument(req, uriParser, user, group);

			Element updateCommunityElement = doc.createElement("updateCommunity");
			doc.getFirstChild().appendChild(updateCommunityElement);
			updateCommunityElement.appendChild(forum.toXML(doc));

			if (validationException != null) {
				updateCommunityElement.appendChild(validationException.toXML(doc));
				updateCommunityElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(updateForumBreadcrumb + "\"" + forum.getName() + "\"", "", "updateCommunity", group));

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse deleteCommunity(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, ValidationException, AccessDeniedException {

		this.checkAdminAccess(user, group);

		Forum forum = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((forum = this.forumDAO.getCommunity(Integer.valueOf(uriParser.get(3)))) != null)) {

			if(!forum.getGroupID().equals(group.getGroupID())){
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor);
			}

			log.info("User " + user + " deleting forum " + forum + " from group " + group);

			this.forumDAO.deleteCommunity(forum.getForumID());

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID());

			return null;

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse showCommunityThreads(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException {

		Forum forum = null;
		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((forum = this.forumDAO.getCommunity(Integer.valueOf(uriParser.get(3)))) != null)) {

			log.info("User " + user + " listing threads in forum " + forum + " in group " + group);

			Document doc = this.createDocument(req, uriParser, user, group);

			ArrayList<Thread> threads = this.forumDAO.getCommunityThreads(forum.getForumID(), true);

			Element showCommunityThreadsElement = doc.createElement("showCommunityThreads");
			showCommunityThreadsElement.appendChild(XMLUtils.createElement("type", "Thread", doc));

			if (AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
				showCommunityThreadsElement.appendChild(XMLUtils.createElement("admin", "true", doc));
			}
			
			doc.getFirstChild().appendChild(showCommunityThreadsElement);
			showCommunityThreadsElement.appendChild(forum.toXML(doc));

			if (threads != null) {
				for (Thread thread : threads) {
					Element threadElement = thread.toXML(doc);
					
					if(AccessUtils.checkAccess(user, group, GroupAccessLevel.MEMBER) && thread.getPosterID().equals(user.getUserID())){
						XMLUtils.appendNewElement(doc, threadElement, "owner", "");
					}

					threadElement.appendChild(XMLUtils.createElement("replies", this.forumDAO.countCommunityPosts(thread.getThreadID()).toString(), doc));
					showCommunityThreadsElement.appendChild(threadElement);
				}
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(forum.getName(), "", "showCommunityThreads", group));

		} else {
			throw new URINotFoundException(uriParser);
		}

	}

	@GroupMethod
	public ForegroundModuleResponse updateCommunityThread(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		Thread thread = null;
		Forum forum = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((thread = this.forumDAO.getThread(Integer.valueOf(uriParser.get(3)))) != null)) {

			if(!AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN,GroupAccessLevel.PUBLISHER) && !thread.getPosterID().equals(user.getUserID())){
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor);
			}

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {

					thread = THREAD_POPULATOR.populate(thread, req);

					log.info("User " + user + " updating thread " + thread + " in forum " + forum + " in group " + group);

					this.forumDAO.updateCommunityThread(thread, user.getUserID());

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/showCommunityPosts/" + thread.getThreadID());

					return null;

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			Document doc = this.createDocument(req, uriParser, user, group);
			forum = this.forumDAO.getCommunity(thread.getForumID());
			Element updateCommunityElement = doc.createElement("updateCommunityThread");
			doc.getFirstChild().appendChild(updateCommunityElement);
			updateCommunityElement.appendChild(thread.toXML(doc));
			
			if (validationException != null) {
				updateCommunityElement.appendChild(validationException.toXML(doc));
				updateCommunityElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(forum.getName(), "", "showCommunityThreads/" + forum.getForumID(), group), this.getMethodBreadcrumb(updateThreadBreadcrumb + "\"" + thread.getSubject() + "\"", "", "updateCommunityThread", group));

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse deleteCommunityThread(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, ValidationException, AccessDeniedException {

		Thread thread = null;
		Forum forum = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((thread = this.forumDAO.getThread(Integer.valueOf(uriParser.get(3)))) != null)) {

			if(!AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN,GroupAccessLevel.PUBLISHER)){
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor);
			}

			log.info("User " + user + " deleting thread " + thread + " from forum " + forum + " in group " + group);

			this.forumDAO.deleteCommunityThread(thread.getThreadID());

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/showCommunityThreads/" + thread.getForumID());

			return null;

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse addCommunityThread(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException {

		Forum forum = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((forum = this.forumDAO.getCommunity(Integer.valueOf(uriParser.get(3)))) != null)) {

			ValidationException validationException = null;

			Thread thread = null;
			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {

					thread = THREAD_POPULATOR.populate(req);

					log.info("User " + user + " updating thread " + thread + " to forum " + forum + " in group " + group);

					this.forumDAO.addCommunityThread(thread, group, forum.getForumID(), user.getUserID());

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/showCommunityPosts/" + thread.getThreadID());

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			Document doc = this.createDocument(req, uriParser, user, group);
			Element addCommunityElement = doc.createElement("addCommunityThread");
			addCommunityElement.appendChild(forum.toXML(doc));
			doc.getFirstChild().appendChild(addCommunityElement);

			if (validationException != null) {
				addCommunityElement.appendChild(validationException.toXML(doc));
				addCommunityElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}
			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(forum.getName(), "", "showCommunityThreads/" + forum.getForumID(), group), this.getMethodBreadcrumb("Lägg till diskussion ", "", "addCommunityThread", group));
		} else {
			throw new URINotFoundException(uriParser);
		}

	}

	@GroupMethod
	public ForegroundModuleResponse showCommunityPosts(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		Thread thread = null;

		ValidationException validationException = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((thread = this.forumDAO.getThread(Integer.valueOf(uriParser.get(3)))) != null)) {

			Document doc = this.createDocument(req, uriParser, user, group);

			// addCommunityPost
			Post forumPost = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {

					forumPost = POST_POPULATOR.populate(req);

					log.info("User " + user + " adding post " + forumPost + " to thread " + thread + " in group " + group);

					this.forumDAO.addCommunityPost(forumPost, thread.getThreadID(), user.getUserID());

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/showCommunityPosts/" + thread.getThreadID());

					return null;

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			log.info("User " + user + " requested thread " + thread + " in group " + group);

			ArrayList<Post> posts = this.forumDAO.getCommunityPosts(thread.getThreadID());

			Element showCommunityPostElement = doc.createElement("showCommunityPosts");
			showCommunityPostElement.appendChild(XMLUtils.createElement("type", "Post", doc));

			if (AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
				showCommunityPostElement.appendChild(XMLUtils.createElement("admin", "true", doc));
			}

			Integer communityID = thread.getForumID();

			Forum community = this.forumDAO.getCommunity(communityID);

			doc.getFirstChild().appendChild(showCommunityPostElement);
			Element threadElement = thread.toXML(doc);

			showCommunityPostElement.appendChild(threadElement);
			if (posts != null) {
				for (Post post : posts) {
					Element postElement = post.toXML(doc);
					postElement.appendChild(XMLUtils.createElement("replies", this.forumDAO.countCommunityPosts(post.getPostID()).toString(), doc));
					threadElement.appendChild(postElement);
					
					if(AccessUtils.checkAccess(user, group, GroupAccessLevel.MEMBER) && post.getPosterID().equals(user.getUserID())){
						XMLUtils.appendNewElement(doc, postElement, "owner", "");
					}
				}
			} else {
				threadElement.appendChild(XMLUtils.createElement("noPosts", "", doc));
			}

			if (validationException != null) {
				showCommunityPostElement.appendChild(validationException.toXML(doc));
				showCommunityPostElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(community.getName(), "", "showCommunityThreads/" + communityID, group), this.getMethodBreadcrumb(thread.getSubject(), "", uriParser.getCurrentURI(true), group));

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse updateCommunityPost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, AccessDeniedException {

		Post post = null;
		Thread thread = null;
		Forum community = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((post = this.forumDAO.getPost(Integer.valueOf(uriParser.get(3)))) != null)) {

			if(!AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN,GroupAccessLevel.PUBLISHER) && !post.getPosterID().equals(user.getUserID())){
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor);
			}

			ValidationException validationException = null;

			if (req.getMethod().equalsIgnoreCase("POST")) {
				try {

					post = POST_POPULATOR.populate(post, req);

					log.info("User " + user + " updating post " + post + " in group " + group);

					this.forumDAO.updateCommunityPost(post, user.getUserID());

					res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/showCommunityPosts/" + post.getThreadID());

					return null;

				} catch (ValidationException e) {
					validationException = e;
				}
			}

			thread = this.forumDAO.getThread(post.getThreadID());
			community = this.forumDAO.getCommunity(thread.getForumID());
			Document doc = this.createDocument(req, uriParser, user, group);

			Element updateCommunityPostElement = doc.createElement("updateCommunityPost");
			doc.getFirstChild().appendChild(updateCommunityPostElement);
			updateCommunityPostElement.appendChild(post.toXML(doc));

			if (validationException != null) {
				updateCommunityPostElement.appendChild(validationException.toXML(doc));
				updateCommunityPostElement.appendChild(RequestUtils.getRequestParameters(req, doc));
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group), this.getMethodBreadcrumb(community.getName(), "", "showCommunityThreads/" + community.getForumID(), group), this.getMethodBreadcrumb(thread.getSubject(), "", "showCommunityPosts/" + thread.getThreadID(), group), this.getMethodBreadcrumb(updateForumPostBreadcrumb, "", "updateCommunityPost", group));

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@GroupMethod
	public ForegroundModuleResponse deleteCommunityPost(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws SQLException, URINotFoundException, IOException, ValidationException, AccessDeniedException {

		Post post = null;

		if (uriParser.size() == 4 && NumberUtils.isInt(uriParser.get(3)) && ((post = this.forumDAO.getPost(Integer.valueOf(uriParser.get(3)))) != null)) {

			if(!AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN,GroupAccessLevel.PUBLISHER) && !post.getPosterID().equals(user.getUserID())){
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor);
			}

			log.info("User " + user + " deleting post " + post + " in group " + group);

			this.forumDAO.deleteCommunityPost(post.getPostID());

			res.sendRedirect(uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/" + group.getGroupID() + "/showCommunityPosts/" + post.getThreadID());

			return null;

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	private Breadcrumb getMethodBreadcrumb(String name, String description, String method, CommunityGroup group) {
		return new Breadcrumb(name, description, this.getFullAlias() + "/" + group.getGroupID() + "/" + method, URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user, CommunityGroup group) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));

		doc.appendChild(document);

		return doc;
	}

	@Override
	public List<IdentifiedEvent> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {

		List<IdentifiedEvent> events = this.forumDAO.getEvents(group,startStamp);

		if(events != null){

			for(IdentifiedEvent event : events){
				event.setTitle(StringUtils.substring(event.getTitle(), 50, "..."));
				event.setDescription(StringUtils.substring(event.getDescription(), 50, "..."));
				event.setFullAlias(this.getFullAlias(group) + "/showCommunityPosts/" + event.getId());
			}
		}

		return events;
	}
}
