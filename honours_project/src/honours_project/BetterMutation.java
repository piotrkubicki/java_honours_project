package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BetterMutation extends Operator {
	private double probabilityFactor;
	
	public BetterMutation(double probabilityFactor) {
		this.probabilityFactor = probabilityFactor;
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		Random rand = new Random();
		double min = 0F;
		double max = 1F;
		
		for (Individual ind : individuals) {
			List<Slot> permutation = ind.getSlotsPermutation();
			
			for (int i = 0; i < Evolution.EVENTS_NUMBER; i++) {
				double p = min = rand.nextFloat() * (max - min);
				
				if (p > probabilityFactor) {
					int index = rand.nextInt(Evolution.EVENTS_NUMBER);
					Slot temp = permutation.get(index);
					permutation.set(index, permutation.get(i));
					permutation.set(i, temp);
				}
			}
			
			result.add(new Individual(permutation, ind.getEventsPermutation()));
		}
		
		return result;
	}

}
