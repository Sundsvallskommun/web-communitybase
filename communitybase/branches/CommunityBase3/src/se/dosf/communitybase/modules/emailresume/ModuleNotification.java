package se.dosf.communitybase.modules.emailresume;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class ModuleNotification extends GeneratedElementable {

	@XMLElement
	private final String id;
	
	@XMLElement
	private final String name;

	public ModuleNotification(String id, String name) {

		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {

		return id;
	}

	public String getName() {

		return name;
	}
}
