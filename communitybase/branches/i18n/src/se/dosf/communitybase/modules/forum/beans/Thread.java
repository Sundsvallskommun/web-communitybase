package se.dosf.communitybase.modules.forum.beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class Thread {

	private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat time = new SimpleDateFormat("HH:mm");

	private ArrayList<Post> posts;
	private ArrayList<Post> forumThreads;
	private Integer threadID;
	private Integer forumID;

	@WebPopulate(required=true,maxLength=255)
	private String subject;

	@WebPopulate(required=true,maxLength=65000)
	private String message;

	private CommunityUser poster;
	private CommunityUser changer;
	private Integer posterID;
	private Timestamp changed;
	private Integer changerID;
	private Timestamp posted;
	private CommunityUser user;

	public Integer getForumID() {
		return forumID;
	}
	public String getSubject() {
		return subject;
	}
	public Timestamp getChanged() {
		return changed;
	}
	public void setChanged(Timestamp changed) {
		this.changed = changed;
	}
	public String getMessage() {
		return message;
	}
	public Integer getPosterID() {
		return posterID;
	}

	public Integer getChangerID() {
		return changerID;
	}
	public Timestamp getPosted() {
		return posted;
	}

	public void setForumID(Integer forumID) {
		this.forumID = forumID;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public void setPosterID(Integer posterID) {
		this.posterID = posterID;
	}

	public void setChangerID(Integer changerID) {
		this.changerID = changerID;
	}
	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}

	public ArrayList<Post> getForumThreads() {
		return forumThreads;
	}
	public void setForumThreads(ArrayList<Post> forumThreads) {
		this.forumThreads = forumThreads;
	}
	public void setThreadID(Integer threadID) {
		this.threadID = threadID;
	}
	public Integer getThreadID() {
		return threadID;
	}
	public void setPosts(ArrayList<Post> posts) {
		this.posts = posts;
	}
	public ArrayList<Post> getPosts() {
		return posts;
	}
	public void setUser(CommunityUser user) {
		this.user = user;
	}
	public CommunityUser getUser() {
		return user;
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
		return StringUtils.substring(this.subject.replace("\n", " "), 30, "...") + " (ID: " + this.threadID + ")";
	}

	public Element toXML(Document doc){
		Element forumThreadElement = doc.createElement("thread");
		Element postedByElement = doc.createElement("postedBy");
		Element changedByElement = doc.createElement("changedBy");

		if(this.getThreadID() != null){
			forumThreadElement.appendChild(XMLUtils.createElement("forumThreadID", this.threadID.toString(), doc));
		}

		if(this.getForumID() != null){
			forumThreadElement.appendChild(XMLUtils.createElement("forumID", this.forumID.toString(), doc));
		}

		if(this.getPosterID() != null){
			forumThreadElement.appendChild(XMLUtils.createElement("posterID", this.posterID.toString(), doc));
		}

		if(this.getSubject() != null){
			forumThreadElement.appendChild(XMLUtils.createElement("subject", this.subject, doc));
		}

		if(this.getMessage() != null){
			forumThreadElement.appendChild(XMLUtils.createElement("message", this.message, doc));
		}

		if(this.getChanged() != null){

			if(this.getChanger() != null){
				changedByElement.appendChild(this.getChanger().toXML(doc));
			}

			changedByElement.appendChild(XMLUtils.createElement("changedDate", date.format(getChanged()), doc));
			changedByElement.appendChild(XMLUtils.createElement("changedTime", time.format(getChanged()), doc));

			forumThreadElement.appendChild(changedByElement);
		}

		if(this.getPosted() != null){

			if(this.getPoster() != null){
				postedByElement.appendChild(this.getPoster().toXML(doc));
			}

			postedByElement.appendChild(XMLUtils.createElement("postedDate", date.format(getPosted()), doc));
			postedByElement.appendChild(XMLUtils.createElement("postedTime", time.format(getPosted()), doc));

			forumThreadElement.appendChild(postedByElement);
		}

		return forumThreadElement;
	}

}
