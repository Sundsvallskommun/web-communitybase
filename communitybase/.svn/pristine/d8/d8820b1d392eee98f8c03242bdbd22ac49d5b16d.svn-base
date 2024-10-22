package se.dosf.communitybase.modules.calendar.beans;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.interfaces.Accessible;
import se.dosf.communitybase.utils.CalendarUtil;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public class CalendarPost implements Accessible, Elementable {

	private Integer calendarID;
	private String description;
	private String details;
	private Timestamp startTime;
	private Timestamp endTime;
	private Integer posterID;
	private Timestamp posted;
	private boolean sendReminder;

	private String url;
	private boolean global;
	private List<CommunityGroup> groups;
	private List<School> schools;

	private String fullDate;

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
	public boolean isSendReminder() {
		return sendReminder;
	}

	public void setSendReminder(boolean sendReminder) {
		this.sendReminder = sendReminder;
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

	@Override
	public void setGroups(List<CommunityGroup> groups) {
		this.groups = groups;
	}

	@Override
	public List<CommunityGroup> getGroups() {
		return groups;
	}

	@Override
	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	@Override
	public List<School> getSchools() {
		return schools;
	}

	@Override
	public void setGlobal(boolean global) {
		this.global = global;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

	public void setFullDate(Language language) {

		if(posted != null) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(posted.getTime());

			this.fullDate = CalendarUtil.getFullDateTimeString(calendar, language);

		}
	}

	public String getFullDate() {
		return fullDate;
	}

	@Override
	public String toString() {
		return StringUtils.substring(this.description, 30, "...") + " (ID: " + this.calendarID + ")";
	}

	@Override
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

		//TODO Set default locale (Locale.setDefault()) on OpenHiearchy startup from config.xml
		Calendar cal = Calendar.getInstance(new Locale("sv"));
		cal.setTimeInMillis(startTime.getTime());

		if(this.startTime != null){

			calendarPost.appendChild(XMLUtils.createCDATAElement("date", DateUtils.DATE_TIME_FORMATTER.format(getDate()).toString(), doc));

			Element dateTimeElement = doc.createElement("datetime");

			XMLUtils.appendNewElement(doc, dateTimeElement, "month", cal.get(Calendar.MONTH) + 1);

			XMLUtils.appendNewElement(doc, dateTimeElement, "monthtext", CalendarUtil.getMonth(cal));

			XMLUtils.appendNewElement(doc, dateTimeElement, "day", cal.get(Calendar.DAY_OF_MONTH));

			XMLUtils.appendNewElement(doc, dateTimeElement, "daytext", CalendarUtil.getDay(cal));

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

			calendarPost.appendChild(XMLUtils.createElement("posted", DateUtils.DATE_TIME_FORMATTER.format(this.posted).toString(), doc));

			calendarPost.appendChild(XMLUtils.createElement("fullposted", CalendarUtil.toFullDateString(cal), doc));
		}

		if(this.sendReminder) {
			XMLUtils.appendNewElement(doc, calendarPost, "sendReminder", "true");
		}
		
		XMLUtils.appendNewElement(doc, calendarPost, "fullDate", this.fullDate);

		if(this.url != null){
			calendarPost.appendChild(XMLUtils.createCDATAElement("url", this.url, doc));
		}

		if(this.groups != null){
			XMLUtils.append(doc, calendarPost, "groups", this.groups);
		}

		if(this.schools != null){
			XMLUtils.append(doc, calendarPost, "schools", this.schools);
		}

		calendarPost.appendChild(XMLUtils.createElement("global", this.schools == null && this.groups == null, doc));

		return calendarPost;
	}



}