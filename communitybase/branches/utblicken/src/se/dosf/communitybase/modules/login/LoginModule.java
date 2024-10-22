package se.dosf.communitybase.modules.login;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.login.BaseLoginModule;

public class LoginModule extends BaseLoginModule<CommunityUser> {

	private CommunityUserDAO userDAO;
	private CommunityGroupDAO groupDAO;
	private CommunitySchoolDAO schoolDAO;
	protected Logger log = Logger.getLogger(this.getClass());

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.createDAOs(dataSource);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		if(!this.dataSource.equals(dataSource)){
			this.createDAOs(dataSource);
		}

		super.update(moduleDescriptor, dataSource);
	}

	protected void createDAOs(DataSource dataSource) {
		this.userDAO = new CommunityUserDAO(dataSource);
		this.groupDAO = new CommunityGroupDAO(dataSource);
		this.schoolDAO = new CommunitySchoolDAO(dataSource);
		this.userDAO.setGroupDao(this.groupDAO);
		this.groupDAO.setUserDao(this.userDAO);
		this.userDAO.setSchoolDAO(schoolDAO);
	}

	@Override
	protected CommunityUser findByUsernamePassword(String username, String password) throws SQLException {
		return this.userDAO.findByEmailPassword(username, password, true,true);
	}

	@Override
	protected void setLastLogin(CommunityUser user) throws SQLException {

		//TODO rethink!?
		// Set last login timestamp
		Timestamp lastLogin = user.getLastLogin();

		user.setCurrentLogin(new Timestamp(System.currentTimeMillis()));
		user.setLastLogin(user.getCurrentLogin());

		userDAO.update(user, false, false, false);

		user.setLastLogin(lastLogin);
	}
}
