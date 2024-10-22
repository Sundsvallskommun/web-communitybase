package se.dosf.communitybase.beans;

import java.sql.Timestamp;

import se.unlogic.standardutils.dao.annotations.DAOManaged;


public class SimpleEvent extends Event{

	@DAOManaged
	private String title;

	@DAOManaged
	private String description;

	@DAOManaged
	private Timestamp added;

	private String fullAlias;

	public SimpleEvent(String title, String description, Timestamp added, String url) {
		super();
		this.title = title;
		this.description = description;
		this.added = added;
		this.fullAlias = url;
	}

	public SimpleEvent(String title, String description, Timestamp added) {
		super();
		this.title = title;
		this.description = description;
		this.added = added;
	}

	public SimpleEvent(){}


	@Override
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	@Override
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public Timestamp getAdded() {
		return added;
	}


	public void setAdded(Timestamp timestamp) {
		this.added = timestamp;
	}


	@Override
	public String getFullAlias() {
		return fullAlias;
	}


	public void setFullAlias(String url) {
		this.fullAlias = url;
	}
}
