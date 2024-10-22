package se.dosf.communitybase.beans;

import java.util.Comparator;
import java.util.Date;

import se.dosf.communitybase.modules.calendar.beans.CalendarPost;

public class SortCalendarComparator implements Comparator<CalendarPost> {

	@Override
	public int compare(CalendarPost one, CalendarPost two) {
		
		Date oneDate = new Date(one.getDate().getTime());
		Date twoDate = new Date(one.getDate().getTime());
		
		if(oneDate.getTime() == twoDate.getTime()){
			if(one.getStartTime().before(two.getStartTime())){
				return -1;
			}
			
			return 1;
		}else if(oneDate.getTime() > twoDate.getTime()){
			return -1;
		}
		
		return 1;

	}

}
