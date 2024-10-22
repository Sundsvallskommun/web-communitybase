package se.dosf.communitybase.modules.forum.beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class Forum {

	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd");

	private Integer forumID;

	@WebPopulate(required=true,maxLength=255)
	private String name;

	@WebPopulate(required=true,maxLength=65000)
	private String description;

	private Integer groupID;
	private Timestamp date;
	private Integer threadID;
	private ArrayList<Thread> threads;

	public void setForumID(Integer forumID) {
		this.forumID = forumID;
	}

	public Integer getForumID() {
		return forumID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.name.replace("\n", " "), 30, "...") + " (ID: " + this.forumID + ")";
	}

	public void setThreads(ArrayList<Thread> threads) {
		this.threads = threads;
	}

	public ArrayList<Thread> getThreads() {
		return threads;
	}

	public void setThreadID(Integer threadID) {
		this.threadID = threadID;
	}

	public Integer getThreadID() {
		return threadID;
	}

	public Element toXML(Document doc) {

		Element communityElement = doc.createElement("community");

		if (this.getForumID() != null) {
			communityElement.appendChild(XMLUtils.createElement("forumID", this.forumID.toString(), doc));
		}

		if (this.getName() != null) {
			communityElement.appendChild(XMLUtils.createCDATAElement("name", this.name, doc));
		}

		if (this.getGroupID() != null) {
			communityElement.appendChild(XMLUtils.createElement("groupID", this.groupID.toString(), doc));
		}

		if (this.getDate() != null) {
			communityElement.appendChild(XMLUtils.createElement("date", dateTimeFormat.format(getDate()), doc));
		}

		if (this.getDescription() != null) {
			communityElement.appendChild(XMLUtils.createCDATAElement("description", this.getDescription().toString(), doc));
		}

		if (this.getUrl() != null) {
			communityElement.appendChild(XMLUtils.createCDATAElement("url", this.getUrl().toString(), doc));
		}

		if (this.getThreads() != null) {
			Element threadElement = doc.createElement("Threads");
			for (Thread thread : threads) {
				threadElement.appendChild(thread.toXML(doc));
				communityElement.appendChild(threadElement);
			}
		}

		return communityElement;
	}

	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
