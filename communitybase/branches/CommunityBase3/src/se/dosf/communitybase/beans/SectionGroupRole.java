package se.dosf.communitybase.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "communitybase_section_group_role")
@XMLElement
public class SectionGroupRole extends GeneratedElementable {

	@Key
	@DAOManaged
	@XMLElement
	private Integer sectionID;

	@Key
	@DAOManaged
	@XMLElement
	private Integer groupID;

	@DAOManaged
	@XMLElement
	private Integer roleID;

	public SectionGroupRole() {

	}

	public SectionGroupRole(Integer sectionID, Integer groupID, Integer roleID) {

		this.sectionID = sectionID;
		this.groupID = groupID;
		this.roleID = roleID;
	}

	@Override
	public String toString() {

		return "SectionGroupRole (sectionID: " + sectionID + ", groupID: " + groupID + ", roleID: " + roleID + ")";
	}

	public Integer getSectionID() {

		return sectionID;
	}

	public void setSectionID(Integer sectionID) {

		this.sectionID = sectionID;
	}

	public Integer getGroupID() {

		return groupID;
	}

	public void setGroupID(Integer groupID) {

		this.groupID = groupID;
	}

	public Integer getRoleID() {

		return roleID;
	}

	public void setRoleID(Integer roleID) {

		this.roleID = roleID;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
		result = prime * result + ((sectionID == null) ? 0 : sectionID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SectionGroupRole other = (SectionGroupRole) obj;
		if (groupID == null) {
			if (other.groupID != null)
				return false;
		} else if (!groupID.equals(other.groupID))
			return false;
		if (sectionID == null) {
			if (other.sectionID != null)
				return false;
		} else if (!sectionID.equals(other.sectionID))
			return false;
		return true;
	}
}