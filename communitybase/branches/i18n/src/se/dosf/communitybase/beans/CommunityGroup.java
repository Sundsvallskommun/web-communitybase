package se.dosf.communitybase.beans;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.interfaces.Group;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLable;

public class CommunityGroup implements Group, XMLable {

	private static final long serialVersionUID = 8659200293579096188L;
	private Integer groupID;
	private String name;
	private String email;
	private School school;
	private ArrayList<CommunityUser> users;

	public void setUsers(ArrayList<CommunityUser> users) {
		this.users = users;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getDescription() {
		return this.name;
	}

	public Integer getGroupID() {
		return groupID;
	}

	public String getName() {
		return name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public ArrayList<CommunityUser> getUsers() {
		return this.users;
	}

	public boolean isEnabled() {
		return true;
	}

	public void setGroupID(Integer groupID) {
		this.groupID = groupID;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.name, 30, "...") + " (ID: " + this.groupID + ")";
	}

	public Element toXML(Document doc) {
		Element cGroups = doc.createElement("group");

		if (this.getGroupID() != null) {
			cGroups.appendChild(XMLUtils.createElement("groupID", this.getGroupID().toString(), doc));
		}

		if (this.getName() != null) {
			cGroups.appendChild(XMLUtils.createCDATAElement("name", this.getName(), doc));
		}

		if(email != null) {
			cGroups.appendChild(XMLUtils.createCDATAElement("email", email, doc));
		}
		
		cGroups.appendChild(XMLUtils.createCDATAElement("enabled", "true", doc));

		if (this.getSchool() != null) {
			cGroups.appendChild(this.getSchool().toXML(doc));
		}

		return cGroups;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
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
		CommunityGroup other = (CommunityGroup) obj;
		if (groupID == null) {
			if (other.groupID != null) {
				return false;
			}
		} else if (!groupID.equals(other.groupID)) {
			return false;
		}
		return true;
	}

}
