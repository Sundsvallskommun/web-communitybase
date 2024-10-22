package se.dosf.communitybase.modules.calendar.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.modules.calendar.beans.CalendarPost;
import se.dosf.communitybase.utils.ScriptFilter;

import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

public class CalendarPopulator implements BeanResultSetPopulator<CalendarPost>, BeanRequestPopulator<CalendarPost> {
	
	public CalendarPost populate(ResultSet rs) throws SQLException {
		
		CalendarPost calendarPost = new CalendarPost();

		calendarPost.setCalendarID(rs.getInt("calendarID"));
		calendarPost.setDescription(rs.getString("title"));
		calendarPost.setDetails(rs.getString("details"));
		calendarPost.setEndTime(rs.getTimestamp("endTime"));
		calendarPost.setStartTime(rs.getTimestamp("startTime"));
		calendarPost.setPosted(rs.getTimestamp("posted"));
		calendarPost.setPosterID(rs.getInt("posterID"));

		return calendarPost;
	}
	
	public CalendarPost populate(HttpServletRequest req, ArrayList<String> chosenGroups, ArrayList<String> chosenSchools) throws ValidationException{
		
		return this.populate(new CalendarPost(), req, chosenGroups, chosenSchools);
		
	}

	@SuppressWarnings("unchecked")
	public CalendarPost populate(CalendarPost post, HttpServletRequest req, ArrayList<String> chosenGroups, ArrayList<String> chosenSchools) throws ValidationException {		

		post = this.populate(post, req);
		
		// check how to publish

		if(req.getParameter("global") == null){
			
			post.setGlobal(false);
			
			String[] schools = req.getParameterValues("schoolbase");
			
			if(schools != null){
				chosenSchools.addAll(Arrays.asList(schools));
			}
			
			Enumeration<String> names = req.getParameterNames();

			while(names.hasMoreElements()){

				String name = names.nextElement();
				
				if(name.startsWith("groupschool")){
					
					String[] groups = req.getParameterValues(name);
					
					for(String group : groups){
					
						String[] schoolAndGroup = group.split(":");
						
						if(!chosenSchools.contains(schoolAndGroup[0])){
							chosenGroups.add(schoolAndGroup[1]);
						}
					
					}
					
				}
				
			}

			if(chosenGroups.isEmpty() && chosenSchools.isEmpty()){
				throw new ValidationException(new ValidationError("NoGroup"));
			}
			
		} else{
			post.setGlobal(true);
		}
		
		return post;
		
	}
	
	public CalendarPost populate(HttpServletRequest req) throws ValidationException {		
		return this.populate(new CalendarPost(), req);
	}

	public CalendarPost populate(CalendarPost post, HttpServletRequest req) throws ValidationException {
		
		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		String startDate = ValidationUtils.validateNotEmptyField("startdate", req, validationErrors);
		String title = ValidationUtils.validateNotEmptyField("title", req, validationErrors);
		String description = ValidationUtils.validateNotEmptyField("description", req, validationErrors);
		
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
		
		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}
		
		String startTime = req.getParameter("starttime");		
		String endTime = req.getParameter("endtime");
		
		if(!StringUtils.isEmpty(startTime)){
			String[] time = startTime.split(":");
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
			calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
		}else{
			calendar.set(Calendar.SECOND, 1);
		}
		
		post.setStartTime(new Timestamp(calendar.getTimeInMillis()));
		
		if(!StringUtils.isEmpty(endTime)){
			String[] time = endTime.split(":");
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
			calendar.set(Calendar.MINUTE, Integer.parseInt(time[1]));
		}else{
			calendar.set(Calendar.SECOND, 1);
		}
		
		post.setEndTime(new Timestamp(calendar.getTimeInMillis()));
		
		return post;
	}

}
