package honours_project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Individual {
	private List<Room> rooms = new ArrayList<Room>();
	private List<Integer> permutation = new ArrayList<Integer>();
	private List<Event> unplacedEvents = new ArrayList<Event>();
	private int fitness = 0;
	
	private int clash = 0;
	private int end = 0;
	private int single = 0;
	private int three = 0;
	
	public Individual() {
		permutation = new ArrayList<Integer>();
		
		for (int i = 0; i < Application.EVENTS_NUMBER; i++) {
			permutation.add(i);
		}
		
		Collections.shuffle(permutation);
		
		// empty rooms
		for (int i = 0; i < Application.ROOMS_NUMBER; i++) {
			Room room = new Room();
			
			rooms.add(room);
		}
	}
	
	public int getClashes() {
		return clash;
	}

	public List<Integer> getPermutation() {
		return permutation;
	}

	public void setPermutation(List<Integer> permutation) {
		this.permutation = permutation;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	public Room getRoom(int roomId) {
		return rooms.get(roomId);
	}
	
	public void evaluate() {
		createPhenotype();
		singleEvents();
		endOfDayEvents();
		moreThanThreeEvents();
	}
	
	private void singleEvents() {
		List<Integer> students = new ArrayList<Integer>();
		
		for (int i = 0; i < Application.SLOTS_NUMBER; i++) {
			for (Room room : rooms) {
				Event event = room.getSlot(i);
				
				if (event != null) {
					for (Student s : event.getStudents()) {
						students.add(s.getStudentId());
					}
				}
			}
			
			if ((i + 1) % (Application.ROOMS_NUMBER - 1) == 0 && i > 0) {
				Set<Integer> set = new HashSet<Integer>(students);
				
				for (Integer s : set) {
					long occ = students.stream().filter(p -> p.equals(s)).count();
					
					if (occ == 1) {
						single++;
					}
				}
				
				students = new ArrayList<Integer>();
			}
		}
		System.out.println("Single: " + single);
	}
	
	// calculate students having events in last slot of the day
	private void endOfDayEvents() {
		for (int i = 1; i < 6; i++) {
			int index = i * 9 - 1;
			
			List<Integer> students = new ArrayList<Integer>();
			
			for (Room room : rooms) {
				Event event = room.getSlot(index);
				
				if (event != null) {
					for (Student s : event.getStudents()) {
						students.add(s.getStudentId());
					}
				}
			}
			
			end += students.size();
		}
		System.out.println("End: " + end);
	}
	
	private void moreThanThreeEvents() {
		List<List<Integer>> students = new ArrayList<List<Integer>>();
		
		for (int i = 0; i < Application.SLOTS_NUMBER; i++) {
			List<Integer> subList = new ArrayList<Integer>();
			
			for (Room room : rooms) {
				Event event = room.getSlot(i);
				
				if (event != null) {
					for (Student s : event.getStudents()) {
						subList.add(s.getStudentId());
					}
				}
			}
			students.add(subList);
			
			if (students.size() == 3) {
				List<Integer> flatten = students.stream().flatMap(List::stream).collect(Collectors.toList());
				Set<Integer> set = new HashSet<Integer>(flatten);
				
				for (Integer s : set) {
					long occ = flatten.stream().filter(p -> p.equals(s)).count();
					
					if (occ > 2) {
						three++;
					}
				}
				
				students.remove(0);
			}
			
			if ((i + 1) % (Application.ROOMS_NUMBER - 1) == 0 && i > 0) {
				students = new ArrayList<List<Integer>>();
			}
		}
		System.out.println("Three: " + three);
	}
	
	public void createPhenotype() {
		for (Integer eventId : permutation) {
			Event event = Application.events.get(eventId);
			boolean found = false;
			
			for (Room room : event.getSuitableRooms()) {
				for (int i = 0; i < Application.SLOTS_NUMBER; i++) {
//					System.out.println("Empty: " + (rooms.get(room.getId()).getSlot(i) == null) + " Clash: " + studentsNoClash(event, i, room.getId()));
					if (rooms.get(room.getId()).getSlot(i) == null && studentsNoClash(event, i)) {
						rooms.get(room.getId()).setSlot(i, event);
						found = true;
						break;
					}
				}
				
				if (found) {
					break;
				}
			}
			
			if (found == false) {
				unplacedEvents.add(event);
			}
		}
		
		System.out.println("Missed events: " + unplacedEvents.size());
	}
	
	// check for clashing students between selected event and events in other rooms within same time slot
	private boolean studentsNoClash(Event event, int slot) {
		boolean feasible = true;
		
		for (Student student : event.getStudents()) {
			for (Room room : rooms) {
				Event slotEvent = room.getSlot(slot);
					
				if (slotEvent != null) {
					for (Student slotStudent : slotEvent.getStudents()) {
						if (slotStudent.getStudentId() == student.getStudentId()) {
							clash++;
							feasible = false;
						}
					}
				}
			}
		}
		
		return feasible;
	}
	
	public void saveSolution() {
		Map solution = new HashMap<Integer, Integer[]>();
		
		for (int i = 0; i < Application.ROOMS_NUMBER; i++) {
			for (int j = 0; j < Application.SLOTS_NUMBER; j++) {
				Room room = rooms.get(i);
				Event event = room.getSlot(j);
				
				if (event != null) {
					Integer[] pair = {j, i};
					solution.put(event.getId(), pair);
				}
			}
		}
		
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(Application.SOLUTION_FILENAME);
			
			for (int i = 0; i < Application.EVENTS_NUMBER; i++) {
				Integer[] pair = (Integer[]) solution.get(i);
				
				if (pair != null) {
					fw.append(String.valueOf(pair[0]) + " " + String.valueOf(pair[1]) + "\n");
				} else {
					fw.append("-1 -1\n");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
