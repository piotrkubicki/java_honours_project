package honours_project;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Individual {
	public static Map<Integer, List<Slot>> slotsMap = new HashMap<>();

	private Room[] rooms = new Room[Evolution.roomsNumber];
	private int[] eventsPermutation = new int[Evolution.eventsNumber];
	private List<Event> unplacedEvents = new ArrayList<Event>();
	private int fitness = 0;
	
	private int clash = 0;
	private int end = 0;
	private int single = 0;
	private int three = 0;
	private int total = 0;
	public Map<Integer, Integer> costMap = new HashMap<>();
	
	public Individual() {
		initCostMap();
		// empty rooms
		for (int i = 0; i < Evolution.roomsNumber; i++) {
			Room room = new Room(i, null, Evolution.slotsNumber);
			
			rooms[i] = room;
		}

		for (int i = 0; i < Evolution.eventsNumber; i++) {
			eventsPermutation[i] = Evolution.events.get(i).getId();
		}
		
		shuffleArray(eventsPermutation);
		eventsPermutation = getHarderFirst(eventsPermutation);
		
		evaluate();
	}


	public Individual(int[] permutation) {
		initCostMap();
		for (int i = 0; i < Evolution.eventsNumber; i++)
			this.eventsPermutation[i] = permutation[i];
		// empty rooms

		for (int i = 0; i < Evolution.roomsNumber; i++) {
			Room room = new Room(i, null, Evolution.slotsNumber);
			
			rooms[i] = room;
		}

	}
	
	private void shuffleArray(int[] ar) {
	    for (int i = ar.length - 1; i > 0; i--) {
	      int index = Evolution.randomGenerator.nextInt(i + 1);
	      
	      int a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	 }
	
	public Room[] getRooms() {
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

	public int[] getPermutation() {
		return eventsPermutation;
	}

	public void setPermutation(int[] permutation) {
		this.eventsPermutation = permutation;
	}

	public int getFitness() {
		return fitness;
	}

	public void setFitness(int fitness) {
		this.fitness = fitness;
	}
	
	public Room getRoom(int roomId) {
		return rooms[roomId];
	}
	
	public void evaluate() {
		initCostMap();
		build();
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
	
	private void calculateEventsCost(int slotId, int studentId) {
		for (int i = slotId - 2; i < slotId; i++) {
			for (Room room : rooms) {
				Event event = room.getSlot(i);
				
				if (event != null) {
					for (Student student : event.getStudents()) {
						if (student.getStudentId() == studentId) {
							int cost = costMap.get(event.getId()) + 1;
							costMap.put(event.getId(), cost);
							total++;
							break;
						}
					}
				}
			}
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
						calculateEventsCost(i, s);
					}
				}
				
				students.remove(0);
			}
			
			if ((i + 1) % (Evolution.roomsNumber - 1) == 0 && i > 0) {
				students = new ArrayList<List<Integer>>();
			}
		}
		
		System.out.println("THREE: " + three + " TOTAL: " + total);
	}
	
	public void build() {
		for (Integer eventId : eventsPermutation) {
			Event event = Evolution.events.get(eventId);
			boolean found = false;
			List<Slot> tempSlots = new ArrayList<Slot>();
			
			for (Room room : event.getSuitableRooms()) {
				for (int i = 0; i < Evolution.slotsNumber; i++) {
					Room selectedRoom = findRoom(room.getId());
					
					if (selectedRoom != null) {
						if (selectedRoom.getSlot(i) == null && studentsNoClash(event, i)) {
							if (i != 0 && (i + 1) % 9 == 0) {
								tempSlots.add(new Slot(room.getId(), i, null));
							} else {
								selectedRoom.setSlot(i, event);
								Slot slot = new Slot(room.getId(), i, null);
								unplacedEvents.remove(event);
								
								if (!haveSlot(event, slot)) {
									slotsMap.get(event.getId()).add(slot);
								}
							
								found = true;
								break;
							}
						}
					}
				}
				
				if (!found) {
					for (Slot slot : tempSlots) {
						Room selectedRoom = findRoom(slot.getRoomId());
						selectedRoom.setSlot(slot.getSlotId(), event);
						unplacedEvents.remove(event);
						found = true;
						break;
					}
				} else {
					break;
				}
			}
			
			if (found == false) {
				unplacedEvents.add(event);
			}
		}
		
		allocateUnplacedEvents();
	}
	
	private int[] getHarderFirst(int[] permutation) {
		Hashtable<Integer, Integer> temp = new Hashtable<Integer, Integer>();
		
		int[] result = new int[permutation.length];
		
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
		int counter = 0;
		
		while (i.hasNext()) {
			Map.Entry tt = (Map.Entry) i.next();
			result[counter] = ((Integer) tt.getKey());
			counter++;
		}
		
		return result;
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
				Room room = rooms[i];
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
	
	public static void initSlotsMap() {
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			slotsMap.put(i, new ArrayList<>());
		}
	}
	
	private boolean haveSlot(Event event, Slot slot) {
		List<Slot> slots = slotsMap.get(event.getId());
		
		for (Slot s : slots) {
			if (s.getRoomId() == slot.getRoomId() && s.getSlotId() == slot.getSlotId()) {
				return true;
			}
		}
		
		return false;
	}
	
	private void allocateUnplacedEvents() {
		List<Event> temp = new ArrayList<>();
		for (Event event : unplacedEvents) {
			for (Slot slot : slotsMap.get(event.getId())) {
				Room selectedRoom = findRoom(slot.getRoomId());
				Event oldEvent = selectedRoom.getSlot(slot.getSlotId());
				selectedRoom.setSlot(slot.getSlotId(), null);
				
				if (oldEvent == null) {
					if (selectedRoom.getSlot(slot.getSlotId()) == null && studentsNoClash(event, slot.getSlotId())) {
						selectedRoom.setSlot(slot.getSlotId(), event);
						temp.add(event);
					}
				} 
				else if (studentsNoClash(event, slot.getSlotId())) {
					if (relocateEvent(oldEvent, slot)) {
						selectedRoom.setSlot(slot.getSlotId(), event);
						temp.add(event);
					} else {
						selectedRoom.setSlot(slot.getSlotId(), oldEvent);
					}
				} else {
					selectedRoom.setSlot(slot.getSlotId(), oldEvent);
				}
			}
		}
		
		for (Event event : temp)
			unplacedEvents.remove(event);
	}

	private boolean relocateEvent(Event event, Slot currentSlot) {
		for (Slot slot : slotsMap.get(event.getId())) {
			if (slot.getRoomId() != currentSlot.getRoomId() && slot.getSlotId() != currentSlot.getSlotId()) {
				Room selectedRoom = findRoom(slot.getRoomId());
				
				if (selectedRoom.getSlot(slot.getSlotId()) == null && studentsNoClash(event, slot.getSlotId())) {
					selectedRoom.setSlot(slot.getSlotId(), event);
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void initCostMap() {
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			costMap.put(i, 0);
		}
	}
	
	private Room findRoom(int id) {
		
		return rooms[id];
	}
}
