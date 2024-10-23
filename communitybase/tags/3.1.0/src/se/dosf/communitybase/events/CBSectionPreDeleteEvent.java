package se.dosf.communitybase.events;

import java.io.Serializable;


/** This event is used to signal modules regarding sections marked for deletion.
 *
 * @author Exuvo */
public class CBSectionPreDeleteEvent implements Serializable{

	private static final long serialVersionUID = 2213985425003782786L;

	private Integer sectionID;

	public CBSectionPreDeleteEvent(Integer sectionID) {

		this.sectionID = sectionID;
	}

	public Integer getSectionID() {

		return sectionID;
	}

}
