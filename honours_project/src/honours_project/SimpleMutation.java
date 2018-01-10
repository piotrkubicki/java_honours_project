package honours_project;

import java.util.List;
import java.util.Random;

public class SimpleMutation extends Operator {

	@Override
	public Individual execute(List<Individual> individuals) {
		List<Integer> permutation = individuals.get(0).getPermutation();
		Random rand = new Random();
		int max = Evolution.EVENTS_NUMBER;
		
		int index1 = rand.nextInt(max);
		int index2 = rand.nextInt(max);
		
		int temp = permutation.get(index1);
		permutation.set(index1, permutation.get(index2));
		permutation.set(index2, temp);
		
		return new Individual(permutation);
	}

}
