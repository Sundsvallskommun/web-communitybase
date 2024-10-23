package se.dosf.communitybase.modules.forum.beans;

import java.sql.Timestamp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class Post {

	private Integer postID;
	private Integer threadID;

	@WebPopulate(required=true,maxLength=65000)
	private String message;
	private CommunityUser poster;
	private CommunityUser changer;
	private Integer posterID;
	private Integer changerID;
	private Timestamp changed;
	private Timestamp posted;

	public void setPostID(Integer postID) {
		this.postID = postID;
	}

	public Integer getPostID() {
		return postID;
	}

	public void setThreadID(Integer threadID) {
		this.threadID = threadID;
	}

	public Integer getThreadID() {
		return threadID;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setPosterID(Integer posterID) {
		this.posterID = posterID;
	}

	public Integer getPosterID() {
		return posterID;
	}

	public void setChangerID(Integer changerID) {
		this.changerID = changerID;
	}

	public Integer getChangerID() {
		return changerID;
	}

	public void setChanged(Timestamp changedDate) {
		this.changed = changedDate;
	}

	public Timestamp getChanged() {
		return changed;
	}

	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}

	public Timestamp getPosted() {
		return posted;
	}

	public void setPoster(CommunityUser poster) {
		this.poster = poster;
	}

	public CommunityUser getPoster() {
		return poster;
	}

	public void setChanger(CommunityUser changer) {
		this.changer = changer;
	}

	public CommunityUser getChanger() {
		return changer;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.message.replace("\n", " "), 30, "...") + " (ID: " + this.postID + ")";
	}

	public Element toXML(Document doc) {

		Element forumPostElement = doc.createElement("post");
		Element postedByElement = doc.createElement("postedBy");
		Element changed = doc.createElement("changedBy");

		if (this.getPostID() != null) {
			forumPostElement.appendChild(XMLUtils.createElement("postID", this.postID.toString(), doc));
		}

		if (this.getPosterID() != null) {
			forumPostElement.appendChild(XMLUtils.createElement("posterID", this.posterID.toString(), doc));
		}

		if (this.getThreadID() != null) {
			forumPostElement.appendChild(XMLUtils.createElement("threadID", this.threadID.toString(), doc));
		}

		if (this.getMessage() != null) {
			forumPostElement.appendChild(XMLUtils.createCDATAElement("message", this.message, doc));
		}

		if (this.getChanged() != null) {

			changed.appendChild(XMLUtils.createElement("changedDate", DateUtils.DATE_FORMATTER.format(getChanged()), doc));
			changed.appendChild(XMLUtils.createElement("changedTime", TimeUtils.TIME_FORMATTER.format(getChanged()), doc));

			if(this.getChanger() != null){
				changed.appendChild(this.getChanger().toXML(doc));
			}

			forumPostElement.appendChild(changed);
		}

		if (this.getPosted() != null) {

			if(this.getPoster() != null){
				postedByElement.appendChild(this.getPoster().toXML(doc));
			}

			postedByElement.appendChild(XMLUtils.createElement("postedDate", DateUtils.DATE_FORMATTER.format(getPosted()), doc));
			postedByElement.appendChild(XMLUtils.createElement("postedTime", TimeUtils.TIME_FORMATTER.format(getPosted()), doc));

			forumPostElement.appendChild(postedByElement);
		}

		return forumPostElement;
	}

}
