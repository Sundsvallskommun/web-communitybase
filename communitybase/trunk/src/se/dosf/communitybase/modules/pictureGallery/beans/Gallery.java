package se.dosf.communitybase.modules.pictureGallery.beans;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.enums.RelationType;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public class Gallery implements Elementable, Comparable<Gallery>{

	private Integer sectionID;
	private Integer relationID;
	private String sectionName;
	private ArrayList<Picture> pictures;
	private RelationType type;
	private Timestamp posted;

	private String description;
	private String url;
	private Timestamp date;

	public void setSectionID(Integer sectionID) {
		this.sectionID = sectionID;
	}

	public Integer getSectionID() {
		return sectionID;
	}

	public void setRelationID(Integer relationID) {
		this.relationID = relationID;
	}

	public Integer getRelationID() {
		return relationID;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public void setPictures(ArrayList<Picture> pictures) {
		this.pictures = pictures;
	}

	public ArrayList<Picture> getPictures() {
		return pictures;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setRelationType(RelationType type) {
		this.type = type;
	}

	public RelationType getRelationType() {
		return type;
	}

	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}

	public Timestamp getPosted() {
		return posted;
	}

	public Element toXML(Document doc) {

		Element pictureGallery = doc.createElement("gallery");

		if (this.sectionID != null) {
			pictureGallery.appendChild(XMLUtils.createElement("sectionID", this.sectionID.toString(), doc));
		}

		if (this.relationID != null) {
			pictureGallery.appendChild(XMLUtils.createElement("groupID", this.relationID.toString(), doc));
		}

		if (this.sectionName != null) {
			pictureGallery.appendChild(XMLUtils.createCDATAElement("sectionName", this.sectionName, doc));
		}

		if (this.description != null) {
			pictureGallery.appendChild(XMLUtils.createCDATAElement("description", this.description, doc));
		}

		if (this.posted != null) {
			pictureGallery.appendChild(XMLUtils.createCDATAElement("posted", DateUtils.DATE_TIME_FORMATTER.format(this.posted), doc));
			pictureGallery.appendChild(XMLUtils.createCDATAElement("postedInMillis", String.valueOf(this.posted.getTime()), doc));
		}

		if (this.date != null) {
			pictureGallery.appendChild(XMLUtils.createElement("date", this.date.toString(), doc));
		}

		if (this.url != null) {
			pictureGallery.appendChild(XMLUtils.createCDATAElement("url", this.url, doc));
		}

		if (this.type != null) {
			pictureGallery.appendChild(XMLUtils.createElement("relationType", this.type.name(), doc));
		}

		if (this.pictures != null && !this.pictures.isEmpty()) {

			Element picturesElement = doc.createElement("pictures");

			for (Picture picture : this.pictures) {
				picturesElement.appendChild(picture.toXML(doc));
			}

			pictureGallery.appendChild(picturesElement);

		}

		return pictureGallery;

	}

	@Override
	public String toString() {
		return this.getSectionName() + "(ID: " + this.sectionID + ");";
	}

	public int compareTo(Gallery o) {

		return sectionName.compareTo(o.getSectionName());
	}

}
