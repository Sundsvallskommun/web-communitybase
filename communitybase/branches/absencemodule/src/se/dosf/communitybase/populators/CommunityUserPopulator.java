package se.dosf.communitybase.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.emailutils.populators.EmailPopulator;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.populators.StringPopulator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

public class CommunityUserPopulator implements BeanResultSetPopulator<CommunityUser>, BeanRequestPopulator<CommunityUser> {

	private static final EmailPopulator EMAIL_POPULATOR = new EmailPopulator();
	
	public CommunityUser populate(ResultSet rs) throws SQLException {

		CommunityUser cUser = new CommunityUser();

		cUser.setUserID(rs.getInt("userID"));
		cUser.setEmail(rs.getString("email"));
		cUser.setFirstname(rs.getString("firstName"));
		cUser.setLastname(rs.getString("lastName"));
		cUser.setPhoneHome(rs.getString("phoneHome"));
		cUser.setPhoneMobile(rs.getString("phoneMobile"));
		cUser.setPhoneWork(rs.getString("phoneWork"));
		cUser.setLastLogin(rs.getTimestamp("LastLogin"));
		cUser.setPassword(rs.getString("password"));
		cUser.setResume(rs.getInt("resume"));
		cUser.setLastResume(rs.getTimestamp("lastResume"));
		cUser.setEnabled(rs.getBoolean("enabled"));
		cUser.setAdmin(rs.getBoolean("admin"));
		cUser.setAdded(rs.getTimestamp("added"));

		return cUser;
	}

	public CommunityUser populate(HttpServletRequest req) throws ValidationException {
		throw new UnsupportedOperationException();
	}

	public CommunityUser populate(CommunityUser user, HttpServletRequest req) throws ValidationException {
		
		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();
		
		String firstname = ValidationUtils.validateParameter("firstname", req, true, 0, 60, StringPopulator.getPopulator(), validationErrors);
		String lastname = ValidationUtils.validateParameter("lastname", req, true, 0, 80, StringPopulator.getPopulator(), validationErrors);
		
		String phoneHome = ValidationUtils.validateParameter("phoneHome", req, false, 0, 30, StringPopulator.getPopulator(), validationErrors);
		String phoneMobile = ValidationUtils.validateParameter("phoneMobile", req, false, 0, 30, StringPopulator.getPopulator(), validationErrors);
		String phoneWork = ValidationUtils.validateParameter("phoneWork", req, false, 0, 30, StringPopulator.getPopulator(), validationErrors);
		
		boolean changePassword = req.getParameter("changepassword") != null;

		String email = ValidationUtils.validateParameter("email", req, true, 0, 255, EMAIL_POPULATOR, validationErrors);

		if(changePassword){
			
			String password = ValidationUtils.validateParameter("password", req, true, 6, 50, StringPopulator.getPopulator(), validationErrors);
			
			if(validationErrors.isEmpty()){
				
				if(!password.equals(req.getParameter("passwordReference"))) {
					validationErrors.add(new ValidationError("passwordsDontMatch"));
				} else {
					user.setPassword(password);
				}
				
			}
			
		}
		
		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}
		
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setEmail(email);
		user.setPhoneHome(phoneHome);
		user.setPhoneMobile(phoneMobile);
		user.setPhoneWork(phoneWork);

		boolean resume = req.getParameter("emailresume") != null;
		
		if(resume){
			user.setResume(Integer.valueOf(req.getParameter("resumetime")));					
		}else{
			user.setResume(null);
		}
		
		return user;
		
	}
	
	
}
