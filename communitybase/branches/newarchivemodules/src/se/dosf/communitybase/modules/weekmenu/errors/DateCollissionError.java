package se.dosf.communitybase.modules.weekmenu.errors;

import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name="validationError")
public class DateCollissionError extends ValidationError {

	private static final long serialVersionUID = -8854662031232377811L;
	
	@XMLElement
	private final String collider;

	public DateCollissionError(String messageKey, String collider) {

		super(messageKey);

		this.collider = collider;
	}

	public String getCollider() {

		return collider;
	}

}
