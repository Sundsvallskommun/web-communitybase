package se.dosf.communitybase.modules.sectionevents;

import se.dosf.communitybase.beans.SectionEvent;


public interface EventFilter {

	/**
	 * @param sectionEvent
	 * @return true if the event should be deleted from the event queue
	 */
	public boolean deleteEvent(SectionEvent sectionEvent);
}
