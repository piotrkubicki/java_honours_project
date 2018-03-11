package honours_project;

public abstract class Language {
	
	// FIELDS
	public abstract String getRun();
	public abstract String getRunTime();
	public abstract String getStop();
	public abstract String getRuns();
	public abstract String getFitness();
	public abstract String getMissedEvents();
	public abstract String getSingle();
	public abstract String getThree();
	public abstract String getEnd();
	public abstract String getLoadFile();
	public abstract String getSave();
	public abstract String getTitle();
	public abstract String getPopulationSize();
	public abstract String getGeneration();
	public abstract String getEventCost();
	public abstract String getEvents();
	public abstract String getFeasibility();
	public abstract String getClear();
	public abstract String getElapsedTime();
	public abstract String getTournamenSize();
	public abstract String getMutationFactor();
	
	// MESSAGES TITLES
	public abstract String getErrorTitle();
	public abstract String getSuccessTitle();
	
	//MESSAGES BODY
	public abstract String getNoFileError();
	public abstract String getBestError();
	public abstract String getNoNumberError();
	public abstract String getRequiredError();
	public abstract String getErrorGeneric();
	public abstract String getDone();
}
