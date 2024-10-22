package se.dosf.communitybase.events;

import java.io.Serializable;

import se.unlogic.hierarchy.core.beans.User;

public class CBSectionAccessRequestEvent implements Serializable{

	private static final long serialVersionUID = 1252142633035926448L;
	
	private final Integer sectionID;
	private final User user;

	public CBSectionAccessRequestEvent(Integer sectionID, User user) {

		super();
		this.sectionID = sectionID;
		this.user = user;
	}

	public User getUser() {

		return user;
	}

	public Integer getSectionID() {

		return sectionID;
	}
}