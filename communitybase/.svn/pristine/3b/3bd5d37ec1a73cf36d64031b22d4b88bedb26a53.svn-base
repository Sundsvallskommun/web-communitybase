package se.dosf.communitybase.modules.members;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.modules.AnotatedCommunityModule;
import se.dosf.communitybase.modules.CommunityModule;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class MembersModule extends AnotatedCommunityModule implements CommunityModule {

	private CommunityGroupDAO groupDAO;
	private CommunityUserDAO userDAO;

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

	private void createDAOs(DataSource dataSource) {
		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO.setUserDao(this.userDAO);
		this.userDAO.setGroupDao(this.groupDAO);
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

		return new SimpleForegroundModuleResponse(doc, getGroupBreadcrumb(group, uriParser), this.getModuleBreadcrumb(group));

	}

	private Document createDocument(HttpServletRequest req, URIParser uriParser, CommunityUser user, CommunityGroup group) {
		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(this.moduleDescriptor.toXML(doc));
		document.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));
		document.appendChild(group.toXML(doc));

		doc.appendChild(document);

		return doc;
	}

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
}
