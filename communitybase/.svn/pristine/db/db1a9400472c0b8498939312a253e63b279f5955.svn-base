package se.dosf.communitybase.modules.search;

import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement
public class SectionHit extends GeneratedElementable {

	@XMLElement(fixCase = true)
	private final Integer sectionID;

	@XMLElement(fixCase = true)
	private final String fullAlias;

	@XMLElement(fixCase = true)
	private final String name;

	@XMLElement(fixCase = true)
	private final String fragment;

	@XMLElement(fixCase = true)
	private final Integer memberCount;

	@XMLElement(fixCase = true)
	private final String accessMode;

	public SectionHit(Integer sectionID, String fullAlias, String name, String fragment, Integer memberCount, String accessMode) {

		super();
		this.sectionID = sectionID;
		this.fullAlias = fullAlias;
		this.name = name;
		this.fragment = fragment;
		this.memberCount = memberCount;
		this.accessMode = accessMode;
	}

	public String getFullAlias() {

		return fullAlias;
	}

	public String getName() {

		return name;
	}

	public String getFragment() {

		return fragment;
	}

	public Integer getMemberCount() {

		return memberCount;
	}

	public Integer getSectionID() {

		return sectionID;
	}

	public String getAccessMode() {

		return accessMode;
	}
	
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((sectionID == null) ? 0 : sectionID.hashCode());
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
		SectionHit other = (SectionHit) obj;
		if (sectionID == null) {
			if (other.sectionID != null)
				return false;
		} else if (!sectionID.equals(other.sectionID))
			return false;
		return true;
	}
}
