package honours_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OnePointCrossover extends Operator {

	
	public OnePointCrossover() {
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
				
				int p1Index = -1;
				Slot p1Slot = null;
				int p2Index = -1;
				Slot p2Slot = null;
				
				for (int k = 0; k < parent1.getPermutation().size(); k++) {
					if (parent1.getPermutation().get(k).getRoomId() == i && parent1.getPermutation().get(k).getSlotId() == j) {
						p1Index = k;
						p1Slot = parent1.getPermutation().get(k);
						break;
					}
				}
				
				for (int k = 0; k < parent2.getPermutation().size(); k++) {
					if (parent2.getPermutation().get(k).getRoomId() == i && parent2.getPermutation().get(k).getSlotId() == j) {
						p2Index = k;
						p2Slot = parent2.getPermutation().get(k);
						break;
					}
				}
				
				if (step < cutPoint) {
					while (firstPermutation.get(p1Index) != null) {
						p1Index++;
						
						if (p1Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p1Index = 0;
					}
					firstPermutation.set(p1Index, new Slot(i, j, p1Slot.getPossibleEvents()));
					
					while (secondPermutation.get(p2Index) != null) {
						p2Index++;
						
						if (p2Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p2Index = 0;
					}
					secondPermutation.set(p2Index, new Slot(i, j, p2Slot.getPossibleEvents()));
				} else {
					while (firstPermutation.get(p2Index) != null) {
						p2Index++;
						
						if (p2Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p2Index = 0;
					}
					firstPermutation.set(p2Index, new Slot(i, j, p2Slot.getPossibleEvents()));
					
					while (secondPermutation.get(p1Index) != null) {
						p1Index++;
						
						if (p1Index >= (Evolution.SLOTS_NUMBER * Evolution.ROOMS_NUMBER)) 
							p1Index = 0;
					}
					secondPermutation.set(p1Index, new Slot(i, j, p1Slot.getPossibleEvents()));
				}
			}
		}

		Individual child1 = new Individual(firstPermutation);
		Individual child2 = new Individual(secondPermutation);
		
		List<Individual> result = new ArrayList<Individual>();
		result.add(child1);
		result.add(child2);
		
		return result;
	}

}
