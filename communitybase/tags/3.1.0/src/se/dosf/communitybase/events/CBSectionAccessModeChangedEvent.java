package se.dosf.communitybase.events;

import java.io.Serializable;

import se.dosf.communitybase.enums.SectionAccessMode;


/** This event is used to signal modules regarding sections marked for deletion.
 *
 * @author Exuvo */
public class CBSectionAccessModeChangedEvent implements Serializable{

	private static final long serialVersionUID = 2213985425003782786L;

	private final Integer sectionID;
	private final SectionAccessMode previousAccessMode;
	private final SectionAccessMode currentAccessMode;

	public CBSectionAccessModeChangedEvent(Integer sectionID, SectionAccessMode previousAccessMode, SectionAccessMode currentAccessMode) {

		super();
		this.sectionID = sectionID;
		this.previousAccessMode = previousAccessMode;
		this.currentAccessMode = currentAccessMode;
	}

	public SectionAccessMode getPreviousAccessMode() {

		return previousAccessMode;
	}

	public SectionAccessMode getCurrentAccessMode() {

		return currentAccessMode;
	}

	public Integer getSectionID() {

		return sectionID;
	}

}
