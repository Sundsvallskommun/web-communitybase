package se.dosf.communitybase.beans;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "communitybase_section_accessrequests")
@XMLElement(name = "AccessRequest")
public class SectionAccessRequest extends GeneratedElementable {

	@Key
	@DAOManaged
	@XMLElement
	private Integer sectionID;

	@Key
	@DAOManaged(columnName = "userID")
	@XMLElement
	private User user;

	@DAOManaged
	@XMLElement
	private String comment;
	
	@Override
	public String toString() {
		
		return "User " + user + " for section " + sectionID;
	}

	public Integer getSectionID() {

		return sectionID;
	}

	public void setSectionID(Integer sectionID) {

		this.sectionID = sectionID;
	}

	public User getUser() {

		return user;
	}

	public void setUser(User user) {

		this.user = user;
	}

	public String getComment() {

		return comment;
	}

	public void setComment(String comment) {

		this.comment = comment;
	}
}