package se.dosf.communitybase.modules.newsletter.beans;

import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.utils.CalendarUtil;
import se.unlogic.standardutils.xml.XMLUtils;

public class NewsLetter {

	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd");
	private static SimpleDateFormat completeDate = new SimpleDateFormat("yyyy-MM-dd");
	private Timestamp date;
	private String URL;

	private Integer newsletterID;
	private String title;
	private String message;
	private Integer groupID;
	private Integer userID;
	private Blob image;
	private String mimetype;
	private String imageLocation;


	public Integer getNewsletterID() {
		return newsletterID;
	}

	public void setNewsletterID(Integer newsletterID) {
		this.newsletterID = newsletterID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Integer getUserID() {
		return userID;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setDescription(String title) {
		this.title = title;
	}

	public void setURL(String url) {
		URL = url;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.title;
	}

	public String getUrl() {
		//return ID ??
		return this.URL;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Blob getImage() {
		return image;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	public String getImageLocation() {
		return imageLocation;
	}

	@Override
	public String toString(){
		return this.title + " (ID: " + this.newsletterID + ")";
	}

	public Element toXML(Document doc) {

		Element newsletterEvent = doc.createElement("newsletter");

		if(this.newsletterID != null){
			newsletterEvent.appendChild(XMLUtils.createElement("newsletterID", this.newsletterID.toString(), doc));
		}

		if(this.title != null){
			newsletterEvent.appendChild(XMLUtils.createCDATAElement("title", this.title, doc));
		}

		if(this.date != null){
			newsletterEvent.appendChild(XMLUtils.createCDATAElement("date", dateTimeFormat.format(date), doc));
		}

		if(this.date != null){
			newsletterEvent.appendChild(XMLUtils.createElement("postedDate", completeDate.format(date), doc));

			Calendar calendar =  Calendar.getInstance();
			calendar.setTime(this.date);

			newsletterEvent.appendChild(XMLUtils.createElement("fullposted", CalendarUtil.toFullDateString(calendar), doc));
		}

		if(this.URL != null){
			newsletterEvent.appendChild(XMLUtils.createCDATAElement("url", this.URL, doc));
		}

		if(this.message != null){
			newsletterEvent.appendChild(XMLUtils.createCDATAElement("message", this.message, doc));
		}

		if(this.groupID != null){
			newsletterEvent.appendChild(XMLUtils.createElement("groupID", this.groupID.toString(), doc));
		}

		if(this.groupID != null){
			newsletterEvent.appendChild(XMLUtils.createElement("userID", this.userID.toString(), doc));
		}

		if(this.imageLocation != null){
			newsletterEvent.appendChild(XMLUtils.createElement("imagelocation", this.imageLocation, doc));
		}

		return newsletterEvent;
	}

}

