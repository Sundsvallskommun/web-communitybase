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
import se.dosf.communitybase.interfaces.Publishable;
import se.dosf.communitybase.modules.AnnotatedCommunityModule;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.xml.XMLUtils;

public class PublishingFactory {

	private CommunityGroupDAO groupDAO;
	
	private CommunitySchoolDAO schoolDAO;
	
	public PublishingFactory(AnnotatedCommunityModule communityModule) {

		groupDAO = communityModule.getGroupDAO();
		schoolDAO = communityModule.getSchoolDAO();
	}
	
	public PublishingFactory(CommunityGroupDAO groupDAO, CommunitySchoolDAO schoolDAO) {
	
		this.groupDAO = groupDAO;
		this.schoolDAO = schoolDAO;
	}
	
	@SuppressWarnings("unchecked")
	public void validateAndSetPublishing(HttpServletRequest req, Publishable publishable, List<ValidationError> validationErrors) throws SQLException {
		
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
				publishable.setGroups(this.groupDAO.getGroups(chosenGroups, false));
			} else {
				publishable.setGroups(null);
			}
			
			if(!chosenSchools.isEmpty()) {
				publishable.setSchools(this.schoolDAO.getSchools(chosenSchools));
			} else {
				publishable.setSchools(null);
			}
			
			publishable.setGlobal(false);
			
		} else {
			publishable.setGlobal(true);
		}
		
	}
	
	public Element getPublisherXML(CommunityUser user, Document doc) throws SQLException {

		Element schoolsElement = doc.createElement("schools");
		
		if(!user.isAdmin()) {
			
			Map<CommunityGroup, GroupRelation> groups = user.getGroupMap();
			
			List<School> schoolAdmins = user.getSchools() != null ? user.getSchools() : new ArrayList<School>();
			
			HashMap<School, Element> addedSchools = new HashMap<School, Element>();
			
			if (groups != null && !groups.isEmpty()) {
				
				for (Entry<CommunityGroup, GroupRelation> entry : groups.entrySet()) {
	
					if(entry.getKey().getGroupID() > -1) {
					
						School school = entry.getKey().getSchool();
						
						if (!schoolAdmins.contains(school) && entry.getValue().getAccessLevel() != GroupAccessLevel.MEMBER) {
						
							if (addedSchools.containsKey(school)) {
								
								addedSchools.get(school).appendChild(entry.getKey().toXML(doc));
								
							} else {
								
								Element groupsElement = doc.createElement("groups");
								
								groupsElement.appendChild(entry.getKey().toXML(doc));
								
								addedSchools.put(school, groupsElement);
								
							}
						
						} else if(entry.getValue().getAccessLevel() != GroupAccessLevel.MEMBER) {
		
							Element groupsElement = doc.createElement("groups");
							
							XMLUtils.append(doc, groupsElement, this.groupDAO.getGroups(school));
							
							addedSchools.put(school, groupsElement);
							
						}
					
					}
					
				}
				
			}
			
			for (School schoolAdmin : schoolAdmins) {
				
				if (!addedSchools.containsKey(schoolAdmin)) {
					
					Element groupsElement = doc.createElement("groups");
					
					XMLUtils.append(doc, groupsElement, this.groupDAO.getGroups(schoolAdmin));
					
					addedSchools.put(schoolAdmin, groupsElement);
				}
				
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
