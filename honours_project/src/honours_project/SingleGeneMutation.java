package honours_project;

import java.util.ArrayList;
import java.util.List;

public class SingleGeneMutation extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		int max = Evolution.eventsNumber;
		
		List<Individual> result = new ArrayList<Individual>();
		
		for (Individual ind : individuals) {
			List<Slot> slotsPermutation = ind.getSlotsPermutation();
			List<Integer> eventsPermutation = ind.getEventsPermutation();
			// mutate slots permutation
			int index1 = Evolution.randomGenerator.nextInt(max);
			int index2 = Evolution.randomGenerator.nextInt(max);
			
			Slot temp = slotsPermutation.get(index1);
			slotsPermutation.set(index1, slotsPermutation.get(index2));
			slotsPermutation.set(index2, temp);
			// mutate events permutation
			index1 = Evolution.randomGenerator.nextInt(max);
			index2 = Evolution.randomGenerator.nextInt(max);
			
			Integer tempEvent = eventsPermutation.get(index1);
			eventsPermutation.set(index1, eventsPermutation.get(index2));
			eventsPermutation.set(index2, tempEvent);
			
			result.add(new Individual(slotsPermutation, eventsPermutation));
		}
		
		return result;
	}

}
