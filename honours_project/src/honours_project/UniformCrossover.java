package honours_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UniformCrossover extends Operator {
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {

		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);

		Event[] permutation = new Event[Evolution.eventsNumber];
		
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			if (Evolution.randomGenerator.nextDouble() >= 0.5) {
				if (containValue(permutation, parent1.getPermutation()[i]) == false) {
					Event event = parent1.getPermutation()[i];
					Event tempEvent = null;
					
					for (int j = 0; j < Evolution.eventsNumber; j++) {
						if (parent2.getPermutation()[j].getId() == event.getId())
							tempEvent = parent2.getPermutation()[j];
					}
					
					permutation[i] = new Event(event.getId(), event.getSuitableRooms(), event.getStudents(), event.getSlot(), tempEvent.getSlot());
				}
			} else {
				if (containValue(permutation, parent2.getPermutation()[i]) == false) {
					Event event = parent2.getPermutation()[i];
					Event tempEvent = null;
					
					for (int j = 0; j < Evolution.eventsNumber; j++) {
						if (parent1.getPermutation()[j].getId() == event.getId())
							tempEvent = parent1.getPermutation()[j];
					}
					
					permutation[i] = new Event(event.getId(), event.getSuitableRooms(), event.getStudents(), event.getSlot(), tempEvent.getSlot());
				}
			}
		}
		
		// add missing events
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			if (permutation[i] == null) {
				for (Event event : parent1.getPermutation()) {
					if (containValue(permutation, event) == false) {
						Event tempEvent = null;
						
						for (int j = 0; j < Evolution.eventsNumber; j++) {
							if (parent2.getPermutation()[j].getId() == event.getId())
								tempEvent = parent1.getPermutation()[j];
						}
						
						permutation[i] = new Event(event.getId(), event.getSuitableRooms(), event.getStudents(), event.getSlot(), tempEvent.getSlot());
					}
				}
			}
		}
		
		Individual child = new Individual(permutation);
		child.costMap = new HashMap<>(parent1.costMap);
		
		//set mutator
		if (Evolution.randomGenerator.nextDouble() < 0.5)
			child.mutator = new RandomSwapMutation();
		else 
			child.mutator = new StealMutation();
		
		List<Individual> result = new ArrayList<Individual>();
		result.add(child);
		
		return result;
	}
	
	private boolean containValue(Event[] permutation, Event value) {
		for (int i = 0; i < permutation.length; i++) {
			if (permutation[i] != null) {
				if (permutation[i].getId() == value.getId()) {
					return true;
				}
			}
		}
		
		return false;
	}
}
