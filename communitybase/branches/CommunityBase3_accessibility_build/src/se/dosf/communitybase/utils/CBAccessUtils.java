package se.dosf.communitybase.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.Posted;
import se.dosf.communitybase.interfaces.Role;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.attributes.AttributeHandler;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.xml.XMLUtils;

public class CBAccessUtils {
	
	public static boolean hasAddContentAccess(User user, Role role) {

		return role != null && role.hasAddContentAccess();
	}

	public static boolean hasUpdateContentAccess(User user, Posted bean, Role role) {

		if (user == null || role == null) {

			return false;
		}

		if (role.hasUpdateOwnContentAccess() && bean.getPoster() != null && bean.getPoster().equals(user)) {

			return true;
		}

		if (role.hasUpdateOtherContentAccess()) {

			return true;
		}

		return false;
	}

	public static boolean hasDeleteContentAccess(User user, Posted bean, Role role) {

		if (user == null || role == null) {

			return false;
		}

		if (role.hasDeleteOwnContentAccess() && bean.getPoster() != null && bean.getPoster().equals(user)) {

			return true;
		}

		if (role.hasDeleteOtherContentAccess()) {

			return true;
		}

		return false;
	}
	
	public static boolean hasAddCommentAccess(User user, Role role) {

		return role != null && role.hasAddCommentAccess();
	}
	
	public static boolean hasManageModulesAccess(User user, Role role) {

		return role != null && role.hasManageModulesAccess();
	}

	public static void appendBeanAccess(Document doc, Element targetElement, User user, Posted bean, Role role) {

		if(hasUpdateContentAccess(user, bean, role)) {
			XMLUtils.appendNewElement(doc, targetElement, "hasUpdateAccess", true);
		}

		if(hasDeleteContentAccess(user, bean, role)) {
			XMLUtils.appendNewElement(doc, targetElement, "hasDeleteAccess", true);
		}

	}

	public static boolean isExternalUser(User user){

		AttributeHandler attributeHandler = user.getAttributeHandler();

		if(attributeHandler != null && attributeHandler.getPrimitiveBoolean(CBConstants.EXTERNAL_USER_ATTRIBUTE)){

			return true;
		}

		return false;
	}

	public static List<Integer> getUserSections(User user, CBInterface cbInterface){
		
		//TODO append more groups if sectionTypeAdmin when called from search module

		Collection<Group> groups = user.getGroups();

		if (groups != null) {

			List<Integer> sectionIDs = new ArrayList<Integer>(groups.size());

			for (Group group : groups) {

				AttributeHandler attributeHandler = group.getAttributeHandler();

				if (attributeHandler != null) {

					Integer sectionID = attributeHandler.getInt(CBConstants.GROUP_SECTION_ID_ATTRIBUTE);

					if (sectionID != null) {
						sectionIDs.add(sectionID);
					}

				}
				
				List<Integer> groupSectionIDs = cbInterface.getGroupSections(group.getGroupID());
				
				if (groupSectionIDs != null) {
					sectionIDs.addAll(groupSectionIDs);
				}

			}

			if (!sectionIDs.isEmpty()){

				return CollectionUtils.removeDuplicates(sectionIDs);
			}
		}

		return null;
	}
}
