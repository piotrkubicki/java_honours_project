package honours_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.TreeMap;

public class Evolution extends Observable implements Runnable {
	
	public enum State { INITIALIZE, STARTING, RUNNING, FINISHING, STOP };
	private List<Observer> observers = new ArrayList<Observer>();
	
	public static String filename = "";
	
	public static Random randomGenerator = new Random(Parameters.seed);
	
	public static int eventsNumber;
	public static int roomsNumber;
	public static int featuresNumber;
	public static int studentsNumber;
	public static int slotsNumber = 45;
	public static int runsNumber = 1;
	
	
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
	
	private Individual best = null;
	
	private List<Individual> solutions = new ArrayList<Individual>();
	private List<List<Integer>> results = new ArrayList<>();
	
	private int generation;
	
	boolean running = true;
	
	public Evolution() {}
	
	public void setFile(String filename) {
		Evolution.filename = filename;
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
			eventsNumber = Integer.parseInt(sizes[0]);
			roomsNumber = Integer.parseInt(sizes[1]);
			featuresNumber = Integer.parseInt(sizes[2]);
			studentsNumber = Integer.parseInt(sizes[3]);
			
			int begin = 1;
			int end = begin + roomsNumber;
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				roomsSizes.add(Integer.parseInt(line));
			}
			
			begin = end;
			end = begin + (studentsNumber * eventsNumber);
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				studentsData.add(Integer.parseInt(line));
			}
			
			begin = end;
			end = begin + (featuresNumber * roomsNumber);
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				roomsData.add(Integer.parseInt(line));
			}
			
			begin = end;
			end = begin + (eventsNumber * featuresNumber);
			
			for (int i = begin; i < end; i++) {
				line = br.readLine();
				eventsData.add(Integer.parseInt(line));
			}
			
			begin = 0;
			for (int i = 0; i < studentsNumber; i++) {
				end = begin + eventsNumber;
				List studentEvents = new ArrayList<Integer>();
				
				for (int j = begin; j < end; j++) {
					studentEvents.add(studentsData.get(j));
				}
				
				studentsEvents.add(studentEvents);
				begin += eventsNumber;
			}
			
			begin = 0;
			for (int i = 0; i < roomsNumber; i++) {
				end = begin + featuresNumber;
				List roomFeatures = new ArrayList<Integer>();
				
				for (int j = begin; j < end; j++) {
					roomFeatures.add(roomsData.get(j));
				}
				
				roomsFeatures.add(roomFeatures);
				begin += featuresNumber;
			}
			
			begin = 0;
			for (int i = 0; i < eventsNumber; i++) {
				end = begin + featuresNumber;
				List eventFeatures = new ArrayList<Integer>();
				
				for (int j = begin; j < end; j++) {
					eventFeatures.add(eventsData.get(j));
				}
				
				eventsFeatures.add(eventFeatures);
				begin += featuresNumber;
			}
			
			// PREPARE STUDENTS
			for (int i = 0; i < studentsNumber; i++) {
				students.add(new Student(i, studentsEvents.get(i)));
			}
			
			// PREPARE ROOMS
			for (int i = 0; i < roomsNumber; i++) {
				rooms.add(new Room(i, roomsFeatures.get(i), roomsSizes.get(i)));
			}
			
			// PREPARE EVENTS
			for (int i = 0; i < eventsNumber; i++) {
				events.add(new Event(i, eventsFeatures.get(i), rooms, students));
			}
			
			prepareSlotsMap();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		population = new ArrayList<Individual>();
		selector = new NTournamentSelect(Parameters.tournamentSize, Parameters.populationSize);
		crossover = new SinglePointCrossover();
//		mutator = new SingleGeneMutation();
		mutator = new MultiGenesMutation(Parameters.mutationRate);
		insertion = new SimpleInsertion();
		findBest = new FindBest();
	}
	
	private void prepareSlotsMap() {
		for (Room room : Evolution.rooms) {
			for (int i = 0; i < Evolution.slotsNumber; i++) {
				List<Integer> suitableEvents = findEvents(room, events);
				slotsMap.put(Arrays.asList(room.getId(), i),suitableEvents);
			}
		}
	}
	
	public void stopEvolution() {
		running = false;
	}
	
	public boolean saveSolution(String filename) {
		int i = 0;
		
		for (Individual solution : solutions) {
			solution.saveSolution(filename + "_run_" + i);
			i++;
		}
		
		if (saveStats(filename + "_results")) {
			return true;
		} else {
			return false;
		}
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
		
		for (int j = 0; j < Parameters.populationSize; j++) {
			population.add(new Individual());
		}
		
		if (Parameters.runTime > 0)
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
		
		for (int i = 0; i < runsNumber; i++) {
			initialize();
			List<Integer> stats = new ArrayList<Integer>();
			
			
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
				
				for (Individual ind : childs) {
					System.out.println("UnEv: " + ind.unplacedEventsNumber() + " Fitness: " + ind.getFitness());
				}
				
				stats.add(best.getFitness());
			}
			results.add(stats);
			solutions.add(best);
		}
		
		stoping();
	}
	
	public void setRunsNumber(int numberOfRuns) {
		runsNumber = numberOfRuns;
	}
	
	private void startTimer() {
		new Thread(new Runnable() {
			 public void run() {
				 try {
					Thread.sleep(Parameters.runTime);
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
				for (int i = 0; i < featuresNumber; i++) {
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
	
	private boolean saveStats(String filename) {
		
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(filename + ".csv");
			int lineCounter = 0;
			
			for (int i = 0; i < results.size(); i++) {
				fw.append(i + ",");
			}
			
			fw.append("\n");
			
			while (true) {
				String line = "";
				int emptyColumns = 0;
			
				for (int i = 0; i < results.size(); i++) {
					if (lineCounter >= results.get(i).size()) {
						line += ",";
						emptyColumns++;
					} else {
						if (results.get(i).get(lineCounter) != null) {
							line += results.get(i).get(lineCounter).toString() + ',';
						}
					}
				}
				
				if (emptyColumns >= results.size()) {
					break;
				}
				
				fw.append(line + "\n");
				
				lineCounter++;
			}
			fw.append('\n');
			fw.append(Parameters.printParams());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return true;
	}
	
	public void clear() {
		solutions = new ArrayList<>();
		results = new ArrayList<>();
	}
}
