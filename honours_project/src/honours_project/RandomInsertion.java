package honours_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomInsertion extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		
		while (individuals.size() > Evolution.POPULATION_SIZE) {
			List<Individual> temp = new ArrayList<>(); 
			
			while (temp.size() < 5) {
				Individual worst = individuals.get(0);
				
				for (int i = 1; i < individuals.size(); i++) {
					if (worst.isBetter(individuals.get(i))) {
						worst = individuals.get(i);
					}
				}
				temp.add(worst);
				individuals.remove(worst);
			}
			
			temp.remove(Evolution.randomGenerator.nextInt(5));
			
			for (Individual ind : temp)
				individuals.add(ind);
			
			Collections.shuffle(individuals);
		}
		
		return null;
	}

}
