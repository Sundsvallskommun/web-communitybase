package se.dosf.communitybase.daos;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.hierarchy.core.interfaces.ModuleDescriptor;
import se.unlogic.standardutils.dao.TransactionHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.BooleanQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.IntegerPopulator;

public class CommunityModuleDAO extends BaseDAO {

	public CommunityModuleDAO(DataSource ds) {
		super(ds);
	}

	public ArrayList<Integer> getEnabledModules(CommunityGroup group) throws SQLException {

		if (group.isEnabled()) {

			String sql = "SELECT moduleID FROM groupmodules WHERE groupID = ?";

			ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(this.dataSource, true, sql, IntegerPopulator.getPopulator());
			query.setInt(1, group.getGroupID());

			return query.executeQuery();
		}

		return null;

	}

	public boolean isModuleEnabled(int moduleID, CommunityGroup group) throws SQLException {

		if (group.isEnabled()) {

			BooleanQuery query = new BooleanQuery(this.dataSource, true, "SELECT moduleID FROM groupmodules WHERE groupID = ? AND moduleID = ?");
			query.setInt(1, group.getGroupID());
			query.setInt(2, moduleID);

			return query.executeQuery();
		}

		return false;
	}

	public void enableModule(Integer moduleID, CommunityGroup group) throws SQLException {

		TransactionHandler transactionHandler = null;

		try {
			transactionHandler = new TransactionHandler(dataSource);

			BooleanQuery checkQuery = transactionHandler.getBooleanQuery("SELECT * FROM groupmodules WHERE groupID = ? AND moduleID = ?");

			checkQuery.setInt(1,group.getGroupID());
			checkQuery.setInt(2, moduleID);

			if(!checkQuery.executeQuery()){

				UpdateQuery insertQuery = transactionHandler.getUpdateQuery("INSERT INTO groupmodules VALUES (?,?)");

				insertQuery.setInt(1,group.getGroupID());
				insertQuery.setInt(2, moduleID);

				insertQuery.executeUpdate();
			}

			transactionHandler.commit();

		} finally  {
			TransactionHandler.autoClose(transactionHandler);
		}

	}

	public void disableModule(ModuleDescriptor moduleDescriptor, CommunityGroup group) throws SQLException {

		UpdateQuery query = new UpdateQuery(dataSource, true, "DELETE FROM groupmodules WHERE groupID = ? AND moduleID = ?");

		query.setInt(1,group.getGroupID());
		query.setInt(2, moduleDescriptor.getModuleID());

		query.executeUpdate();
	}
}