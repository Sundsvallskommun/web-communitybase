package se.dosf.communitybase.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Receipt;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;

public class ReceiptItemPopulator implements BeanResultSetPopulator<Receipt> {

	private UserHandler userHandler;
	
	public ReceiptItemPopulator(UserHandler userHandler){
		this.userHandler = userHandler;
	}
	
	public Receipt populate(ResultSet rs) throws SQLException {
		
		return new Receipt((CommunityUser)this.userHandler.getUser(rs.getInt(1), true), 
				rs.getInt(2), 
				rs.getTimestamp(3),
				rs.getTimestamp(4));
	
	}

}
