package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiGenesMutation extends Operator {
	
	private double mutationFactor;
	private int chromosomeLength;
	
	public MultiGenesMutation(double mutationFactor, int chromosomeLength) {
		this.mutationFactor = mutationFactor;
		this.chromosomeLength = chromosomeLength;
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		Random rand = new Random();
		double min = 0F;
		double max = 1F;
		
		for (Individual ind : individuals) {
			List<Slot> slotsPermutation = ind.getSlotsPermutation();
			List<Integer> eventsPermutation = ind.getEventsPermutation();
			
			for (int i = 0; i < chromosomeLength; i++) {
				double p = min = rand.nextFloat() * (max - min);
				
				if (p > mutationFactor) {
					int index = rand.nextInt(chromosomeLength);
					Slot temp = slotsPermutation.get(index);
					slotsPermutation.set(index, slotsPermutation.get(i));
					slotsPermutation.set(i, temp);
					
					index = rand.nextInt(chromosomeLength);
					
					Integer tempEvent = eventsPermutation.get(index);
					eventsPermutation.set(index, eventsPermutation.get(i));
					eventsPermutation.set(i, tempEvent);
				}
			}
			
			result.add(new Individual(slotsPermutation, ind.getEventsPermutation()));
		}
		
		return result;
	}

}
