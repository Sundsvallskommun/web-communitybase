package se.dosf.communitybase.beans;

import java.util.Comparator;

import se.unlogic.standardutils.dao.enums.Order;

public class CommunityGroupComparator implements Comparator<CommunityGroup> {

	private static final CommunityGroupComparator ASC_COMPARATOR = new CommunityGroupComparator(Order.ASC);
	private static final CommunityGroupComparator DESC_COMPARATOR = new CommunityGroupComparator(Order.DESC);
	
	private Order orderBy;
	
	public CommunityGroupComparator(Order orderBy) {
		this.orderBy = orderBy;
	}
	
	public int compare(CommunityGroup o1, CommunityGroup o2) {
		
		int result = o1.getName().compareTo(o2.getName());
		
		if(result == 0) {
			result = o1.getSchool().getName().compareTo(o2.getSchool().getName());
		}
		
		return orderBy.equals(Order.ASC) ? result : result * -1;
	}

	public static CommunityGroupComparator getInstance(Order order) {
		
		if(order.equals(Order.ASC)) {
			return ASC_COMPARATOR;
		}
		
		return DESC_COMPARATOR;
	}

}
