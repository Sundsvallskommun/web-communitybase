package se.dosf.communitybase.beans;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.enums.GroupAccessLevel;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.xml.XMLUtils;

public class CommunityUser extends User {

	private static final long serialVersionUID = 5571711627678650626L;

	private Integer userID;
	private String email;
	
	@WebPopulate(required=true,maxLength=60)
	private String firstname;

	@WebPopulate(required=true,maxLength=80)
	private String lastname;

	@WebPopulate
	private String phoneHome;

	@WebPopulate
	private String phoneMobile;

	@WebPopulate
	private String phoneWork;

	private Timestamp lastLogin;
	private Timestamp currentLogin;

	@WebPopulate(required=true,minLength=6,maxLength=50)
	private String password;

	private Timestamp lastResume;
	private Integer resume;

	private boolean enabled;

	private boolean admin;

	private Timestamp added;

	private Map<CommunityGroup, GroupRelation> groups;
	private List<School> schools;
	
	@WebPopulate(paramName="languageCode")
	private Language language;

	public CommunityUser() {}

	public String getUserName() {
		return this.email;
	}

	public List<School> getSchools() {
		return schools;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	@Override
	public List<? extends CommunityGroup> getGroups() {

		if (this.groups != null) {
			return new ArrayList<CommunityGroup>(groups.keySet());
		}

		return null;
	}

	public Map<CommunityGroup, GroupRelation> getGroupMap(){
		return this.groups;
	}

	public void removeGroup(CommunityGroup group) {

		if (this.groups != null) {
			this.groups.remove(group);
		}
	}

	public GroupAccessLevel getAccessLevel(CommunityGroup group) {

		GroupRelation groupRelation = this.getGroupRelation(group);

		if (groupRelation != null) {
			return groupRelation.getAccessLevel();
		}

		return null;
	}

	public String getComment(CommunityGroup group) {

		GroupRelation groupRelation = this.getGroupRelation(group);

		if (groupRelation != null) {
			return groupRelation.getComment();
		}

		return null;
	}

	public GroupRelation getGroupRelation(CommunityGroup group) {

		if (this.groups != null) {
			return groups.get(group);
		}

		return null;
	}

	public void setGroups(Map<CommunityGroup, GroupRelation> groups) {
		this.groups = groups;
	}

	public void addGroup(CommunityGroup communityGroup, GroupRelation groupRelation) {

		if (this.groups == null) {
			this.groups = new HashMap<CommunityGroup, GroupRelation>();
		}

		this.groups.put(communityGroup, groupRelation);
	}

	public Integer getResume() {
		return resume;
	}

	public void setResume(Integer resume) {
		this.resume = resume;
	}

	public void setAdded(Timestamp added) {
		this.added = added;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setFirstname(String firstName) {
		this.firstname = firstName;
	}

	public void setLastname(String lastName) {
		this.lastname = lastName;
	}

	public void setLastLogin(Timestamp lastLogin) {
		this.lastLogin = lastLogin;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUserID(Integer userID) {
		this.userID = userID;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public Timestamp getAdded() {
		return added;
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getFirstname() {
		return firstname;
	}

	@Override
	public Timestamp getLastLogin() {
		return lastLogin;
	}

	@Override
	public String getLastname() {
		return lastname;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Integer getUserID() {
		return userID;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAdmin() {
		return admin;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public String getPhoneHome() {
		return phoneHome;
	}

	public void setPhoneHome(String phoneHome) {
		this.phoneHome = phoneHome;
	}

	public String getPhoneMobile() {
		return phoneMobile;
	}

	public void setPhoneMobile(String phoneMobile) {
		this.phoneMobile = phoneMobile;
	}

	public String getPhoneWork() {
		return phoneWork;
	}

	public void setPhoneWork(String phoneWork) {
		this.phoneWork = phoneWork;
	}

	public Timestamp getLastResume() {
		return lastResume;
	}

	public void setLastResume(Timestamp lastResume) {
		this.lastResume = lastResume;
	}

	@Override
	public String toString() {
		return this.getFirstname() + " " + this.getLastname() + " (ID: " + this.getUserID() + ")";
	}

	@Override
	public List<Element> getAdditionalXML(Document doc) {

		// Insert the root element node
		Element communityUserAttributesElement = doc.createElement("communityUserAttributes");

		// Add lastlogin
		if (this.getPhoneHome() != null) {
			communityUserAttributesElement.appendChild(XMLUtils.createCDATAElement("phoneHome", this.getPhoneHome().toString(), doc));
		}

		if (this.getPhoneMobile() != null) {
			communityUserAttributesElement.appendChild(XMLUtils.createCDATAElement("phoneMobile", this.getPhoneMobile().toString(), doc));
		}

		if (this.getPhoneWork() != null) {
			communityUserAttributesElement.appendChild(XMLUtils.createCDATAElement("phoneWork", this.getPhoneWork().toString(), doc));
		}

		if (this.getLastResume() != null) {
			communityUserAttributesElement.appendChild(XMLUtils.createElement("lastResume", this.getLastResume().toString(), doc));
		}

		if(this.getResume() != null) {
			communityUserAttributesElement.appendChild(XMLUtils.createCDATAElement("resume", this.getResume().toString(), doc));
		}
		
		if (groups != null && !this.groups.isEmpty()) {

			Element groupsElement = doc.createElement("groups");

			communityUserAttributesElement.appendChild(groupsElement);

			for (Entry<CommunityGroup, GroupRelation> entry : this.groups.entrySet()) {

				Element groupElement = entry.getKey().toXML(doc);

				groupElement.appendChild(entry.getValue().toXML(doc));

				groupsElement.appendChild(groupElement);
			}
		}

		if(schools != null && !this.schools.isEmpty()){

			Element schoolsElement = doc.createElement("schools");
			communityUserAttributesElement.appendChild(schoolsElement);

			for(School school : schools){
				schoolsElement.appendChild(school.toXML(doc));
			}
		}

		return Collections.singletonList(communityUserAttributesElement);
	}


	@Override
	public Timestamp getCurrentLogin() {
		return currentLogin;
	}


	public void setCurrentLogin(Timestamp currentLogin) {
		this.currentLogin = currentLogin;
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}
	
}
