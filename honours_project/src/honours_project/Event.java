package honours_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Event {
	private int eventId;
	private List<Student> students = new ArrayList<Student>();
	private List<Room> suitableRooms = new ArrayList<Room>(); 
	
	public Event(int eventId, List<Integer> features, List<Room> rooms, List<Student> students) {
		this.eventId = eventId;
		
		setStudents(students);
		findRooms(features, rooms);
	}
	
	public int getId() {
		return eventId;
	}
	
	public List<Room> getSuitableRooms() {
		return suitableRooms;
	}

	public List<Student> getStudents() {
		return students;
	}
	
	private void findRooms(List<Integer> features, List<Room> rooms) {
		TreeMap<Integer, Room> temp = new TreeMap<Integer, Room>();
		
		for (Room room : rooms) {
			boolean feasible = true;
			int ff = 0;
			
			if (room.getSpaces() < students.size()) {
				feasible = false;
			} else {
				for (int i = 0; i < Evolution.FEATURES_NUMBER; i++) {
					List<Integer> roomFeatures = room.getFeatures();
					if (features.get(i) == 1 && roomFeatures.get(i) == 0) {
						feasible = false;
						break;
					}
					
					if (features.get(i) == 0 && roomFeatures.get(i) == 1) {
						ff++;
					}
				}
			}
			
			if (feasible) {
				while (temp.containsKey(ff)) {
					ff++;
				} 
				
				temp.put(ff, room);
			}
		}
		
		for (Map.Entry<Integer, Room> entry : temp.entrySet()) {
			suitableRooms.add(entry.getValue());
		}
	}
	
	private void setStudents(List<Student> studentsList) {
		for (Student student : studentsList) {
			List<Integer> studentEvents = student.getEvents();
			
			if (studentEvents.get(eventId) == 1) {
				students.add(student);
			}
		}
	}
}
