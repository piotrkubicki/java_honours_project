package honours_project;

public class NoGUI {

	public static void main(String[] args) {
		Evolution evolution = new Evolution();
		
		int processNr = Integer.parseInt(args[0]);
		
		evolution.setFile(args[1]);
		Parameters.runsNumber = Integer.parseInt(args[3]);
		Parameters.runTime = Integer.parseInt(args[4]) * 1000;
		Parameters.populationSize = Integer.parseInt(args[5]);
		Parameters.tournamentSize = Integer.parseInt(args[6]);
		Parameters.mutationRate = Double.parseDouble(args[7]);
		Parameters.seed = System.currentTimeMillis() + processNr;
		
		evolution.run();
		
		evolution.save(args[2]);
	}

}
