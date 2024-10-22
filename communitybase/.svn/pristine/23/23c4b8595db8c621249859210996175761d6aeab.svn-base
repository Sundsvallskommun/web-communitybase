package se.dosf.communitybase.modules.i18n;

import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.modules.AnnotatedForegroundModule;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.URIParser;

public class SetLanguageModule extends AnnotatedForegroundModule {

	@Override
	@WebPublic(alias="setlanguage")
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Language language = null;

		String langCodeParam = req.getParameter("languageCode");
		String redirectParam = req.getParameter("redirectTo");

		// Set session language
		if(!StringUtils.isEmpty(langCodeParam) && (language = Language.getLanguage(langCodeParam)) != null) {
			req.getSession().setAttribute("language", language);
		}

		// Redirect to requested URL
		if(!StringUtils.isEmpty(redirectParam)) {
			res.sendRedirect(redirectParam);
			return null;
		} 

		// Redirect to language specific default URL
		else {
			
			SectionDescriptor sectionDescriptor;
			Entry<SectionDescriptor, Section> entry = null;

			// Redirect to language section
			if(language != null || (language = (Language) req.getSession().getAttribute("language")) != null || user != null && (language = user.getLanguage()) != null /* TODO: try get language from browser*/) {
				entry = this.systemInterface.getRootSection().getSectionCache().getEntry(language.getLanguageCode());
				if(entry != null) {
					sectionDescriptor = entry.getKey();
					res.sendRedirect(req.getContextPath() + "/" + sectionDescriptor.getAlias());
					return null;
				} else {
					log.warn("No section with alias " + language.getLanguageCode() + " found for user language " + language + ". Fallback on system default language section");
				}
			}

			// Fall back on system default language section
			if(systemInterface.getDefaultLanguage() != null) {
				if((entry = this.systemInterface.getRootSection().getSectionCache().getEntry(systemInterface.getDefaultLanguage().getLanguageCode())) != null) {
					sectionDescriptor = entry.getKey();
					res.sendRedirect(req.getContextPath() + "/" + sectionDescriptor.getAlias());
					return null;
				} else {
					log.warn("No section with alias " + systemInterface.getDefaultLanguage().getLanguageCode() + " found for system default language " + systemInterface.getDefaultLanguage() + ". Fallback on root section");
				}
			}

			// Fall back on root section
			sectionDescriptor = this.systemInterface.getRootSection().getSectionDescriptor();
			res.sendRedirect(req.getContextPath() + "/" + sectionDescriptor.getAlias());
			return null;
		}
	}
}