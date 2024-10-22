package se.dosf.communitybase.modules.filearchive.beans;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.modules.filearchive.enums.SectionType;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public class Section implements Elementable, Comparable<Section>{

	private Integer sectionID;
	private Integer parentID;
	private String name;
	private SectionType type;
	private List<File> files;

	public void setSectionID(Integer sectionID) {

		this.sectionID = sectionID;
	}

	public Integer getSectionID() {

		return sectionID;
	}

	public void setParentID(Integer parentID) {

		this.parentID = parentID;
	}

	public Integer getParentID() {

		return parentID;
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

			XMLUtils.appendNewElement(doc, sectionElement, "type", type);
			XMLUtils.append(doc, sectionElement, "files", files);

		} else {

			Element noSectionElement = doc.createElement("noSection");
			sectionElement.appendChild(noSectionElement);
		}

		XMLUtils.appendNewElement(doc, sectionElement, "parentID", parentID);

		return sectionElement;
	}

	public SectionType getType() {

		return type;
	}

	public void setType(SectionType type) {

		this.type = type;
	}

	public List<File> getFiles() {

		return files;
	}

	public void setFiles(List<File> files) {

		this.files = files;
	}

	public int compareTo(Section o) {

		return this.getName().compareTo(o.getName());
	}

}
