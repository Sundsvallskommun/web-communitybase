package se.dosf.communitybase.modules.absence.beans;

import java.util.Comparator;

import se.unlogic.standardutils.dao.enums.Order;

public class AbsenceGroupComparator implements Comparator<Absence> {

	private static final AbsenceGroupComparator ASC_COMPARATOR = new AbsenceGroupComparator(Order.ASC);
	private static final AbsenceGroupComparator DESC_COMPARATOR = new AbsenceGroupComparator(Order.DESC);
	
	private Order orderBy;
	
	public AbsenceGroupComparator(Order orderBy) {
		this.orderBy = orderBy;
	}
	
	public int compare(Absence o1, Absence o2) {
		
		if(orderBy.equals(Order.ASC)) {
			return o1.getGroup().getName().compareTo(o2.getGroup().getName());
		} else {
			return o2.getGroup().getName().compareTo(o1.getGroup().getName());
		}
		
	}

	public static AbsenceGroupComparator getInstance(Order order) {
		
		if(order.equals(Order.ASC)) {
			return ASC_COMPARATOR;
		}
		
		return DESC_COMPARATOR;
	}

}
