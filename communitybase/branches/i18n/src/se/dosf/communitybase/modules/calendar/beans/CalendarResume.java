package se.dosf.communitybase.modules.calendar.beans;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.SortCalendarComparator;
import se.dosf.communitybase.utils.CalendarUtil;
import se.unlogic.standardutils.xml.XMLUtils;

public class CalendarResume {
	/*
	 * innehåller allt som en resume behöver dvs.
	 * gruppnamn skolnamn separerar datum
	 * 
	 * en resume med många calender poster
	 */
	
	private static SimpleDateFormat year = new SimpleDateFormat("yyyy");
	private static SimpleDateFormat month = new SimpleDateFormat("MM");
	private static SimpleDateFormat day = new SimpleDateFormat("dd");
	private SortCalendarComparator sortCalendar = new SortCalendarComparator();
	private Set<CalendarPost> calendarPosts = new TreeSet<CalendarPost>(sortCalendar);
	private String calendarAlias; 

	public Set<CalendarPost> getCalendarPosts() {
		return calendarPosts;
	}
	
	public void setCalendarPosts(Set<CalendarPost> calendarPosts){
		this.calendarPosts = calendarPosts;
	}
	
	public void setCalendarAlias(String calendarAlias){
		this.calendarAlias = calendarAlias;
	}

	public Element toXML(Document doc) {

		Element calendarResume = doc.createElement("calendarResume");
		
		if(calendarPosts != null && !calendarPosts.isEmpty()){
			
			String currentYear = null;
			String currentMonth = null;
			String currentDay = null;
			
			Element yearElement = null; 
			Element monthElement = null;
			Element dayElement = null;
			
			Calendar cal = Calendar.getInstance();
			
			int position = 0;
			
			for(CalendarPost calendarPost : this.calendarPosts){				
				
				cal.setTimeInMillis(calendarPost.getDate().getTime());
				
				if(position == 0){

					currentYear = year.format(calendarPost.getDate());
					currentMonth = month.format(calendarPost.getDate());
					currentDay = day.format(calendarPost.getDate());
					
					yearElement = doc.createElement("year");
					yearElement.appendChild(XMLUtils.createElement("yearnumber", currentYear, doc));
					calendarResume.appendChild(yearElement);
					
					monthElement = doc.createElement("month");
					monthElement.appendChild(XMLUtils.createElement("monthnumber", CalendarUtil.getMonth(cal) , doc));
					yearElement.appendChild(monthElement);
					
					dayElement = doc.createElement("day");
					cal.setTime(new Date(calendarPost.getDate().getTime()));
					dayElement.appendChild(XMLUtils.createElement("monthday", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), doc));
					dayElement.appendChild(XMLUtils.createElement("weekday", CalendarUtil.getDay(cal), doc));
					monthElement.appendChild(dayElement);
					
					position = 1;
					
				}
				
				if(!currentYear.equals(year.format(calendarPost.getDate()))){
					
					// nytt yearnuymber
					currentYear = year.format(calendarPost.getDate());
					currentMonth = month.format(calendarPost.getDate());
					
					yearElement = doc.createElement("year");
					calendarResume.appendChild(yearElement);
					
					yearElement.appendChild(XMLUtils.createElement("yearnumber", currentYear, doc));
					
					monthElement = doc.createElement("month");
					yearElement.appendChild(monthElement);
					
					monthElement.appendChild(XMLUtils.createElement("monthnumber", CalendarUtil.getMonth(cal) , doc));
					
					
				}

				if(!currentMonth.equals(month.format(calendarPost.getDate()))){
					
					// ny månad
					currentMonth = month.format(calendarPost.getDate());
					currentDay = day.format(calendarPost.getDate());
					
					monthElement = doc.createElement("month");
					yearElement.appendChild(monthElement);
					
					monthElement.appendChild(XMLUtils.createElement("monthnumber", CalendarUtil.getMonth(cal) , doc));
										
					dayElement = doc.createElement("day");
					monthElement.appendChild(dayElement);
					
					cal.setTime(new Date(calendarPost.getDate().getTime()));
					dayElement.appendChild(XMLUtils.createElement("monthday", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), doc));
					dayElement.appendChild(XMLUtils.createElement("weekday", CalendarUtil.getDay(cal), doc));
					
					
				}
				
				if(!currentDay.equals(day.format(calendarPost.getDate()))){
					
					// ny dag
					currentDay = day.format(calendarPost.getDate());
					
					dayElement = doc.createElement("day");
					monthElement.appendChild(dayElement);
					
					cal.setTime(new Date(calendarPost.getDate().getTime()));
					dayElement.appendChild(XMLUtils.createElement("monthday", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), doc));
					dayElement.appendChild(XMLUtils.createElement("weekday", CalendarUtil.getDay(cal), doc));

				}
								
				calendarPost.setUrl(calendarAlias + "/showPost/" + calendarPost.getCalendarID());
				
				dayElement.appendChild(calendarPost.toXML(doc));

			}
			
		}
				
		return calendarResume;
	}


} 
