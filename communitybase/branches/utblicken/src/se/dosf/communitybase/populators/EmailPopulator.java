package se.dosf.communitybase.populators;

import javax.servlet.http.HttpServletRequest;

import se.unlogic.emailutils.framework.EmailUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.webutils.http.BeanRequestPopulator;

public class EmailPopulator implements BeanRequestPopulator<String> {

	public String populate(HttpServletRequest req) throws ValidationException {

		String email = req.getParameter("email");

		if (StringUtils.isEmpty(email)) {
			throw new ValidationException(new ValidationError("email", ValidationErrorType.RequiredField));
		} else if (!EmailUtils.isValidEmailAddress(email)) {
			throw new ValidationException(new ValidationError("email", ValidationErrorType.InvalidFormat));
		}

		return email;
	}

	public String populate(String bean, HttpServletRequest req) throws ValidationException {
		return null;
	}
}
