package honours_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StealMutation extends Operator {
	private double mutationRate;
	
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
				if (Parameters.withPenalties == true) {
					mutationRate = (Parameters.mutationRate + ((double) ind.penaltiesMap.get(permutation[i].getId()) / 1000f));
				} else { 
					mutationRate = Parameters.mutationRate;
				}
				
				if (Evolution.randomGenerator.nextFloat() < mutationRate) {
					Event event = permutation[i];
					List<Slot> slots = new ArrayList<>();
					
					for (int roomNumber : event.getSuitableRooms()) {
						for (Slot slot : ind.getRooms()[roomNumber].getSlots()) {
							if (ind.studentsNoClash(event, slot.getSlotId(), roomNumber)) {
								if (event.getSlot() != null) {
									Slot tempSlot = event.getSlot();
									
									if (slot.getRoomId() == tempSlot.getRoomId() && slot.getSlotId() == tempSlot.getSlotId()) {
										continue; // skip if same slot
									} else {
										slots.add(slot);
									}
								} 
							}
						}
					}
					
					if (slots.isEmpty() == false) {
						Slot slot = slots.remove(Evolution.randomGenerator.nextInt(slots.size()));
					
						if (slot.getAllocatedEvent() != null) {
							Event tempEvent = slot.getAllocatedEvent();
							tempEvent.setSlot(null);
							
							Slot tempSlot = event.getSlot();
							
							if (tempSlot != null) {
								if (tempEvent.getSuitableRooms().contains(tempSlot.getRoomId())) {
									if (ind.studentsNoClash(tempEvent, tempSlot.getSlotId()	, tempSlot.getRoomId())) {
										tempEvent.setSlot(new Slot(tempSlot.getRoomId(), tempSlot.getSlotId()));
									}
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
						
						if (slots.isEmpty() == false) {
							Slot rSlot = slots.remove(Evolution.randomGenerator.nextInt(slots.size()));
							event.setReserveSlot(new Slot(rSlot.getRoomId(), rSlot.getSlotId()));
						}
					}
				}
			}
			
			Individual child = new Individual(permutation);
			child.penaltiesMap = new HashMap<>(ind.penaltiesMap);
			result.add(child);
		}
		
		return result;
	}

}
