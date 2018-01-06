package honours_project;

public class EnglishLanguage extends Language {
	public static final String RUN = "Run";
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
	public static final String POPULATION_SIZE = "Popilation size";
	
	// MESSAGE TITLES
	public static final String ERROR_MESSAGE_TITLE = "Error";
	
	// MESSAGE BODY
	public static final String NO_FILE_ERROR = "Please select problem file!";
	public static final String BEST_ERROR = "Please run program to find solution!";
	public static final String NO_NUMBER_ERROR = "Please provide number value!";
	
	public String getRun() {
		return RUN;
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
}
