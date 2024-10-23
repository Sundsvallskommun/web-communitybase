package se.dosf.communitybase.modules.securesection;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.annotations.HTMLEditorSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleFilterModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class SecureSectionForegroundModule extends AnnotatedForegroundModule {

	@ModuleSetting
	@HTMLEditorSettingDescriptor(name = "Access denied text", description = "Access denied text")
	private String accessDeniedText;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Required login level", description = "Required login level", formatValidator = PositiveStringIntegerValidator.class, required = true)
	private Integer requiredLoginLevel = 5;
	
	private SecureSectionFilterModule filterModule;
	
	@Override
	public void unload() throws Exception {
		
		if (systemInterface.getSystemStatus() != SystemStatus.STOPPING && filterModule != null) {
			try {
				systemInterface.getFilterModuleCache().unload(filterModule.getDescriptor());

			} catch (KeyNotCachedException e) {

				log.debug("Unable to unload filter module, module not cached", e);

			} catch (Exception e) {

				log.error("Error unloading filter module", e);
			}
		}
	}

	@Override
	protected void moduleConfigured() throws Exception {

		if (filterModule == null) {
			log.info("Adding virtual background module to section " + sectionInterface.getSectionDescriptor());
			
			SimpleFilterModuleDescriptor descriptor = new SimpleFilterModuleDescriptor();
			descriptor.setAdminAccess(true);
			descriptor.setAliases(Collections.singletonList("regexp:^[0-9]+(/?$|/.*)")); // Sections only have sectionID as aliases for now
			descriptor.setAnonymousAccess(false);
			descriptor.setClassname(SecureSectionFilterModule.class.getName());
			descriptor.setEnabled(true);
			descriptor.setName(moduleDescriptor.getName() + " (filter)");
			descriptor.setPriority(0);
			descriptor.setUserAccess(true);
			
			filterModule = (SecureSectionFilterModule) systemInterface.getFilterModuleCache().cache(descriptor);
			
			filterModule.setParentModule(this);
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);
		
		XMLUtils.appendNewElement(doc, documentElement, "AccessDeniedText", accessDeniedText);

		return new SimpleForegroundModuleResponse(doc);
	}

	public Integer getLoginLevel() {

		return requiredLoginLevel;
	}
}