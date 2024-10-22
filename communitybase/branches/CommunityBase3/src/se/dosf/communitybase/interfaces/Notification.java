package se.dosf.communitybase.interfaces;

import java.sql.Timestamp;

import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;

public interface Notification {

	public Integer getNotificationID();

	public Integer getSectionID();

	public Integer getSourceModuleID();

	public Integer getUserID();

	public Timestamp getAdded();

	public boolean isEnabled();

	public boolean isRead();

	public boolean isShown();
	
	public String getNotificationType();

	public Integer getExternalNotificationID();

	public AttributeHandler getAttributeHandler();

}