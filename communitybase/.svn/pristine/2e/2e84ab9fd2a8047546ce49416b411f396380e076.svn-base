package se.dosf.communitybase.utils;

import java.util.Collection;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.interfaces.Publishable;
import se.unlogic.hierarchy.core.interfaces.ModuleDescriptor;
import se.unlogic.standardutils.collections.CollectionUtils;

public class AccessUtils {

	public static boolean checkAccess(CommunityUser user, CommunityGroup group) {

		if (checkAccess(user, group.getSchool())) {

			return true;

		}  else if (user.getAccessLevel(group) != null) {

			return true;
		}

		return false;
	}

	public static boolean checkAccess(CommunityUser user, CommunityGroup group, GroupAccessLevel... requiredAccessLevels) {

		if (checkAccess(user, group.getSchool())) {

			return true;

		}else if (requiredAccessLevels != null) {

			GroupAccessLevel userAccessLevel = user.getAccessLevel(group);

			if(userAccessLevel != null){

				for (GroupAccessLevel requiredAccessLevel : requiredAccessLevels) {

					if (requiredAccessLevel.ordinal() >= userAccessLevel.ordinal()) {
						return true;
					}
				}

			}
		}

		return false;
	}

	public static boolean checkAccess(CommunityUser user, School school) {

		if (user == null) {

			return false;

		} else if (user.isAdmin()) {

			return true;

		} else if (user.getSchools() != null && user.getSchools().contains(school)) {
			return true;
		}

		return false;
	}

	public static boolean checkAdminAccess(Publishable publishable, CommunityUser user, GroupAccessLevel... requiredAccessLevels) {

		if (publishable.isGlobal()) {
			if (!user.isAdmin()) {
				return false;
			}
		} else {

			if (publishable.getGroups() != null) {
				for (CommunityGroup group : publishable.getGroups()) {
					if (!AccessUtils.checkAccess(user, group, requiredAccessLevels)) {
						return false;
					}
				}
			}

			if (publishable.getSchools() != null) {
				for (School school : publishable.getSchools()) {
					if (!AccessUtils.checkAccess(user, school)) {
						return false;
					}
				}
			}

		}

		return true;
	}

	public static boolean checkReadAccess(Publishable publishable, CommunityUser user, GroupAccessLevel... requiredAccessLevels) {

		if (publishable.isGlobal() || user.isAdmin()) {

			return true;

		} else {

			if (publishable.getGroups() != null) {
				for (CommunityGroup group : publishable.getGroups()) {
					if (AccessUtils.checkAccess(user, group, requiredAccessLevels)) {
						return true;
					}
				}
			}

			if (publishable.getSchools() != null) {
				for (School school : publishable.getSchools()) {
					if (AccessUtils.checkAccess(user, school)) {
						return true;
					}

					if(!CollectionUtils.isEmpty(user.getCommunityGroups())){

						for(CommunityGroup communityGroup : user.getCommunityGroups()){

							if(communityGroup.getSchool().equals(school)){

								return true;
							}
						}
					}
				}
			}

		}

		return false;
	}
	
	public static boolean checkAccess(ModuleDescriptor moduleDescriptor, CommunityGroup group) {
		
		Collection<Integer> allowedGroupIDs = moduleDescriptor.getAllowedGroupIDs();
		
		if(moduleDescriptor.allowsUserAccess() || (!CollectionUtils.isEmpty(allowedGroupIDs) && allowedGroupIDs.contains(group.getGroupID()))) {
			return true;
		}
		
		return false;
	}
	
}
