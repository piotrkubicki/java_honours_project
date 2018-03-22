package honours_project;

import java.util.List;

public class RemoveWorseInsertion extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		while (individuals.size() > Parameters.populationSize) {
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
