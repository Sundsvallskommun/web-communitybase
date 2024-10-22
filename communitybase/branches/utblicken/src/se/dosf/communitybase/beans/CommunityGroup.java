package se.dosf.communitybase.beans;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.standardutils.xml.XMLable;

public class CommunityGroup extends Group implements XMLable {

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
	
	@Override
	protected List<Element> getAdditionalXML(Document doc) {
		
		List<Element> elements = new ArrayList<Element>();
		
		if (school != null) {
			elements.add(school.toXML(doc));
		}
		
		if(email != null) {
			elements.add(XMLUtils.createElement("email", email, doc));
		}
		
		return elements;
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupID == null) ? 0 : groupID.hashCode());
		return result;
	}
}
