package honours_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiSwapMutation extends Operator {
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		
		for (Individual ind : individuals) {
			
			ind.evaluate();
			
			int[] permutation = new int[Evolution.eventsNumber];
			Slot[] eventsSlotsCopy = new Slot[Evolution.eventsNumber];
			Slot[] reservedEventsSlotsCopy = new Slot[Evolution.eventsNumber];
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
				eventsSlotsCopy[i] = ind.eventsSlots[i];
				reservedEventsSlotsCopy[i] = ind.reservedEventsSlots[i];
			}
			
			for (int i = 0 ; i < Evolution.eventsNumber; i++) {
				permutation[i] = ind.getPermutation()[i];
			}
			
//			for (int eventId : permutation) {
//				System.out.print(eventId + " ");
//			}
//			
//			System.out.println();
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
				double newCost = (Parameters.mutationRate + ((double) ind.costMap.get(permutation[i]) / 1000f));
				boolean found = false;
				
				if (Evolution.randomGenerator.nextFloat() < newCost) {
					Event event = Evolution.events.get(permutation[i]);
					
					for (int roomNumber : event.getSuitableRooms()) {
						for (Slot slot : ind.getRooms()[roomNumber].getSlots()) {
						
							if (ind.studentsNoClash(event, slot.getSlotId(), roomNumber)) {
								if (slot.getAllocatedEvent() != null) {
									Event tempEvent = slot.getAllocatedEvent();
									
									eventsSlotsCopy[tempEvent.getId()] = null;
									
									int index = -1;
									
									for (int k = 0; k < permutation.length; k++) {
//										System.out.println(tempEvent.getId() + " " + permutation[k] + " " + k);
										if (tempEvent.getId() == permutation[k]) {
											index = k;
											break;
										}
									}
									
//									permutation[index] = event.getId();
//									permutation[i] = tempEvent.getId();
								}
								
								eventsSlotsCopy[event.getId()] = new Slot(slot.getRoomId(), slot.getSlotId());
								
								found = true;
							}
						}
						
						if (found == true)
							break;
					}
				}
			}
			
			Individual child = new Individual(permutation, eventsSlotsCopy, reservedEventsSlotsCopy);
			child.costMap = new HashMap<>(ind.costMap);
			result.add(child);
		}
		
		return result;
	}

}
