package honours_project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Individual {

	public static Map<Integer, List<Slot>> slotsMap = new HashMap<>();
	
	private List<Room> rooms = new ArrayList<Room>();
	private List<Slot> slotsPermutation;
	private List<Integer> eventsPermutation;
	private Map<Integer, Event> unplacedEvents = new HashMap<Integer, Event>();
	private int fitness = 0;
	
	private int clash = 0;
	private int end = 0;
	private int single = 0;
	private int three = 0;
	
	public Individual() {
		slotsPermutation = new ArrayList<Slot>();
		eventsPermutation = new ArrayList<Integer>();
		
		// empty rooms
		for (int i = 0; i < Evolution.roomsNumber; i++) {
			Room room = new Room();
			
			rooms.add(room);
		}
		
		for (Event event : Evolution.events) {
			eventsPermutation.add(event.getId());
			unplacedEvents.put(event.getId(), event);
		}
		
		for (Room room : Evolution.rooms) {
			for (int i = 0; i < Evolution.slotsNumber; i++) {
				Slot slot = new Slot(room.getId(), i, Evolution.slotsMap.get(Arrays.asList(room.getId(), i)));
				slotsPermutation.add(slot);
			}
		}
		
		Collections.shuffle(slotsPermutation);
		eventsPermutation = getHarderFirst(eventsPermutation);
		
		evaluate();
	}
	
	public Individual(List<Slot> slotsPermutation, List<Integer> eventsPermutation) {
		this.slotsPermutation = slotsPermutation;
		this.eventsPermutation = eventsPermutation;
		
		// empty rooms
		for (int i = 0; i < Evolution.roomsNumber; i++) {
			Room room = new Room();
			
			rooms.add(room);
		}
		
		for (Event event : Evolution.events) {
			unplacedEvents.put(event.getId(), event);
		}
		
		this.eventsPermutation = getHarderFirst(this.eventsPermutation);
				
		evaluate();
	}
	
	public List<Integer> getEventsPermutation() {
		return eventsPermutation;
	}

	public void setEventsPermutation(List<Integer> eventsPermutation) {
		this.eventsPermutation = eventsPermutation;
	}

	private List<Integer> getHarderFirst(List<Integer> permutation) {
		Map<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
		
		List<Integer> result = new ArrayList<Integer>();
		
		for (Integer i : permutation) {
			int key = Evolution.events.get(i).getSuitableRooms().size();
			
			temp.put(i, key);
		}
		
		ArrayList t = new ArrayList(temp.entrySet());
		
		Collections.sort(t, new Comparator() {
			public int compare(Object o1, Object o2) {
				Map.Entry e1 = (Map.Entry) o1;
				Map.Entry e2 = (Map.Entry) o2;
				Integer first = (Integer) e1.getValue();
				Integer second = (Integer) e2.getValue();
				
				return first.compareTo(second);
			}
		});
		
		Iterator i = t.iterator();
		
		while (i.hasNext()) {
			Map.Entry tt = (Map.Entry) i.next();
			result.add((Integer) tt.getKey());
		}
		
		return result;
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

	public List<Slot> getSlotsPermutation() {
		return slotsPermutation;
	}

	public void setSlotsPermutation(List<Slot> permutation) {
		this.slotsPermutation = permutation;
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
		
		for (int i = 0; i < Evolution.slotsNumber; i++) {
			for (Room room : rooms) {
				Event event = room.getSlot(i);
				
				if (event != null) {
					for (Student s : event.getStudents()) {
						students.add(s.getStudentId());
					}
				}
			}
			
			if ((i + 1) % (Evolution.roomsNumber - 1) == 0 && i > 0) {
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
		
		for (int i = 0; i < Evolution.slotsNumber; i++) {
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
			
			if ((i + 1) % (Evolution.roomsNumber - 1) == 0 && i > 0) {
				students = new ArrayList<List<Integer>>();
			}
		}
	}
	
	public void createPhenotype() {
		for (Integer eventId : eventsPermutation) {
			List<Slot> temp = new ArrayList<Slot>();
			Event event = Evolution.events.get(eventId);
			boolean found = false;
		
			// find feasible slot
			for (Slot slot : slotsPermutation) {
				if (slot.getAllocatedEvent() == null && slot.getPossibleEvents().contains(eventId) && studentsNoClash(event, slot.getSlotId())) {
					if (slot.getSlotId() != 0 && (slot.getSlotId() + 1) % 9 == 0) {
						temp.add(slot);
					} else {
						slot.setAllocatedEvent(event);
						unplacedEvents.remove(eventId);
						rooms.get(slot.getRoomId()).setSlot(slot.getSlotId(), slot.getAllocatedEvent());
						
						if (!haveSlot(event, slot)) {
							slotsMap.get(event.getId()).add(slot);
						}
						
						found = true;
						break;
					}
				}
			}
			// if slot not found select temp slot if any
			if (!found) {
				for (Slot slot : temp) {
					slot.setAllocatedEvent(event);
					unplacedEvents.remove(eventId);
					rooms.get(slot.getRoomId()).setSlot(slot.getSlotId(), slot.getAllocatedEvent());
					break;
				}
			}
			
			// move temp slots at the end of the permutation
			for (Slot slot : temp) {
				slotsPermutation.remove(slot);
				slotsPermutation.add(slot);
			}
		}
		
		allocateUnplacedEvents();
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
		
		for (int i = 0; i < Evolution.roomsNumber; i++) {
			for (int j = 0; j < Evolution.slotsNumber; j++) {
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
			fw = new FileWriter(filename + ".sln");
			
			for (int i = 0; i < Evolution.eventsNumber; i++) {
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
	
	public static void intiSlotsMap() {
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			slotsMap.put(i, new ArrayList<>());
//			System.out.println("SLOT: " + i);
		}
	}
	
	private boolean haveSlot(Event event, Slot slot) {
		List<Slot> slots = slotsMap.get(event.getId());
//		System.out.println(event.getId() + " " + slots);
		
		for (Slot s : slots) {
			if (s.getRoomId() == slot.getRoomId() && s.getSlotId() == slot.getSlotId()) {
				return true;
			}
		}
		
		return false;
	}
	
	private void allocateUnplacedEvents() {
		List<Event> temp = new ArrayList<>();
		for (Event event : unplacedEvents.values()) {
			for (Slot slot : slotsMap.get(event.getId())) {
				Event oldEvent = rooms.get(slot.getRoomId()).getSlot(slot.getSlotId());
				
				if (oldEvent == null) {
					rooms.get(slot.getRoomId()).setSlot(slot.getSlotId(), event);
					temp.add(event);
				} 
				else if (relocateEvent(oldEvent)) {
					rooms.get(slot.getRoomId()).setSlot(slot.getSlotId(), event);
					temp.add(event);
				}
			}
		}
		
		for (Event event : temp)
			unplacedEvents.remove(event.getId());
	}
	
	private boolean relocateEvent(Event event) {
		System.out.println(event.getId());
		for (Slot slot : slotsMap.get(event.getId())) {
			if (rooms.get(slot.getRoomId()).getSlot(slot.getSlotId()) == null && studentsNoClash(event, slot.getSlotId())) {
				rooms.get(slot.getRoomId()).setSlot(slot.getSlotId(), event);
				
				return true;
			}
		}
		
		return false;
	}
}
