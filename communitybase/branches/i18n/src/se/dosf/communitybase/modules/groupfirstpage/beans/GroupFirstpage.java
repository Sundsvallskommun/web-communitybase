package se.dosf.communitybase.modules.groupfirstpage.beans;

import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.utils.CalendarUtil;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.xml.XMLUtils;

public class GroupFirstpage {

	@DAOManaged
	private Integer groupID;

	@DAOManaged
	private String title;
	
	@DAOManaged
	private String content;

	@DAOManaged
	private Timestamp changed;
	
	@DAOManaged
	private Blob image;
	
	@DAOManaged
	private String imageLocation;
	
	
	public Integer getGroupID() {
		return groupID;
	}


	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public Timestamp getChanged() {
		return changed;
	}


	public void setChanged(Timestamp changed) {
		this.changed = changed;
	}

	public void setImage(Blob image) {
		this.image = image;
	}

	public Blob getImage() {
		return image;
	}


	public String getImageLocation() {
		return imageLocation;
	}


	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}


	public Element toXML(Document doc){
		
		Element firstpageElement = doc.createElement("groupFirstpage");
		
		if(this.groupID != null){
			XMLUtils.appendNewElement(doc, firstpageElement, "groupID", this.groupID);
		}
		
		if(this.title != null){
			XMLUtils.appendNewCDATAElement(doc, firstpageElement, "title", this.title);
		}
		
		if(this.content != null){
			XMLUtils.appendNewCDATAElement(doc, firstpageElement, "content", this.content);
		}
		
		if(this.changed != null){
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(this.changed.getTime());
			XMLUtils.appendNewCDATAElement(doc, firstpageElement, "changed", CalendarUtil.toFullDateString(calendar));
		}
		
		if(this.imageLocation != null){
			XMLUtils.appendNewCDATAElement(doc, firstpageElement, "imagelocation", this.imageLocation);
		}
		
		return firstpageElement;
		
	}
}
