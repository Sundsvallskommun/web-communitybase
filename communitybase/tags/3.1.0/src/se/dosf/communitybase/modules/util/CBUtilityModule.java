package se.dosf.communitybase.modules.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.modules.userprofile.UserProfileModule;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;

public class CBUtilityModule extends AnnotatedForegroundModule {

	@InstanceManagerDependency(required = true)
	private CBInterface cbInterface;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(CBUtilityModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + CBUtilityModule.class.getName());
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(CBUtilityModule.class, this);

		super.unload();

	}
	
	public long getSectionLogoLastModified(Integer sectionID) {
		
		File sectionLogo = cbInterface.getSectionLogo(sectionID);

		if (sectionLogo != null) {

			return sectionLogo.lastModified();
		}
		
		return 0;
	}

	@WebPublic(alias = "sectionlogo")
	public ForegroundModuleResponse getSectionLogo(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer sectionID = null;

		if (uriParser.size() == 3 && (sectionID = NumberUtils.toInt(uriParser.get(2))) != null) {

			File sectionLogo = cbInterface.getSectionLogo(sectionID);

			try {
				if (sectionLogo != null) {

					HTTPUtils.sendFile(sectionLogo, req, res, ContentDisposition.INLINE);

				} else {

					URL url = UserProfileModule.class.getResource("staticcontent/pics/empty-profileimage.png");

					HTTPUtils.sendFile(url, url.openStream(), "empty-profileimage.png", req, res, ContentDisposition.INLINE);
				}				
				
			} catch (IOException e) {
				log.info("Error sending section logo for sectionID " + sectionID + " to user " + user);
			}
			
			return null;

		}

		throw new URINotFoundException(uriParser);

	}

}
