package honours_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Evolution extends Observable implements Runnable {
	
	private List<Observer> observers = new ArrayList<Observer>();
	
	public static String filename = "";
	
	public static int POPULATION_SIZE = 0;
	public static int EVENTS_NUMBER;
	public static int ROOMS_NUMBER;
	public static int FEATURES_NUMBER;
	public static int STUDENTS_NUMBER;
	public static int SLOTS_NUMBER = 45;
	
	public static List<Integer> roomsSizes = new ArrayList<Integer>();
	public static List<Integer> studentsData = new ArrayList<Integer>();
	public static List<Integer> roomsData = new ArrayList<Integer>();
	public static List<Integer> eventsData = new ArrayList<Integer>();
	
	public static List<List<Integer>> studentsEvents = new ArrayList<List<Integer>>();
	public static List<List<Integer>> roomsFeatures = new ArrayList<List<Integer>>();
	public static List<List<Integer>> eventsFeatures = new ArrayList<List<Integer>>();
	
	public static List<Room> rooms = new ArrayList<Room>();
	public static List<Event> events = new ArrayList<Event>();
	public static List<Student> students = new ArrayList<Student>();

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
		crossover = new SimpleCrossover();
		mutator = new SimpleMutation();
		insertion = new SimpleInsertion();
		findBest = new FindBest();
	}
	
	public void setFile(String filename) {
		Evolution.filename = filename;
	}
	
	public void setPopulatinoSize(int size) {
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
	
	private void prepareData() {
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
	
	public void run() {
		prepareData();
		
		running = true;
		population = new ArrayList<Individual>();
		generation = 0;
		
		for (int i = 0; i < POPULATION_SIZE; i++) {
			population.add(new Individual());
		}
		
		while (running) {
			Individual parent1 = selector.run(population);
			Individual parent2 = selector.run(population);

			List<Individual> parents = new ArrayList<Individual>();
			parents.add(parent1);
			parents.add(parent2);
			
			List<Individual> childs = new ArrayList<Individual>();
			
			Individual child = crossover.run(parents);
			childs.add(child);
			child = mutator.run(childs);
			population.add(child);
			
			// second child
			parents = new ArrayList<Individual>();
			parents.add(parent2);
			parents.add(parent1);
			
			childs = new ArrayList<Individual>();
			
			Individual child2 = crossover.run(parents);
			childs.add(child2);
			child2 = mutator.run(childs);
			population.add(child2);
			
			insertion.run(population);
			best = findBest.run(population);
			
			generation++;
			
			notifyAllObservers();
		}
	}
}
