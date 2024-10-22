package se.dosf.communitybase.modules.sectionevents;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import se.dosf.communitybase.beans.SectionEvent;
import se.dosf.communitybase.interfaces.SectionEventProvider;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.SystemStartupListener;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.SectionStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.listeners.ForegroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.listeners.SectionCacheListener;
import se.unlogic.hierarchy.core.interfaces.modules.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.KeyAlreadyCachedException;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.time.MillisecondTimeUnits;
import se.unlogic.webutils.http.URIParser;

public class CBSectionEventHandler extends AnnotatedForegroundModule implements SectionCacheListener, ForegroundModuleCacheListener {

	private Logger log = Logger.getLogger(this.getClass());

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max event queue size", description = "The maximum number of events to queue for each section", required = true)
	private Integer maxEventQueueSize = 200;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Min event queue size", description = "If the number of events in the queue goes below this refill the handler will try to refill it", required = true)
	private Integer minEventQueueSize = 180;
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Back dating days", description = "Days do backdate lastlogin for new users with no previous login date", required = true)
	private Integer backDatingDays = 5;

	private final ConcurrentHashMap<Integer, EventQueue> sectionEventMap = new ConcurrentHashMap<Integer, EventQueue>();

	private Lock cacheLock;

	private AtomicInteger atomicEventQueueSize;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		//This lock is used to lock caching and unloading of modules and sections while the section tree is being scanned
		cacheLock = new ReentrantLock();
	}

	@Override
	protected void moduleConfigured() throws Exception {

		atomicEventQueueSize = new AtomicInteger(maxEventQueueSize);
	}

	private void scanSection(SectionInterface section, boolean recusive) {

		if (log.isDebugEnabled()) {

			log.debug("Scanning section " + section.getSectionDescriptor());
		}

		EventQueue eventQueue = sectionEventMap.get(section.getSectionDescriptor().getSectionID());

		if(eventQueue == null){

			eventQueue = new EventQueue(atomicEventQueueSize);

			sectionEventMap.put(section.getSectionDescriptor().getSectionID(), eventQueue);
		}

		ArrayList<Entry<ForegroundModuleDescriptor, ForegroundModule>> foregroundModules = section.getForegroundModuleCache().getCachedModules();

		for (Entry<ForegroundModuleDescriptor, ForegroundModule> moduleEntry : foregroundModules) {

			if (moduleEntry.getValue() instanceof SectionEventProvider) {

				addModuleEvents(moduleEntry.getKey(), (SectionEventProvider) moduleEntry.getValue(), eventQueue);
			}
		}

		if (recusive) {

			for (Section subSection : section.getSectionCache().getSections()) {

				scanSection(subSection, true);
			}
		}
	}

	private void addModuleEvents(ForegroundModuleDescriptor moduleDescriptor, SectionEventProvider moduleInstance, EventQueue eventQueue) {

		SectionEvent oldestEvent = eventQueue.getOldestEvent();

		Timestamp breakpoint;

		if (oldestEvent != null) {

			breakpoint = oldestEvent.getTimestamp();

		} else {

			breakpoint = null;
		}

		List<SectionEvent> moduleEvents;

		try {
			moduleEvents = moduleInstance.getEvents(breakpoint, this.maxEventQueueSize);

		} catch (Exception e) {

			log.error("Error getting events from module " + moduleDescriptor, e);
			return;
		}

		if (moduleEvents != null) {

			//TODO change to debug
			if (log.isInfoEnabled()) {

				log.info("Got " + moduleEvents.size() + " events from module " + moduleDescriptor + " in section " + moduleDescriptor.getSectionID());
			}

			eventQueue.addEvents(moduleEvents);
		}
	}

	@Override
	public void unload() throws Exception {

		systemInterface.getInstanceHandler().removeInstance(CBSectionEventHandler.class, this);

		systemInterface.removeSectionCacheListener(this);
		systemInterface.removeForegroundModuleCacheListener(this);

		super.unload();
	}

	public void addEvent(Integer sectionID, SectionEvent event) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		EventQueue queue = sectionEventMap.get(sectionID);

		if (queue == null) {

			log.warn("Unable to add event " + event + " to event queue for sectionID " + sectionID + ", section not found in map");
			return;
		}

		queue.addEvent(event);
	}

	public void addEvents(Integer sectionID, List<SectionEvent> events) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		EventQueue queue = sectionEventMap.get(sectionID);

		if (queue == null) {

			log.warn("Unable to add events " + events + " to event queue for sectionID " + sectionID + ", section not found in map");
			return;
		}

		queue.addEvents(events);
	}

	public List<SectionEvent> getEvents(Integer sectionID, int startIndex, int count) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		if (startIndex < 0) {

			throw new RuntimeException("startIndex has to be >= 0");
		}

		if (count < 1) {

			throw new RuntimeException("count has to be >= 1");
		}

		EventQueue queue = sectionEventMap.get(sectionID);

		if (queue == null) {

			return null;
		}

		return queue.getEvents(startIndex, count);
	}
	
	public int getLatestEvents(Integer sectionID, Timestamp lastLogin) {

		List<SectionEvent> events = getEvents(sectionID, 0, maxEventQueueSize);
		
		if (!CollectionUtils.isEmpty(events)) {
			int counter = 0;
			
			if (lastLogin == null) {
				lastLogin = new Timestamp(System.currentTimeMillis() - MillisecondTimeUnits.DAY * backDatingDays);
			}
			
			for (SectionEvent event : events) {
				if (event.getTimestamp().before(lastLogin)) {
					break;
				}
				
				counter++;
			}
			
			return counter;
		}
		
		return 0;
	}

	public int getEventsCount(Integer sectionID) {
		
		EventQueue queue = sectionEventMap.get(sectionID);

		if (queue == null) {

			return 0;
		}

		return queue.size();
	}	
	
	public void replaceEvent(Integer sectionID, SectionEvent event) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		EventQueue eventQueue = sectionEventMap.get(sectionID);

		if (eventQueue == null) {

			return;
		}

		eventQueue.replaceEvent(event);
	}

	public void replaceEvent(Integer sectionID, Collection<SectionEvent> events) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		EventQueue eventQueue = sectionEventMap.get(sectionID);

		if (eventQueue == null) {

			return;
		}

		eventQueue.replaceEvents(events);
	}

	public void removeEvent(Integer sectionID, SectionEvent event) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		EventQueue eventQueue = sectionEventMap.get(sectionID);

		if (eventQueue == null) {

			return;
		}

		int oldQueueSize = eventQueue.size();

		eventQueue.removeEvent(event);

		if (oldQueueSize < eventQueue.size() && eventQueue.size() < minEventQueueSize) {

			//Events were removed beyond min size, refill the queue
			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface != null) {
				scanSection(sectionInterface, false);
			}
		}
	}

	public void removeEvent(Integer sectionID, Collection<SectionEvent> events) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		EventQueue eventQueue = sectionEventMap.get(sectionID);

		if (eventQueue == null) {

			return;
		}

		int oldQueueSize = eventQueue.size();

		eventQueue.removeEvents(events);

		if (oldQueueSize < eventQueue.size() && eventQueue.size() < minEventQueueSize) {

			//Events were removed beyond min size, refill the queue
			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface != null) {
				scanSection(sectionInterface, false);
			}
		}
	}

	@Override
	public void sectionCached(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyAlreadyCachedException {

		Lock lock = this.cacheLock;

		if (lock != null) {

			lock.lock();
		}

		try {
			sectionEventMap.putIfAbsent(sectionInstance.getSectionDescriptor().getSectionID(), new EventQueue(atomicEventQueueSize));

		} finally {

			if (lock != null) {

				lock.unlock();
			}
		}
	}

	@Override
	public void sectionUpdated(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

	}

	@Override
	public void sectionUnloaded(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

		Lock lock = this.cacheLock;

		if (lock != null) {

			lock.lock();
		}

		try {
			sectionEventMap.remove(sectionDescriptor.getSectionID());

		} finally {

			if (lock != null) {

				lock.unlock();
			}
		}

	}

	@Override
	public void moduleCached(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

		Lock lock = this.cacheLock;

		if (lock != null) {

			lock.lock();
		}

		try {

			if (moduleInstance instanceof SectionEventProvider) {

				EventQueue eventQueue = this.sectionEventMap.get(moduleDescriptor.getSectionID());

				if (eventQueue == null) {

					log.warn("Unable to add events from module " + moduleDescriptor + " to event queue for sectionID " + moduleDescriptor.getSectionID() + ", section not found in map");
					return;
				}

				addModuleEvents(moduleDescriptor, (SectionEventProvider) moduleInstance, eventQueue);
			}

		} finally {

			if (lock != null) {

				lock.unlock();
			}
		}

	}

	@Override
	public void moduleUpdated(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

		Lock lock = this.cacheLock;

		if (lock != null) {

			lock.lock();
		}

		try {
			if (moduleInstance instanceof SectionEventProvider) {

				EventQueue eventQueue = this.sectionEventMap.get(moduleDescriptor.getSectionID());

				if (eventQueue == null) {

					log.warn("Unable to update events from module " + moduleDescriptor + " in event queue for sectionID " + moduleDescriptor.getSectionID() + ", section not found in map");
					return;
				}

				eventQueue.removeModuleEvents(moduleDescriptor.getModuleID());

				addModuleEvents(moduleDescriptor, (SectionEventProvider) moduleInstance, eventQueue);
			}

		} finally {

			if (lock != null) {

				lock.unlock();
			}
		}

	}

	@Override
	public void moduleUnloaded(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

		Lock lock = this.cacheLock;

		if (lock != null) {

			lock.lock();
		}

		try {

			if (moduleInstance instanceof SectionEventProvider) {

				EventQueue eventQueue = this.sectionEventMap.get(moduleDescriptor.getSectionID());

				if (eventQueue == null) {

					//log.warn("Unable to delete events from module " + moduleDescriptor + " in event queue for sectionID " + moduleDescriptor.getSectionID() + ", section not found in map");

					return;
				}

				SectionInterface sectionInterface = systemInterface.getSectionInterface(moduleDescriptor.getSectionID());

				if (sectionInterface == null) {

					log.warn("Unable to find section interface for section " + moduleDescriptor.getSectionID());

					return;
				}

				SectionStatus sectionStatus = sectionInterface.getSectionStatus();

				//If the section is going down, don't refill the queue
				if (sectionStatus == SectionStatus.STOPPING || sectionStatus == SectionStatus.STOPPED) {

					eventQueue.removeModuleEvents(moduleDescriptor.getModuleID());

					return;
				}

				int oldQueueSize = eventQueue.size();

				eventQueue.removeModuleEvents(moduleDescriptor.getModuleID());

				if (oldQueueSize < eventQueue.size() && eventQueue.size() < minEventQueueSize) {

					//Events were removed beyond min size, refill the queue
					scanSection(sectionInterface, false);
				}
			}

		} finally {

			if (lock != null) {

				lock.unlock();
			}
		}
	}

	public Integer getMaxEventQueueSize() {

		return maxEventQueueSize;
	}

	public Integer getMinEventQueueSize() {

		return minEventQueueSize;
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " listing section event statistics");

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<div class='contentitem'>");
		stringBuilder.append("<h1>Cached events per section</h1><br>");

		stringBuilder.append("<table class='border fifty'><tr><th>Section ID</th><th>Events</th></tr>");

		for(Entry<Integer, EventQueue> entry : sectionEventMap.entrySet()){

			stringBuilder.append("<tr><td>" + entry.getKey() + "</td><td>" + entry.getValue().size() + "</td></tr>");
		}

		stringBuilder.append("</table></div>");

		return new SimpleForegroundModuleResponse(stringBuilder.toString(), moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	public void filterEvents(Integer sectionID, Integer moduleID, EventFilter filter) {

		if (sectionID == null) {

			throw new NullPointerException("sectionID cannot be null");
		}

		if (moduleID == null) {

			throw new NullPointerException("moduleID cannot be null");
		}		
		
		EventQueue eventQueue = sectionEventMap.get(sectionID);

		if (eventQueue == null) {

			return;
		}
		
		eventQueue.filter(moduleID, filter);
	}

	@SystemStartupListener(priority = 800)
	public void systemStarted() {

		cacheLock.lock();

		try {
			log.info("Scanning sections...");
			
			systemInterface.addSectionCacheListener(this);
			systemInterface.addForegroundModuleCacheListener(this);

			//Scan sections and modules
			scanSection(systemInterface.getRootSection(), true);

			if (!systemInterface.getInstanceHandler().addInstance(CBSectionEventHandler.class, this)) {

				throw new RuntimeException("Unable to register " + this.moduleDescriptor + " in global instance handler using key " + CBSectionEventHandler.class.getSimpleName() + ", another instance is already registered using this key.");
			}

		} finally {
			cacheLock.unlock();
			cacheLock = null;
		}

		log.info("Scanned " + sectionEventMap.size() + " sections");
	}
}
