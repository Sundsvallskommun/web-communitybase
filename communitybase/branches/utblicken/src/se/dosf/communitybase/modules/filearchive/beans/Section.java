package se.dosf.communitybase.modules.filearchive.beans;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class Section {

	private Integer sectionID;
	private Integer groupID;
	private String name;

	public void setSectionID(Integer sectionID) {
		this.sectionID = sectionID;
	}

	public Integer getSectionID() {
		return sectionID;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.name, 30, "...") + " (ID: " + this.sectionID + ")";
	}

	public Element toXML(Document doc) {

		Element sectionElement = doc.createElement("section");

		if (this.getSectionID() != null) {
			sectionElement.appendChild(XMLUtils.createElement("sectionID", this.sectionID.toString(), doc));
			if (this.getName() != null) {
				sectionElement.appendChild(XMLUtils.createCDATAElement("sectionName", this.name, doc));
			}
		} else {
			Element noSectionElement = doc.createElement("noSection");
			sectionElement.appendChild(noSectionElement);
		}

		return sectionElement;
	}

}
