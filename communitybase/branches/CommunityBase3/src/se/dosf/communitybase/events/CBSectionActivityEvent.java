package se.dosf.communitybase.events;

import java.io.Serializable;

public class CBSectionActivityEvent implements Serializable {

	private static final long serialVersionUID = -2640180846668221245L;
	
	private Integer sectionID;

	public CBSectionActivityEvent(Integer sectionID) {

		this.sectionID = sectionID;
	}

	public Integer getSectionID() {

		return sectionID;
	}
}
