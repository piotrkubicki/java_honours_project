package honours_project;

import java.util.ArrayList;
import java.util.List;

public class MultiGenesMutation extends Operator {
	private double probabilityFactor;
	
	public MultiGenesMutation(double probabilityFactor) {
		this.probabilityFactor = probabilityFactor;
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		double min = 0F;
		double max = 1F;
		
		for (Individual ind : individuals) {
			List<Integer> permutation = ind.getPermutation();
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
				double p = min = Evolution.randomGenerator.nextFloat() * (max - min);
				
				if (p > probabilityFactor) {
					int index = Evolution.randomGenerator.nextInt(Evolution.eventsNumber);
					int temp = permutation.get(index);
					permutation.set(index, permutation.get(i));
					permutation.set(i, temp);
				}
			}
			
			result.add(new Individual(permutation));
		}
		
		return result;
	}

}
