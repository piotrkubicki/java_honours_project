package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SinglePointCrossover extends Operator {
	
	public SinglePointCrossover() {}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		Random rand = new Random();
		int cutPoint = rand.nextInt(Evolution.EVENTS_NUMBER);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);
		
		List<Integer> firstPermutation = parent1.getPermutation();
		List<Integer> secondPermutation = parent2.getPermutation();
		
		for (int i = cutPoint; i < parent2.getPermutation().size(); i++) {
			int gene = parent2.getPermutation().get(i);
			firstPermutation.remove(parent1.getPermutation().indexOf(gene));
			firstPermutation.add(i, gene);
		}
		
		for (int i = cutPoint; i < parent1.getPermutation().size(); i++) {
			int gene = parent1.getPermutation().get(i);
			secondPermutation.remove(parent2.getPermutation().indexOf(gene));
			secondPermutation.add(i, gene);
		}
		
		
		Individual child1 = new Individual(firstPermutation);
		Individual child2 = new Individual(secondPermutation);
		List<Individual> result = new ArrayList<Individual>();
		result.add(child1);
		result.add(child2);
		
		return result;
	}

}
