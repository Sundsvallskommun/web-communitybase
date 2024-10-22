package se.dosf.communitybase.modules.login;

import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.modules.login.BaseLoginModule;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

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

	private void createDAOs(DataSource dataSource) {
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

	@Override
	public SimpleForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		//TODO restrict number of failed login per given time period

		String username = req.getParameter("username");
		String password = req.getParameter("password");

		if (!StringUtils.isEmpty(username) && password != null) {
			CommunityUser loginUser = this.findByUsernamePassword(username, password);

			if (loginUser != null) {

				if (loginUser.isEnabled()) {
					// Set last login timestamp
					this.setLastLogin(loginUser);

					// Add user to session
					req.getSession(true).setAttribute("user", loginUser);

					// Set session timeout
					if (loginUser.isAdmin()) {
						req.getSession(true).setMaxInactiveInterval(this.adminSessionTimeout);
					} else {
						req.getSession(true).setMaxInactiveInterval(this.userSessionTimeout);
					}

					log.info("User " + loginUser + " logged in from address " + req.getRemoteHost());

					// Redirect to requested URL
					if (uriParser.size() > 0 && !uriParser.get(0).equals(this.moduleDescriptor.getAlias()) && !this.logoutModuleAliases.contains(uriParser.getFormattedURI())) {

						if (!StringUtils.isEmpty(req.getQueryString())) {
							res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI() + "?" + req.getQueryString());
						} else {
							res.sendRedirect(req.getContextPath() + uriParser.getFormattedURI());
						}

					} 
					
					// Redirect to default URL - calculate default URL from user language
					else {
						
						Language language;
						
						if((language = (Language) req.getSession().getAttribute("language")) == null) {
							language = loginUser.getLanguage();
						}
						
						SectionDescriptor sectionDescriptor;
						String uri;
						
						if (!loginUser.isAdmin()) {
							
							if(language != null) {
								sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionCache().getEntry(language.getLanguageCode()).getKey();						
								uri = req.getContextPath() + "/" + sectionDescriptor.getAlias();
							} else {
								sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionDescriptor();
								uri = req.getContextPath();
							}

							res.sendRedirect(uri + sectionDescriptor.getUserDefaultURI());

						} else {

							if(language != null) {
								sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionCache().getEntry(language.getLanguageCode()).getKey();
								uri = req.getContextPath() + "/" + sectionDescriptor.getAlias();
							} else {
								sectionDescriptor = this.sectionInterface.getSystemInterface().getRootSection().getSectionDescriptor();
								uri = req.getContextPath();
							}
							
							res.sendRedirect(uri + sectionDescriptor.getAdminDefaultURI());
							
						}
					}

					return null;
				} else {
					log.warn("Login refused for user " + loginUser + " (account disabled) accessing from address " + req.getRemoteHost());

					Document doc = this.createDocument(req, uriParser);

					doc.getDocumentElement().appendChild(doc.createElement("AccountDisabled"));

					return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
				}

			} else {
				log.warn("Failed login attempt using username " + req.getParameter("username") + " from address " + req.getRemoteHost());

				Document doc = this.createDocument(req, uriParser);

				doc.getDocumentElement().appendChild(XMLUtils.createElement("LoginFailed", "", doc));

				return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
			}

		} else {
			Document doc = this.createDocument(req, uriParser);

			doc.getDocumentElement().appendChild(XMLUtils.createElement("Login", "", doc));

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), this.getDefaultBreadcrumb());
		}
	}
}
