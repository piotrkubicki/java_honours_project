package honours_project;

import java.util.ArrayList;
import java.util.List;

public class MultiGenesMutation extends Operator {
	
	private double mutationRate;
	
	public MultiGenesMutation(double mutationRate) {
		this.mutationRate = mutationRate;
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> result = new ArrayList<Individual>();
		double min = 0F;
		double max = 1F;
		
		for (Individual ind : individuals) {
			List<Slot> slotsPermutation = ind.getSlotsPermutation();
			List<Integer> eventsPermutation = ind.getEventsPermutation();
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
				double p = min = Evolution.randomGenerator.nextFloat() * (max - min);
				
				if (p < mutationRate) {
					int index = Evolution.randomGenerator.nextInt(Evolution.eventsNumber);
					Slot temp = slotsPermutation.get(index);
					slotsPermutation.set(index, slotsPermutation.get(i));
					slotsPermutation.set(i, temp);
				}
				
				p = min = Evolution.randomGenerator.nextFloat() * (max - min);
				
				if (p < mutationRate) {
					int index = Evolution.randomGenerator.nextInt(Evolution.eventsNumber);
					
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
