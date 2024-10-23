package se.dosf.communitybase.modules.newsletter.beans;

import se.dosf.communitybase.beans.Receipt;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name="newsletterreceipts")
@XMLElement
public class NewsLetterReceipt extends Receipt {
	
	@Key
	@DAOManaged(columnName="weekLetterID")
	@ManyToOne
	@XMLElement
	private NewsLetter newsLetter;

	public void setNewsLetter(NewsLetter newsLetter) {
		this.newsLetter = newsLetter;
	}

	public NewsLetter getNewsLetter() {
		return newsLetter;
	}
	
}
