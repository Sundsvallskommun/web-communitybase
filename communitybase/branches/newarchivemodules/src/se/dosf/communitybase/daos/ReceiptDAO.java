package se.dosf.communitybase.daos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOFactory;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.populators.BeanStringPopulator;
import se.unlogic.standardutils.populators.QueryParameterPopulator;

public class ReceiptDAO<BeanType> extends AnnotatedDAO<BeanType> {

	public ReceiptDAO(DataSource dataSource, Class<BeanType> beanClass, AnnotatedDAOFactory daoFactory, List<? extends QueryParameterPopulator<?>> queryParameterPopulators, List<? extends BeanStringPopulator<?>> typePopulators) {
		super(dataSource, beanClass, daoFactory, queryParameterPopulators, typePopulators);
	}

	public void checkReadStatus(CommunityUser user, Integer objectID) throws SQLException {
		
		Timestamp currentTime =  new Timestamp(System.currentTimeMillis());
		
		LowLevelQuery<BeanType> query = new LowLevelQuery<BeanType>();
		
		query.setSql("INSERT INTO " + this.getTableName() + " VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE lastReadTime = ?" );
		query.addParameters(user.getUserID(), objectID, currentTime, currentTime, currentTime);
		
		this.update(query);
	}

}
