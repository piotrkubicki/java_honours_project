package honours_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SinglePointCrossover extends Operator {

	
	public SinglePointCrossover() {
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		Random rand = new Random();
		
		int cutPoint = rand.nextInt(Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);
		
		List<Slot> firstPermutation = new ArrayList<Slot>();
		List<Slot> secondPermutation = new ArrayList<Slot>();
		
		for (int i = 0; i < (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER); i++) {
			firstPermutation.add(null);
			secondPermutation.add(null);
		}
		int step = 0;
		for (int i = 0; i < Evolution.ROOMS_NUMBER; i++) {
			for (int j = 0; j < Evolution.SLOTS_NUMBER; j++) {
				step++;
				Slot slot1 = new Slot(i, j, Evolution.slotsMap.get(Arrays.asList(i, j)));
				Slot slot2 = new Slot(i, j, Evolution.slotsMap.get(Arrays.asList(i, j)));
				int p1Index = -1;
				int p2Index = -1;
				
				for (int k = 0; k < parent1.getSlotsPermutation().size(); k++) {
					if (parent1.getSlotsPermutation().get(k).getRoomId() == i && parent1.getSlotsPermutation().get(k).getSlotId() == j) {
						p1Index = k;
						break;
					}
				}
				
				for (int k = 0; k < parent2.getSlotsPermutation().size(); k++) {
					if (parent2.getSlotsPermutation().get(k).getRoomId() == i && parent2.getSlotsPermutation().get(k).getSlotId() == j) {
						p2Index = k;
						break;
					}
				}
				
				if (step < cutPoint) {
					while (firstPermutation.get(p1Index) != null) {
						p1Index++;
						
						if (p1Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p1Index = 0;
					}
					firstPermutation.set(p1Index, slot1);
					
					while (secondPermutation.get(p2Index) != null) {
						p2Index++;
						
						if (p2Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p2Index = 0;
					}
					secondPermutation.set(p2Index, slot2);
				} else {
					while (firstPermutation.get(p2Index) != null) {
						p2Index++;
						
						if (p2Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p2Index = 0;
					}
					firstPermutation.set(p2Index, slot1);
					
					while (secondPermutation.get(p1Index) != null) {
						p1Index++;
						
						if (p1Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p1Index = 0;
					}
					secondPermutation.set(p1Index, slot2);
				}
			}
		}

		Individual child1 = new Individual(firstPermutation, parent1.getEventsPermutation());
//		Individual child2 = new Individual(secondPermutation, parent2.getEventsPermutation());
//		Individual child1 = new Individual(parent1.getSlotsPermutation(), parent1.getEventsPermutation());
//		Individual child2 = new Individual(parent2.getSlotsPermutation(), parent2.getEventsPermutation());
		
		List<Individual> result = new ArrayList<Individual>();
		result.add(child1);
//		result.add(child2);
		
		return result;
	}

}
