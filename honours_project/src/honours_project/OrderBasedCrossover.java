package honours_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OrderBasedCrossover extends Operator {

	public OrderBasedCrossover() {
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		
		int cutPoint1 = Evolution.randomGenerator.nextInt((Evolution.eventsNumber) - 1);
		int valuesLeft = Evolution.eventsNumber - cutPoint1;
		
		int cutPoint2 = cutPoint1 + Evolution.randomGenerator.nextInt(valuesLeft);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);

		Event[] permutation = new Event[Evolution.eventsNumber];
		
		for (int i = cutPoint1; i < cutPoint2; i++) {
			Event event = parent1.getPermutation()[i];
			permutation[i] = new Event(event.getId(), event.getSuitableRooms(), event.getStudents(), event.getSlot(), event.getReserveSlot());
		}
		
		int k = cutPoint2;
		int l = k;
		
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			if (k >= Evolution.eventsNumber)
				k = 0;
			
			if (l >= Evolution.eventsNumber)
				l = 0;
			
			if (!containValue(permutation, parent2.getPermutation()[l])) {
				while (permutation[k] != null)
					k++;
				
				Event event = parent2.getPermutation()[l];
				permutation[k] = new Event(event.getId(), event.getSuitableRooms(), event.getStudents(), event.getSlot(), event.getReserveSlot());
				k++;
			}
			l++;
		}
		
		Individual child = new Individual(permutation);
		child.costMap = new HashMap<>(parent1.costMap);
		
		//set mutator
		
		double chance = 0;
		
		if (parent1.mutator instanceof SingleSwapMutation) {
			chance += 0.05;
		} else {
			chance -= 0.05;
		}
		
//		if (parent2.mutator instanceof SingleSwapMutation) {
//			chance += 0.05;
//		} else {
//			chance -= 0.05;
//		}
//		
//		if (Evolution.randomGenerator.nextDouble() < 0.3 + chance)
//			child.mutator = new SingleSwapMutation();
//		else 
			child.mutator = new MultiSwapMutation();
		
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
