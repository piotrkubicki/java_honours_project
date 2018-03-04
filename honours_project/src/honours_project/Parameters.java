package honours_project;

import java.lang.reflect.Field;

public class Parameters {
	
	public static long seed = System.currentTimeMillis();
	public static int tournamentSize;
	public static double mutationRate;
	public static long runTime = 0;
	public static int populationSize;
	
	public static int getTournamentSize() {
		return tournamentSize;
	}

	public static void setTournamentSize(int tournament) {
		tournamentSize = tournament;
	}

	public static double getMutationRate() {
		return mutationRate;
	}

	public static void setMutationSize(double rate) {
		mutationRate = rate;
	}
	

	public static void setRunTime(long timePerRun) {
		runTime = timePerRun;
	}
	
	public static void setPopulationSize(int size) {
		populationSize = size;
	}
	
	public static String printParams() {
		String str = "";
		for(Field field : Parameters.class.getDeclaredFields()) {
			String name = field.getName();
			Object val = null;
			try {
				val = field.get(null);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			str += name + " \t" + val + "\r\n";
			
		}
		return str;
	}
}
