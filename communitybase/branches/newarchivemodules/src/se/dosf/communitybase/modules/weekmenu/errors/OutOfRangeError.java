package se.dosf.communitybase.modules.weekmenu.errors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "validationError")
public class OutOfRangeError extends ValidationError {

	private static final long serialVersionUID = -1970084046096411915L;

	@XMLElement
	private final Integer minimum;

	@XMLElement
	private final Integer maximum;
	
	public OutOfRangeError(String fieldName, String messageKey, Integer minimum, Integer maximum) {

		super(fieldName, (ValidationErrorType) null, messageKey);

		this.minimum = minimum;
		this.maximum = maximum;
	}

	public Integer getMinimum() {

		return minimum;
	}

	public Integer getMaximum() {

		return maximum;
	}

}
