package honours_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalSearch extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		for (Individual ind : individuals) {
			Individual neighbour = null;
			
			List<Event> unplacedEvents = new ArrayList<Event>();
			
			for (Event event : ind.getUnplacedEvents().values()) {
				unplacedEvents.add(event);
			}
			
			for (Event event : unplacedEvents) {
				List<Slot> nPermutation = null;
				int step = 0;
				boolean end = false;
				
				while (!end) {
					end = true;
					nPermutation = new ArrayList<Slot>();
					int temp = 0;
					
					for (Slot slot : ind.getPermutation()) {
						temp++;
						if (slot.getAllocatedEvent() != null && temp > step) {
							if (slot.getPossibleEvents().contains(event.getId())) {
								nPermutation.add(0, new Slot(slot.getRoomId(), slot.getSlotId(), slot.getPossibleEvents()));
								step = temp;
								end = false;
							} else {
								nPermutation.add(new Slot(slot.getRoomId(), slot.getSlotId(), slot.getPossibleEvents()));
							}
						} else {
							nPermutation.add(new Slot(slot.getRoomId(), slot.getSlotId(), slot.getPossibleEvents()));
						}
					}
					
					neighbour = new Individual(nPermutation);
	
					System.out.println("C: " + ind.unplacedEventsNumber() + " N: " + neighbour.unplacedEventsNumber());
					
					if (!ind.isBetter(neighbour)) {
						break;
					}
				}
				
				if (!ind.isBetter(neighbour)) {
					System.out.println("BETTER");
					List<Slot> permutation = new ArrayList<Slot>();
					
					for (Slot slot : neighbour.getPermutation()) {
						permutation.add(new Slot(slot.getRoomId(), slot.getSlotId(), slot.getPossibleEvents()));
					}
					List<Room> rooms = new ArrayList<Room>();
					for (int i = 0; i < Evolution.ROOMS_NUMBER; i++) {
						Room room = new Room();
						
						rooms.add(room);
					}
					
					Map<Integer, Event> e = new HashMap<Integer, Event>();
					for (Event eventt : Evolution.events) {
						e.put(eventt.getId(), eventt);
					}
					
					ind.setPermutation(permutation);
					ind.setUnplacedEvents(e);
					ind.setRooms(rooms);
					
					ind.evaluate();
					
				}
			}
			
		}
		
		return null;
	}

}
