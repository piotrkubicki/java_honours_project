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
}
