package se.dosf.communitybase.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.interfaces.ForegroundModuleConfiguration;
import se.dosf.communitybase.interfaces.SectionType;
import se.unlogic.hierarchy.core.beans.Bundle;
import se.unlogic.hierarchy.core.beans.MenuItem;
import se.unlogic.hierarchy.core.beans.ModuleMenuItem;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.menu.MenuSorter;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;


public class CBMenuSorter implements MenuSorter {

	private final SectionInterface sectionInterface;
	private final SectionType sectionType;

	public CBMenuSorter(SectionInterface sectionInterface, SectionType sectionType) {

		super();
		this.sectionInterface = sectionInterface;
		this.sectionType = sectionType;
	}

	@Override
	public void sort(List<MenuItem> menuItems) {

		//Get menuitems in current order
		ArrayList<MenuItem> originalMenuItemList = new ArrayList<MenuItem>(sectionInterface.getMenuCache().getMenuItemSet());

		//Put all menuIndex values into separate list
		ArrayList<Integer> menuIndexList = new ArrayList<Integer>(originalMenuItemList.size());

		for(MenuItem menuItem : originalMenuItemList){

			menuIndexList.add(menuItem.getMenuIndex());
		}

		//Create new list for reordered menuitems
		ArrayList<MenuItem> updatedMenuItemList = new ArrayList<MenuItem>(originalMenuItemList.size());

		List<ForegroundModuleDescriptor> moduleDescriptors = sectionInterface.getForegroundModuleCache().getCachedModuleDescriptors();

		//Sort modules based on menuIndex in module configuration of section type
		for(ForegroundModuleConfiguration foregroundModuleConfiguration : sectionType.getSupportedForegroundModules()){

			Integer translatedModuleID = getTranslatedModuleID(foregroundModuleConfiguration.getModuleID(), moduleDescriptors);

			if(translatedModuleID != null){

				Iterator<MenuItem> iterator = originalMenuItemList.iterator();

				while(iterator.hasNext()){

					MenuItem menuItem = iterator.next();

					if((menuItem instanceof ModuleMenuItem && translatedModuleID.equals(((ModuleMenuItem)menuItem).getModuleID())) || (menuItem instanceof Bundle && translatedModuleID.equals(((Bundle)menuItem).getModuleID()))){

						updatedMenuItemList.add(menuItem);
						iterator.remove();
						break;
					}
				}
			}
		}

		//Transfer remaining menuitems to new list
		if(!originalMenuItemList.isEmpty()){

			updatedMenuItemList.addAll(originalMenuItemList);
		}

		//Set menuIndex
		int index = 0;

		while(index < menuIndexList.size()){

			updatedMenuItemList.get(index).setMenuIndex(menuIndexList.get(index));
			
			index++;
		}
	}

	private static Integer getTranslatedModuleID(Integer moduleID, List<ForegroundModuleDescriptor> moduleDescriptors) {

		for(ForegroundModuleDescriptor moduleDescriptor : moduleDescriptors){

			Integer sourceModuleID = moduleDescriptor.getAttributeHandler().getInt(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE);

			if(moduleID.equals(sourceModuleID)){

				return moduleDescriptor.getModuleID();
			}
		}

		return null;
	}
}
