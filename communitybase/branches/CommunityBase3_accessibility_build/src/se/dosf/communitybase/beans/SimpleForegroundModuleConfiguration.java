package se.dosf.communitybase.beans;

import se.dosf.communitybase.interfaces.ForegroundModuleConfiguration;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.OrderBy;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "communitybase_section_type_foreground_modules")
@XMLElement(name = "ForegroundModuleConfiguration")
public class SimpleForegroundModuleConfiguration extends SimpleModuleConfiguration implements ForegroundModuleConfiguration {

	@DAOManaged
	@OrderBy
	@XMLElement
	private Integer menuIndex;

	@DAOManaged
	@XMLElement
	private boolean allowedStartpage;

	@Override
	public Integer getMenuIndex() {

		return menuIndex;
	}

	public void setMenuIndex(Integer menuIndex) {

		this.menuIndex = menuIndex;
	}

	@Override
	public boolean isAllowedStartpage() {

		return allowedStartpage;
	}

	public void setAllowedStartpage(boolean allowedStartpage) {

		this.allowedStartpage = allowedStartpage;
	}
}