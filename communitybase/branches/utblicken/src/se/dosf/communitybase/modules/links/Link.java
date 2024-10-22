package se.dosf.communitybase.modules.links;

import java.sql.Timestamp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.enums.LinkType;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public class Link implements Elementable {

	private Timestamp date; // posted

	@WebPopulate(required=true,maxLength=5000)
	private String description;

	@WebPopulate(required=true,maxLength=1000,populatorID="url")
	private String url;

	private CommunityUser postedBy;
	private Integer linkID;
	private LinkType linkType;


	public Integer getLinkID() {
		return linkID;
	}


	public void setLinkID(Integer linkID) {
		this.linkID = linkID;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Timestamp getDate() {
		return this.date;
	}

	public String getDescription() {
		return this.description;
	}

	public String getUrl() {
		return this.url;
	}

	public LinkType getLinkType() {
		return linkType;
	}

	public void setLinkType(LinkType linkType) {
		this.linkType = linkType;
	}

	public void setPostedBy(CommunityUser postedBy) {
		this.postedBy = postedBy;
	}


	public CommunityUser getPostedBy() {
		return postedBy;
	}


	@Override
	public String toString() {
		return StringUtils.substring(this.url.replace("\n", ""), 30, "...") + " (ID: " + this.linkID + ")";
	}


	public Element toXML(Document doc) {

		Element linkElement = doc.createElement("link");
		Element postedBy = doc.createElement("postedBy");

		if (this.getDate() != null) {
			linkElement.appendChild(XMLUtils.createElement("posted", DateUtils.DATE_TIME_FORMATTER.format(date), doc));


			linkElement.appendChild(XMLUtils.createCDATAElement("postedInMillis", String.valueOf(this.date.getTime()), doc));
		}

		if (this.getDescription() != null) {
			linkElement.appendChild(XMLUtils.createCDATAElement("description", this.getDescription(), doc));
		}

		if (this.getUrl() != null) {
			linkElement.appendChild(XMLUtils.createCDATAElement("url", this.getUrl(), doc));
		}

		if (this.getLinkID() != null) {
			linkElement.appendChild(XMLUtils.createElement("linkID", this.getLinkID().toString(), doc));
		}

		if (this.linkType != null) {
			linkElement.appendChild(XMLUtils.createElement("linkType", linkType.toString(), doc));
		}

		if(this.getPostedBy() != null){
			postedBy.appendChild(this.getPostedBy().toXML(doc));
			linkElement.appendChild(postedBy);
		}

		return linkElement;
	}

}
