package se.dosf.communitybase.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "communitybase_section_favourites")
@XMLElement
public class SectionFavourite extends GeneratedElementable {

	@Key
	@DAOManaged
	@XMLElement
	private Integer sectionID;

	@Key
	@DAOManaged
	@XMLElement
	private Integer userID;

	public SectionFavourite() {}
	
	public SectionFavourite(Integer sectionID, Integer userID) {

		this.sectionID = sectionID;
		this.userID = userID;
	}
	
	public Integer getSectionID() {

		return sectionID;
	}

	public void setSectionID(Integer sectionID) {

		this.sectionID = sectionID;
	}

	public Integer getUserID() {

		return userID;
	}

	public void setUserID(Integer userID) {

		this.userID = userID;
	}

}
