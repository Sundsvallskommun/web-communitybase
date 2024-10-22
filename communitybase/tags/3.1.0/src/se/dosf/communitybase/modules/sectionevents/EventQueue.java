package se.dosf.communitybase.modules.sectionevents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import se.dosf.communitybase.SectionEventComparator;
import se.dosf.communitybase.beans.SectionEvent;
import se.unlogic.standardutils.collections.CollectionUtils;


public class EventQueue {

	private static final SectionEventComparator COMPARATOR = new SectionEventComparator();
	
	private final AtomicInteger maxQueueSize;
	private final Set<SectionEvent> eventSet = Collections.newSetFromMap(new ConcurrentHashMap<SectionEvent, Boolean>());
	private ArrayList<SectionEvent> eventList;

	public EventQueue(AtomicInteger maxQueueSize) {

		super();
		this.maxQueueSize = maxQueueSize;
	}
	
	public void addEvent(SectionEvent event){
		
		if(eventSet.add(event)){

			setUpdated();
		}
	}
	
	public void addEvents(Collection<SectionEvent> events){
		
		if(eventSet.addAll(events)){
		
			setUpdated();
		}
	}	

	public void replaceEvent(SectionEvent event){
		
		eventSet.remove(event);

		addEvent(event);
	}
	
	public void replaceEvents(Collection<SectionEvent> events){
		
		eventSet.remove(events);

		addEvents(events);
	}
	
	protected void removeEvent(SectionEvent event){
		
		if(eventSet.remove(event)){
			
			setUpdated();
		}
	}
	
	protected void removeEvents(Collection<SectionEvent> events){
		
		if(eventSet.remove(events)){
			
			setUpdated();
		}
	}
	
	public void removeModuleEvents(int moduleID){
		
		boolean setUpdated = false;
		
		Iterator<SectionEvent> iterator = eventSet.iterator();
		
		while(iterator.hasNext()){
			
			if(iterator.next().getModuleID() == moduleID){
				
				iterator.remove();
				
				setUpdated = true;
			}
		}
		
		if(setUpdated){
			
			setUpdated();
		}
	}
	
	private synchronized void setUpdated() {

		if(eventSet.isEmpty()){
			
			eventList = null;
			return;
		}
		
		ArrayList<SectionEvent> newEventList = new ArrayList<SectionEvent>(eventSet);
		
		Collections.sort(newEventList, COMPARATOR);
		
		int maxQueueSize = this.maxQueueSize.get();
		
		if(newEventList.size() > maxQueueSize){
			
			newEventList.removeAll(newEventList.subList(maxQueueSize -1, newEventList.size() - 1));
		}
		
		this.eventList = newEventList;
	}
	
	public List<SectionEvent> getEvents(int startIndex, int count){
		
		ArrayList<SectionEvent> eventList = this.eventList;
		
		if(eventList == null || eventList.isEmpty() || startIndex >= eventList.size()){
			
			return null;
		}
		
		int toIndex;
		
		if(startIndex + count >= eventList.size()){
			
			toIndex = eventList.size();
			
		}else{
			
			toIndex = startIndex + count;
		}
		
		return new ArrayList<SectionEvent>(eventList.subList(startIndex, toIndex));
	}

	public SectionEvent getOldestEvent() {

		ArrayList<SectionEvent> eventList = this.eventList;
		
		if(CollectionUtils.isEmpty(eventList)){
			
			return null;
		}
		
		return eventList.get(eventList.size() - 1);
	}

	public int size() {

		return CollectionUtils.getSize(eventList);
	}

	public void filter(Integer moduleID, EventFilter filter) {
		
		boolean setUpdated = false;

		Iterator<SectionEvent> iterator = eventSet.iterator();
		
		while(iterator.hasNext()){
			
			SectionEvent event = iterator.next();
			
			if(event.getModuleID() == moduleID && filter.deleteEvent(event)){
				
				iterator.remove();
				setUpdated = true;
			}
		}
			
		if(setUpdated){
			
			setUpdated();
		}			
	}
}
