package honours_project;

import java.util.ArrayList;
import java.util.List;

public class FindBest extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {
		if (individuals.size() == 0)
			return null;
		
		Individual best = individuals.get(0);
		
		for (int i = 0; i < individuals.size(); i++) {
			if (!best.isBetter(individuals.get(i))) {
				best = individuals.get(i);
			}
		}
		
		List<Individual> result = new ArrayList<Individual>();
		result.add(best);
		
		return result;
	}

}
