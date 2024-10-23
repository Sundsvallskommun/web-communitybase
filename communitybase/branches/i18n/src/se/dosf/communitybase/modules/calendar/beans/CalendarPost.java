package se.dosf.communitybase.modules.calendar.beans;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.utils.CalendarUtil;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class CalendarPost {

	private static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private Integer calendarID;
	private String description;
	private String details;
	private Timestamp startTime;
	private Timestamp endTime;
	private Integer posterID;
	private Timestamp posted;

	private String url;
	private boolean global;
	private ArrayList<CommunityGroup> groups;
	private ArrayList<School> schools;


	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public Integer getCalendarID() {
		return calendarID;
	}

	public void setCalendarID(Integer calendarID) {
		this.calendarID = calendarID;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public Timestamp getPosted() {
		return posted;
	}
	public String getDetails() {
		return details;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public Integer getPosterID() {
		return posterID;
	}

	public void setPosted(Timestamp posted) {
		this.posted = posted;
	}
	public void setDetails(String details) {
		this.details = details;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public void setPosterID(Integer posterID) {
		this.posterID = posterID;
	}

	public void setDate(Timestamp date) {
		this.startTime = date;
	}

	public Timestamp getDate() {
		return startTime;
	}

	public void setGroups(ArrayList<CommunityGroup> groups) {
		this.groups = groups;
	}

	public ArrayList<CommunityGroup> getGroups() {
		return groups;
	}

	public void setSchools(ArrayList<School> schools) {
		this.schools = schools;
	}
	
	public ArrayList<School> getSchools() {
		return schools;
	}
	
	public void setGlobal(boolean global) {
		this.global = global;
	}

	public boolean isGlobal() {
		return global;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.description, 30, "...") + " (ID: " + this.calendarID + ")";
	}

	public Element toXML(Document doc) {

		Element calendarPost = doc.createElement("calendarPost");

		if(this.calendarID != null){
			calendarPost.appendChild(XMLUtils.createCDATAElement("id", this.calendarID.toString(), doc));
		}

		if(this.description != null){
			calendarPost.appendChild(XMLUtils.createCDATAElement("title", this.description, doc));
		}

		if(this.details != null){
			calendarPost.appendChild(XMLUtils.createCDATAElement("details", this.details, doc));
		}

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(startTime.getTime());

		if(this.startTime != null){
			
			calendarPost.appendChild(XMLUtils.createCDATAElement("date", dateTimeFormat.format(getDate()).toString(), doc));

			Element dateTimeElement = doc.createElement("datetime");

			XMLUtils.appendNewElement(doc, dateTimeElement, "month", cal.get(Calendar.MONTH) + 1);

			XMLUtils.appendNewElement(doc, dateTimeElement, "monthtext", CalendarUtil.getMonth(cal));

			XMLUtils.appendNewElement(doc, dateTimeElement, "day", cal.get(Calendar.DAY_OF_MONTH));

			XMLUtils.appendNewElement(doc, dateTimeElement, "daytext", CalendarUtil.getDay(cal));

			cal.setMinimalDaysInFirstWeek(4);
			
			XMLUtils.appendNewElement(doc, dateTimeElement, "week", String.valueOf(cal.get(Calendar.WEEK_OF_YEAR)));

			calendarPost.appendChild(dateTimeElement);

			calendarPost.appendChild(XMLUtils.createCDATAElement("startTime", TimeUtils.hourAndMinutesToString(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)), doc));

		}


		if(this.endTime != null){
			cal.setTimeInMillis(endTime.getTime());
			calendarPost.appendChild(XMLUtils.createCDATAElement("endTime", TimeUtils.hourAndMinutesToString(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)), doc));
		}

		if(this.posterID != null){
			calendarPost.appendChild(XMLUtils.createElement("poster", this.posterID.toString(), doc));
		}

		if(this.posted != null){
			cal.setTime(this.posted);

			calendarPost.appendChild(XMLUtils.createElement("posted", dateTimeFormat.format(this.posted).toString(), doc));

			calendarPost.appendChild(XMLUtils.createElement("fullposted", CalendarUtil.toFullDateString(cal), doc));
		}

		if(this.url != null){
			calendarPost.appendChild(XMLUtils.createCDATAElement("url", this.url, doc));
		}

		if(this.groups != null){
			XMLUtils.append(doc, calendarPost, "groups", this.groups);
		}

		if(this.schools != null){
			XMLUtils.append(doc, calendarPost, "schools", this.schools);

		}

		if(this.schools == null && this.groups == null){
			calendarPost.appendChild(XMLUtils.createElement("global", "global", doc));
		}

		return calendarPost;
	}



}