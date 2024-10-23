package se.dosf.communitybase.modules.invitation.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name="communitybase_invitation_section_invitations")
@XMLElement
public class SectionInvitation extends GeneratedElementable {

	public static final String SECTION_INVITATIONS_TABLE = SectionInvitation.class.getAnnotation(Table.class).name();
	
	@Key
	@DAOManaged
	@XMLElement
	private Integer sectionID;

	@Key
	@ManyToOne
	@DAOManaged(columnName="invitationID")
	@XMLElement
	private Invitation invitation;
	
	@DAOManaged
	@XMLElement
	private Integer roleID;

	public Integer getSectionID() {

		return sectionID;
	}

	public void setSectionID(Integer sectionID) {

		this.sectionID = sectionID;
	}
	
	public Invitation getInvitation() {
		
		return invitation;
	}

	
	public void setInvitation(Invitation invitation) {
	
		this.invitation = invitation;
	}

	public Integer getRoleID() {

		return roleID;
	}

	public void setRoleID(Integer roleID) {

		this.roleID = roleID;
	}

}
