package se.dosf.communitybase.utils;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.time.TimeUtils;

public class CalendarUtil {

	private static final String[] monthNames = {"Januari", "Februari",
            "Mars", "April", "Maj", "Juni", "Juli",
            "Augusti", "September", "Oktober", "November", "December" };
	
	private static final String[] dayNames = {"Söndag", "Måndag", "Tisdag",
        "Onsdag", "Torsdag", "Fredag", "Lördag" };
	
	private static final Map<Language, String> clockLanguages = new HashMap<Language, String>();

	static {
		clockLanguages.put(Language.Swedish, "klockan");
		clockLanguages.put(Language.English, "at");
		clockLanguages.put(Language.Finnish, "at");
	}
	
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
	
	public static final String getWeekDayName(Calendar calendar, Locale locale) {
		
		String dayNames[] = new DateFormatSymbols(locale).getWeekdays();
		
		return dayNames[calendar.get(Calendar.DAY_OF_WEEK)];
		
	}
	
	public static final String getMonthName(Calendar calendar, Locale locale) {
		
		String monthNames[] = new DateFormatSymbols(locale).getMonths();
		
		return monthNames[calendar.get(Calendar.MONTH)];
		
	}
	
	public static String getFullDateTimeString(Calendar calendar, Language language) {
		
		Locale locale = new Locale(language.getLanguageCode());
		
		return getWeekDayName(calendar, locale) + ", " + calendar.get(Calendar.DATE) + " " + getMonthName(calendar, locale) + " " + clockLanguages.get(language) + " " + TimeUtils.hourAndMinutesToString(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
	}
	
}
