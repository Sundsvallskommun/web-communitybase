package se.dosf.communitybase.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.beans.CommunityUser;
import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.i18n.Language;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;
import se.unlogic.webutils.validation.ValidationUtils;

public class CommunityUserPopulator implements BeanResultSetPopulator<CommunityUser>, BeanRequestPopulator<CommunityUser> {

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
		cUser.setLanguage(Language.getLanguage(rs.getString("languageCode")));

		return cUser;
	}

	public CommunityUser populate(HttpServletRequest req)throws ValidationException {
		throw new UnsupportedOperationException();
	}

	public CommunityUser populate(CommunityUser user, HttpServletRequest req) throws ValidationException {

		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();

		String firstname = ValidationUtils.validateNotEmptyField("firstname", req, validationErrors);
		String lastname = ValidationUtils.validateNotEmptyField("lastname", req, validationErrors);

		boolean changePassword = req.getParameter("changepassword") != null;

		String email = req.getParameter("email");

		if (StringUtils.isEmpty(email)) {
			throw new ValidationException(new ValidationError("email", ValidationErrorType.RequiredField));
		} else if (!EmailUtils.isValidEmailAddress(email)) {
			throw new ValidationException(new ValidationError("email", ValidationErrorType.InvalidFormat));
		}

		if(changePassword){

			String password = ValidationUtils.validateNotEmptyField("password", req, validationErrors);

			if(validationErrors.isEmpty()){

				if(password.length() < 6){
					validationErrors.add(new ValidationError("password", ValidationErrorType.TooShort));
				}else if(password.length() > 50){
					validationErrors.add(new ValidationError("password", ValidationErrorType.TooLong));
				}else if(!password.equals(req.getParameter("passwordReference"))){
					validationErrors.add(new ValidationError("passwordsDontMatch"));
				}else{
					user.setPassword(password);
				}

			}

		}
		
		String languageCode = req.getParameter("languageCode");
		if(!StringUtils.isEmpty(languageCode)) {
			Language language;
			if(languageCode.length() != 2 || (language = Language.getLanguage(languageCode)) == null) {
				validationErrors.add(new ValidationError("invalidLanguageCode"));
			} else {
				user.setLanguage(language);
			}
		}

		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}

		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setEmail(email);
		user.setPhoneHome(req.getParameter("phoneHome"));
		user.setPhoneMobile(req.getParameter("phoneMobile"));
		user.setPhoneWork(req.getParameter("phoneWork"));

		boolean resume = req.getParameter("emailresume") != null;

		if(resume){
			user.setResume(Integer.valueOf(req.getParameter("resumetime")));					
		}else{
			user.setResume(null);
		}

		return user;

	}


}
