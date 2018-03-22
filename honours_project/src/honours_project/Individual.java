package honours_project;

import java.util.ArrayList;
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

	private Room[] rooms = new Room[Evolution.roomsNumber];
	private int[] eventsPermutation = new int[Evolution.eventsNumber];
	public Slot[] eventsSlots = new Slot[Evolution.eventsNumber];
	public Slot[] reservedEventsSlots = new Slot[Evolution.eventsNumber];
	public List<Integer> unplacedEvents = new ArrayList<>();
	private double fitness = 0;
	
	private int end = 0;
	private int single = 0;
	private int three = 0;
	
	public Map<Integer, Integer> costMap = new HashMap<>();
	public Map<Slot, Integer> slotsCostMap = new HashMap<>();
	
	public List<Integer> temp = new ArrayList<>();
	
	public Individual() {
		
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
		

		initCostMap();
		evaluate();
	}


	public Individual(int[] permutation, Slot[] slotsPermutation, Slot[] reservedSlotsPermutation) {
		initCostMap();
		for (int i = 0; i < Evolution.eventsNumber; i++)
			this.eventsPermutation[i] = permutation[i];
		// empty rooms

		for (int i = 0; i < Evolution.roomsNumber; i++) {
			Room room = new Room(i, null, Evolution.slotsNumber);
			
			rooms[i] = room;
		}
		
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			this.eventsSlots[i] = slotsPermutation[i];
			this.reservedEventsSlots[i] = reservedSlotsPermutation[i];
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

	public int[] getPermutation() {
		return eventsPermutation;
	}

	public void setPermutation(int[] permutation) {
		this.eventsPermutation = permutation;
	}

	public double getFitness() {
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
		caclFitness();
	}
	
	public void caclFitness() {
		single = 0;
		three = 0;
		end = 0;
		singleEvents();
		endOfDayEvents();
		moreThanThreeEvents();
		
		int missedEventsPenalty = unplacedEvents.size() * 100;
		fitness = (single) + end + three + missedEventsPenalty;
	}
	
	private void singleEvents() {
		List<Integer> students = new ArrayList<Integer>();
		
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 9; j++) {
				int slot = i * 9 + j;
				
				for (Room room : rooms) {
					Event event = room.getSlot(slot).getAllocatedEvent();
					
					if (event != null) {
						for (Student s : event.getStudents()) {
							students.add(s.getStudentId());
						}
					}
				}
			}

			Set<Integer> set = new HashSet<Integer>(students);
			
			for (Integer s : set) {
				long occ = students.stream().filter(p -> p.equals(s)).count();
				
				if (occ == 1) {
					single++;
					
					for (int j = 0; j < 9; j++) {
						calculateEventsCost(i * 9 + j, s);
					}
				}
			}
			
			students = new ArrayList<Integer>();
		}
	}
	
	// calculate students having events in last slot of the day
	private void endOfDayEvents() {
		for (int i = 1; i < 6; i++) {
			int index = i * 9 - 1;
			
			for (Room room : rooms) {
				Event event = room.getSlot(index).getAllocatedEvent();
				
				if (event != null) {
					end += event.getStudents().size();
					costMap.put(event.getId(), event.getStudents().size());
				}
			}
		}
	}
	
	private void moreThanThreeEvents() {
		List<List<Integer>> students = new ArrayList<List<Integer>>();
		
		for (int i = 0; i < Evolution.slotsNumber; i++) {
			List<Integer> subList = new ArrayList<Integer>();
			
			for (Room room : rooms) {
				Event event = room.getSlot(i).getAllocatedEvent();
				
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
				
				int k = i - 2;
				
				while (k < 0) {
					k++;
				}
				
				for (Integer s : set) {
					long occ = flatten.stream().filter(p -> p.equals(s)).count();
					
					if (occ > 2) {
						three++;
						
						for (; k <= i; k++) {
							calculateEventsCost(k, s);
						}
					}
				}
				
				students.remove(0);
			}
			
			if ((i + 1) % (Evolution.roomsNumber - 1) == 0 && i > 0) {
				students = new ArrayList<List<Integer>>();
			}
		}
	}
	
	private void calculateEventsCost(int slotId, int studentId) {
		for (Room room : rooms) {
			Event event = room.getSlot(slotId).getAllocatedEvent();
					
			if (event != null) {
				for (Student student : event.getStudents()) {
					if (student.getStudentId() == studentId) {
						int cost = costMap.get(event.getId()) + 1;
						costMap.put(event.getId(), cost);
								
						break;
					}
				}
			}
		}
	}
	
	public void build() {
		int[] columns = new int[Evolution.slotsNumber];
		int[] rows = new int[Evolution.roomsNumber];
		
		for (Integer eventId : eventsPermutation) {
			Event event = Evolution.events.get(eventId);
			boolean found = false;
			List<Slot> reservedSlots = new ArrayList<Slot>();
			List<Slot> slots = new ArrayList<Slot>();
			
			Slot selectedSlot = eventsSlots[event.getId()];
			
			if (selectedSlot != null) {
				Slot slot = rooms[selectedSlot.getRoomId()].getSlot(selectedSlot.getSlotId());
				
				if (slot.getAllocatedEvent() == null && studentsNoClash(event, slot.getSlotId(), slot.getRoomId())) {
					slot.setAllocatedEvent(event);
					eventsSlots[event.getId()] = slot;
					found = true;
					columns[slot.getSlotId()] += 1;
					rows[slot.getRoomId()] += 1;
				}
			} 
			
			if (found == false) {
				selectedSlot = reservedEventsSlots[event.getId()];
				if (selectedSlot != null) {
					Slot slot = rooms[selectedSlot.getRoomId()].getSlot(selectedSlot.getSlotId());
					
					if (slot.getAllocatedEvent() == null && studentsNoClash(event, slot.getSlotId(), slot.getRoomId())) {
						slot.setAllocatedEvent(event);
						eventsSlots[event.getId()] = slot;
						found = true;
						columns[slot.getSlotId()] += 1;
						rows[slot.getRoomId()] += 1;
					}
				}
			}
			
			if (found == false) {
				for (int roomNumber : event.getSuitableRooms()) {
					for (Slot slot : rooms[roomNumber].getSlots()) {
						if (slot.getAllocatedEvent() == null && studentsNoClash(event, slot.getSlotId(), roomNumber)) {
							if (slot.getSlotId() != 0 && (slot.getSlotId() + 1) % 9 == 0) {
								reservedSlots.add(slot);
							} else {
								slots.add(slot);
							}
						}
					}
				}
				
				Slot best = null;
				for (Slot slot : slots) {
					if (best == null)
						best = slot;
					else {
						int val = columns[best.getSlotId()];
						int newVal = columns[slot.getSlotId()];
						
						if (val > newVal) {
							best = slot;
						}
					}
				}
				
				if (best != null) {
					best.setAllocatedEvent(event);
					found = true;
					columns[best.getSlotId()] += 1;
					rows[best.getRoomId()] += 1;
					eventsSlots[event.getId()] = new Slot(best.getRoomId(), best.getSlotId());
				}
				
				if (found == false) {
					for (Slot slot : reservedSlots) {
						if (best == null)
							best = slot;
						else {
							int val = columns[best.getSlotId()];
							int newVal = columns[slot.getSlotId()];
							
							if (val > newVal) {
								best = slot;
							}
						}
					}
					
					if (best != null) {
						best.setAllocatedEvent(event);
						found = true;
						columns[best.getSlotId()] += 1;
						rows[best.getRoomId()] += 1;
						eventsSlots[event.getId()] = new Slot(best.getRoomId(), best.getSlotId());
					}
				}
			}
			
			if (found == false) {
				unplacedEvents.add(event.getId());
			}
		}
	}
	
	private int[] getHarderFirst(int[] permutation) {
		Map<Integer, Integer> temp = new LinkedHashMap<Integer, Integer>();
		
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
	public boolean studentsNoClash(Event event, int slot, int roomId) {
		boolean feasible = true;
		
		for (Student student : event.getStudents()) {
			for (Room room : rooms) {
				if (room.getId() != roomId) {
					Event slotEvent = room.getSlot(slot).getAllocatedEvent();
						
					if (slotEvent != null) {
						for (Student slotStudent : slotEvent.getStudents()) {
							if (slotStudent.getStudentId() == student.getStudentId()) {
								feasible = false;
							}
						}
					}
				}
			}
		}
		
		return feasible;
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
	
	private void initCostMap() {
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			costMap.put(i, 0);
		}
	}
	
	public Individual copy() {
		int [] permutationCopy = new int[Evolution.eventsNumber];
		
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			permutationCopy[i] = eventsPermutation[i];
		}
		
		Slot[] eventsSlotsCopy = new Slot[Evolution.eventsNumber];
		Slot[] reserveEventsSlotsCopy = new Slot[Evolution.eventsNumber];
		
		for (int i = 0; i < Evolution.eventsNumber; i++) {
			eventsSlotsCopy[i] = this.eventsSlots[i];
			reserveEventsSlotsCopy[i] = this.reservedEventsSlots[i];
		}
		
		return new Individual(permutationCopy, eventsSlotsCopy, reserveEventsSlotsCopy);
	}
}
