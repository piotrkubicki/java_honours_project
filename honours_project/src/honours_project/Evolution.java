package honours_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

public class Evolution extends Observable implements Runnable {
	
	public enum State { INITIALIZE, STARTING, RUNNING, FINISHING, STOP };
	private List<Observer> observers = new ArrayList<Observer>();
	
	public static String filename = "";
	
	public static int POPULATION_SIZE;
	public static int TOURNAMENT_SIZE;
	public static double MUTATION_FACTOR;
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
	
	// map slots with suitable events, key is a list with room id as first and slot id as second element
	protected static Map<List<Integer>, List<Integer>> slotsMap = new HashMap<List<Integer>, List<Integer>>();

	private List<Individual> population;
	private Operator selector;
	private Operator crossover;
	private Operator mutator;
	private Operator insertion;
	private Operator findBest;
	private Operator localSearch;
	
	Individual best = null;
	
	private int generation;
	
	boolean running = true;
	
	public Evolution() {}
	
	public void setFile(String filename) {
		Evolution.filename = filename;
	}
	
	public static void setPopulationSize(int size) {
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
	
	public static int getTournamentSize() {
		return TOURNAMENT_SIZE;
	}

	public static void setTournamentSize(int tournamentSize) {
		System.out.println(tournamentSize);
		TOURNAMENT_SIZE = tournamentSize;
	}

	public static double getMutationSize() {
		return MUTATION_FACTOR;
	}

	public static void setMutationSize(double mutationFactor) {
		MUTATION_FACTOR = mutationFactor;
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
			
			prepareSlotsMap();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		population = new ArrayList<Individual>();
		selector = new NTournamentSelect(TOURNAMENT_SIZE, POPULATION_SIZE);
		crossover = new SinglePointCrossover();
		mutator = new SingleGeneMutation();
//		mutator = new MultiGenesMutation(MUTATION_FACTOR, EVENTS_NUMBER);
		insertion = new SimpleInsertion();
		findBest = new FindBest();
		localSearch = new SwapNextEventLocalSearch();
	}
	
	private void prepareSlotsMap() {
		for (Room room : Evolution.rooms) {
			for (int i = 0; i < Evolution.SLOTS_NUMBER; i++) {
				List<Integer> suitableEvents = findEvents(room, events);
				slotsMap.put(Arrays.asList(room.getId(), i),suitableEvents);
			}
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
//				childs.add(localSearch.execute(childs).get(0));
				population.addAll(childs);
				insertion.execute(population);
				Individual oldBest = best;
				best = findBest.execute(population).get(0);
				
				if (oldBest != null && !oldBest.isBetter(best)) {
					best = localSearch.execute(Arrays.asList(best)).get(0);
					population.remove(oldBest);
					population.add(best);
				}
				
				generation++;
	
				notifyAllObservers();
				
//				for (Individual ind : childs) {
//					for (Slot s : ind.getSlotsPermutation()) {
//						System.out.print(s.getRoomId() + " : " + s.getSlotId() + " ");
//					}
//						System.out.println();
//					
//					System.out.println("UnEv: " + ind.unplacedEventsNumber() + " Fitness: " + ind.getFitness());
//				}
				
//				for (Individual ind : population) {
//					System.out.println(ind.getFitness());
//				}
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
	
	private List<Integer> findEvents(Room room, List<Event> events) {
		TreeMap<Integer, Integer> temp = new TreeMap<Integer, Integer>();
		List<Integer> possibleEvents = new ArrayList<Integer>();
		
		for (Event event : events) {
			boolean feasible = true;
			int ff = 0;
			
			if (room.getSpaces() < event.getStudents().size()) {
				feasible = false;
			} else {
				for (int i = 0; i < FEATURES_NUMBER; i++) {
					if (event.getFeatures().get(i) == 1 && room.getFeatures().get(i) == 0) {
						feasible = false;
						break;
					}
					
					if (room.getFeatures().get(i) == 0 && room.getFeatures().get(i) == 1) {
						ff++;
					}
				}
			}
			
			if (feasible) {
				while (temp.containsKey(ff)) {
					ff++;
				} 
			
				temp.put(ff, event.getId());
			}
		}
		
		for (Map.Entry<Integer, Integer> entry : temp.entrySet()) {
			possibleEvents.add(entry.getValue());
		}

		return possibleEvents;
	}
}
