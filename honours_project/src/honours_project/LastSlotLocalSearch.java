package honours_project;

import java.util.List;

public class LastSlotLocalSearch extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < individuals.size(); i++) {
				Individual ind = individuals.get(i);
				Individual copy = new Individual(ind.getPermutation());
				
				if (!ind.isBetter(copy)) {
					System.out.println("Better");
					individuals.remove(ind);
					individuals.add(copy);
				}
				
			}
		}
		
		return null;
	}

}
