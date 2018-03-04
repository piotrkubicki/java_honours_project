package honours_project;

import java.util.ArrayList;
import java.util.List;

public class MultiGenesMutation extends Operator {
	
	private double mutationRate;
	private int chromosomeLength;
	
	public MultiGenesMutation(double mutationRate, int chromosomeLength) {
		this.mutationRate = mutationRate;
		this.chromosomeLength = chromosomeLength;
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		double min = 0F;
		double max = 1F;
		
		for (Individual ind : individuals) {
			List<Slot> slotsPermutation = ind.getSlotsPermutation();
			List<Integer> eventsPermutation = ind.getEventsPermutation();
			
			for (int i = 0; i < chromosomeLength; i++) {
				double p = min = Evolution.randomGenerator.nextFloat() * (max - min);
				
				if (p < mutationRate) {
					int index = Evolution.randomGenerator.nextInt(chromosomeLength);
					Slot temp = slotsPermutation.get(index);
					slotsPermutation.set(index, slotsPermutation.get(i));
					slotsPermutation.set(i, temp);
				}
				
				p = min = Evolution.randomGenerator.nextFloat() * (max - min);
				
				if (p < mutationRate) {
					int index = Evolution.randomGenerator.nextInt(chromosomeLength);
					
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
