package se.dosf.communitybase.modules.absence.beans;

import java.sql.Date;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.beans.CommunityGroup;
import se.dosf.communitybase.beans.School;
import se.dosf.communitybase.interfaces.Publishable;
import se.unlogic.standardutils.dao.enums.Order;
import se.unlogic.standardutils.xml.Elementable;
import se.unlogic.standardutils.xml.XMLElement;
import se.unlogic.standardutils.xml.XMLGenerator;

@XMLElement
public class AbsenceSearch implements Elementable, Publishable {

	@XMLElement
	private Date startDate;
	
	@XMLElement
	private Date endDate;
	
	@XMLElement
	private List<CommunityGroup> groups;
	
	@XMLElement
	private List<School> schools;
	
	@XMLElement
	private boolean global;
	
	@XMLElement
	private String orderBy;

	@XMLElement
	private Order order;
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setGroups(List<CommunityGroup> groups) {
		this.groups = groups;		
	}

	public List<CommunityGroup> getGroups() {
		
		return this.groups;
	}

	public void setSchools(List<School> schools) {
		
		this.schools = schools;
	}

	public List<School> getSchools() {
		
		return this.schools;
	}

	public void setGlobal(boolean global) { 
		
		this.global = global;
	}

	public boolean isGlobal() {
		
		return this.global;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	public Element toXML(Document doc) {
		
		return XMLGenerator.toXML(this, doc);
	}
	
}
