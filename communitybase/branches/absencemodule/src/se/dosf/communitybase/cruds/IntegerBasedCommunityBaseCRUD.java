package se.dosf.communitybase.cruds;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.GenericCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.http.URIParser;

public class IntegerBasedCommunityBaseCRUD<BeanType extends Elementable, CallbackType extends CRUDCallback<CommunityUser>> extends GenericCRUD<BeanType, Integer, CommunityUser, CallbackType> {

	public IntegerBasedCommunityBaseCRUD(CRUDDAO<BeanType, Integer> crudDAO, BeanRequestPopulator<BeanType> populator, String typeElementName, String typeLogName, String listMethodAlias, CallbackType callback) {

		super(crudDAO, populator, typeElementName, typeLogName, listMethodAlias, callback);
	}
	
	public IntegerBasedCommunityBaseCRUD(CRUDDAO<BeanType, Integer> crudDAO, BeanRequestPopulator<BeanType> populator, String typeElementName, String typeElementPluralName, String typeLogName, String typeLogPluralName, String listMethodAlias, CallbackType callback) {

		super(crudDAO, populator, typeElementName, typeElementPluralName, typeLogName, typeLogPluralName, listMethodAlias, callback);
	}

	@Override
	public BeanType getRequestedBean(HttpServletRequest req, HttpServletResponse res, CommunityUser user, URIParser uriParser) throws SQLException, AccessDeniedException {

		BeanType bean;

		if (uriParser.size() > 3 && NumberUtils.isInt(uriParser.get(3)) && (bean = getBean(NumberUtils.toInt(uriParser.get(3)))) != null) {
			return bean;
		}

		return null;
	}

	protected BeanType getBean(Integer beanID) throws SQLException, AccessDeniedException {

		return crudDAO.get(beanID);
	}
	
}
