package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SingleGeneMutation extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		Random rand = new Random();
		int max = Evolution.EVENTS_NUMBER;

		
		List<Individual> result = new ArrayList<Individual>();
		
		for (Individual ind : individuals) {
			List<Slot> slotsPermutation = ind.getSlotsPermutation();
			List<Integer> eventsPermutation = ind.getEventsPermutation();
			// mutate slots permutation
			int index1 = rand.nextInt(max);
			int index2 = rand.nextInt(max);
			
			Slot temp = slotsPermutation.get(index1);
			slotsPermutation.set(index1, slotsPermutation.get(index2));
			slotsPermutation.set(index2, temp);
			// mutate events permutation
			index1 = rand.nextInt(max);
			index2 = rand.nextInt(max);
			
			Integer tempEvent = eventsPermutation.get(index1);
			eventsPermutation.set(index1, eventsPermutation.get(index2));
			eventsPermutation.set(index2, tempEvent);
			
			result.add(new Individual(slotsPermutation, eventsPermutation));
		}
		
		return result;
	}

}
