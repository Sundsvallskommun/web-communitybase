package se.dosf.communitybase.beans;

import se.dosf.communitybase.enums.ModuleAccessMode;
import se.dosf.communitybase.enums.ModuleManagementMode;
import se.dosf.communitybase.interfaces.ModuleConfiguration;
import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

public abstract class SimpleModuleConfiguration extends GeneratedElementable implements ModuleConfiguration {

	@DAOManaged(columnName = "sectionTypeID")
	@ManyToOne
	@Key
	@XMLElement
	private SimpleSectionType sectionType;

	@DAOManaged
	@Key
	@WebPopulate
	@XMLElement
	private Integer moduleID;

	@DAOManaged
	@WebPopulate
	@XMLElement
	private boolean autoEnable;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ModuleManagementMode managementMode;

	@DAOManaged
	@WebPopulate(required = true)
	@XMLElement
	private ModuleAccessMode accessMode;

	@Override
	public SimpleSectionType getSectionType() {

		return sectionType;
	}

	public void setSectionType(SimpleSectionType sectionType) {

		this.sectionType = sectionType;
	}

	@Override
	public boolean isAutoEnable() {

		return autoEnable;
	}

	public void setAutoEnable(boolean autoEnable) {

		this.autoEnable = autoEnable;
	}

	@Override
	public ModuleManagementMode getManagementMode() {

		return managementMode;
	}

	public void setManagementMode(ModuleManagementMode managementMode) {

		this.managementMode = managementMode;
	}

	@Override
	public ModuleAccessMode getAccessMode() {

		return accessMode;
	}

	public void setAccessMode(ModuleAccessMode accessMode) {

		this.accessMode = accessMode;
	}

	@Override
	public Integer getModuleID() {

		return moduleID;
	}

	public void setModuleID(Integer moduleID) {

		this.moduleID = moduleID;
	}
}
