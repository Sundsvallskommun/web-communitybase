package se.dosf.communitybase.modules.filearchive.populators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import se.dosf.communitybase.modules.filearchive.beans.Section;
import se.unlogic.standardutils.dao.BeanResultSetPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;

public class SectionPopulator implements	BeanResultSetPopulator<Section> {

	public Section populate(ResultSet rs) throws SQLException {
		Section filearchive = new Section();
		
		filearchive.setSectionID(rs.getInt("sectionID"));
		filearchive.setParentID(rs.getInt("id"));
		filearchive.setName(rs.getString("name"));
		
		return filearchive;
	}
	public Section populate(HttpServletRequest req) throws ValidationException{
		return this.populate(new Section(), req);
	}


	public Section populate(Section section, HttpServletRequest req) throws ValidationException {
		
		ArrayList<ValidationError> validationErrors = new ArrayList<ValidationError>();
		String name = req.getParameter("sectionname");
		
		if(StringUtils.isEmpty(name)){
			validationErrors.add(new ValidationError("sectionname", ValidationErrorType.RequiredField));
		}
		
		if(!validationErrors.isEmpty()){
			throw new ValidationException(validationErrors);
		}else{
			section.setName(name);
			section.setSectionID(section.getSectionID());
			section.setParentID(section.getParentID());
						
			return section;
		}
	}
}
