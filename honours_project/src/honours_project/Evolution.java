package honours_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Evolution extends Observable implements Runnable {
	
	public enum State { INITIALIZE, STARTING, RUNNING, FINISHING, STOP };
	private List<Observer> observers = new ArrayList<Observer>();
	
	public static String filename = "";
	
	public static int POPULATION_SIZE = 0;
	public static int EVENTS_NUMBER;
	public static int ROOMS_NUMBER;
	public static int FEATURES_NUMBER;
	public static int STUDENTS_NUMBER;
	public static int SLOTS_NUMBER = 45;
	public static int RUNS_NUMBER = 1;
	public static long RUN_TIME = 0;
	
	public static State state = State.STOP;
	
	public static List<Integer> roomsSizes;
	public static List<Integer> studentsData;
	public static List<Integer> roomsData;
	public static List<Integer> eventsData;
	
	public static List<List<Integer>> studentsEvents;
	public static List<List<Integer>> roomsFeatures;
	public static List<List<Integer>> eventsFeatures;
	
	public static List<Room> rooms;
	public static List<Event> events;
	public static List<Student> students;

	private List<Individual> population;
	private Operator selector;
	private Operator crossover;
	private Operator mutator;
	private Operator insertion;
	private Operator findBest;
	
	Individual best = null;
	
	private boolean timeEvolution;
	private boolean progressEvolution;
	
	private long TIME_PER_RUN;
	private int generation;
	
	boolean running = true;
	
	public Evolution() {
		population = new ArrayList<Individual>();
		selector = new SimpleSelect();
		crossover = new SimpleCrossover(10);
		mutator = new SimpleMutation();
		insertion = new SimpleInsertion();
		findBest = new FindBest();
	}
	
	public void setFile(String filename) {
		Evolution.filename = filename;
	}
	
	public void setPopulationSize(int size) {
		POPULATION_SIZE = size;
	}
	
	public int getGeneration() {
		return generation;
	}
	
	public void attach(Observer observer) {
		observers.add(observer);
	}
	
	public void notifyAllObservers() {
		for (Observer observer : observers) {
			observer.update(this, best);
		}
	}
	
	public void prepareData() {
		roomsSizes = new ArrayList<Integer>();
		studentsData = new ArrayList<Integer>();
		roomsData = new ArrayList<Integer>();
		eventsData = new ArrayList<Integer>();
		
		studentsEvents = new ArrayList<List<Integer>>();
		roomsFeatures = new ArrayList<List<Integer>>();
		eventsFeatures = new ArrayList<List<Integer>>();
		
		rooms = new ArrayList<Room>();
		events = new ArrayList<Event>();
		students = new ArrayList<Student>();

		
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(filename);
			br = new BufferedReader(fr);
			
			String line;
			line = br.readLine();
			
			String[] sizes = line.split(" ");
			EVENTS_NUMBER = Integer.parseInt(sizes[0]);
			ROOMS_NUMBER = Integer.parseInt(sizes[1]);
			FEATURES_NUMBER = Integer.parseInt(sizes[2]);
			STUDENTS_NUMBER = Integer.parseInt(sizes[3]);
			
			int begin = 1;
			int end = begin + ROOMS_NUMBER;
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				roomsSizes.add(Integer.parseInt(line));
			}
			
			begin = end;
			end = begin + (STUDENTS_NUMBER * EVENTS_NUMBER);
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				studentsData.add(Integer.parseInt(line));
			}
			
			begin = end;
			end = begin + (FEATURES_NUMBER * ROOMS_NUMBER);
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				roomsData.add(Integer.parseInt(line));
			}
			
			begin = end;
			end = begin + (EVENTS_NUMBER * FEATURES_NUMBER);
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				eventsData.add(Integer.parseInt(line));
			}
			
			begin = 0;
			for (int i = 0; i < STUDENTS_NUMBER; i++) {
				end = begin + EVENTS_NUMBER;
				List studentEvents = new ArrayList<Integer>();
				
				for (int j = begin; j < end; j++) {
					studentEvents.add(studentsData.get(j));
				}
				
				studentsEvents.add(studentEvents);
				begin += EVENTS_NUMBER;
			}
			
			begin = 0;
			for (int i = 0; i < ROOMS_NUMBER; i++) {
				end = begin + FEATURES_NUMBER;
				List roomFeatures = new ArrayList<Integer>();
				
				for (int j = begin; j < end; j++) {
					roomFeatures.add(roomsData.get(j));
				}
				
				roomsFeatures.add(roomFeatures);
				begin += FEATURES_NUMBER;
			}
			
			begin = 0;
			for (int i = 0; i < EVENTS_NUMBER; i++) {
				end = begin + FEATURES_NUMBER;
				List eventFeatures = new ArrayList<Integer>();
				
				for (int j = begin; j < end; j++) {
					eventFeatures.add(eventsData.get(j));
				}
				
				eventsFeatures.add(eventFeatures);
				begin += FEATURES_NUMBER;
			}
			
			// PREPARE STUDENTS
			for (int i = 0; i < STUDENTS_NUMBER; i++) {
				students.add(new Student(i, studentsEvents.get(i)));
			}
			
			// PREPARE ROOMS
			for (int i = 0; i < ROOMS_NUMBER; i++) {
				rooms.add(new Room(i, roomsFeatures.get(i), roomsSizes.get(i)));
			}
			
			// PREPARE EVENTS
			for (int i = 0; i < EVENTS_NUMBER; i++) {
				events.add(new Event(i, eventsFeatures.get(i), rooms, students));
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopEvolution() {
		running = false;
	}
	
	public void saveSolution(String filename) {
		best.saveSolution(filename);
	}
	
	public boolean bestExists() {
		return (best != null);
	}
	
	private void initialize() {
		state = State.INITIALIZE;
		running = true;
		population = new ArrayList<Individual>();
		generation = 0;
		best = null;
		notifyAllObservers();
		
		starting();
	}
	
	private void starting() {
		state = State.STARTING;
		prepareData();
		notifyAllObservers();
		
		for (int j = 0; j < POPULATION_SIZE; j++) {
			population.add(new Individual());
		}
		
		if (RUN_TIME > 0)
			startTimer();
		
		running();
	}
	
	private void running() {
		state = State.RUNNING;
		notifyAllObservers();
	}
	
	private void stoping() {
		state = State.STOP;
		notifyAllObservers();
	}
	
	public void run() {
		
		for (int i = 0; i < RUNS_NUMBER; i++) {
			initialize();
			
			while (running) {
				List<Individual> parents = new ArrayList<Individual>();
				parents.addAll(selector.execute(population)); // select parents
				
				List<Individual> childs = new ArrayList<Individual>();
				childs.addAll(crossover.execute(parents));
				mutator.execute(childs);
				population.addAll(childs);
				insertion.execute(population);
				best = findBest.execute(population).get(0);
				
				generation++;
	
				notifyAllObservers();
			}
		}
		
		stoping();
	}
	
	public void setRunTime(long runTime) {
		RUN_TIME = runTime;
	}
	
	public void setRunsNumber(int numberOfRuns) {
		RUNS_NUMBER = numberOfRuns;
	}
	
	private void startTimer() {
		new Thread(new Runnable() {
			 public void run() {
				 try {
					Thread.sleep(RUN_TIME);
					running = false;
					state = State.FINISHING;
					notifyAllObservers();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
			 }
		}).start();
	}
}
