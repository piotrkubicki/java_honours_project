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
			
			Event[] permutation = new Event[Evolution.eventsNumber];
			
			for (int i = 0 ; i < Evolution.eventsNumber; i++) {
				Event event = ind.getPermutation()[i];
				permutation[i] = new Event(event.getId(), event.getSuitableRooms(), event.getStudents(), event.getSlot(), event.getReserveSlot());
			}
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
				double newCost = (Parameters.mutationRate + ((double) ind.costMap.get(permutation[i].getId()) / 1000f));
				boolean found = false;
				
				if (Evolution.randomGenerator.nextFloat() < newCost) {
					Event event = permutation[i];
					
					for (int roomNumber : event.getSuitableRooms()) {
						for (Slot slot : ind.getRooms()[roomNumber].getSlots()) {
						
							if (ind.studentsNoClash(event, slot.getSlotId(), roomNumber)) {
								if (slot.getAllocatedEvent() != null) {
									Event tempEvent = slot.getAllocatedEvent();
									tempEvent.setSlot(null);
									
									if (event.getSlot() != null) {
										Slot tempSlot = event.getSlot();
										if (ind.studentsNoClash(tempEvent, tempSlot.getSlotId()	, tempSlot.getRoomId())) {
											tempEvent.setSlot(new Slot(tempSlot.getRoomId(), tempSlot.getSlotId()));
										}
									}
									
									int index = -1;
									
									for (int k = 0; k < permutation.length; k++) {
										if (tempEvent.getId() == permutation[k].getId()) {
											index = k;
											break;
										}
									}
									
									permutation[index] = event;
									permutation[i] = tempEvent;
								}
								
								event.setSlot(new Slot(slot.getRoomId(), slot.getSlotId()));
								
//								int index = -1;
//								
//								for (int k = 0; k < permutation.length; k++) {
//									if (event.getId() == permutation[k].getId()) {
//										index = k;
//										break;
//									}
//								}
//								
//								for (int j = index; j > 0; j--) {
//									permutation[j] = permutation[j-1];
//								}
//								
//								permutation[0] = event;
								
								found = true;
								break;
							}
						}
						
						if (found == true)
							break;
					}
				}
			}
			
			Individual child = new Individual(permutation);
			child.costMap = new HashMap<>(ind.costMap);
			result.add(child);
		}
		
		return result;
	}

}
