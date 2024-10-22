package se.dosf.communitybase.beans;

import java.sql.Timestamp;

import se.dosf.communitybase.enums.EventFormat;
import se.dosf.communitybase.interfaces.EventTransformer;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.standardutils.xml.XMLElement;

@XMLElement(name = "SectionEvent")
public class TransformedSectionEvent<T> extends SectionEvent {

	private final T bean;
	private final EventTransformer<T> eventTransformer;
	private final String eventType;

	private ViewFragment emailFragment;
	private ViewFragment smallFragment;
	private ViewFragment largeFragment;

	public TransformedSectionEvent(int moduleID, Timestamp timestamp, T bean, EventTransformer<T> eventFragmentTransformer, String eventType) {

		super(moduleID, timestamp);
		this.bean = bean;
		this.eventTransformer = eventFragmentTransformer;
		this.eventType = eventType;
	}

	@Override
	public ViewFragment getFragment(String fullContextPath, EventFormat format) throws Exception {

		if(format == EventFormat.EMAIL) {

			if(emailFragment == null) {

				emailFragment = eventTransformer.getFragment(bean, EventFormat.EMAIL, fullContextPath, eventType);
			}

			return emailFragment;

		} else if(format == EventFormat.SMALL) {

			if(smallFragment == null) {

				smallFragment = eventTransformer.getFragment(bean, EventFormat.SMALL, fullContextPath, eventType);
			}

			return smallFragment;

		} else if(format == EventFormat.LARGE) {

			if(largeFragment == null) {

				largeFragment = eventTransformer.getFragment(bean, EventFormat.LARGE, fullContextPath, eventType);
			}

			return largeFragment;

		}

		throw new RuntimeException("Unsupported event format: " + format);
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((bean == null) ? 0 : bean.hashCode());
		result = prime * result + ((eventType == null) ? 0 : eventType.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {

		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		TransformedSectionEvent other = (TransformedSectionEvent) obj;
		if(bean == null) {
			if(other.bean != null) {
				return false;
			}
		} else if(!bean.equals(other.bean)) {
			return false;
		}
		if(eventType == null) {
			if(other.eventType != null) {
				return false;
			}
		} else if(!eventType.equals(other.eventType)) {
			return false;
		}
		return true;
	}

	public T getBean() {

		return bean;
	}

	public String getEventType() {

		return eventType;
	}

}
