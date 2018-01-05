package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleCrossover extends Operator {

	@Override
	public Individual run(List<Individual> individuals) {
		Random rand = new Random();
		int max = Application.EVENTS_NUMBER - 10;
		
		int start = rand.nextInt(max);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);
		
		List<Integer> permutation = parent1.getPermutation();
		List<Integer> insertion = new ArrayList<Integer>();
		
		List<Integer> secondPermutation = parent2.getPermutation();
		
		for (int i = start; i < (start + 10); i++) {
			insertion.add(secondPermutation.get(i));
		}
		
		permutation.removeAll(insertion);
		
		for (int i = 0; i < 10; i++) {
			int ins = insertion.get(i);
			permutation.add(start + i, ins);
		}
		
		Individual child = new Individual(permutation);
		
		return child;
	}

}
