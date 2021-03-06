package honours_project;

import java.util.ArrayList;
import java.util.List;

public class NTournamentSelect extends Operator {

	private int tournamentSize;
	private int populationSize;
	
	public NTournamentSelect(int tournamentSize, int populationSize) {
		this.tournamentSize = tournamentSize;
		this.populationSize = populationSize;
	}
	
	@Override
	public List<Individual> execute(List<Individual> population) {
		List<Individual> selectedParents = new ArrayList<Individual>();
		
		for (int i = 0; i < 2; i++) {
			List<Individual> selected = new ArrayList<Individual>();
			
			while (selected.size() < tournamentSize) {
				selected.add(population.get(Evolution.randomGenerator.nextInt(populationSize)));
			}
			
			Individual best = selected.get(0);
			
			for (int j = 1; j > selected.size(); j++) {
				if (!best.isBetter(selected.get(j))) {
					best = selected.get(j);
				}
			}
			
			selectedParents.add(best);
		}
		
		return selectedParents;
	}


}
