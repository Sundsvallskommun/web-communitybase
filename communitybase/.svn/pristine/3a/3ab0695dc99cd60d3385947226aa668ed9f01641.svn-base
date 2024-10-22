package se.dosf.communitybase.modules.settingscascade;

import java.sql.SQLException;
import java.util.List;

import se.dosf.communitybase.CBConstants;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.events.CRUDEvent;
import se.unlogic.hierarchy.core.exceptions.CachePreconditionException;
import se.unlogic.hierarchy.core.exceptions.ModuleNotCachedException;
import se.unlogic.hierarchy.core.exceptions.ModuleUpdateException;
import se.unlogic.hierarchy.core.exceptions.ModuleUpdatedInProgressException;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;


public class ModuleSettingsCascadeModule extends AnnotatedForegroundModule {

	@EventListener(channel=SimpleForegroundModuleDescriptor.class)
	public void foregroundModuleUpdated(CRUDEvent<SimpleForegroundModuleDescriptor> event, EventSource source){
		
		if(source == EventSource.LOCAL){
			
			for(SimpleForegroundModuleDescriptor moduleDescriptor : event.getBeans()){

				try {
					List<SimpleForegroundModuleDescriptor> matchingDescriptors = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModulesByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, moduleDescriptor.getModuleID().toString());
					
					if(matchingDescriptors != null){
						
						for(SimpleForegroundModuleDescriptor matchingDescriptor : matchingDescriptors){
							
							if(matchingDescriptor.equals(moduleDescriptor)){
								
								continue;
							}
							
							log.info("Cascading settings from foreground module " + moduleDescriptor + " to foreground module " + matchingDescriptor);
							
							matchingDescriptor.setMutableSettingHandler(moduleDescriptor.getMutableSettingHandler());
							
							try {
								matchingDescriptor.saveSettings(systemInterface);
								
							} catch (SQLException e) {
								log.error("Error updating settings of foreground module " + matchingDescriptor, e);
								continue;
							}
							
							SectionInterface sectionInterface = systemInterface.getSectionInterface(matchingDescriptor.getSectionID());
							
							if(sectionInterface != null && sectionInterface.getForegroundModuleCache().isCached(matchingDescriptor)){
								
								try {
									sectionInterface.getForegroundModuleCache().update(matchingDescriptor);
									
								} catch (ModuleNotCachedException e) {
								} catch (CachePreconditionException e) {
								} catch (ModuleUpdateException e) {
									
									log.error("Error updating instance foreground module " + matchingDescriptor, e);
									
								} catch (ModuleUpdatedInProgressException e) {}
							}
						}
					}
				} catch (SQLException e) {
					
					log.error("Error getting foreground module descriptors matching attribute " + CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE + " and value " + moduleDescriptor.getModuleID().toString(), e);
				}
			}
		}
	}
	
	@EventListener(channel=SimpleBackgroundModuleDescriptor.class)
	public void backgroundModuleUpdated(CRUDEvent<SimpleBackgroundModuleDescriptor> event, EventSource source){
		
		if(source == EventSource.LOCAL){
			
			for(SimpleBackgroundModuleDescriptor moduleDescriptor : event.getBeans()){

				try {
					List<SimpleBackgroundModuleDescriptor> matchingDescriptors = systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().getModulesByAttribute(CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE, moduleDescriptor.getModuleID().toString());

					if(matchingDescriptors != null){
						
						for(SimpleBackgroundModuleDescriptor matchingDescriptor : matchingDescriptors){
							
							if(matchingDescriptor.equals(moduleDescriptor)){
								
								continue;
							}
							
							log.info("Cascading module settings from background module " + moduleDescriptor + " to background module " + matchingDescriptor);
							
							matchingDescriptor.setMutableSettingHandler(moduleDescriptor.getMutableSettingHandler());
							
							try {
								matchingDescriptor.saveSettings(systemInterface);
							} catch (SQLException e) {
								log.error("Error updating settings of background module " + matchingDescriptor, e);
								continue;
							}
							
							SectionInterface sectionInterface = systemInterface.getSectionInterface(matchingDescriptor.getSectionID());
							
							if(sectionInterface != null && sectionInterface.getBackgroundModuleCache().isCached(matchingDescriptor)){
								
								try {
									sectionInterface.getBackgroundModuleCache().update(matchingDescriptor);
									
								} catch (ModuleNotCachedException e) {
								} catch (CachePreconditionException e) {
								} catch (ModuleUpdateException e) {
									
									log.error("Error updating instance background module " + matchingDescriptor, e);
									
								} catch (ModuleUpdatedInProgressException e) {}
							}							
						}
					}
				} catch (SQLException e) {
					
					log.error("Error getting background module descriptors matching attribute " + CBConstants.MODULE_SOURCE_MODULE_ID_ATTRIBUTE + " and value " + moduleDescriptor.getModuleID().toString(), e);
				}
			}			
		}		
	}	
}
