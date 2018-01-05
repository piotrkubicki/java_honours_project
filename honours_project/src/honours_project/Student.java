package honours_project;

import java.util.List;

public class Student {
	private int studentId;
	private List<Integer> events;

	public Student(int id, List<Integer> events) {
		studentId = id;
		this.events = events;
	}
	
	public List<Integer> getEvents() {
		return events;
	}

	public int getStudentId() {
		return studentId;
	}
	
	
}
