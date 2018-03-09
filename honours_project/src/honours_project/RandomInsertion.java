package honours_project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomInsertion extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		
		while (individuals.size() > Parameters.populationSize) {
			List<Individual> temp = new ArrayList<>(); 
			
			int replacementRate = Evolution.randomGenerator.nextInt(Parameters.populationSize / 10) + 1;
			
			while (temp.size() < replacementRate) {
				Individual worst = individuals.get(0);
				
				for (int i = 1; i < individuals.size(); i++) {
					if (worst.isBetter(individuals.get(i))) {
						worst = individuals.get(i);
					}
				}
				temp.add(worst);
				individuals.remove(worst);
			}
			
			temp.remove(Evolution.randomGenerator.nextInt(replacementRate));
			
			for (Individual ind : temp)
				individuals.add(ind);
			
			Collections.shuffle(individuals);
		}
		
		return null;
	}

}
