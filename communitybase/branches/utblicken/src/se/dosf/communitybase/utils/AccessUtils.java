package se.dosf.communitybase.utils;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.enums.GroupAccessLevel;

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
}
