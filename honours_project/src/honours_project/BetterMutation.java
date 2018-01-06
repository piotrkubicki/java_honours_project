package honours_project;

import java.util.List;
import java.util.Random;

public class BetterMutation extends Operator {
	private double probabilityFactor;
	
	public BetterMutation(double probabilityFactor) {
		this.probabilityFactor = probabilityFactor;
	}
	
	@Override
	public Individual run(List<Individual> individuals) {
		List<Integer> permutation = individuals.get(0).getPermutation();
		Random rand = new Random();
		double min = 0F;
		double max = 1F;
		
		for (int i = 0; i < Evolution.EVENTS_NUMBER; i++) {
			double p = min = rand.nextFloat() * (max - min);
			
			if (p > probabilityFactor) {
				int index = rand.nextInt(Evolution.EVENTS_NUMBER);
				int temp = permutation.get(index);
				permutation.set(index, permutation.get(i));
				permutation.set(i, temp);
			}
		}
		
		return new Individual(permutation);
	}

}
