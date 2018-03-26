package honours_project;

import java.util.ArrayList;
import java.util.List;

public class SingleSwapMutation extends Operator {

	@Override
	public List<Individual> execute(List<Individual> individuals) {

		int max = Evolution.eventsNumber;
		List<Individual> result = new ArrayList<Individual>();
		
		for (Individual ind : individuals) {
//			int[] permutation = new int[Evolution.eventsNumber];
//			
//			for (int i = 0 ; i < Evolution.eventsNumber; i++)
//				permutation[i] = ind.getPermutation()[i];
//			
//			int index1 = Evolution.randomGenerator.nextInt(max);
//			int index2 = Evolution.randomGenerator.nextInt(max);
//			
//			int temp = permutation[index1];
//			permutation[index1] = permutation[index2];
//			permutation[index2] = temp;

//			result.add(new Individual(permutation));
		}
		
		return result;
	}

}
