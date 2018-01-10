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
	public List<Individual> execute(List<Individual> individuals) {
		Random rand = new Random();
		int max = Evolution.EVENTS_NUMBER - combinationLength;
		
		int start = rand.nextInt(max);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);
		
		List<Integer> permutation = parent1.getPermutation();
		List<Integer> insertion1 = new ArrayList<Integer>();
		
		List<Integer> secondPermutation = parent2.getPermutation();
		List<Integer> insertion2 = new ArrayList<Integer>();
		
		for (int i = start; i < (start + combinationLength); i++) {
			insertion1.add(permutation.get(i));
			insertion2.add(secondPermutation.get(i));
		}
		
		permutation.removeAll(insertion2);
		secondPermutation.removeAll(insertion1);
		
		for (int i = 0; i < combinationLength; i++) {
			int ins = insertion2.get(i);
			permutation.add(start + i, ins);
			ins = insertion1.get(i);
			secondPermutation.add(start + i, ins);
		}
		
		Individual child1 = new Individual(permutation);
		Individual child2 = new Individual(secondPermutation);
		List<Individual> result = new ArrayList<Individual>();
		result.add(child1);
		result.add(child2);
		
		return result;
	}

}
