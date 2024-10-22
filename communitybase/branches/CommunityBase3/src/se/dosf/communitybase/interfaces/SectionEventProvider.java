package se.dosf.communitybase.interfaces;

import java.sql.Timestamp;
import java.util.List;

import se.dosf.communitybase.beans.SectionEvent;


public interface SectionEventProvider {

	public List<SectionEvent> getEvents(Timestamp breakpoint, int count) throws Exception;
}
