package se.dosf.communitybase.utils;

import java.util.List;
import java.util.Map.Entry;

import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;

public class ModuleUtils {

	@SuppressWarnings("unchecked")
	public static <T extends ForegroundModule> T getCachedModule(SectionInterface sectionInterface, Class<T> clazz) {

		List<Entry<ForegroundModuleDescriptor, ForegroundModule>> sectionModules = sectionInterface.getForegroundModuleCache().getCachedModules();

		for (Entry<ForegroundModuleDescriptor, ForegroundModule> entry : sectionModules) {

			if (entry.getValue().getClass().equals(clazz)) {

				return (T) entry.getValue();
			}
		}

		return null;
	}
}
