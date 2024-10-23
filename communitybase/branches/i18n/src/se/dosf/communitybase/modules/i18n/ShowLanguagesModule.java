package se.dosf.communitybase.modules.i18n;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.AnnotatedBackgroundModule;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.modules.ModuleSetting;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XSLTCacher;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class ShowLanguagesModule extends AnnotatedBackgroundModule {

	@ModuleSetting(allowsNull=false)
	@TextFieldSettingDescriptor(name="Endpoint",description="Endpoint for setting language",required=true)
	protected String endpoint = "setlanguage";
	
	@Override
	public void init(BackgroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {
		super.init(moduleDescriptor, sectionInterface, dataSource);
	}

	public BackgroundModuleResponse processRequest(HttpServletRequest req, User user, URIParser uriParser) throws Exception {

		Document doc = XMLUtils.createDomDocument();
		Element documentElement = doc.createElement("document");

		// Add request info
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));

		// Add current module details
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));
		
		// Add section details
		documentElement.appendChild(this.sectionInterface.getSectionDescriptor().toXML(doc));

		Element languagesElement = doc.createElement("languages");
		
		languagesElement.appendChild(XMLUtils.createElement("endpoint", this.endpoint, doc));
		
		for(Map.Entry<Language, XSLTCacher> entry : this.systemInterface.getLanguageXSLs().entrySet()) {
			languagesElement.appendChild(entry.getKey().toXML(doc));
		}
		
		documentElement.appendChild(languagesElement);
		
		doc.appendChild(documentElement);

		return new SimpleBackgroundModuleResponse(doc);
	}

}

