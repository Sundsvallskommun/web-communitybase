package se.dosf.communitybase.modules.dbcleanup;

import java.sql.SQLException;


public interface DBCleaner {
	
	public void cleanDB() throws SQLException; 

}
