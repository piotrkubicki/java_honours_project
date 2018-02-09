package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MultiGenesMutation extends Operator {
	
	private double mutationRate;
	private int chromosomeLength;
	
	public MultiGenesMutation(double mutationFactor, int chromosomeLength) {
		this.mutationRate = mutationFactor;
		this.chromosomeLength = chromosomeLength;
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		Random rand = new Random();
//		mutationRate = 1/Evolution.EVENTS_NUMBER;
		
		for (Individual ind : individuals) {
			List<Slot> slotsPermutation = ind.getSlotsPermutation();
			List<Event> eventsPermutation = ind.getEventsPermutation();
			
			for (int i = 0; i < chromosomeLength; i++) {
				
				if (rand.nextDouble() < mutationRate) {
					int index = rand.nextInt(chromosomeLength);
					Slot temp = slotsPermutation.get(index);
					slotsPermutation.set(index, slotsPermutation.get(i));
					slotsPermutation.set(i, temp);
				}
				
				if (rand.nextDouble() < mutationRate) {
					int index = rand.nextInt(chromosomeLength);
					
					Event tempEvent = eventsPermutation.get(index);
					eventsPermutation.set(index, eventsPermutation.get(i));
					eventsPermutation.set(i, tempEvent);
				}
			}
			
			result.add(new Individual(slotsPermutation, ind.getEventsPermutation()));
		}
		
		return result;
	}

}
