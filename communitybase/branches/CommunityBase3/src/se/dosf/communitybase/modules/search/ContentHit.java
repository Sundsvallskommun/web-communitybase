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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContentHit other = (ContentHit) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		return true;
	}

}
