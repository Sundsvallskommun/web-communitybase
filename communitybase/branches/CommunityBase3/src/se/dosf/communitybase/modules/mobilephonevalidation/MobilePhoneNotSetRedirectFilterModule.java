package se.dosf.communitybase.modules.mobilephonevalidation;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.FilterChain;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.FilterModuleDescriptor;
import se.unlogic.hierarchy.filtermodules.AnnotatedFilterModule;
import se.unlogic.webutils.http.URIParser;

public class MobilePhoneNotSetRedirectFilterModule extends AnnotatedFilterModule {

	private MobilePhoneValidationModule parentModule;
	
	@Override
	public void doFilter(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FilterChain filterChain) throws TransformerException, IOException {

		if (user != null && CBAccessUtils.isExternalUser(user) && !user.getAttributeHandler().isSet(CBConstants.USER_MOBILE_PHONE_ATTRIBUTE) && !parentModule.isExcludedUser(user)) {
			res.sendRedirect(uriParser.getFullContextPath() + parentModule.getFullAlias());

			return;
		}

		filterChain.doFilter(req, res, user, uriParser);
	}
	
	public void setParentModule(MobilePhoneValidationModule parentModule) {
		
		this.parentModule = parentModule;
	}

	public FilterModuleDescriptor getDescriptor() {

		return moduleDescriptor;
	}
}