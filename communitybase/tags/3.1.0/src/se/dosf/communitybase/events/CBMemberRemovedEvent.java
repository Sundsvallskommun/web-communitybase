package se.dosf.communitybase.events;

import java.io.Serializable;

import se.dosf.communitybase.enums.SectionAccessMode;

/** This event is used to signal modules regarding users that have had their section member status revoked.
 *
 * @author Exuvo */
public class CBMemberRemovedEvent implements Serializable {

	private static final long serialVersionUID = 2213985425003782786L;

	private Integer userID;
	private Integer sectionID;
	private SectionAccessMode sectionAccessMode;

	public CBMemberRemovedEvent(Integer userID, Integer sectionID, SectionAccessMode sectionAccessMode) {

		this.userID = userID;
		this.sectionID = sectionID;
		this.sectionAccessMode = sectionAccessMode;
	}

	public Integer getUserID() {

		return userID;
	}

	public Integer getSectionID() {

		return sectionID;
	}

	public SectionAccessMode getSectionAccessMode() {

		return sectionAccessMode;
	}

}
