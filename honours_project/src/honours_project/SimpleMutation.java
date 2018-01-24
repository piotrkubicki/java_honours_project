package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleMutation extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		Random rand = new Random();
		int max = Evolution.EVENTS_NUMBER;

		
		List<Individual> result = new ArrayList<Individual>();
		
		for (Individual ind : individuals) {
			List<Slot> permutation = ind.getSlotsPermutation();
			
			int index1 = rand.nextInt(max);
			int index2 = rand.nextInt(max);
			
			Slot temp = permutation.get(index1);
			permutation.set(index1, permutation.get(index2));
			permutation.set(index2, temp);
			
			result.add(new Individual(permutation, ind.getEventsPermutation()));
		}
		
		return result;
	}

}
