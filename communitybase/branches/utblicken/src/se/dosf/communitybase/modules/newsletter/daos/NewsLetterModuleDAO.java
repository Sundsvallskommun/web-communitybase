package se.dosf.communitybase.modules.newsletter.daos;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.IdentifiedEvent;
import se.dosf.communitybase.beans.Receipt;
import se.dosf.communitybase.modules.newsletter.beans.NewsLetter;
import se.dosf.communitybase.modules.newsletter.populators.NewsLetterPopulator;
import se.dosf.communitybase.populators.ReceiptItemPopulator;
import se.unlogic.hierarchy.core.daos.BaseDAO;
import se.unlogic.hierarchy.core.handlers.UserHandler;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.dao.querys.IntegerKeyCollector;
import se.unlogic.standardutils.dao.querys.ObjectQuery;
import se.unlogic.standardutils.dao.querys.UpdateQuery;
import se.unlogic.standardutils.populators.annotated.AnnotatedResultSetPopulator;

public class NewsLetterModuleDAO extends BaseDAO{

	private static final AnnotatedResultSetPopulator<IdentifiedEvent> EVENT_POPULATOR = new AnnotatedResultSetPopulator<IdentifiedEvent>(IdentifiedEvent.class);
	private static final NewsLetterPopulator POPULATOR = new NewsLetterPopulator();
	private ReceiptItemPopulator RECEIPTPOPULATOR = null;

	public NewsLetterModuleDAO(DataSource ds, UserHandler userHandler) {
		super(ds);

		RECEIPTPOPULATOR = new ReceiptItemPopulator(userHandler);

	}

	public NewsLetter get(int newsletterId) throws SQLException{

		ObjectQuery<NewsLetter> query = new ObjectQuery<NewsLetter>(this.dataSource.getConnection(), true, "SELECT * FROM newsletters WHERE weekLetterID = ?", POPULATOR);

		query.setInt(1, newsletterId);

		return query.executeQuery();
	}

	public ArrayList<NewsLetter> getAll(CommunityGroup group) throws SQLException{

		ArrayListQuery<NewsLetter> query = new ArrayListQuery<NewsLetter>(this.dataSource.getConnection(), true, "SELECT * FROM newsletters WHERE groupID = ? ORDER BY date DESC", POPULATOR);

		query.setInt(1, group.getGroupID());

		return query.executeQuery();
	}

	public Integer add(NewsLetter newsletter) throws SQLException{

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "INSERT INTO newsletters VALUES (null,?,?,?,?,?,?,?,?)");

		query.setString(1, newsletter.getTitle());
		query.setString(2, newsletter.getMessage());
		query.setInt(3, newsletter.getGroupID());
		query.setTimestamp(4, newsletter.getDate());
		query.setInt(5, newsletter.getUserID());

		if(newsletter.getImage() != null){

			query.setString(6, newsletter.getMimetype());
			query.setBlob(7, newsletter.getImage());
			query.setString(8, newsletter.getImageLocation());

		}else{

			query.setNull(6, 0);
			query.setNull(7, 0);
			query.setNull(8, 0);

		}

		IntegerKeyCollector keyCollector = new IntegerKeyCollector();
		
		query.executeUpdate(keyCollector);
		
		return keyCollector.getKeyValue();
	}

	public void update(NewsLetter newsletter) throws SQLException{

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "UPDATE newsletters SET title = ?, message = ?, mimetype = ?, image = ?, imagelocation = ? WHERE weekLetterID = ?");

		query.setString(1, newsletter.getTitle());
		query.setString(2, newsletter.getMessage());
		query.setString(3, newsletter.getMimetype());
		query.setBlob(4, newsletter.getImage());
		query.setString(5, newsletter.getImageLocation());
		query.setInt(6, newsletter.getNewsletterID());

		query.executeUpdate();

	}

	public void delete(NewsLetter newsletter) throws SQLException{

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, "DELETE FROM newsletters WHERE weekletterID = ?");

		query.setInt(1, newsletter.getNewsletterID());

		query.executeUpdate();

	}

	public void checkNewsletterReadStatus(CommunityUser user, NewsLetter newsletter) throws SQLException{

		String sql = "INSERT INTO newsletterreceipts VALUES(?,?,?,?) ON DUPLICATE KEY UPDATE lastReadTime = ?";

		UpdateQuery query = new UpdateQuery(this.dataSource.getConnection(), true, sql);

		Timestamp currentTime =  new Timestamp(System.currentTimeMillis());

		query.setInt(1, user.getUserID());
		query.setInt(2, newsletter.getNewsletterID());
		query.setTimestamp(3, currentTime);
		query.setTimestamp(4, currentTime);
		query.setTimestamp(5, currentTime);

		query.executeUpdate();

	}

	public ArrayList<Receipt> getNewsletterReceipt(NewsLetter newsletter) throws SQLException{

		ArrayListQuery<Receipt> query = new ArrayListQuery<Receipt>(this.dataSource.getConnection(), true, "SELECT * FROM newsletterreceipts WHERE weekLetterID = ?", RECEIPTPOPULATOR);

		query.setInt(1, newsletter.getNewsletterID());

		return query.executeQuery();

	}

	public List<IdentifiedEvent> getEvents(CommunityGroup group, Timestamp lastLogin) throws SQLException {

		ArrayListQuery<IdentifiedEvent> query = new ArrayListQuery<IdentifiedEvent>(dataSource, true, "SELECT weekLetterID as id, title as description,title, date as added FROM newsletters WHERE groupID = ? AND date > ? ORDER by date", EVENT_POPULATOR);

		query.setInt(1, group.getGroupID());
		query.setTimestamp(2, lastLogin);

		return query.executeQuery();
	}

}
