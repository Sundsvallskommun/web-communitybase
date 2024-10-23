package se.dosf.communitybase.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.ClassPathURIResolver;
import se.unlogic.standardutils.xml.XSLTCacher;
import se.unlogic.standardutils.xml.XSLTURICacher;
import se.unlogic.standardutils.xml.XSLVariableReader;

public class ModuleUtils {

	@SuppressWarnings("unchecked")
	public static <T extends ForegroundModule> T getCachedModule(SectionInterface sectionInterface, Class<T> clazz) {

		List<Entry<ForegroundModuleDescriptor, ForegroundModule>> sectionModules = sectionInterface.getForegroundModuleCache().getCachedModuleSet();

		for (Entry<ForegroundModuleDescriptor, ForegroundModule> entry : sectionModules) {

			if (entry.getValue().getClass().equals(clazz)) {

				return (T) entry.getValue();
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends ForegroundModule> List<T> getCachedModules(SectionInterface sectionInterface, Class<T> clazz, boolean recursive) {

		List<T> cachedModules = new ArrayList<T>();

		if(sectionInterface.getMenuCache().size() != 0){

			List<Entry<ForegroundModuleDescriptor, ForegroundModule>> sectionModules = sectionInterface.getForegroundModuleCache().getCachedModuleSet();

			for (Entry<ForegroundModuleDescriptor, ForegroundModule> entry : sectionModules) {

				if (entry.getValue().getClass().equals(clazz)) {

					cachedModules.add((T) entry.getValue());

				}
			}
		}

		if(recursive){

			for(SectionInterface section : sectionInterface.getSectionCache().getSectionMap().values()){

				List<T> modules = getCachedModules(section, clazz, recursive);

				if(modules != null && !modules.isEmpty()){

					cachedModules.addAll(modules);
				}
			}
		}

		if(cachedModules.isEmpty()) {
			return null;
		}

		return cachedModules;

	}

	/**
	 * Caches language specific style sheets from text area module setting
	 * 
	 * The module setting must be in the following format:
	 *
	 * Module setting example:
	 * stylesheet.en.xsl
	 * stylesheet.sv.xsl
	 * stylesheet.fi.xsl
	 * ...
	 * 
	 * The language is determined from the second last term in the filename, e.g. 'en'. 
	 * 
	 * Style sheets path type is Classpath 
	 * 
	 * Style sheets for which the language cannot be determined will be ignored.
	 * */
	public static Map<Language, XSLTCacher> cacheLanguageXSLs(Map<Language, XSLTCacher> languageXSLs, String languageXSLsAsString, ModuleDescriptor moduleDescriptor, Logger log) {
		languageXSLs.clear();
		if(languageXSLs != null && moduleDescriptor != null && !StringUtils.isEmpty(languageXSLsAsString)) {
			String[] languageXSLsArray = StringUtils.splitOnLineBreak(languageXSLsAsString);
			Language language;
			String[] parts;
			for(String languageXSLStr : languageXSLsArray) {
				parts = languageXSLStr.split("\\.");
				if(parts.length > 2 && (language = Language.getLanguage(parts[parts.length-2])) != null) {
					try {
						URL styleSheetURL = Class.forName(moduleDescriptor.getClassname()).getResource(languageXSLStr);
						if(styleSheetURL != null) {
							languageXSLs.put(language, new XSLTURICacher(styleSheetURL.toURI(),ClassPathURIResolver.getInstance()));
						} else {
							throw new Exception("Style sheet URL could not be found for " + languageXSLStr);
						}
					} catch (Exception e) {
						if(log != null) {
							log.warn("Failed caching style sheet " + languageXSLStr, e);
						}
					}
				} else {
					if(log != null) {
						log.warn("No language could be determined from style sheet file name. Ignoring stylesheet " + languageXSLStr + ". Check module setting");
					}
				}
			}
		}
		return languageXSLs;
	}
	
	/**
	 * Caches language specific XSL variable readers from text area module setting
	 * 
	 * The module setting must be in the following format:
	 *
	 * Module setting example:
	 * stylesheet.en.xsl
	 * stylesheet.sv.xsl
	 * stylesheet.fi.xsl
	 * ...
	 * 
	 * The language is determined from the second last term in the filename, e.g. 'en'. 
	 * 
	 * Style sheets path type is Classpath 
	 * 
	 * Style sheets for which the language cannot be determined will be ignored.
	 * */
	public static Map<Language, XSLVariableReader> cacheLanguageXSLVariableReaders(Map<Language, XSLVariableReader> languageXSLVariableReaders, String languageXSLsAsString, ModuleDescriptor moduleDescriptor, Logger log) {
		languageXSLVariableReaders.clear();
		if(languageXSLVariableReaders != null && moduleDescriptor != null && !StringUtils.isEmpty(languageXSLsAsString)) {
			String[] languageXSLsArray = StringUtils.splitOnLineBreak(languageXSLsAsString);
			Language language;
			String[] parts;
			for(String languageXSLStr : languageXSLsArray) {
				parts = languageXSLStr.split("\\.");
				if(parts.length > 2 && (language = Language.getLanguage(parts[parts.length-2])) != null) {
					try {
						URL styleSheetURL = Class.forName(moduleDescriptor.getClassname()).getResource(languageXSLStr);
						if(styleSheetURL != null) {
							languageXSLVariableReaders.put(language, new XSLVariableReader(styleSheetURL.toURI()));
						} else {
							throw new Exception("Style sheet URL could not be found for " + languageXSLStr);
						}
					} catch (Exception e) {
						if(log != null) {
							log.warn("Failed caching XSL variable reader for style sheet " + languageXSLStr, e);
						}
					}
				} else {
					if(log != null) {
						log.warn("No language could be determined from style sheet file name. Ignoring stylesheet " + languageXSLStr + ". Check module setting");
					}
				}
			}
		}
		return languageXSLVariableReaders;
	}
}
