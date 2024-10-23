package se.dosf.communitybase;

import java.util.Comparator;

import se.dosf.communitybase.beans.SectionEvent;


public class SectionEventComparator implements Comparator<SectionEvent>{

	@Override
	public int compare(SectionEvent o1, SectionEvent o2) {

		return o2.getTimestamp().compareTo(o1.getTimestamp());
	}

}
