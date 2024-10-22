package se.dosf.communitybase.utils;

import java.util.Collection;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.Receipt;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.interfaces.Accessible;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ModuleDescriptor;
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

	public static boolean checkAdminAccess(Accessible accessible, CommunityUser user, GroupAccessLevel... requiredAccessLevels) {

		if (accessible.isGlobal()) {
			if (!user.isAdmin()) {
				return false;
			}
		} else {

			if (accessible.getGroups() != null) {
				for (CommunityGroup group : accessible.getGroups()) {
					if (!AccessUtils.checkAccess(user, group, requiredAccessLevels)) {
						return false;
					}
				}
			}

			if (accessible.getSchools() != null) {
				for (School school : accessible.getSchools()) {
					if (!AccessUtils.checkAccess(user, school)) {
						return false;
					}
				}
			}

		}

		return true;
	}

	public static boolean checkReadAccess(Accessible accessible, CommunityUser user, GroupAccessLevel... requiredAccessLevels) {

		if (accessible.isGlobal() || user.isAdmin()) {

			return true;

		} else {

			if (accessible.getGroups() != null) {
				for (CommunityGroup group : accessible.getGroups()) {
					if (AccessUtils.checkAccess(user, group, requiredAccessLevels)) {
						return true;
					}
				}
			}

			if (accessible.getSchools() != null) {
				for (School school : accessible.getSchools()) {
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
	
	public static boolean checkReceiptAccess(Receipt receipt, CommunityUser user, Accessible accessible) {

		if(user.isAdmin()){
			
			return true;
			
		}else{
			
			CommunityUser receiptUser = receipt.getUser();
			
			if(receiptUser == null){
				return false;
			}
			
			if ((accessible.isGlobal() || accessible.getGroups() != null) && receiptUser.getCommunityGroups() != null) {
				
				for (CommunityGroup group : receiptUser.getCommunityGroups()) {
					
					if ((accessible.isGlobal() || accessible.getGroups().contains(group)) && AccessUtils.checkAccess(user, group, GroupAccessLevel.PUBLISHER)) {
						return true;
					}
				}
			}
			
			if(accessible.getSchools() != null){
				
				for(School school : accessible.getSchools()){
										
					if (receiptUser.getCommunityGroups() != null) {
						
						for (CommunityGroup group : receiptUser.getCommunityGroups()) {
							
							if(group.getSchool().equals(school) && AccessUtils.checkAccess(user, group, GroupAccessLevel.PUBLISHER)){
								
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static boolean checkGroupAccess(Accessible accessible, CommunityGroup group) {

		if(accessible != null && ((accessible.getGroups() != null && accessible.getGroups().contains(group)) || (accessible.getSchools() != null && accessible.getSchools().contains(group.getSchool())) || accessible.isGlobal())){

			return true;
		}

		return false;
	}
}
