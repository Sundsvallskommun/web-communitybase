package se.dosf.communitybase.modules.members;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.dosf.communitybase.modules.members.daos.MembersDAO;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class MembersModule extends AnnotatedCommunityModule implements CommunityModule {

	private CommunityGroupDAO groupDAO;
	private CommunityUserDAO userDAO;

	private MembersDAO membersDAO;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		createDAOs(dataSource);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		if(this.dataSource != dataSource){
			createDAOs(dataSource);
		}

		super.update(moduleDescriptor, dataSource);
	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {
		
		super.createDAOs(dataSource);
		
		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO.setUserDao(this.userDAO);
		this.userDAO.setGroupDao(this.groupDAO);
		
		this.membersDAO = new MembersDAO(dataSource);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {

		log.info("User " + user + " listing members in group " + group);

		Document doc = this.createDocument(req, uriParser, user, group);
		Element membersElement = doc.createElement("Members");
		doc.getFirstChild().appendChild(membersElement);

		if (group.getUsers() != null) {

			for (CommunityUser currentUser : group.getUsers()) {
				membersElement.appendChild(currentUser.toXML(doc));
			}
		}
		
		XMLUtils.append(doc, membersElement, "HiddenUsers", "userID", this.membersDAO.getHiddenUserIDs(group));

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getGroupBreadcrumb(group), this.getModuleBreadcrumb(group));

	}
	
	@GroupMethod
	public ForegroundModuleResponse toggleVisibility(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		
		this.checkAdminAccess(user, group);
		
		CommunityUser communityUser;
		
		if (uriParser.size() != 4 || !NumberUtils.isInt(uriParser.get(3)) || (communityUser = this.userDAO.getUser(Integer.valueOf(uriParser.get(3)), false, false)) == null) {

			throw new URINotFoundException(uriParser);

		} else {
			
			boolean isHidden = this.membersDAO.userIsHidden(group, communityUser);
			
			if(isHidden) {
				
				log.info("User " + user + " is making user " + communityUser + " visible");
				
				this.membersDAO.setVisible(group, communityUser);
				
			} else {
				
				log.info("User " + user + " is making user " + communityUser + " invisible");
				
				this.membersDAO.setInVisible(group, communityUser);
			}
			
			this.redirectToDefaultMethod(req, res, group);
			
			return null;
		}
		
	}	
	
	private Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user, CommunityGroup group) {
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));
		if (AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN)) {
			document.appendChild(XMLUtils.createElement("isAdmin", "true", doc));
		}
		
		doc.appendChild(document);

		return doc;
	}

	@Override
	public List<Event> getGroupResume(CommunityGroup group, CommunityUser user, Timestamp startStamp) throws Exception {
		return null;
	}

	@Override
	protected boolean getGroupUsers() {
		return true;
	}

	@Override
	protected boolean getSchoolUsers() {
		return false;
	}
	
	@Override
	protected void checkAdminAccess(CommunityUser user, CommunityGroup group) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN)) {
			throw new AccessDeniedException("Administration permission denied for group " + group);
		}
	}
}
