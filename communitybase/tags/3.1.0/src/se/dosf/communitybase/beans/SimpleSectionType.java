package se.dosf.communitybase.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.dosf.communitybase.interfaces.SectionType;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToMany;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.SimplifiedRelation;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "communitybase_section_types")
@XMLElement(name="SectionType")
public class SimpleSectionType extends GeneratedElementable implements SectionType {

	public static final Field ADD_ACCESS_GROUPS_RELATION = ReflectionUtils.getField(SimpleSectionType.class, "addAccessGroupIDs");
	public static final Field ALLOWED_ROLES_RELATION = ReflectionUtils.getField(SimpleSectionType.class, "allowedRoles");
	public static final Field SUPPORTED_FOREGROUND_MODULES_RELATION = ReflectionUtils.getField(SimpleSectionType.class, "supportedForegroundModules");
	public static final Field SUPPORTED_BACKGROUND_MODULES_RELATION = ReflectionUtils.getField(SimpleSectionType.class, "supportedBackgroundModules");

	@DAOManaged
	@Key
	@XMLElement
	private Integer sectionTypeID;

	@DAOManaged
	@OrderBy
	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;

	@DAOManaged
	@OneToMany
	@SimplifiedRelation(table = "communitybase_section_type_add_access_groups", remoteValueColumnName = "groupID")
	@WebPopulate(paramName = "groupID")
	@XMLElement(childName = "groupID")
	private List<Integer> addAccessGroupIDs;

	@DAOManaged
	@ManyToMany(linkTable = "communitybase_section_type_roles")
	@XMLElement
	private List<SimpleRole> allowedRoles;

	@DAOManaged
	@XMLElement
	private Integer creatorRoleID;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<SimpleForegroundModuleConfiguration> supportedForegroundModules;

	@DAOManaged
	@OneToMany
	@XMLElement
	private List<SimpleBackgroundModuleConfiguration> supportedBackgroundModules;

	@Override
	public Integer getSectionTypeID() {

		return sectionTypeID;
	}

	public void setSectionTypeID(Integer sectionTypeID) {

		this.sectionTypeID = sectionTypeID;
	}

	@Override
	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	@Override
	public List<SimpleRole> getAllowedRoles() {

		return allowedRoles;
	}

	public void setAllowedRoles(ArrayList<SimpleRole> allowedRoles) {

		this.allowedRoles = allowedRoles;
	}

	@Override
	public List<Integer> getAddAccessGroupIDs() {

		return addAccessGroupIDs;
	}

	public void setAddAccessGroupIDs(List<Integer> addAccessGroupIDs) {

		this.addAccessGroupIDs = addAccessGroupIDs;
	}

	@Override
	public Integer getCreatorRoleID() {

		return creatorRoleID;
	}

	public void setCreatorRoleID(Integer defaultRoleID) {

		this.creatorRoleID = defaultRoleID;
	}


	@Override
	public List<SimpleForegroundModuleConfiguration> getSupportedForegroundModules() {

		return supportedForegroundModules;
	}


	public void setSupportedForegroundModules(List<SimpleForegroundModuleConfiguration> supportedForegroundModules) {

		this.supportedForegroundModules = supportedForegroundModules;
	}


	@Override
	public List<SimpleBackgroundModuleConfiguration> getSupportedBackgroundModules() {

		return supportedBackgroundModules;
	}


	public void setSupportedBackgroundModules(List<SimpleBackgroundModuleConfiguration> supportedBackgroundModules) {

		this.supportedBackgroundModules = supportedBackgroundModules;
	}


	public void setAllowedRoles(List<SimpleRole> allowedRoles) {

		this.allowedRoles = allowedRoles;
	}
}
