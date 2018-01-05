package honours_project;

import java.util.ArrayList;
import java.util.List;

public class Event {
	private int eventId;
	private List<Student> students = new ArrayList<Student>();
	private List<Room> suitableRooms = new ArrayList<Room>(); 
	
	public Event(int eventId, List<Integer> features, List<Room> rooms, List<Student> students) {
		this.eventId = eventId;
		findRooms(features, rooms);
		setStudents(students);
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
		for (Room room : rooms) {
			boolean feasible = true;
			
			if (room.getSpaces() < students.size()) {
				feasible = false;
			} else {
				for (int i = 0; i < Application.FEATURES_NUMBER; i++) {
					List<Integer> roomFeatures = room.getFeatures();
					if (features.get(i) == 1 && roomFeatures.get(i) == 0) {
						feasible = false;
						break;
					}
				}
			}
			
			if (feasible) {
				suitableRooms.add(room);
			}
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
