package se.dosf.communitybase.modules.search;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class ContentHit extends GeneratedElementable {

	@XMLElement(fixCase = true)
	private final String alias;

	@XMLElement(fixCase = true)
	private final String title;

	@XMLElement(fixCase = true)
	private final String infoLine;

	@XMLElement(fixCase = true)
	private final String fragment;

	@XMLElement(fixCase = true)
	private final String type;

	@XMLElement(fixCase = true)
	private final String sectionName;

	@XMLElement(fixCase = true)
	private final String sectionAlias;

	public ContentHit(String alias, String title, String infoLine, String fragment, String type, String sectionName, String sectionAlias) {

		super();
		this.alias = alias;
		this.title = title;
		this.infoLine = infoLine;
		this.fragment = fragment;
		this.type = type;
		this.sectionName = sectionName;
		this.sectionAlias = sectionAlias;
	}

	public String getAlias() {

		return alias;
	}

	public String getTitle() {

		return title;
	}

	public String getInfoLine() {

		return infoLine;
	}

	public String getFragment() {

		return fragment;
	}

	public String getType() {

		return type;
	}

	public String getSectionName() {

		return sectionName;
	}

	public String getSectionAlias() {

		return sectionAlias;
	}

}
