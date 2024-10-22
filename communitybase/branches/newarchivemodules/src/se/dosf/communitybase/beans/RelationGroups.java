package se.dosf.communitybase.beans;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import se.dosf.communitybase.enums.GroupAccessLevel;
import se.unlogic.standardutils.collections.CollectionUtils;

public class RelationGroups {

	public static final CommunityGroup MEMBER_GROUP = new CommunityGroup(-1, GroupAccessLevel.MEMBER.toString(), new School(-1, GroupAccessLevel.MEMBER.toString()));
	public static final CommunityGroup ADMIN_GROUP = new CommunityGroup(-2, GroupAccessLevel.ADMIN.toString(), new School(-2, GroupAccessLevel.ADMIN.toString()));
	public static final CommunityGroup PUBLISHER_GROUP = new CommunityGroup(-3, GroupAccessLevel.PUBLISHER.toString(), new School(-3, GroupAccessLevel.PUBLISHER.toString()));
	public static final CommunityGroup SCHOOLADMIN_GROUP = new CommunityGroup(-4, "SCHOOLADMIN", new School(-4, "SCHOOLADMIN"));
	public static final CommunityGroup GLOBALADMIN_GROUP = new CommunityGroup(-5, "GLOBALADMIN", new School(-5, "GLOBALADMIN"));
	
	public static final List<CommunityGroup> relationGroups = Arrays.asList(MEMBER_GROUP, PUBLISHER_GROUP, ADMIN_GROUP, SCHOOLADMIN_GROUP, GLOBALADMIN_GROUP);
	
	public static List<CommunityGroup> getRelationGroups() {
		
		return relationGroups;

	}
	
	public static List<CommunityGroup> appendRelationGroups(List<CommunityGroup> communityGroups) {
		
		if(!CollectionUtils.isEmpty(communityGroups)) {
			
			communityGroups.addAll(RelationGroups.getRelationGroups());
			
			return communityGroups;
			
		} else {
			
			return new ArrayList<CommunityGroup>(RelationGroups.getRelationGroups());
			
		}
		
	}
	
	public static void setUserRelationGroups(CommunityUser user) {
	
		if(user.isAdmin()) {
			user.addGroup(RelationGroups.GLOBALADMIN_GROUP, new GroupRelation(GroupAccessLevel.ADMIN, null));
		}
		
		if(!CollectionUtils.isEmpty(user.getSchools())) {
			user.addGroup(RelationGroups.SCHOOLADMIN_GROUP, new GroupRelation(GroupAccessLevel.ADMIN, null));
		}
		
		Collection<CommunityGroup> groups = user.getCommunityGroups();
		
		if(!CollectionUtils.isEmpty(groups)) {
			
			boolean adminGroupAdded = false;
			boolean publisherGroupAdded = false;
			boolean memberGroupAdded = false;
			
			for(CommunityGroup group : groups) {
				
				GroupAccessLevel userAccessLevel = user.getAccessLevel(group);
				
				if(!adminGroupAdded && userAccessLevel.equals(GroupAccessLevel.ADMIN)) {
					user.addGroup(RelationGroups.ADMIN_GROUP, new GroupRelation(GroupAccessLevel.ADMIN, null));
					adminGroupAdded = true;
				}
				
				if(!publisherGroupAdded && userAccessLevel.equals(GroupAccessLevel.PUBLISHER)) {
					user.addGroup(RelationGroups.PUBLISHER_GROUP, new GroupRelation(GroupAccessLevel.PUBLISHER, null));
					publisherGroupAdded = true;
				}
				
				if(!memberGroupAdded && userAccessLevel.equals(GroupAccessLevel.MEMBER)) {
					user.addGroup(RelationGroups.MEMBER_GROUP, new GroupRelation(GroupAccessLevel.MEMBER, null));
					memberGroupAdded = true;
				}
				
				if(adminGroupAdded && publisherGroupAdded && memberGroupAdded) {
					break;
				}
				
			}
			
		}
		
	}
}
