package se.dosf.communitybase.modules.notifications;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.dosf.communitybase.interfaces.Notification;
import se.dosf.communitybase.interfaces.NotificationFragment;
import se.unlogic.hierarchy.core.beans.LinkTag;
import se.unlogic.hierarchy.core.beans.ScriptTag;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;

public class SimpleNotificationFragment implements NotificationFragment {

	private final Notification notification;
	private final ViewFragment fragment;

	public SimpleNotificationFragment(Notification notification, ViewFragment fragment) {

		super();
		this.notification = notification;
		this.fragment = fragment;
	}

	public String getHTML() {

		return fragment.getHTML();
	}

	public List<ScriptTag> getScripts() {

		return fragment.getScripts();
	}

	public List<LinkTag> getLinks() {

		return fragment.getLinks();
	}

	public Element toXML(Document doc) {

		return fragment.toXML(doc);
	}

	public Notification getNotification() {

		return notification;
	}

}
