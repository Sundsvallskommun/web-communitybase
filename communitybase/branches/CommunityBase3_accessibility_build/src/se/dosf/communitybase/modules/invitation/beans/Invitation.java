package se.dosf.communitybase.modules.invitation.beans;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "communitybase_invitation_invitations")
@XMLElement
public class Invitation extends GeneratedElementable {

	public static final Field SECTION_INVITATIONS_RELATION = ReflectionUtils.getField(Invitation.class, "sectionInvitations");

	@Key
	@DAOManaged(autoGenerated = true)
	@XMLElement
	private Integer invitationID;

	@DAOManaged
	@XMLElement
	private String email;

	@DAOManaged
	@XMLElement
	private UUID linkID;

	@DAOManaged
	@XMLElement
	private int sendCount;

	@DAOManaged
	@XMLElement
	private Timestamp lastSent;

	@DAOManaged
	@OneToMany(autoAdd = true, autoUpdate = true, autoGet=true)
	@XMLElement
	private List<SectionInvitation> sectionInvitations;

	public Integer getInvitationID() {

		return invitationID;
	}

	public void setInvitationID(Integer invitationID) {

		this.invitationID = invitationID;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}

	public UUID getLinkID() {

		return linkID;
	}

	public void setLinkID(UUID linkID) {

		this.linkID = linkID;
	}

	public int getSendCount() {

		return sendCount;
	}

	public void setSendCount(int sendCount) {

		this.sendCount = sendCount;
	}

	public Timestamp getLastSent() {

		return lastSent;
	}

	public void setLastSent(Timestamp lastSent) {

		this.lastSent = lastSent;
	}

	public List<SectionInvitation> getSectionInvitations() {

		return sectionInvitations;
	}

	public void setSectionInvitations(List<SectionInvitation> sectionInvitations) {

		this.sectionInvitations = sectionInvitations;
	}

	@Override
	public String toString() {

		return this.email + " (ID: " + this.invitationID + ")";
	}
}
