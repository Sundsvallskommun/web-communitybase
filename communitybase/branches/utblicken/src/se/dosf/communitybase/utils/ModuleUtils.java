package se.dosf.communitybase.utils;

import java.util.List;
import java.util.Map.Entry;

import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;

public class ModuleUtils {

	@SuppressWarnings("unchecked")
	public static <T extends ForegroundModule> T getCachedModule(SectionInterface sectionInterface, Class<T> clazz) {

		List<Entry<ForegroundModuleDescriptor, ForegroundModule>> sectionModules = sectionInterface.getForegroundModuleCache().getCachedModuleSet();

		for (Entry<ForegroundModuleDescriptor, ForegroundModule> entry : sectionModules) {

			if (entry.getValue().getClass().equals(clazz)) {

				return (T) entry.getValue();
			}
		}

		return null;
	}
}
