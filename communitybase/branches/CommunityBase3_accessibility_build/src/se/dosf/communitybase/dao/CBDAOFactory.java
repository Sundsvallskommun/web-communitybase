package se.dosf.communitybase.dao;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import se.dosf.communitybase.beans.SectionAccessRequest;
import se.dosf.communitybase.beans.SectionFavourite;
import se.dosf.communitybase.beans.SectionGroupRole;
import se.dosf.communitybase.beans.SimpleRole;
import se.dosf.communitybase.beans.SimpleSectionType;
import se.dosf.communitybase.modules.invitation.beans.Invitation;
import se.unlogic.hierarchy.core.handlers.GroupHandler;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.db.tableversionhandler.TableUpgradeException;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;

public class CBDAOFactory {

	protected Logger log = Logger.getLogger(this.getClass());

	private final AnnotatedDAO<SimpleSectionType> sectionTypeDAO;
	private final AnnotatedDAO<SimpleRole> roleDAO;
	private final AnnotatedDAO<Invitation> invitationDAO;
	private final AnnotatedDAO<SectionFavourite> sectionFavouriteDAO;
	private final AnnotatedDAO<SectionGroupRole> sectionGroupRoleDAO;
	private final AnnotatedDAO<SectionAccessRequest> accessRequestDAO;

	public CBDAOFactory(DataSource dataSource, UserHandler userHandler, GroupHandler groupHandler) throws TableUpgradeException, SQLException, SAXException, IOException, ParserConfigurationException {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, CBDAOFactory.class.getName(), new XMLDBScriptProvider(this.getClass().getResourceAsStream("DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource, userHandler, groupHandler, false, true, false);

		sectionTypeDAO = daoFactory.getDAO(SimpleSectionType.class);
		roleDAO = daoFactory.getDAO(SimpleRole.class);
		invitationDAO = daoFactory.getDAO(Invitation.class);
		sectionFavouriteDAO = daoFactory.getDAO(SectionFavourite.class);
		sectionGroupRoleDAO = daoFactory.getDAO(SectionGroupRole.class);
		accessRequestDAO = daoFactory.getDAO(SectionAccessRequest.class);
	}

	public AnnotatedDAO<SimpleSectionType> getSectionTypeDAO() {

		return sectionTypeDAO;
	}

	public TransactionHandler getTransactionHandler() throws SQLException {

		return roleDAO.createTransaction();
	}

	public AnnotatedDAO<Invitation> getInvitationDAO() {

		return invitationDAO;
	}

	public AnnotatedDAO<SimpleRole> getRoleDAO() {

		return roleDAO;
	}

	public AnnotatedDAO<SectionFavourite> getSectionFavouriteDAO() {

		return sectionFavouriteDAO;
	}

	public AnnotatedDAO<SectionGroupRole> getSectionGroupRoleDAO() {

		return sectionGroupRoleDAO;
	}
	
	public AnnotatedDAO<SectionAccessRequest> getAccessRequestDAO() {
		
		return accessRequestDAO;
	}
}