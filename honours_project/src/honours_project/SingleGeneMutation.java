package honours_project;

import java.util.ArrayList;
import java.util.List;

public class SingleGeneMutation extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {

		int max = Evolution.eventsNumber;
		List<Individual> result = new ArrayList<Individual>();
		
		for (Individual ind : individuals) {
			List<Integer> permutation = ind.getPermutation();
			
			int index1 = Evolution.randomGenerator.nextInt(max);
			int index2 = Evolution.randomGenerator.nextInt(max);
			
			int temp = permutation.get(index1);
			permutation.set(index1, permutation.get(index2));
			permutation.set(index2, temp);
			
			result.add(new Individual(permutation));
		}
		
		return result;
	}

}
