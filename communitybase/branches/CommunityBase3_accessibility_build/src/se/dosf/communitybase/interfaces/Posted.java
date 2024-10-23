package se.dosf.communitybase.interfaces;

import java.sql.Timestamp;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.xml.Elementable;

public interface Posted extends Elementable {

	public User getPoster();

	public Timestamp getPosted();

	public User getEditor();

	public Timestamp getUpdated();

	public void setPosted(Timestamp posted);

	public void setPoster(User poster);

	public void setUpdated(Timestamp updated);

	public void setEditor(User editor);

}
