package se.dosf.communitybase.beans;

import java.io.Serializable;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLable;

public class School implements Serializable, Elementable, XMLable {

	private static final long serialVersionUID = 1L;
	private Integer schoolID;
	private String name;
	private ArrayList<CommunityGroup> groups;
	private ArrayList<CommunityUser> admins;

	public School(Integer schoolID, String name) {
		this.schoolID = schoolID;
		this.name = name;
	}

	public Integer getSchoolID() {
		return schoolID;
	}

	public String getName() {
		return name;
	}

	public void setSchoolID(Integer schoolID) {
		this.schoolID = schoolID;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.name, 30, "...") + " (ID: " + this.schoolID + ")";
	}

	public Element toXML(Document doc) {

		Element schoolElement = doc.createElement("school");

		if (this.getSchoolID() != null) {
			schoolElement.appendChild(XMLUtils.createElement("schoolID", this.getSchoolID().toString(), doc));
		}

		if (this.getName() != null) {
			schoolElement.appendChild(XMLUtils.createCDATAElement("name", this.getName().toString(), doc));
		}

		XMLUtils.append(doc, schoolElement, "groups", this.groups);
		XMLUtils.append(doc, schoolElement, "admins", this.admins);

		return schoolElement;

	}

	public ArrayList<CommunityUser> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<CommunityUser> admins) {
		this.admins = admins;
	}

	/**
	 * @return the groups
	 */
	public ArrayList<CommunityGroup> getGroups() {
		return groups;
	}

	/**
	 * @param groups
	 *            the groups to set
	 */
	public void setGroups(ArrayList<CommunityGroup> groups) {
		this.groups = groups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
		+ ((schoolID == null) ? 0 : schoolID.hashCode());
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
		School other = (School) obj;
		if (schoolID == null) {
			if (other.schoolID != null) {
				return false;
			}
		} else if (!schoolID.equals(other.schoolID)) {
			return false;
		}
		return true;
	}
}
