package honours_project;

public class EnglishLanguage extends Language {
	// FIELDS
	public static final String RUN = "Run";
	public static final String RUN_TIME = "Run time";
	public static final String STOP = "Stop";
	public static final String RUNS = "Runs";
	public static final String FITNESS = "Fitness";
	public static final String SINGLE = "Single";
	public static final String THREE = "Three";
	public static final String END = "End";
	public static final String MISSED_EVENTS = "Missed events";
	public static final String LOAD_FILE = "Load file";
	public static final String SAVE = "Save";
	public static final String TITLE = "EA Timetabling";
	public static final String POPULATION_SIZE = "Population size";
	public static final String GENERATION = "Generation";
	public static final String STUDENTS = "Students";
	public static final String EVENTS = "Events";
	public static final String FEASIBILITY = "Feasibility";
	public static final String CLEAR = "Clear";
	public static final String ELAPSED_TIME = "Elapsed time";
	public static final String TOURNAMENT_SIZE = "Tournament size";
	public static final String MUTATION_FACTOR = "Mutation factor";
	
	// MESSAGE TITLES
	public static final String ERROR_MESSAGE_TITLE = "Error";
	
	// MESSAGE BODY
	public static final String NO_FILE_ERROR = "Please select problem file!";
	public static final String BEST_ERROR = "Please run program to find solution!";
	public static final String NO_NUMBER_ERROR = "Please provide number value!";
	public static final String REQUIRED = "Field is required!";
	
	public String getRunTime() {
		return RUN_TIME;
	}
	
	public String getStop() {
		return STOP;
	}

	@Override
	public String getRuns() {
		return RUNS;
	}

	@Override
	public String getFitness() {
		return FITNESS;
	}

	@Override
	public String getMissedEvents() {
		return MISSED_EVENTS;
	}

	@Override
	public String getSingle() {
		return SINGLE;
	}

	@Override
	public String getThree() {
		return THREE;
	}

	@Override
	public String getEnd() {
		return END;
	}

	@Override
	public String getLoadFile() {
		return LOAD_FILE;
	}

	@Override
	public String getSave() {
		return SAVE;
	}

	@Override
	public String getTitle() {
		return TITLE;
	}

	@Override
	public String getErrorTitle() {
		return ERROR_MESSAGE_TITLE;
	}

	@Override
	public String getNoFileError() {
		return NO_FILE_ERROR;
	}

	@Override
	public String getBestError() {
		return BEST_ERROR;
	}
	
	@Override
	public String getPopulationSize() {
		return POPULATION_SIZE;
	}

	@Override
	public String getNoNumberError() {
		return NO_NUMBER_ERROR;
	}

	@Override
	public String getGeneration() {
		return GENERATION;
	}

	@Override
	public String getStudents() {
		return STUDENTS;
	}

	@Override
	public String getEvents() {
		return EVENTS;
	}

	@Override
	public String getFeasibility() {
		return FEASIBILITY;
	}

	@Override
	public String getRun() {
		return RUN;
	}

	@Override
	public String getClear() {
		return CLEAR;
	}
	
	@Override
	public String getElapsedTime() {
		return ELAPSED_TIME;
	}

	@Override
	public String getRequiredError() {
		return REQUIRED;
	}

	@Override
	public String getTournamenSize() {
		return TOURNAMENT_SIZE;
	}

	@Override
	public String getMutationFactor() {
		return MUTATION_FACTOR;
	}
}
