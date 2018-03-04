package honours_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SinglePointCrossover extends Operator {

	public SinglePointCrossover() {
	}
	
	@Override
	public List<Individual> execute(List<Individual> individuals) {
		
		int cutPoint = Evolution.randomGenerator.nextInt(Evolution.slotsNumber * Evolution.roomsNumber);
		
		Individual parent1 = individuals.get(0);
		Individual parent2 = individuals.get(1);
		
		List<Slot> firstPermutation = new ArrayList<Slot>();
		
		for (int i = 0; i < (Evolution.slotsNumber * Evolution.roomsNumber); i++) {
			firstPermutation.add(null);
		}
		
		int step = 0;
		
		for (int i = 0; i < Evolution.roomsNumber; i++) {
			for (int j = 0; j < Evolution.slotsNumber; j++) {
				step++;
				Slot slot1 = new Slot(i, j, Evolution.slotsMap.get(Arrays.asList(i, j)));
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
						
						if (p1Index >= (Evolution.slotsNumber * Evolution.roomsNumber)) 
							p1Index = 0;
					}
					firstPermutation.set(p1Index, slot1);
				} else {
					while (firstPermutation.get(p2Index) != null) {
						p2Index++;
						
						if (p2Index >= (Evolution.slotsNumber * Evolution.roomsNumber)) 
							p2Index = 0;
					}
					firstPermutation.set(p2Index, slot1);
				}
			}
		}

		Individual child1 = new Individual(firstPermutation, parent1.getEventsPermutation());
		
		List<Individual> result = new ArrayList<Individual>();
		result.add(child1);
		
		return result;
	}

}
