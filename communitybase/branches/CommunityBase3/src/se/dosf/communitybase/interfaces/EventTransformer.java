package se.dosf.communitybase.interfaces;

import se.dosf.communitybase.enums.EventFormat;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;


public interface EventTransformer<T> {

	public ViewFragment getFragment(T bean, EventFormat format, String fullContextPath, String eventType) throws Exception;
}
