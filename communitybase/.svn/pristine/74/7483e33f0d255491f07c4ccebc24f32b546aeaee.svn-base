package se.dosf.communitybase.beans;

import java.awt.image.BufferedImage;

import se.dosf.communitybase.enums.SectionAccessMode;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.xml.XMLElement;

public class NewSectionValues {

	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;

	@WebPopulate(maxLength = 4096)
	@XMLElement
	private String description;

	@WebPopulate(required = true)
	@XMLElement
	private SectionAccessMode accessMode;

	@WebPopulate(required = true)
	@XMLElement
	private Integer sectionTypeID;

	private BufferedImage logo;

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public SectionAccessMode getAccessMode() {

		return accessMode;
	}

	public void setAccessMode(SectionAccessMode accessMode) {

		this.accessMode = accessMode;
	}

	public Integer getSectionTypeID() {

		return sectionTypeID;
	}

	public void setSectionTypeID(Integer sectionTypeID) {

		this.sectionTypeID = sectionTypeID;
	}

	public BufferedImage getLogo() {

		return logo;
	}

	public void setLogo(BufferedImage logo) {

		this.logo = logo;
	}
}
