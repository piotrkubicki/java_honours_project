package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleCrossover extends Operator {

	private int combinationLength;
	
	public SimpleCrossover(int combinationLength) {
		this.combinationLength = combinationLength;
	}
	
	@Override
	public Individual run(List<Individual> individuals) {
		Random rand = new Random();
		int max = Evolution.EVENTS_NUMBER - combinationLength;
		
		int start = rand.nextInt(max);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);
		
		List<Integer> permutation = parent1.getPermutation();
		List<Integer> insertion = new ArrayList<Integer>();
		
		List<Integer> secondPermutation = parent2.getPermutation();
		
		for (int i = start; i < (start + combinationLength); i++) {
			insertion.add(secondPermutation.get(i));
		}
		
		permutation.removeAll(insertion);
		
		for (int i = 0; i < combinationLength; i++) {
			int ins = insertion.get(i);
			permutation.add(start + i, ins);
		}
		
		Individual child = new Individual(permutation);
		
		return child;
	}

}
