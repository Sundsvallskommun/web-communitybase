package se.dosf.communitybase.modules.oldcontentremover.beans;

import java.io.Serializable;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLUtils;

@XMLElement(name="Content")
public class OldContent extends GeneratedElementable implements Elementable, Serializable {

	private static final long serialVersionUID = 1248005380694372164L;

	@XMLElement(name="Name")
	private final String name;

	@XMLElement(name="URL")
	private final String url;

	private final Date creationDate;

	@XMLElement(name="ID")
	private final Integer id;

	@XMLElement(name="Type")
	private final String type;
	

	public OldContent(Integer id, String name, String url, Date creationDate, String type) {

		this.name = name;
		this.id = id;
		this.url = url;
		this.creationDate = creationDate;
		this.type = type;
	}

	@Override
	public String toString() {

		return StringUtils.toLogFormat(name, 30) + " (ID: " + id + ")";
	}

	public static long getSerialversionuid() {

		return serialVersionUID;
	}

	public String getName() {

		return name;
	}

	public String getURL() {

		return url;
	}

	public Date getCreationDate() {

		return creationDate;
	}

	public String getType() {

		return type;
	}

	public Integer getID() {

		return id;
	}

	@Override
	public Element toXML(Document doc) {

		Element element = super.toXML(doc);
		
		XMLUtils.appendNewCDATAElement(doc, element, "CreationDate", DateUtils.DATE_TIME_FORMATTER.format(creationDate));
		
		return element;
	}
	
	

}
