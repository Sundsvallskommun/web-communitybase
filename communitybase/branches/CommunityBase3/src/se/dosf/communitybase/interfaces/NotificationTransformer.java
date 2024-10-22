package se.dosf.communitybase.interfaces;

import se.dosf.communitybase.enums.NotificationFormat;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;


public interface NotificationTransformer {

	public ViewFragment getFragment(Notification notification, NotificationFormat format, String fullContextPath) throws Exception;
}
