package se.dosf.communitybase.modules;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.dosf.communitybase.annotations.GroupMethod;
import se.dosf.communitybase.annotations.SchoolMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunityModuleDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.daos.CommunityUserDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.beans.SettingDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.foregroundmodules.SimpleForegroundModule;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.xml.XSLVariableReader;
import se.unlogic.webutils.http.URIParser;

public abstract class AnnotatedCommunityModule extends SimpleForegroundModule implements CommunityModule {

	private static final Class<?>[] GroupParameterTypes = { HttpServletRequest.class, HttpServletResponse.class, CommunityUser.class, URIParser.class, CommunityGroup.class };
	private static final Class<?>[] SchoolParameterTypes = { HttpServletRequest.class, HttpServletResponse.class, CommunityUser.class, URIParser.class, School.class };

	private final HashMap<String, Method> methodMap = new HashMap<String, Method>();
	private CommunityGroupDAO communityGroupDAO;
	private CommunitySchoolDAO communitySchoolDAO;
	private CommunityModuleDAO communityModuleDAO;
	private CommunityUserDAO communityUserDAO;

	protected List<ScriptTag> scripts;
	protected List<LinkTag> links;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		this.communityGroupDAO = new CommunityGroupDAO(dataSource);
		this.communityUserDAO = new CommunityUserDAO(dataSource);
		this.communityGroupDAO.setUserDao(this.communityUserDAO);
		this.communityUserDAO.setGroupDao(this.communityGroupDAO);
		this.communitySchoolDAO = new CommunitySchoolDAO(dataSource);
		this.communityModuleDAO = new CommunityModuleDAO(dataSource);

		Method[] methods = this.getClass().getMethods();

		for (Method method : methods) {

			if (checkMethod(method, GroupMethod.class, this.getGroupParameterTypes())) {
				continue;
			} else {
				checkMethod(method, SchoolMethod.class, this.getSchoolParameterTypes());
			}

		}

		XSLVariableReader variableReader = ModuleUtils.getXSLVariableReader(moduleDescriptor, sectionInterface.getSystemInterface());

		if(variableReader != null){

			ModuleUtils.setXSLVariables(variableReader,this, AnnotatedCommunityModule.class,moduleDescriptor);
		}
		
		ModuleUtils.setModuleSettings(this,AnnotatedCommunityModule.class, moduleDescriptor.getSettingHandler());
		
		if(variableReader != null){

			this.scripts = ModuleUtils.getScripts(variableReader, sectionInterface, moduleDescriptor);
			this.links = ModuleUtils.getLinks(variableReader, sectionInterface, moduleDescriptor);
		}
		
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);

		XSLVariableReader variableReader = ModuleUtils.getXSLVariableReader(moduleDescriptor, sectionInterface.getSystemInterface());

		if(variableReader != null){

			ModuleUtils.setXSLVariables(variableReader,this, AnnotatedCommunityModule.class,moduleDescriptor);
		}
		
		ModuleUtils.setModuleSettings(this,AnnotatedCommunityModule.class, moduleDescriptor.getSettingHandler());
		
		if(variableReader != null){

			this.scripts = ModuleUtils.getScripts(variableReader, sectionInterface, moduleDescriptor);
			this.links = ModuleUtils.getLinks(variableReader, sectionInterface, moduleDescriptor);
		}
		
	}

	public Breadcrumb getModuleBreadcrumb(CommunityGroup group) {
		return new Breadcrumb(this.moduleDescriptor.getName(), moduleDescriptor.getDescription(), this.getFullAlias() + "/" + group.getGroupID(), URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	public Breadcrumb getGroupBreadcrumb(CommunityGroup group) {
		return new Breadcrumb(group.getName(), group.getDescription(), "/groupfirstpage/" + group.getGroupID(), URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	protected boolean checkMethod(Method method, Class<? extends Annotation> annotationClass, Class<?>[] parameterTypes) {

		if (method.getAnnotation(annotationClass) == null) {

			log.debug("Method " + method.getName() + " is NOT web public, skipping.");

			return false;

		} else {

			if (!method.getReturnType().equals(ForegroundModuleResponse.class)) {

				log.error("Method " + method.getName() + " has invalid responsetype, skipping.");

			} else if (!Arrays.equals(method.getParameterTypes(), parameterTypes)) {

				log.error("Method " + method.getName() + " has invalid parametertypes, skipping.");

			} else {
				log.debug("Caching method " + method.getName());

				this.methodMap.put(method.getName(), method);
			}

			return true;
		}

	}

	protected Class<?>[] getGroupParameterTypes() {
		return GroupParameterTypes;
	}

	protected Class<?>[] getSchoolParameterTypes() {
		return SchoolParameterTypes;
	}


	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (user == null) {

			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "User has to be logged in");

		} else if (!(user instanceof CommunityUser)) {

			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Wrong usertype");

		}

		if (uriParser.size() > 2) {

			Method method = this.methodMap.get(uriParser.get(2));

			if (method == null) {
				return this.methodNotFound(req, res, (CommunityUser) user, uriParser);
			} else {

				try {

					if (method.getAnnotation(GroupMethod.class) != null) {

						CommunityGroup group = this.communityGroupDAO.getGroup(Integer.valueOf(uriParser.get(1)), this.getGroupUsers());

						if (group != null) {

							if (moduleEnabled(group) || AccessUtils.checkAccess((CommunityUser) user, group, GroupAccessLevel.ADMIN)) {
								return this.invokeGroupMethod(method, req, res, (CommunityUser) user, uriParser, group);
							} else {
								throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied to group " + group);
							}
						}

					} else if (method.getAnnotation(SchoolMethod.class) != null) {

						School school = this.communitySchoolDAO.getSchool(Integer.valueOf(uriParser.get(1)), getSchoolUsers(),false);

						if (AccessUtils.checkAccess((CommunityUser) user, school)) {
							return this.invokeSchoolMethod(method, req, res, (CommunityUser) user, uriParser, school);
						} else {
							throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied to school " + school);
						}
					}

					this.methodNotFound(req, res, (CommunityUser) user, uriParser);
					return null;

				} catch (InvocationTargetException e) {

					if (e.getCause() != null && e.getCause() instanceof Exception) {
						throw (Exception) e.getCause();
					} else {
						throw e;
					}
				}

			}
		} else if (uriParser.size() == 2 && NumberUtils.isInt(uriParser.get(1))) {

			CommunityGroup group = this.communityGroupDAO.getGroup(Integer.valueOf(uriParser.get(1)), true);

			if (group != null && AccessUtils.checkAccess((CommunityUser)user, group)) {

				if (moduleEnabled(group) || AccessUtils.checkAccess((CommunityUser) user, group, GroupAccessLevel.ADMIN)) {
					ForegroundModuleResponse moduleResponse = this.defaultMethod(req, res, (CommunityUser) user, uriParser, group);
					
					this.appendScriptsAndLinks(moduleResponse);

					return moduleResponse;
				} else {
					throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Permission denied");
				}

			}

			this.methodNotFound(req, res, (CommunityUser) user, uriParser);
			return null;

		} else {

			this.methodNotFound(req, res, (CommunityUser) user, uriParser);
			return null;

		}
	}

	protected boolean getGroupUsers() {
		return false;
	}

	protected boolean getSchoolUsers() {
		return false;
	}

	public String getFullAlias(CommunityGroup group) {
		return this.getFullAlias() + "/" + group.getGroupID();
	}

	protected ForegroundModuleResponse invokeGroupMethod(Method method, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception {
		
		ForegroundModuleResponse moduleResponse = (ForegroundModuleResponse) method.invoke(this, req, res, user, uriParser, group);
		
		this.appendScriptsAndLinks(moduleResponse);
		
		return moduleResponse;
	}

	protected ForegroundModuleResponse invokeSchoolMethod(Method method, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, School school) throws Exception {
		
		ForegroundModuleResponse moduleResponse = (ForegroundModuleResponse)  method.invoke(this, req, res, user, uriParser, school);
		
		this.appendScriptsAndLinks(moduleResponse);
		
		return moduleResponse;
	}

	public abstract ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, CommunityGroup group) throws Exception;

	public ForegroundModuleResponse methodNotFound(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
	}

	protected void redirectToDefaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityGroup group) throws IOException {
		res.sendRedirect(this.getURI(req) + "/" + group.getGroupID());
	}

	protected boolean moduleEnabled(CommunityGroup group) throws SQLException {
		return this.communityModuleDAO.isModuleEnabled(this.moduleDescriptor.getModuleID(), group);
	}

	public ModuleType getModuleType() {
		return ModuleType.Public;
	}

	protected void checkAdminAccess(CommunityUser user, CommunityGroup group) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, group, GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER)) {
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Administration permission denied for group " + group);
		}
	}

	protected void checkAdminAccess(CommunityUser user, School school) throws AccessDeniedException {

		if (!AccessUtils.checkAccess(user, school)) {
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Administration permission denied for school " + school);
		}
	}

	protected void checkAdminAccess(CommunityUser user) throws AccessDeniedException {

		if (!user.isAdmin()) {
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Global administration permission denied");
		}
	}
	
	protected void appendScriptsAndLinks(ForegroundModuleResponse moduleResponse) {
		
		if(moduleResponse != null){

			if(this.scripts != null){

				moduleResponse.addScripts(scripts);
			}

			if(this.links != null){

				moduleResponse.addLinks(links);
			}
		}
		
	}

	public ForegroundModuleDescriptor getModuleDescriptor() {
		return this.moduleDescriptor;
	}
	
	@Override
	public List<? extends SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settingDescriptors = new ArrayList<SettingDescriptor>();
		
		ModuleUtils.addSettings(settingDescriptors, super.getSettings());
		
		try {
			ModuleUtils.addSettings(settingDescriptors, ModuleUtils.getAnnotatedSettingDescriptors(this,AnnotatedCommunityModule.class, systemInterface.getUserHandler()));
			
		} catch (IllegalArgumentException e) {

			throw new RuntimeException(e);
						
		} catch (IllegalAccessException e) {

			throw new RuntimeException(e);
			
		} catch (InstantiationException e) {

			throw new RuntimeException(e);
		}
		
		return settingDescriptors;
	}

}
