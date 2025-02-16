package se.dosf.communitybase.modules.weekmenu.beans;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGenerator;

@Table(name = "weekmenu_school_offsets")
@XMLElement
public class SchoolTemplateOffset implements Elementable {

	@DAOManaged(autoGenerated = true)
	@Key
	@XMLElement
	private Integer schoolOffsetID;

	@DAOManaged
	@XMLElement
	private Integer weekMenuTemplateID;

	@DAOManaged
	@XMLElement
	private Integer schoolID;

	@DAOManaged
	@XMLElement
	private Integer offset;

	public Integer getSchoolOffsetID() {

		return schoolOffsetID;
	}

	public void setSchoolOffsetID(Integer schoolOffsetID) {

		this.schoolOffsetID = schoolOffsetID;
	}

	public Integer getOffset() {

		return offset;
	}

	public void setOffset(Integer offset) {

		this.offset = offset;
	}

	public Integer getWeekMenuTemplateID() {

		return weekMenuTemplateID;
	}

	public void setWeekMenuTemplateID(Integer weekMenuTemplateID) {

		this.weekMenuTemplateID = weekMenuTemplateID;
	}

	public Integer getSchoolID() {

		return schoolID;
	}

	public void setSchoolID(Integer schoolID) {

		this.schoolID = schoolID;
	}

	@Override
	public String toString() {

		return offset + "(ID: " + schoolOffsetID + ")";
	}

	@Override
	public Element toXML(Document doc) {

		return XMLGenerator.toXML(this, doc);
	}

}
