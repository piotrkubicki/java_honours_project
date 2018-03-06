package honours_project;

import java.util.ArrayList;
import java.util.List;

public class OrderBasedCrossover extends Operator {

	public OrderBasedCrossover() {
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		
		int cutPoint1 = Evolution.randomGenerator.nextInt((Evolution.eventsNumber) - 1);
		int valuesLeft = Evolution.eventsNumber - cutPoint1;
		
		int cutPoint2 = cutPoint1 + Evolution.randomGenerator.nextInt(valuesLeft);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);

		int[] permutation = new int[Evolution.eventsNumber];
		
		for (int i = cutPoint1; i < cutPoint2; i++) {
			permutation[i] = parent1.getPermutation()[i];
		}
		
		int k = cutPoint2;
		
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			if (k >= Evolution.eventsNumber)
				k = 0;
			
			if (!containValue(permutation, parent2.getPermutation()[k])) {
				permutation[k] = parent2.getPermutation()[k];
			}
			k++;
		}
		
		Individual child = new Individual(permutation);
		List<Individual> result = new ArrayList<Individual>();
		result.add(child);
		
		return result;
	}
	
	private boolean containValue(int[] permutation, int value) {
		for (int i = 0; i < permutation.length; i++) {
			if (permutation[i] == value) {
				return true;
			}
		}
		
		return false;
	}

}
