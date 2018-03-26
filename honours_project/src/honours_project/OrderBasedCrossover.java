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
		
		// move missed events in front
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			for (int missed : parent1.unplacedEvents) {
				if (permutation[i].getId() == missed) {
					Event temp = permutation[i];
					
					for (int j = i; j > 0; j--) {
						permutation[j] = permutation[j-1];
					}
					
					permutation[0] = temp;
				}
			}
		}
		
		Individual child = new Individual(permutation);
		child.costMap = new HashMap<>(parent1.costMap);
		
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
