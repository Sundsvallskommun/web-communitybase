package se.dosf.communitybase.modules.pictureGallery.beans;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import se.unlogic.standardutils.xml.XMLUtils;

public class Picture implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer fileID;
	private Integer sectionID;
	private String filename;
	private String description;
	private Blob smallPic;
	private Blob mediumPic;
	private Blob largePic;
	private String contentTypeSmall;
	private String contentTypeMedium;
	private String contentTypeLarge;
	private Integer postedBy;
	private Timestamp posted;

	public Picture() {
		super();
	}

	public Integer getFileID() {
		return fileID;
	}

	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}

	public Integer getSectionID() {
		return sectionID;
	}

	public void setSectionID(Integer sectionID) {
		this.sectionID = sectionID;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Blob getSmallPic() {
		return smallPic;
	}

	public void setSmallPic(Blob smallPic) {
		this.smallPic = smallPic;
	}

	public Blob getMediumPic() {
		return mediumPic;
	}

	public void setMediumPic(Blob mediumPic) {
		this.mediumPic = mediumPic;
	}

	public Blob getLargePic() {
		return largePic;
	}

	public void setLargePic(Blob largePic) {
		this.largePic = largePic;
	}

	public String getContentTypeSmall() {
		return contentTypeSmall;
	}

	public void setContentTypeSmall(String contentTypeSmall) {
		this.contentTypeSmall = contentTypeSmall;
	}

	public String getContentTypeMedium() {
		return contentTypeMedium;
	}

	public void setContentTypeMedium(String contentTypeMedium) {
		this.contentTypeMedium = contentTypeMedium;
	}

	public String getContentTypeLarge() {
		return contentTypeLarge;
	}

	public void setContentTypeLarge(String contentTypeLarge) {
		this.contentTypeLarge = contentTypeLarge;
	}

	public void setPostedBy(Integer postedBy) {
		this.postedBy = postedBy;
	}

	public Integer getPostedBy() {
		return postedBy;
	}

	public Timestamp getPosted() {
		return posted;
	}

	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}

	public Node toXML(Document doc) {

		Element pictureElement = doc.createElement("picture");

		if (this.fileID != null) {
			pictureElement.appendChild(XMLUtils.createElement("fileID", fileID.toString(), doc));
		}

		if (this.sectionID != null) {
			pictureElement.appendChild(XMLUtils.createElement("sectionID", this.sectionID.toString(), doc));
		}

		if (this.filename != null) {
			pictureElement.appendChild(XMLUtils.createCDATAElement("filename", this.filename, doc));
		}

		if (this.description != null) {
			pictureElement.appendChild(XMLUtils.createCDATAElement("description", this.description, doc));
		}

		if (this.postedBy != null) {
			pictureElement.appendChild(XMLUtils.createCDATAElement("postedBy", this.postedBy.toString(), doc));
		}

		if (this.posted != null) {
			pictureElement.appendChild(XMLUtils.createElement("posted", this.posted.toString(), doc));
		}

		return pictureElement;
	}

	@Override
	public String toString() {
		return this.filename + "(ID: " + fileID + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fileID == null) ? 0 : fileID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Picture other = (Picture) obj;
		if (fileID == null) {
			if (other.fileID != null) {
				return false;
			}
		} else if (!fileID.equals(other.fileID)) {
			return false;
		}
		return true;
	}
}
