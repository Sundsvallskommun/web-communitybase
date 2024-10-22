package se.dosf.communitybase.beans;

import java.sql.Timestamp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLUtils;

public abstract class Event implements Elementable {

	public abstract String getTitle();

	public abstract String getDescription();

	public abstract Timestamp getAdded();

	public abstract String getFullAlias();

	@Override
	public final Element toXML(Document doc) {

		Element eventElement = doc.createElement("event");

		XMLUtils.appendNewCDATAElement(doc, eventElement, "title", this.getTitle());
		XMLUtils.appendNewCDATAElement(doc, eventElement, "description", this.getDescription());

		Timestamp timestamp = this.getAdded();

		if(timestamp != null){
			XMLUtils.appendNewCDATAElement(doc, eventElement, "added", DateUtils.DATE_TIME_FORMATTER.format(timestamp));
		}

		XMLUtils.appendNewCDATAElement(doc, eventElement, "fullAlias", this.getFullAlias());

		return eventElement;
	}
}
