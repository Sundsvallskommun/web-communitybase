package se.dosf.communitybase.beans;

import java.sql.Timestamp;

import se.dosf.communitybase.enums.EventFormat;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public abstract class SectionEvent extends GeneratedElementable {

	private final int moduleID;
	private final Timestamp timestamp;

	public SectionEvent(int moduleID, Timestamp timestamp) {

		this.moduleID = moduleID;
		this.timestamp = timestamp;
	}

	public final int getModuleID() {

		return moduleID;
	}
	public final Timestamp getTimestamp() {

		return timestamp;
	}

	public abstract ViewFragment getFragment(String fullContextPath, EventFormat format) throws Exception;
}
