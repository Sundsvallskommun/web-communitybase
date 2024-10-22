package se.dosf.communitybase.beans;

import java.sql.Timestamp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

public class Receipt implements Elementable {

	@Key
	@DAOManaged(columnName="userID")
	@XMLElement
	private CommunityUser user;
	
	private Integer objectID;
	
	@DAOManaged
	@XMLElement
	private Timestamp firstReadTime;
	
	@DAOManaged
	@XMLElement
	private Timestamp lastReadTime;
	
	public Receipt() { }
	
	public Receipt(CommunityUser user, Integer objectID, Timestamp firstReadTime, Timestamp lastReadTime){
		
		this.user = user;
		this.objectID = objectID;
		this.firstReadTime = firstReadTime;
		this.lastReadTime = lastReadTime;
		
	}
	
	public CommunityUser getUser() {
		return user;
	}
	
	public Timestamp getFirstReadTime() {
		return firstReadTime;
	}

	public void setFirstReadTime(Timestamp firstReadTime) {
		this.firstReadTime = firstReadTime;
	}

	public Timestamp getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(Timestamp lastReadTime) {
		this.lastReadTime = lastReadTime;
	}

	public void setUser(CommunityUser user) {
		this.user = user;
	}

	public void setObjectID(Integer objectID) {
		this.objectID = objectID;
	}

	public Integer getObjectID() {
		return objectID;
	}
	

	@Override
	public Element toXML(Document doc){
		
		Element receiptElement = doc.createElement("receipt");
		
		if(user != null){
			receiptElement.appendChild(user.toXML(doc));
		}
		
		if(objectID != null){
			receiptElement.appendChild(XMLUtils.createElement("objectID", this.objectID.toString(), doc));
		}
		
		if(firstReadTime != null){
			receiptElement.appendChild(XMLUtils.createElement("firstReadTime", DateUtils.DATE_TIME_FORMATTER.format(this.firstReadTime), doc));
		}
		
		if(lastReadTime != null){
			receiptElement.appendChild(XMLUtils.createElement("lastReadTime", DateUtils.DATE_TIME_FORMATTER.format(this.lastReadTime), doc));
		}
		
		return receiptElement;
		
	}

	
	
	
	
}
