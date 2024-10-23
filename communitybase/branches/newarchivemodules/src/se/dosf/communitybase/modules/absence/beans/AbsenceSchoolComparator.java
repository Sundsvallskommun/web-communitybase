package se.dosf.communitybase.modules.absence.beans;

import java.util.Comparator;

import se.unlogic.standardutils.enums.Order;

public class AbsenceSchoolComparator implements Comparator<Absence> {

	private static final AbsenceSchoolComparator ASC_COMPARATOR = new AbsenceSchoolComparator(Order.ASC);
	private static final AbsenceSchoolComparator DESC_COMPARATOR = new AbsenceSchoolComparator(Order.DESC);
	
	private Order orderBy;
	
	public AbsenceSchoolComparator(Order orderBy) {
		this.orderBy = orderBy;
	}
	
	@Override
	public int compare(Absence o1, Absence o2) {
		
		if(orderBy.equals(Order.ASC)) {
			return o1.getGroup().getSchool().getName().compareTo(o2.getGroup().getSchool().getName());
		} else {
			return o2.getGroup().getSchool().getName().compareTo(o1.getGroup().getSchool().getName());
		}
		
	}
	
	public static AbsenceSchoolComparator getInstance(Order order) {
		
		if(order.equals(Order.ASC)) {
			return ASC_COMPARATOR;
		}
		
		return DESC_COMPARATOR;
	}

}
