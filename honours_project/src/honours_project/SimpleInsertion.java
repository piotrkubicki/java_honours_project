package honours_project;

import java.util.List;

public class SimpleInsertion extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		while (individuals.size() > Evolution.POPULATION_SIZE) {
			Individual worst = individuals.get(0);
			
			for (int i = 1; i < individuals.size() - 2; i++) {
				if (worst.isBetter(individuals.get(i))) {
					worst = individuals.get(i);
				}
			}
//			System.out.println("WORST: " + worst.getFitness() + " " + worst.unplacedEventsNumber());
			individuals.remove(worst);
		}
		
		return null;
	}

}
