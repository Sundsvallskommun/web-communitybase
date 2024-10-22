package se.dosf.communitybase.beans;

import java.util.Collections;
import java.util.List;

import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.interfaces.SectionType;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.enums.EnumUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "communitybase_section_type_add_access")
@XMLElement
public class AddSectionType extends GeneratedElementable {

	@DAOManaged
	@Key
	private Integer sectionTypeID;

	@DAOManaged
	@Key
	@XMLElement
	private Integer groupID;

	@DAOManaged
	@XMLElement
	private String accessModes;

	@DAOManaged(columnName = "sectionTypeID")
	@ManyToOne
	private SimpleSectionType sectionType;

	public Integer getSectionTypeID() {

		return sectionTypeID;
	}

	public void setSectionTypeID(Integer sectionTypeID) {

		this.sectionTypeID = sectionTypeID;
	}

	public Integer getGroupID() {

		return groupID;
	}

	public void setGroupID(Integer groupID) {

		this.groupID = groupID;
	}

	public List<SectionAccessMode> getAccessModes() {

		if (StringUtils.isEmpty(accessModes)) {
			return Collections.emptyList();
		}

		return EnumUtils.toEnum(SectionAccessMode.class, accessModes.split(","));
	}

	public void setAccessModes(List<SectionAccessMode> accessModes) {

		StringBuilder b = new StringBuilder();

		if (!CollectionUtils.isEmpty(accessModes)) {

			for (SectionAccessMode access : accessModes) {
				b.append(access.name());
			}
		}

		this.accessModes = b.toString();
	}

	public SectionType getSectionType() {

		return sectionType;
	}

	public void setSectionType(SimpleSectionType simpleSectionType) {

		this.sectionType = simpleSectionType;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessModes == null) ? 0 : accessModes.hashCode());
		result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
		result = prime * result + ((sectionTypeID == null) ? 0 : sectionTypeID.hashCode());
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
		AddSectionType other = (AddSectionType) obj;
		if (accessModes == null) {
			if (other.accessModes != null)
				return false;
		} else if (!accessModes.equals(other.accessModes))
			return false;
		if (groupID == null) {
			if (other.groupID != null)
				return false;
		} else if (!groupID.equals(other.groupID))
			return false;
		if (sectionTypeID == null) {
			if (other.sectionTypeID != null)
				return false;
		} else if (!sectionTypeID.equals(other.sectionTypeID))
			return false;
		return true;
	}

}
