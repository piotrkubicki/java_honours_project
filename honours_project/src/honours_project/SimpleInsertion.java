package honours_project;

import java.util.List;

public class SimpleInsertion extends Operator {

	@Override
	public Individual run(List<Individual> individuals) {
		while (individuals.size() > Evolution.POPULATION_SIZE) {
			Individual worst = individuals.get(0);
			
			for (int i = 1; i < individuals.size(); i++) {
				if (worst.isBetter(individuals.get(i))) {
					worst = individuals.get(i);
				}
			}
			
			individuals.remove(worst);
		}
		
		return null;
	}

}
