package se.dosf.communitybase.modules.pictureGallery.beans;

import java.sql.Timestamp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class PictureComment {

	private Integer commentID;
	private Integer fileID;
	private String comment;
	private Timestamp date;
	private User user;

	public PictureComment() {
		super();
	}

	public Integer getCommentID() {
		return commentID;
	}

	public void setCommentID(Integer commentID) {
		this.commentID = commentID;
	}

	public Integer getFileID() {
		return fileID;
	}

	public void setFileID(Integer fileID) {
		this.fileID = fileID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {

		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Node toXML(Document doc) {

		Element commentElement = doc.createElement("comment");

		if (this.commentID != null) {
			commentElement.appendChild(XMLUtils.createElement("commentID", commentID.toString(), doc));
		}

		if (this.fileID != null) {
			commentElement.appendChild(XMLUtils.createElement("fileID", fileID.toString(), doc));
		}

		if (this.comment != null) {
			commentElement.appendChild(XMLUtils.createCDATAElement("comment", comment.toString(), doc));
		}

		if (this.date != null) {
			commentElement.appendChild(XMLUtils.createElement("date", DateUtils.DATE_TIME_FORMATTER.format(date), doc));
		}

		if (this.user != null) {
			commentElement.appendChild(XMLUtils.createCDATAElement("user", user.getFirstname() + " " + user.getLastname(), doc));
		}

		return commentElement;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.comment, 30, "...") + " (ID: " + this.commentID + ")";
	}
}
