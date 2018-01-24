package honours_project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Individual {
	private List<Room> rooms = new ArrayList<Room>();
	private List<Slot> permutation = new ArrayList<Slot>();
	private Map<Integer, Event> unplacedEvents = new HashMap<Integer, Event>();
	private int fitness = 0;
	
	private int clash = 0;
	private int end = 0;
	private int single = 0;
	private int three = 0;
	
	public Individual() {
		permutation = new ArrayList<Slot>();
		
		// empty rooms
		for (int i = 0; i < Evolution.ROOMS_NUMBER; i++) {
			Room room = new Room();
			
			rooms.add(room);
		}
		
		for (Event event : Evolution.events) {
			unplacedEvents.put(event.getId(), event);
		}
		Evolution.prepareSlotsMap();
		for (Room room : Evolution.rooms) {
			for (int i = 0; i < Evolution.SLOTS_NUMBER; i++) {
				Slot slot = new Slot(room.getId(), i, Evolution.slotsMap.get(Arrays.asList(room.getId(), i)));
				permutation.add(slot);
			}
		}
		
		Collections.shuffle(permutation);
//		System.out.println(permutation);
//		permutation = getHarderFirst(permutation);
//		System.out.println(permutation);
		
		evaluate();
	}
	
	public Individual(List<Slot> permutation) {
		this.permutation = permutation;
		
		// empty rooms
		for (int i = 0; i < Evolution.ROOMS_NUMBER; i++) {
			Room room = new Room();
			
			rooms.add(room);
		}
		
		for (Event event : Evolution.events) {
			unplacedEvents.put(event.getId(), event);
		}
				
		evaluate();
	}
	
	public List<Room> getRooms() {
		return rooms;
	}
	
	public int getEnd() {
		return end;
	}

	public int getSingle() {
		return single;
	}

	public int getThree() {
		return three;
	}

	public int getClashes() {
		return clash;
	}

	public List<Slot> getPermutation() {
		return permutation;
	}

	public void setPermutation(List<Slot> permutation) {
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
		
		int missedEventsPenalty = unplacedEvents.size() * 1000;
		fitness = single + end + three + missedEventsPenalty;
	}
	
	private void singleEvents() {
		List<Integer> students = new ArrayList<Integer>();
		
		for (int i = 0; i < Evolution.SLOTS_NUMBER; i++) {
			for (Room room : rooms) {
				Event event = room.getSlot(i);
				
				if (event != null) {
					for (Student s : event.getStudents()) {
						students.add(s.getStudentId());
					}
				}
			}
			
			if ((i + 1) % (Evolution.ROOMS_NUMBER - 1) == 0 && i > 0) {
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
	}
	
	private void moreThanThreeEvents() {
		List<List<Integer>> students = new ArrayList<List<Integer>>();
		
		for (int i = 0; i < Evolution.SLOTS_NUMBER; i++) {
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
			
			if ((i + 1) % (Evolution.ROOMS_NUMBER - 1) == 0 && i > 0) {
				students = new ArrayList<List<Integer>>();
			}
		}
	}
	
	public void createPhenotype() {
		for (Slot slot : permutation) {
			boolean found = false;
			
			if (!unplacedEvents.isEmpty()) {
				
				for (Integer eventId : slot.getPossibleEvents()) {
					
					if (unplacedEvents.containsKey(eventId)) {
						Event event = unplacedEvents.get(eventId);
						
						if (studentsNoClash(event, slot.getSlotId())) {
							slot.setAllocatedEvent(event);
							unplacedEvents.remove(eventId);
							found = true;
						}
					}
					
					if (found) {
						break;
					}
				}
			}
			
			rooms.get(slot.getRoomId()).setSlot(slot.getSlotId(), slot.getAllocatedEvent());
		}
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
	
	public void saveSolution(String filename) {
		Map solution = new HashMap<Integer, Integer[]>();
		
		for (int i = 0; i < Evolution.ROOMS_NUMBER; i++) {
			for (int j = 0; j < Evolution.SLOTS_NUMBER; j++) {
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
			fw = new FileWriter(filename);
			
			for (int i = 0; i < Evolution.EVENTS_NUMBER; i++) {
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
	
	public int unplacedEventsNumber() {
		return unplacedEvents.size();
	}
	
	public boolean isBetter(Individual other) {
		if (unplacedEventsNumber() < other.unplacedEventsNumber()) {
			return true;
		} else if (unplacedEventsNumber() > other.unplacedEventsNumber()) {
			return false;
		} else {
			if (getFitness() <= other.getFitness()) {
				return true;
			} else {
				return false;
			}
		}
	}
}
