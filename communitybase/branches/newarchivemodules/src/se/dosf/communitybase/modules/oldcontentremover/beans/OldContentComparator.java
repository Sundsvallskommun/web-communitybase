package se.dosf.communitybase.modules.oldcontentremover.beans;

import java.util.Comparator;

import se.unlogic.standardutils.enums.Order;

public class OldContentComparator implements Comparator<OldContent> {

	private static final OldContentComparator ASC_COMPARATOR = new OldContentComparator(Order.ASC);
	private static final OldContentComparator DESC_COMPARATOR = new OldContentComparator(Order.DESC);
	
	private Order orderBy;
	
	public OldContentComparator(Order orderBy) {
		this.orderBy = orderBy;
	}
	
	@Override
	public int compare(OldContent o1, OldContent o2) {
		
		int result = o1.getCreationDate().compareTo(o2.getCreationDate());
		
		return orderBy.equals(Order.ASC) ? result : result * -1;
	}

	public static OldContentComparator getInstance(Order order) {
		
		if(order.equals(Order.ASC)) {
			return ASC_COMPARATOR;
		}
		
		return DESC_COMPARATOR;
	}

}
