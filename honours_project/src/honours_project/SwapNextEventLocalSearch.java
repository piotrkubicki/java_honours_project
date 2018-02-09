package honours_project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwapNextEventLocalSearch extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		
		int i = 0;
		Individual currentBest = individuals.get(0);
		Individual ind = individuals.get(0);
		List<Individual> t = new ArrayList<>();
		while (i < ind.getEventsPermutation().size()) {
			List<Slot> slotsPermutation = new ArrayList<Slot>();
			List<Event> eventsPermutation = ind.getEventsPermutation();
			
			// copy permutation
			for (Slot slot : currentBest.getSlotsPermutation()) {
				Slot nSlot = new Slot(slot.getRoomId(), slot.getSlotId(), slot.getPossibleEvents());
				slotsPermutation.add(nSlot);
			}
			
			// make swap
			Event temp = eventsPermutation.get(i + 1);
			eventsPermutation.set(i + 1, eventsPermutation.get(i));
			eventsPermutation.set(i, temp);
			
			Individual tempInd = new Individual(slotsPermutation, eventsPermutation);
			System.out.println("C : " + currentBest.getFitness() + " N : " + tempInd.getFitness());
//			for (Slot s : currentBest.getSlotsPermutation()) {
//				System.out.print(s.getRoomId() + " : " + s.getSlotId() + " ");
//			}
//			System.out.println();
//			for (Slot s : tempInd.getSlotsPermutation()) {
//				System.out.print(s.getRoomId() + " : " + s.getSlotId() + " ");
//			}
//			System.out.println();
			// compare new and old timetable
			if (!currentBest.isBetter(tempInd)) {
				currentBest = tempInd;
				System.out.println("BETTER");
			}
			t.add(tempInd);
			i += 2;
		}
		
		for (Individual in : t) {
			System.out.println(in.getFitness());
//			
//			for (Slot s : in.getSlotsPermutation()) {
//				System.out.print(s.getRoomId() + " : " + s.getSlotId() + " ");
//			}
//			System.out.println();
		}
//		individuals.remove(ind);
//		individuals.add(currentBest);
		
		return Arrays.asList(currentBest);
	}

}
