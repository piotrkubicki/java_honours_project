package honours_project;

import java.util.List;
import java.util.Random;

public class SimpleSelect extends Operator {

	@Override
	public Individual run(List<Individual> individuals) {
		Random rand = new Random();
		int max = Evolution.POPULATION_SIZE;
		
		int index = rand.nextInt(max);
		Individual ind1 = individuals.get(index);
		index = rand.nextInt(max);
		Individual ind2 = individuals.get(index);
		
		if (ind1.isBetter(ind2)) {
			return ind1;
		} else {
			return ind2;
		}
	}

}
