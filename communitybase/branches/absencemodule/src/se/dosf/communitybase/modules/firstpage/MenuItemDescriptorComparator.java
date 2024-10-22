package se.dosf.communitybase.modules.firstpage;

import java.util.Comparator;

import se.unlogic.hierarchy.core.interfaces.MenuItemDescriptor;

public class MenuItemDescriptorComparator implements Comparator<MenuItemDescriptor> {

	public int compare(MenuItemDescriptor m1, MenuItemDescriptor m2) {

		return m1.getName().compareTo(m2.getName());
	}
}
