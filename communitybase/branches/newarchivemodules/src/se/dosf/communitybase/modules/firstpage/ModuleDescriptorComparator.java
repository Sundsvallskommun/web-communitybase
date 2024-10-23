package se.dosf.communitybase.modules.firstpage;

import java.util.Comparator;

import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;

public class ModuleDescriptorComparator implements Comparator<ForegroundModuleDescriptor>{

	@Override
	public int compare(ForegroundModuleDescriptor m1, ForegroundModuleDescriptor m2) {

		return m1.getName().compareTo(m2.getName());
	}

}
