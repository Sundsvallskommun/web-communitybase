package se.dosf.communitybase.utils;

import java.util.Comparator;

import se.dosf.communitybase.beans.IdentifiedEvent;


public class EventComparator implements Comparator<IdentifiedEvent> {

	private static final EventComparator COMPARATOR = new EventComparator();

	@Override
	public int compare(IdentifiedEvent o1, IdentifiedEvent o2) {

		return o1.getAdded().compareTo(o2.getAdded());
	}

	public static EventComparator getComparator(){

		return COMPARATOR;
	}
}
