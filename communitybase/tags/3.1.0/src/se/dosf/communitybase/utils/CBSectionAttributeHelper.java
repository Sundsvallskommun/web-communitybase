package se.dosf.communitybase.utils;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.beans.SimpleSectionType;
import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.interfaces.CBInterface;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.standardutils.bool.BooleanUtils;
import se.unlogic.standardutils.date.DateUtils;
import se.unlogic.standardutils.enums.EnumUtils;

public class CBSectionAttributeHelper {

	public static SimpleSectionType getSectionType(SectionDescriptor descriptor, CBInterface cbInterface) throws SQLException {

		Integer simpleSectionTypeID = getSectionTypeID(descriptor);

		if (simpleSectionTypeID == null) {
			return null;
		}

		return cbInterface.getSectionType(simpleSectionTypeID);
	}

	public static Integer getSectionTypeID(SectionDescriptor descriptor) throws SQLException {

		return descriptor.getAttributeHandler().getInt(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID);
	}

	public static void setSectionType(SectionDescriptor descriptor, SimpleSectionType sectionType) {

		if (sectionType == null) {

			descriptor.getAttributeHandler().removeAttribute(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID);

		} else {

			descriptor.getAttributeHandler().setAttribute(CBConstants.SECTION_ATTRIBUTE_SECTION_TYPE_ID, sectionType.getSectionTypeID());
		}
	}

	public static boolean isArchived(SectionDescriptor descriptor) {

		return BooleanUtils.valueOf(descriptor.getAttributeHandler().getBoolean(CBConstants.SECTION_ATTRIBUTE_ARCHIVED));
	}

	public static void setArchived(SectionDescriptor descriptor, Boolean archived) {

		if (archived == null) {

			descriptor.getAttributeHandler().removeAttribute(CBConstants.SECTION_ATTRIBUTE_ARCHIVED);

		} else {

			descriptor.getAttributeHandler().setAttribute(CBConstants.SECTION_ATTRIBUTE_ARCHIVED, archived);
		}
	}

	public static SectionAccessMode getAccessMode(SectionDescriptor descriptor) {
		
		return EnumUtils.toEnum(SectionAccessMode.class, descriptor.getAttributeHandler().getString(CBConstants.SECTION_ATTRIBUTE_ACCESS_MODE));
	}

	public static void setAccessMode(SectionDescriptor descriptor, SectionAccessMode accessMode) {

		if (accessMode == null) {

			descriptor.getAttributeHandler().removeAttribute(CBConstants.SECTION_ATTRIBUTE_ACCESS_MODE);

		} else {

			descriptor.getAttributeHandler().setAttribute(CBConstants.SECTION_ATTRIBUTE_ACCESS_MODE, accessMode.name());
		}
	}

	public static String getDescription(SectionDescriptor descriptor) {

		return descriptor.getAttributeHandler().getString(CBConstants.SECTION_ATTRIBUTE_DESCRIPTION);
	}

	public static void setDescription(SectionDescriptor descriptor, String description) {

		if (description == null) {

			descriptor.getAttributeHandler().removeAttribute(CBConstants.SECTION_ATTRIBUTE_DESCRIPTION);

		} else {

			descriptor.getAttributeHandler().setAttribute(CBConstants.SECTION_ATTRIBUTE_DESCRIPTION, description);
		}
	}

	public static Timestamp getDeleted(SectionDescriptor descriptor) {
		
		Date date = DateUtils.getDate(DateUtils.DATE_TIME_SECONDS_FORMATTER, descriptor.getAttributeHandler().getString(CBConstants.SECTION_ATTRIBUTE_DELETED));
		
		if(date == null){
			return null;
		}

		return new Timestamp(date.getTime());
	}

	public static void setDeleted(SectionDescriptor descriptor, Timestamp deleted) {

		if (deleted == null) {

			descriptor.getAttributeHandler().removeAttribute(CBConstants.SECTION_ATTRIBUTE_DELETED);

		} else {

			descriptor.getAttributeHandler().setAttribute(CBConstants.SECTION_ATTRIBUTE_DELETED, DateUtils.DATE_TIME_SECONDS_FORMATTER.format(deleted));
		}
	}

}
