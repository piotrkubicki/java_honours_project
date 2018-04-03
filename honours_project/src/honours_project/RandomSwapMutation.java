package honours_project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RandomSwapMutation extends Operator {

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
				double mutationRate = (Parameters.mutationRate + ((double) ind.penaltiesMap.get(permutation[i].getId()) / 1000f));
				
				if (Evolution.randomGenerator.nextFloat() < mutationRate) {
					int index = Evolution.randomGenerator.nextInt(Evolution.eventsNumber);
					Event temp = permutation[index];
					
					permutation[index] = permutation[i];
					permutation[i] = temp;
					
					permutation[index].setSlot(null);
					permutation[index].setReserveSlot(null);
					permutation[i].setSlot(null);
					permutation[i].setReserveSlot(null);
				}
			}
			
			Individual child = new Individual(permutation);
			child.penaltiesMap = new HashMap<>(ind.penaltiesMap);
			result.add(child);
		}
		
		return result;
	}

}
