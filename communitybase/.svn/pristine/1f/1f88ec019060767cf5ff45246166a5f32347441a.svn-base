package se.dosf.communitybase.modules;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Event;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.modules.AnnotatedForegroundModule;
import se.unlogic.hierarchy.modules.MethodMapping;
import se.unlogic.webutils.http.URIParser;


public abstract class AnotatedGlobalModule extends AnnotatedForegroundModule implements CommunityModule {

	private final Class<?>[] defaultParameterTypes = new Class<?>[] { HttpServletRequest.class, HttpServletResponse.class, CommunityUser.class, URIParser.class };
	
	@Override
	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		
		if(!this.moduleDescriptor.allowsAnonymousAccess()){
		
			if (user == null) {
	
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "User not logged in");
	
			} else if (!(user instanceof CommunityUser)) {
	
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Wrong usertype (" + user.getClass() + ")");
			}		
		
		}
		
		if (uriParser.size() > 1) {

			MethodMapping methodMapping = this.methodMap.get(uriParser.get(1));

			if (methodMapping == null) {
				return this.methodNotFound(req, res, (CommunityUser) user, uriParser);
			} else {
				try {
					ForegroundModuleResponse moduleResponse = this.invokeMethod(methodMapping.getMethod(), req, res, (CommunityUser) user, uriParser);
					
					if(this.scripts != null && methodMapping.getWebPublic().autoAppendScripts()){
						
						moduleResponse.addScripts(scripts);
					}
					
					return moduleResponse;
					
				} catch (InvocationTargetException e) {

					if (e.getCause() != null && e.getCause() instanceof Exception) {
						throw (Exception) e.getCause();
					} else {
						throw e;
					}
				}
			}

		} else {
			return this.defaultMethod(req, res, (CommunityUser) user, uriParser);
		}
		
	}

	@Override
	protected Class<?>[] getParameterTypes() {
		return this.defaultParameterTypes;
	}

	public String getFullAlias(CommunityGroup group) {
		return null;
	}

	public List<Event> getGroupResume(CommunityGroup group, CommunityUser user) throws Exception {
		return null;
	}

	protected ForegroundModuleResponse invokeMethod(Method method, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		return (ForegroundModuleResponse) method.invoke(this, req, res, user, uriParser);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {
		return null;
	}

	public abstract ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception;

	public ForegroundModuleResponse methodNotFound(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
	}

	protected void redirectToDefaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityGroup group) throws IOException {
		res.sendRedirect(this.getURI(req) + "/" + group.getGroupID());
	}

	public ModuleType getModuleType() {
		return ModuleType.Public;
	}

	protected void checkAdminAccess(CommunityUser user, CommunityGroup group) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied for group " + group);
		}
		
	}
	
	protected void checkAdminAccess(CommunityUser user, School school) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, school)) {
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied for school " + school);
		}
		
	}
	
	protected void checkSysAdminAccess(CommunityUser user) throws AccessDeniedException{
		
		if(!user.isAdmin()){
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied for user " + user);		
		}
		
	}

	public ForegroundModuleDescriptor getModuleDescriptor() {
		return this.moduleDescriptor;
	}
	
	public Breadcrumb getModuleBreadcrumb() {
		return new Breadcrumb(this.moduleDescriptor.getName(), moduleDescriptor.getDescription(), this.getFullAlias(), URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	public Breadcrumb getGroupBreadcrumb(CommunityGroup group) {
		return new Breadcrumb(group.getName(), group.getDescription(), "#", URLType.FULL);
	}
}
