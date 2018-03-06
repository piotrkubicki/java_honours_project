package honours_project;

import java.util.ArrayList;
import java.util.List;

public class MultiSwapMutation extends Operator {
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		double min = 0F;
		double max = 1F;
		int[] permutation = new int[Evolution.eventsNumber];
		for (Individual ind : individuals) {
			
			
			for (int i = 0 ; i < Evolution.eventsNumber; i++)
				permutation[i] = ind.getPermutation()[i];
			
			for (int i = 0; i < Evolution.eventsNumber - 1; i++) {
				double p = min = Evolution.randomGenerator.nextFloat() * (max - min);
				
				if (p < Parameters.mutationRate) {
					int index = Evolution.randomGenerator.nextInt(Evolution.eventsNumber);
					int temp = permutation[index];
					permutation[index] = permutation[i];
					permutation[i] = temp;
				}
			}

			result.add(new Individual(permutation));
		}
		
		return result;
	}

}
