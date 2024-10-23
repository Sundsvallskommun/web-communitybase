package se.dosf.communitybase.modules.filearchive.beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class File {

	private Integer fileID;
	private Timestamp date;// posted
	private String description;
	private Integer sectionID;
	private String fileName;

	private CommunityUser poster;
	private String contentType;
	private java.sql.Blob blob;
	private String sectionName;
	private Integer groupID;
	private Long fileSize;

	private static SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat time = new SimpleDateFormat("HH:mm");

	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}

	public Integer getFileID() {
		return fileID;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setPoster(CommunityUser poster) {
		this.poster = poster;
	}

	public CommunityUser getPoster() {
		return poster;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setSectionID(Integer sectionID) {
		this.sectionID = sectionID;
	}

	public Integer getSectionID() {
		return sectionID;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setBlob(java.sql.Blob blob) {
		this.blob = blob;
	}

	public java.sql.Blob getBlob() {
		return blob;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.fileName, 30, "...") + " (ID: " + this.fileID + ")";
	}

	public Element toXML(Document doc) {

		Element fileArchiveElement = doc.createElement("file");
		Element postedByElement = doc.createElement("postedBy");

		if (this.getFileID() != null) {
			fileArchiveElement.appendChild(XMLUtils.createElement("fileID", this.fileID.toString(), doc));
		}
		
		if(this.getSectionID() != null){
			fileArchiveElement.appendChild(XMLUtils.createElement("sectionID", this.sectionID.toString(), doc));
		}

		if (this.getFileName() != null) {
			fileArchiveElement.appendChild(XMLUtils.createCDATAElement("filename", this.fileName, doc));
		}

		if (this.getContentType() != null) {
			fileArchiveElement.appendChild(XMLUtils.createCDATAElement("contentType", this.contentType, doc));
		}

		if (this.getDate() != null) {
			if(this.getPoster() != null){
				postedByElement.appendChild(this.getPoster().toXML(doc));
			}
			
			postedByElement.appendChild(XMLUtils.createElement("postedDate", dates.format(getDate()), doc));
			postedByElement.appendChild(XMLUtils.createElement("postedTime", time.format(getDate()), doc));
			fileArchiveElement.appendChild(XMLUtils.createCDATAElement("postedInMillis", String.valueOf(this.date.getTime()), doc));
			fileArchiveElement.appendChild(postedByElement);
		}

		if (this.getDescription() != null) {
			fileArchiveElement.appendChild(XMLUtils.createCDATAElement("description", this.getDescription().toString(), doc));
		}

		/*
		if(this.getPoster() != null){
			poster.appendChild(this.getPoster().toXML(doc));
			fileArchiveElement.appendChild(poster);
		}*/

		return fileArchiveElement;
	}

}
