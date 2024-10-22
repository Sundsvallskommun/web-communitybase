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

import se.dosf.communitybase.annotations.GenericAdminMethod;
import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.modules.firstpage.FirstpageModule;
import se.unlogic.hierarchy.core.annotations.WebPublic;
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

public abstract class AnnotatedAdminModule<BeanType> extends SimpleForegroundModule {

	private final Class<?>[] defaultParameterTypes = new Class<?>[] { HttpServletRequest.class, HttpServletResponse.class, CommunityUser.class, URIParser.class };

	private final HashMap<String, Method> defaultMethodMap = new HashMap<String, Method>();
	private final HashMap<String, Method> genericMethodMap = new HashMap<String, Method>();

	protected List<ScriptTag> scripts;
	protected List<LinkTag> links;
	
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		Class<?>[] genericParameterTypes = new Class<?>[] { HttpServletRequest.class, HttpServletResponse.class, CommunityUser.class, URIParser.class, this.getGenericClass() };

		Method[] methods = this.getClass().getMethods();

		for (Method method : methods) {
			checkMethod(method, GenericAdminMethod.class, genericParameterTypes,genericMethodMap);
			checkMethod(method, WebPublic.class, defaultParameterTypes,defaultMethodMap);
		}

		XSLVariableReader variableReader = ModuleUtils.getXSLVariableReader(moduleDescriptor, sectionInterface.getSystemInterface());

		if(variableReader != null){

			ModuleUtils.setXSLVariables(variableReader,this, AnnotatedAdminModule.class,moduleDescriptor);
		}
		
		ModuleUtils.setModuleSettings(this,AnnotatedAdminModule.class, moduleDescriptor.getSettingHandler());
		
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

			ModuleUtils.setXSLVariables(variableReader,this, AnnotatedAdminModule.class,moduleDescriptor);
		}
		
		ModuleUtils.setModuleSettings(this,AnnotatedAdminModule.class, moduleDescriptor.getSettingHandler());
		
		if(variableReader != null){

			this.scripts = ModuleUtils.getScripts(variableReader, sectionInterface, moduleDescriptor);
			this.links = ModuleUtils.getLinks(variableReader, sectionInterface, moduleDescriptor);
		}
	}

	protected abstract Class<? extends BeanType> getGenericClass();

	public abstract ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, BeanType bean) throws Exception;

	public abstract ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception;

	protected boolean checkMethod(Method method, Class<? extends Annotation> annotationClass, Class<?>[] parameterTypes, HashMap<String, Method> methodMap) {

		if (method.getAnnotation(annotationClass) == null) {

			log.debug("Method " + method.getName() + " is NOT anotated with class " + annotationClass + ", skipping.");

			return false;

		} else {

			if (!method.getReturnType().equals(ForegroundModuleResponse.class)) {

				log.error("Method " + method.getName() + " has invalid responsetype, skipping.");

			} else if (!Arrays.equals(method.getParameterTypes(), parameterTypes)) {

				log.error("Method " + method.getName() + " has invalid parametertypes, skipping.");

			} else {
				log.debug("Caching method " + method.getName());

				methodMap.put(method.getName(), method);
			}

			return true;
		}

	}


	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		if (user == null) {

			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "User not logged in");

		} else if (!(user instanceof CommunityUser)) {

			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Wrong usertype (" + user.getClass() + ")");
		}

		Integer requestedID = null;
		Method requstedDefaultMethod = null;

		if (uriParser.size() < 2) {

			return defaultMethod(req, res, (CommunityUser) user, uriParser);

		}else if((requstedDefaultMethod = this.defaultMethodMap.get(uriParser.get(1))) != null){

			if(!user.isAdmin()){
				throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor);
			}

			try {

				return this.invokeMethod(requstedDefaultMethod, req, res, (CommunityUser) user, uriParser);

			} catch (InvocationTargetException e) {

				if (e.getCause() != null && e.getCause() instanceof Exception) {
					throw (Exception) e.getCause();
				} else {
					throw e;
				}
			}

		}else if ((requestedID = NumberUtils.toInt(uriParser.get(1))) == null) {

			throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
		}

		BeanType bean = this.getRequestedBean(requestedID);

		if (!checkAccess(bean, (CommunityUser) user)) {
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor);
		}

		if (uriParser.size() == 2) {

			return this.defaultMethod(req, res, (CommunityUser) user, uriParser, bean);

		} else {

			Method method = this.genericMethodMap.get(uriParser.get(2));

			if (method == null) {

				return this.methodNotFound(req, res, (CommunityUser) user, uriParser);

			} else {

				try {

					return this.invokeMethod(method, req, res, (CommunityUser) user, uriParser, bean);

				} catch (InvocationTargetException e) {

					if (e.getCause() != null && e.getCause() instanceof Exception) {
						throw (Exception) e.getCause();
					} else {
						throw e;
					}
				}
			}
		}
	}

	protected abstract boolean checkAccess(BeanType bean, CommunityUser user) throws AccessDeniedException;

	protected abstract BeanType getRequestedBean(Integer requestedID) throws SQLException;

	public String getFullAlias(CommunityGroup group) {
		return this.getFullAlias() + "/" + group.getGroupID();
	}

	protected ForegroundModuleResponse invokeMethod(Method method, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		return (ForegroundModuleResponse) method.invoke(this, req, res, user, uriParser);
	}

	protected ForegroundModuleResponse invokeMethod(Method method, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser, BeanType bean) throws Exception {
		return (ForegroundModuleResponse) method.invoke(this, req, res, user, uriParser, bean);
	}

	protected ForegroundModuleResponse methodNotFound(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		throw new URINotFoundException(this.sectionInterface.getSectionDescriptor(), moduleDescriptor, uriParser);
	}

	public ModuleType getModuleType() {
		return ModuleType.Administration;
	}

	public ForegroundModuleDescriptor getModuleDescriptor() {
		return this.moduleDescriptor;
	}

	public FirstpageModule getFirstPageModule() {

		FirstpageModule firstpageModule = se.dosf.communitybase.utils.ModuleUtils.getCachedModule(sectionInterface, FirstpageModule.class);

		if (firstpageModule == null) {

			log.warn("Module " + moduleDescriptor + " unable to find FirstpageModule!");
		}

		return firstpageModule;
	}

	public ForegroundModuleResponse redirectToDefaultMethod(HttpServletRequest req, HttpServletResponse res) throws IOException {
		res.sendRedirect(this.getURI(req));
		return null;
	}

	public void redirectToDefaultMethod(HttpServletRequest req, HttpServletResponse res, CommunityGroup group) throws IOException {
		res.sendRedirect(this.getURI(req) + "/" + group.getGroupID());
	}

	public Breadcrumb getModuleBreadcrumb(CommunityGroup group) {
		return new Breadcrumb(this.moduleDescriptor.getName(), moduleDescriptor.getDescription(), this.getFullAlias() + "/" + group.getGroupID(), URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	public Breadcrumb getGroupBreadcrumb(CommunityGroup group) {
		return new Breadcrumb(group.getName(), group.getDescription(), "/groupfirstpage/" + group.getGroupID(), URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	@Override
	public List<? extends SettingDescriptor> getSettings() {

		ArrayList<SettingDescriptor> settingDescriptors = new ArrayList<SettingDescriptor>();
		
		ModuleUtils.addSettings(settingDescriptors, super.getSettings());
		
		try {
			ModuleUtils.addSettings(settingDescriptors, ModuleUtils.getAnnotatedSettingDescriptors(this,AnnotatedAdminModule.class, systemInterface.getUserHandler()));
			
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
