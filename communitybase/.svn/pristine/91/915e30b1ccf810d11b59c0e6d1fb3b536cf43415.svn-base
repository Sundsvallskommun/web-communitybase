package se.dosf.communitybase.beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public class Invitation implements Elementable {

	protected static SimpleDateFormat DATEFORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private Integer invitationID;
	private String email;
	private String senderEmail;
	private Timestamp expires;
	private Timestamp lastSent;
	private Timestamp updated;
	private String linkID;
	private boolean resend;
	private boolean admin;
	private List<School> schools;
	private Map<CommunityGroup, GroupRelation> groups;

	/**
	 * @return the invitationID
	 */
	public Integer getInvitationID() {
		return invitationID;
	}

	/**
	 * @param invitationID
	 *            the invitationID to set
	 */
	public void setInvitationID(Integer invitationID) {
		this.invitationID = invitationID;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the senderEmail
	 */
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	/**
	 * @param senderEmail
	 *            the senderEmail to set
	 */
	public String getSenderEmail() {
		return senderEmail;
	}

	/**
	 * @return the added
	 */
	public Timestamp getExpires() {
		return expires;
	}

	/**
	 * @param added
	 *            the added to set
	 */
	public void setExpires(Timestamp expires) {
		this.expires = expires;
	}

	/**
	 * @return the lastSent
	 */
	public Timestamp getLastSent() {
		return lastSent;
	}

	/**
	 * @param lastSent
	 *            the lastSent to set
	 */
	public void setLastSent(Timestamp lastSent) {
		this.lastSent = lastSent;
	}

	/**
	 * @return the admin
	 */
	public boolean isAdmin() {
		return admin;
	}

	/**
	 * @param admin
	 *            the admin to set
	 */
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	/**
	 * @return the schools
	 */
	public List<School> getSchools() {
		return schools;
	}

	/**
	 * @param schools
	 *            the schools to set
	 */
	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public Map<CommunityGroup, GroupRelation> getGroups() {
		return groups;
	}

	public void setGroups(Map<CommunityGroup, GroupRelation> groups) {
		this.groups = groups;
	}

	public Element toXML(Document doc) {

		Element invitationElement = doc.createElement("invitation");

		XMLUtils.appendNewElement(doc, invitationElement, "invitationID", invitationID);
		XMLUtils.appendNewElement(doc, invitationElement, "email", email);
		XMLUtils.appendNewElement(doc, invitationElement, "expires", DATEFORMATTER.format(expires));

		if(this.senderEmail != null){
			XMLUtils.appendNewElement(doc, invitationElement, "senderEmail", senderEmail);
		}
		
		if(this.updated != null){
			XMLUtils.appendNewElement(doc, invitationElement, "updated", DATEFORMATTER.format(this.updated));
		}

		if(this.lastSent != null){
			XMLUtils.appendNewElement(doc, invitationElement, "lastSent", DATEFORMATTER.format(this.lastSent));
		}

		XMLUtils.appendNewElement(doc, invitationElement, "linkID", linkID);

		if(admin){
			invitationElement.appendChild(doc.createElement("admin"));
		}

		XMLUtils.appendNewElement(doc, invitationElement, "resend", resend);

		if (groups != null && !this.groups.isEmpty()) {

			Element groupsElement = doc.createElement("groups");

			invitationElement.appendChild(groupsElement);

			for (Entry<CommunityGroup, GroupRelation> entry : this.groups.entrySet()) {

				Element groupElement = entry.getKey().toXML(doc);

				groupElement.appendChild(entry.getValue().toXML(doc));

				groupsElement.appendChild(groupElement);
			}
		}

		if(schools != null && !this.schools.isEmpty()){

			Element schoolsElement = doc.createElement("schools");
			invitationElement.appendChild(schoolsElement);

			for(School school : schools){
				schoolsElement.appendChild(school.toXML(doc));
			}
		}

		return invitationElement;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public String getLinkID() {
		return linkID;
	}

	public void setLinkID(String linkID) {
		this.linkID = linkID;
	}

	public boolean isResend() {
		return resend;
	}

	public void setResend(boolean reSend) {
		this.resend = reSend;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.email, 30, "...") + " (ID: " + this.invitationID + ")";
	}
}
