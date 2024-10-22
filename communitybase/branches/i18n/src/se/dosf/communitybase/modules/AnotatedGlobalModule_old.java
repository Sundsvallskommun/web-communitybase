package se.dosf.communitybase.modules;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.enums.ModuleType;
import se.dosf.communitybase.utils.AccessUtils;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.URLType;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SettingHandler;
import se.unlogic.hierarchy.core.utils.XSLVariableReaderFactory;
import se.unlogic.hierarchy.modules.ModuleSetting;
import se.unlogic.hierarchy.modules.SimpleForegroundModule;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.XSLVariableReader;
import se.unlogic.webutils.http.URIParser;

public abstract class AnotatedGlobalModule_old extends SimpleForegroundModule implements CommunityModule {

	private final Class<?>[] defaultParameterTypes = new Class<?>[] { HttpServletRequest.class, HttpServletResponse.class, CommunityUser.class, URIParser.class };

	private final HashMap<String, Method> methodMap = new HashMap<String, Method>();

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptorBean, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptorBean, sectionInterface, dataSource);

		Method[] methods = this.getClass().getMethods();

		for (Method method : methods) {
			checkMethod(method, WebPublic.class, defaultParameterTypes, methodMap);
		}		
		
		this.setModuleSettings();
		this.setXSLVariables();
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {
		super.update(moduleDescriptor, dataSource);
		this.setModuleSettings();
		this.setXSLVariables();
	}

	public Breadcrumb getModuleBreadcrumb() {
		return new Breadcrumb(this.moduleDescriptor.getName(), moduleDescriptor.getDescription(), this.getFullAlias(), URLType.RELATIVE_FROM_CONTEXTPATH);
	}

	public Breadcrumb getGroupBreadcrumb(CommunityGroup group) {
		return new Breadcrumb(group.getName(), group.getDescription(), "#", URLType.FULL);
	}

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
		
		if (uriParser.size() > 1) {

			Method method = this.methodMap.get(uriParser.get(1));

			if (method == null) {
				return this.methodNotFound(req, res, (CommunityUser) user, uriParser);
			} else {
				try {
					return this.invokeMethod(method, req, res, (CommunityUser) user, uriParser);
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

	protected ForegroundModuleResponse invokeMethod(Method method, HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws Exception {
		return (ForegroundModuleResponse) method.invoke(this, req, res, user, uriParser);
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
			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor(), this.moduleDescriptor, "Administration permission denied for group " + group);
		}
		
	}

	public ForegroundModuleDescriptor getModuleDescriptor() {
		return this.moduleDescriptor;
	}

	protected void setModuleSettings() {

		SettingHandler settingHandler = moduleDescriptor.getSettingHandler();

		Class<?> clazz = this.getClass();

		while (clazz != AnotatedGlobalModule_old.class) {

			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {

				ModuleSetting moduleSetting;

				if ((moduleSetting = field.getAnnotation(ModuleSetting.class)) != null) {

					try {
						if (Modifier.isFinal(field.getModifiers())) {

							log.warn("Field " + field.getName() + " in class " + clazz + " is final, skipping");

						} else if (field.getType() == String.class) {

							String value = settingHandler.getString(field.getName());

							if (value != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, value);
							}

						} else if (field.getType() == List.class && field.getGenericType() == String.class) {

							List<String> values = settingHandler.getStrings(field.getName());

							if (values != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, values);
							}

						} else if (field.getType() == Integer.class) {

							Integer value = settingHandler.getInt(field.getName());

							if (value != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, value);
							}

						} else if (field.getType() == List.class && ReflectionUtils.checkGenericTypes(field, Integer.class)) {

							List<Integer> values = settingHandler.getInts(field.getName());

							if (values != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, values);
							}

						} else if (field.getType() == Long.class) {

							Long value = settingHandler.getLong(field.getName());

							if (value != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, value);
							}

						} else if (field.getType() == List.class && ReflectionUtils.checkGenericTypes(field, Long.class)) {

							List<Long> values = settingHandler.getLongs(field.getName());

							if (values != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, values);
							}

						} else if (field.getType() == Double.class) {

							Double value = settingHandler.getDouble(field.getName());

							if (value != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, value);
							}

						} else if (field.getType() == List.class && ReflectionUtils.checkGenericTypes(field, Double.class)) {

							List<Double> values = settingHandler.getDoubles(field.getName());

							if (values != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, values);
							}

						} else if (field.getType() == Boolean.class) {

							Boolean value = settingHandler.getBoolean(field.getName());

							if (value != null || moduleSetting.allowsNull()) {
								this.setFieldValue(field, value);
							}

						} else {
							log.warn("Field " + field.getName() + " in class " + clazz + " is of an unallowed type " + field.getType() + " or unallowed generic type " + field.getGenericType() + ", skipping");
						}
					} catch (IllegalArgumentException e) {

						log.error("Unable to set modulesetting field " + field.getName() + " in class " + clazz, e);

					} catch (IllegalAccessException e) {

						log.error("Unable to set modulesetting field " + field.getName() + " in class " + clazz, e);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	protected void setXSLVariables() {

		XSLVariableReader variableReader = null;

		Class<?> clazz = this.getClass();

		outer: while (clazz != AnotatedGlobalModule_old.class) {

			Field[] fields = clazz.getDeclaredFields();

			for (Field field : fields) {

				if (field.getAnnotation(XSLVariable.class) != null) {

					if (!Modifier.isFinal(field.getModifiers()) && field.getType() == String.class) {

						if (variableReader == null) {
							try {
								variableReader = XSLVariableReaderFactory.getVariableReader(moduleDescriptor, sectionInterface);

								if (variableReader == null) {
									log.error("Unable to create XSLVariableReader for module " + moduleDescriptor + ", XSLVariable anotated fields will not be set!");
									break outer;
								}

							} catch (SAXException e) {
								log.error("Unable to create XSLVariableReader for module " + moduleDescriptor + ", XSLVariable anotated fields will not be set!", e);
								break outer;
							} catch (IOException e) {
								log.error("Unable to create XSLVariableReader for module " + moduleDescriptor + ", XSLVariable anotated fields will not be set!", e);
								break outer;
							} catch (ParserConfigurationException e) {
								log.error("Unable to create XSLVariableReader for module " + moduleDescriptor + ", XSLVariable anotated fields will not be set!", e);
								break outer;
							} catch (ClassNotFoundException e) {
								log.error("Unable to create XSLVariableReader for module " + moduleDescriptor + ", XSLVariable anotated fields will not be set!", e);
								break outer;
							} catch (URISyntaxException e) {
								log.error("Unable to create XSLVariableReader for module " + moduleDescriptor + ", XSLVariable anotated fields will not be set!", e);
								break outer;
							} catch (XPathExpressionException e) {
								log.error("Unable to create XSLVariableReader for module " + moduleDescriptor + ", XSLVariable anotated fields will not be set!", e);
								break outer;
							}
						}

						String value = variableReader.getValue(field.getName());

						if (value != null) {
							try {
								log.debug("Setting XSL variable value for field " + field.getName() + " in class " + clazz);

								this.setFieldValue(field, value);

							} catch (IllegalArgumentException e) {

								log.error("Unable to set XSL variable field " + field.getName() + " in class " + clazz, e);

							} catch (IllegalAccessException e) {

								log.error("Unable to set XSL variable field " + field.getName() + " in class " + clazz, e);
							}
						} else {
							log.warn("No XSL variable value found in XSL for field " + field.getName() + " in module " + moduleDescriptor);
						}
					} else {
						log.warn("Field " + field.getName() + " in class " + clazz + " is either final or not a String, skipping");
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	private void setFieldValue(Field field, Object value) throws IllegalArgumentException, IllegalAccessException {

		boolean declaredAccessible = field.isAccessible();

		if (!declaredAccessible) {
			field.setAccessible(true);
		}

		field.set(this, value);

		if (!declaredAccessible) {
			field.setAccessible(false);
		}
	}
}
