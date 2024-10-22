package se.dosf.communitybase.modules.search;

import java.util.List;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class UserHit extends GeneratedElementable {

	@XMLElement(fixCase = true)
	private final Integer userID;

	@XMLElement(fixCase = true)
	private final String fullName;

	@XMLElement(fixCase = true)
	private final String email;

	@XMLElement(fixCase = true)
	private final String organization;

	@XMLElement(fixCase = true)
	private final boolean isExternal;

	@XMLElement(fixCase = true, childName = "Attribute")
	private final List<String> attributes;

	public UserHit(Integer userID, String fullName, String email, String organization, boolean isExternal, List<String> attributes) {

		super();
		this.userID = userID;
		this.fullName = fullName;
		this.email = email;
		this.organization = organization;
		this.isExternal = isExternal;
		this.attributes = attributes;
	}

	public Integer getUserID() {

		return userID;
	}

	public String getFullName() {

		return fullName;
	}

	public String getEmail() {

		return email;
	}

	public String getOrganization() {

		return organization;
	}

	public boolean isExternal() {

		return isExternal;
	}

	public List<String> getAttributes() {

		return attributes;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((userID == null) ? 0 : userID.hashCode());
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
		UserHit other = (UserHit) obj;
		if (userID == null) {
			if (other.userID != null)
				return false;
		} else if (!userID.equals(other.userID))
			return false;
		return true;
	}
}