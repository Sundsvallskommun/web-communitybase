package se.dosf.communitybase.modules.calendar.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.modules.calendar.beans.CalendarPost;
import se.dosf.communitybase.utils.ScriptFilter;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

public class CalendarPopulator implements BeanResultSetPopulator<CalendarPost>, BeanRequestPopulator<CalendarPost> {

	@Override
	public CalendarPost populate(ResultSet rs) throws SQLException {

		CalendarPost calendarPost = new CalendarPost();

		calendarPost.setCalendarID(rs.getInt("calendarID"));
		calendarPost.setDescription(rs.getString("title"));
		calendarPost.setDetails(rs.getString("details"));
		calendarPost.setEndTime(rs.getTimestamp("endTime"));
		calendarPost.setStartTime(rs.getTimestamp("startTime"));
		calendarPost.setPosted(rs.getTimestamp("posted"));
		calendarPost.setPosterID((Integer) rs.getObject("posterID"));
		calendarPost.setSendReminder(rs.getBoolean("sendReminder"));
		
		return calendarPost;
	}

	@Override
	public CalendarPost populate(HttpServletRequest req) throws ValidationException {
		return this.populate(new CalendarPost(), req);
	}

	@Override
	public CalendarPost populate(CalendarPost post, HttpServletRequest req) throws ValidationException {

		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String startDate = ValidationUtils.validateNotEmptyParameter("startdate", req, validationErrors);
		String title = ValidationUtils.validateNotEmptyParameter("title", req, validationErrors);
		String description = ValidationUtils.validateNotEmptyParameter("description", req, validationErrors);

		if(startDate != null && !DateUtils.isValidDate(DateUtils.DATE_FORMATTER, startDate)){
			validationErrors.add(new ValidationError("startdate", ValidationErrorType.InvalidFormat));
		}

		if(description != null){

			if(!ScriptFilter.validateNoScriptField(description)){
				validationErrors.add(new ValidationError("InvalidContent"));
			}

		}

		post.setDetails(description);
		post.setDescription(title);

		Calendar calendar = Calendar.getInstance();

		Date date = DateUtils.getDate(DateUtils.DATE_FORMATTER, startDate);

		if(date != null){

			calendar.setTime(date);

			if(calendar.get(Calendar.YEAR) > 2037){
				validationErrors.add(new ValidationError("startdate", ValidationErrorType.TooLong));
			}else if(calendar.get(Calendar.YEAR) < 1970){
				validationErrors.add(new ValidationError("startdate", ValidationErrorType.TooShort));
			}

		}

		String startTime = req.getParameter("starttime");
		String endTime = req.getParameter("endtime");

		if(!StringUtils.isEmpty(startTime)){
			String[] time = startTime.split(":");
			if(time.length == 2 && NumberUtils.isInt(time[0]) && NumberUtils.isInt(time[1])) {
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
				calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
			} else {
				validationErrors.add(new ValidationError("starttime", ValidationErrorType.InvalidFormat));
			}
		} else{
			calendar.set(Calendar.SECOND, 1);
		}

		post.setStartTime(new Timestamp(calendar.getTimeInMillis()));

		if(!StringUtils.isEmpty(endTime)){
			String[] time = endTime.split(":");
			if(time.length == 2 && NumberUtils.isInt(time[0]) && NumberUtils.isInt(time[1])) {
				calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
				calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
			} else {
				validationErrors.add(new ValidationError("endtime", ValidationErrorType.InvalidFormat));
			}
		} else{
			calendar.set(Calendar.SECOND, 1);
		}

		post.setEndTime(new Timestamp(calendar.getTimeInMillis()));

		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}

		post.setSendReminder(req.getParameter("sendReminder") != null);
		
		return post;
	}

}
