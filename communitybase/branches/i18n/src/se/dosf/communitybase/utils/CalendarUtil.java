package se.dosf.communitybase.utils;

import java.util.Calendar;

import se.unlogic.standardutils.time.TimeUtils;

public class CalendarUtil {

	private static final String[] monthNames = {"Januari", "Februari",
            "Mars", "April", "Maj", "Juni", "Juli",
            "Augusti", "September", "Oktober", "November", "December" };
	
	private static final String[] dayNames = {"Söndag", "Måndag", "Tisdag",
        "Onsdag", "Torsdag", "Fredag", "Lördag" };
	
	public static String getMonth(Calendar calendar){
		
		return monthNames[calendar.get(Calendar.MONTH)];
				
	}
	
	public static String getDay(Calendar calendar){
		
		return dayNames[calendar.get((Calendar.DAY_OF_WEEK))-1];
				
	}
	
	public static String toFullDateString(Calendar calendar){
		
		return getDay(calendar) + 
				" den " + calendar.get(Calendar.DATE) +
				" " + getMonth(calendar) + 
				" klockan " + TimeUtils.hourAndMinutesToString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
		
	}
	
}
