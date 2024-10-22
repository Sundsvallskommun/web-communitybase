package se.dosf.communitybase.modules.search.beans;

import se.unlogic.hierarchy.core.interfaces.Prioritized;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class SearchType extends GeneratedElementable implements Prioritized {

	@XMLElement
	private String type;

	@XMLElement
	private String name;

	private int priority;
	
	public SearchType() {
	}
	
	public SearchType(String type, String name, int priority) {
		
		this.type = type;
		this.name = name;
		this.priority = priority;
	}

	public String getType() {

		return type;
	}

	public void setType(String type) {

		this.type = type;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	@Override
	public int getPriority() {

		return priority;
	}

	public void setPriority(int priority) {

		this.priority = priority;
	}
}