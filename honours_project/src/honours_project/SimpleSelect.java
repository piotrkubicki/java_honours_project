package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleSelect extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		List<Individual> selected = new ArrayList<Individual>();
		Random rand = new Random();
		int max = Evolution.POPULATION_SIZE;
		
		for (int i = 0; i < 2; i++) {
			int index = rand.nextInt(max);
			Individual ind1 = individuals.get(index);
			index = rand.nextInt(max);
			Individual ind2 = individuals.get(index);
			
			if (ind1.isBetter(ind2)) {
				selected.add(ind1);
			} else {
				selected.add(ind2);
			}
		}
		
		return selected;
	}

}
