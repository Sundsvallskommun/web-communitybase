package se.dosf.communitybase.beans;

import java.util.Comparator;

import se.unlogic.standardutils.enums.Order;

public class SchoolComparator implements Comparator<School> {

	private static final SchoolComparator ASC_COMPARATOR = new SchoolComparator(Order.ASC);
	private static final SchoolComparator DESC_COMPARATOR = new SchoolComparator(Order.DESC);
	
	private Order orderBy;
	
	public SchoolComparator(Order orderBy) {
		this.orderBy = orderBy;
	}
	
	@Override
	public int compare(School o1, School o2) {
		
		int result = o1.getName().compareTo(o2.getName());
		
		return orderBy.equals(Order.ASC) ? result : result * -1;
	}

	public static SchoolComparator getInstance(Order order) {
		
		if(order.equals(Order.ASC)) {
			return ASC_COMPARATOR;
		}
		
		return DESC_COMPARATOR;
	}

}
