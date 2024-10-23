package se.dosf.communitybase.utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.CommunityUser;
import se.dosf.communitybase.beans.GroupRelation;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.daos.CommunityGroupDAO;
import se.dosf.communitybase.daos.CommunitySchoolDAO;
import se.dosf.communitybase.enums.GroupAccessLevel;
import se.dosf.communitybase.interfaces.Accessible;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;

public class AccessibleFactory {

	private CommunityGroupDAO groupDAO;
	
	private CommunitySchoolDAO schoolDAO;
	
	public AccessibleFactory(AnnotatedCommunityModule communityModule) {

		groupDAO = communityModule.getGroupDAO();
		schoolDAO = communityModule.getSchoolDAO();
	}
	
	public AccessibleFactory(CommunityGroupDAO groupDAO, CommunitySchoolDAO schoolDAO) {
	
		this.groupDAO = groupDAO;
		this.schoolDAO = schoolDAO;
	}
	
	@SuppressWarnings("unchecked")
	public void validateAndSetAccess(HttpServletRequest req, Accessible accessible, List<ValidationError> validationErrors, CommunityUser user) throws SQLException, AccessDeniedException {
		
		if (req.getParameter("global") == null) {

			List<Integer> chosenGroups = new ArrayList<Integer>();
			
			List<Integer> chosenSchools = new ArrayList<Integer>();
			
			String[] schoolParams = req.getParameterValues("schoolbase");

			if (schoolParams != null) {
				chosenSchools.addAll(NumberUtils.toInt(schoolParams));
			}

			Enumeration<String> names = req.getParameterNames();

			while (names.hasMoreElements()) {

				String name = names.nextElement();

				if (name.startsWith("groupschool")) {

					String[] groupParams = req.getParameterValues(name);

					for (String group : groupParams) {

						String[] schoolAndGroup = group.split(":");

						if (!chosenSchools.contains(NumberUtils.toInt(schoolAndGroup[0]))) {
							chosenGroups.add(NumberUtils.toInt(schoolAndGroup[1]));
						}

					}

				}

			}

			if (chosenGroups.isEmpty() && chosenSchools.isEmpty()) {
				validationErrors.add(new ValidationError("NoGroup"));
				return;
			}

			if(!chosenGroups.isEmpty()) {
				List<CommunityGroup> groups = this.groupDAO.getGroups(chosenGroups, true);
				
				for(CommunityGroup group : groups){

					if (!AccessUtils.checkAccess(user, group, GroupAccessLevel.PUBLISHER)) {
						throw new AccessDeniedException("User does not have publish access to requested group " + group);
					}
				}
				
				accessible.setGroups(groups);
			} else {
				accessible.setGroups(null);
			}
			
			if(!chosenSchools.isEmpty()) {
				List<School> schools = this.schoolDAO.getSchools(chosenSchools);

				for (School school : schools) {

					if (!AccessUtils.checkAccess(user, school)) {
						throw new AccessDeniedException("User does not have publish access to requested school " + school);
					}
				}

				accessible.setSchools(schools);
			} else {
				accessible.setSchools(null);
			}
			
			accessible.setGlobal(false);
			
		} else {
			if (!user.isAdmin()) {
				throw new AccessDeniedException("User attempted to set global flag when not an admin");
			}
			accessible.setGlobal(true);
		}
		
	}
	
	public Element getAccessibleXML(CommunityUser user, Document doc) throws SQLException {

		return getAccessibleXML(user, doc, new GroupAccessLevel[] { GroupAccessLevel.ADMIN, GroupAccessLevel.PUBLISHER });
	}

	public Element getAccessibleXML(CommunityUser user, Document doc, GroupAccessLevel... requiredAccessLevels) throws SQLException {

		Element schoolsElement = doc.createElement("schools");
		
		if(!user.isAdmin()) {
			
			Map<CommunityGroup, GroupRelation> groups = user.getGroupMap();
			
			List<School> schoolAdmins = user.getSchools() != null ? user.getSchools() : new ArrayList<School>();

			HashMap<School, Element> addedSchools = new HashMap<School, Element>();

			if (groups != null && !groups.isEmpty()) {
				
				List<Integer> requiredAccessLevelsL = new ArrayList<Integer>(requiredAccessLevels.length);

				for (int i = 0; i < requiredAccessLevels.length; i++) {

					requiredAccessLevelsL.add(requiredAccessLevels[i].ordinal());
				}

				for (Entry<CommunityGroup, GroupRelation> entry : groups.entrySet()) {
	
					if(entry.getKey().getGroupID() > -1) {
					
						School school = entry.getKey().getSchool();
						
						if (!schoolAdmins.contains(school) && requiredAccessLevelsL.contains(entry.getValue().getAccessLevel().ordinal())) {

							if (addedSchools.containsKey(school)) {
								
								addedSchools.get(school).appendChild(entry.getKey().toXML(doc));
								
							} else {
								
								Element groupsElement = doc.createElement("groups");
								
								groupsElement.appendChild(entry.getKey().toXML(doc));
								
								addedSchools.put(school, groupsElement);
								
							}
						
						}
					
					}
					
				}
				
			}
			
			for (School school : schoolAdmins) {
				
				Element groupsElement = doc.createElement("groups");

				XMLUtils.append(doc, groupsElement, this.groupDAO.getGroups(school));
				
				addedSchools.put(school, groupsElement);
			}
			
			if (!addedSchools.isEmpty()) {
			
				for (School school : addedSchools.keySet()) {
					
					Element schoolElement = school.toXML(doc);
					
					schoolElement.appendChild(addedSchools.get(school));
					
					schoolsElement.appendChild(schoolElement);
					
				}
			
			} else {
				return null;	
			}
			
		} else {
			
			List<School> schools = this.schoolDAO.getSchools(false, true);
			
			if (schools != null) {
				XMLUtils.append(doc, schoolsElement, schools);
			}
			
		}
	
		return schoolsElement;
	
	}
	
}
