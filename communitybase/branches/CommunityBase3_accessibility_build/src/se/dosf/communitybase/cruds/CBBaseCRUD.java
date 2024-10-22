package se.dosf.communitybase.cruds;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.interfaces.Posted;
import se.dosf.communitybase.modules.CBBaseModule;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.utils.crud.BeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;


public class CBBaseCRUD<BeanType extends Posted, IDType, CallbackType extends CBBaseModule> extends ModularCRUD<BeanType, IDType, User, CallbackType> {

	public CBBaseCRUD(BeanIDParser<IDType> idParser, CRUDDAO<BeanType, IDType> crudDAO, BeanRequestPopulator<BeanType> populator, String typeElementName, String typeLogName, String listMethodAlias, CallbackType callback) {

		super(idParser, crudDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
	}

	public CBBaseCRUD(BeanIDParser<IDType> idParser, CRUDDAO<BeanType, IDType> crudDAO, BeanRequestPopulator<BeanType> populator, String typeElementName, String typeElementPluralName, String typeLogName, String typeLogPluralName, String listMethodAlias, CallbackType callback) {

		super(idParser, crudDAO, populator, typeElementName, typeElementPluralName, typeLogName, typeLogPluralName, listMethodAlias, callback);
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {
		
		if(!CBAccessUtils.hasAddContentAccess(user, callback.getCBInterface().getRole(callback.getSectionID(), user))){
			
			throw new AccessDeniedException("Add " + typeLogName + " denied in section " + callback.getSectionDescriptor());
		}
	}

	@Override
	protected void checkUpdateAccess(BeanType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if(!CBAccessUtils.hasUpdateContentAccess(user, bean, callback.getCBInterface().getRole(callback.getSectionID(), user))){
			
			throw new AccessDeniedException("Update " + typeLogName + " " + bean +  " denied in section " + callback.getSectionDescriptor());
		}
	}

	@Override
	protected void checkDeleteAccess(BeanType bean, User user, HttpServletRequest req, URIParser uriParser) throws AccessDeniedException, URINotFoundException, SQLException {

		if(!CBAccessUtils.hasDeleteContentAccess(user, bean, callback.getCBInterface().getRole(callback.getSectionID(), user))){
			
			throw new AccessDeniedException("Delete " + typeLogName + " " + bean +  " denied in section " + callback.getSectionDescriptor());
		}
	}

	@Override
	protected BeanType populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		BeanType bean = super.populateFromAddRequest(req, user, uriParser);
		
		bean.setPoster(user);
		bean.setPosted(TimeUtils.getCurrentTimestamp());
		
		return bean;
	}

	@Override
	protected BeanType populateFromUpdateRequest(BeanType bean, HttpServletRequest req, User user, URIParser uriParser) throws ValidationException, Exception {

		bean =  super.populateFromUpdateRequest(bean, req, user, uriParser);
		
		bean.setEditor(user);
		bean.setUpdated(TimeUtils.getCurrentTimestamp());
		
		return bean;
	}
	
	@Override
	protected void appendBean(BeanType bean, Element targetElement, Document doc, User user) {

		Element beanElement = bean.toXML(doc);
		
		CBAccessUtils.appendBeanAccess(doc, targetElement, user, bean, callback.getCBInterface().getRole(callback.getSectionID(), user));
		
		targetElement.appendChild(beanElement);
		
	}

}
