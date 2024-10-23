package se.dosf.communitybase.modules.securesection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.dosf.communitybase.CBConstants;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.FilterChain;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.FilterModuleDescriptor;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.filtermodules.AnnotatedFilterModule;
import se.unlogic.hierarchy.foregroundmodules.login.LoginSecurityUtils;
import se.unlogic.webutils.http.URIParser;

public class SecureSectionFilterModule extends AnnotatedFilterModule {

	private SecureSectionForegroundModule parentModule;

	@Override
	public void init(FilterModuleDescriptor moduleDescriptor, SystemInterface systemInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, systemInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(SecureSectionFilterModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + SecureSectionFilterModule.class.getName());
		}
	}

	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FilterChain filterChain) throws Exception {

		// TODO will sections be allowed to have own aliases someday?
		Integer sectionID = uriParser.getInt(0);

		if (sectionID != null) {
			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);
			
			if (sectionInterface != null && sectionInterface.getSectionDescriptor().getAttributeHandler().getPrimitiveBoolean(CBConstants.SECTION_ATTRIBUTE_SECURE) == true && AccessUtils.checkAccess(user, sectionInterface.getSectionDescriptor())) {
				Integer loginLevel = LoginSecurityUtils.getSecurityLevel(req);
				
				if (!user.isAdmin() && (loginLevel == null || loginLevel < parentModule.getLoginLevel())) {
					res.sendRedirect(uriParser.getFullContextPath() + parentModule.getFullAlias());
				}
			}
		}
		
		filterChain.doFilter(req, res, user, uriParser);
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(SecureSectionFilterModule.class, this);

		super.unload();
	}

	public void setParentModule(SecureSectionForegroundModule secureSectionForegroundModule) {

		parentModule = secureSectionForegroundModule;
	}

	public FilterModuleDescriptor getDescriptor() {

		return moduleDescriptor;
	}
}