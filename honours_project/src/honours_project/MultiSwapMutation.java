package honours_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultiSwapMutation extends Operator {
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();

		int[] permutation = new int[Evolution.eventsNumber];
		
		for (Individual ind : individuals) {
			
			ind.evaluate();
			
			Slot[] eventsSlotsCopy = new Slot[Evolution.eventsNumber];
			Slot[] reservedEventsSlotsCopy = new Slot[Evolution.eventsNumber];
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
				eventsSlotsCopy[i] = ind.eventsSlots[i];
				reservedEventsSlotsCopy[i] = ind.reservedEventsSlots[i];
			}
			
			for (int i = 0 ; i < Evolution.eventsNumber; i++) {
				permutation[i] = ind.getPermutation()[i];
			}	
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
				double p = Evolution.randomGenerator.nextFloat();
				double newCost = (Parameters.mutationRate + ((double) ind.costMap.get(i) / 1000f));

				if (p < newCost) {
					Event event = Evolution.events.get(permutation[i]);
					
					for (int roomNumber : event.getSuitableRooms()) {
						for (Slot slot : ind.getRooms()[roomNumber].getSlots()) {
							if (slot.getRoomId() != eventsSlotsCopy[i].getRoomId() && slot.getSlotId() != eventsSlotsCopy[i].getSlotId()) {
								if (ind.studentsNoClash(event, slot.getSlotId(), roomNumber)) {
									eventsSlotsCopy[event.getId()] = slot;
									
									int temp = permutation[i];
									
									for (int j = i; j > 0; j--) {
										permutation[j] = permutation[j-1];
									}
									
									permutation[0] = temp;
								}
							}
						}
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
