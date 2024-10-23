package se.dosf.communitybase.populators;

import java.sql.SQLException;

import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.populators.BeanStringPopulator;

public class SchoolTypePopulator implements BeanStringPopulator<School> {

	private final CommunitySchoolDAO schoolDAO;
	private boolean getGroups;
	private boolean getAdmins;

	public SchoolTypePopulator(CommunitySchoolDAO schoolDAO, boolean getGroups, boolean getAdmins) {
		this.schoolDAO = schoolDAO;
		this.getGroups = getGroups;
		this.getAdmins = getAdmins;
	}

	public boolean isGetGroups() {
		return getGroups;
	}

	public void setGetGroups(boolean getGroups) {
		this.getGroups = getGroups;
	}

	public boolean isGetAdmins() {
		return getAdmins;
	}

	public void setGetAdmins(boolean getAdmins) {
		this.getAdmins = getAdmins;
	}

	public String getPopulatorID() {

		return null;
	}

	public Class<? extends School> getType() {
		return School.class;
	}

	public School getValue(String value) {
		try {
			return this.schoolDAO.getSchool(Integer.parseInt(value), this.getAdmins, this.getGroups);
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}
	}

	public boolean validateFormat(String value) {
		return NumberUtils.isInt(value);
	}

}
