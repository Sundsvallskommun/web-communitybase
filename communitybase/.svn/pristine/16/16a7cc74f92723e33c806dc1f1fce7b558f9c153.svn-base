package se.dosf.communitybase.beans;

import java.io.Serializable;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.enums.GroupAccessLevel;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public class GroupRelation implements Elementable, Serializable {

	private static final long serialVersionUID = 2874861416333344519L;
	private GroupAccessLevel accessLevel;
	private String comment;

	public GroupRelation(GroupAccessLevel accessLevel, String comment) {
		super();
		this.accessLevel = accessLevel;
		this.comment = comment;
	}

	public GroupRelation() {}

	public GroupAccessLevel getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(GroupAccessLevel accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public Element toXML(Document doc) {

		Element groupRelationElement = doc.createElement("GroupRelation");

		groupRelationElement.appendChild(XMLUtils.createElement("GroupAccessLevel", this.accessLevel.toString(), doc));

		if (comment != null) {
			groupRelationElement.appendChild(XMLUtils.createCDATAElement("Comment", comment, doc));
		}

		return groupRelationElement;
	}

}
