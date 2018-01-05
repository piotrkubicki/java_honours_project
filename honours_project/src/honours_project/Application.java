package honours_project;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
	public static final String FILENAME = "/home/pz/competition01.tim";
	public static final String SOLUTION_FILENAME = "/home/pz/competition01.sln";
	public static int EVENTS_NUMBER;
	public static int ROOMS_NUMBER;
	public static int FEATURES_NUMBER;
	public static int STUDENTS_NUMBER;
	public static int SLOTS_NUMBER = 45;
	
	public static List<Integer> roomsSizes = new ArrayList<Integer>();
	public static List<Integer> studentsData = new ArrayList<Integer>();
	public static List<Integer> roomsData = new ArrayList<Integer>();
	public static List<Integer> eventsData = new ArrayList<Integer>();
	
	public static List<List<Integer>> studentsEvents = new ArrayList<List<Integer>>();
	public static List<List<Integer>> roomsFeatures = new ArrayList<List<Integer>>();
	public static List<List<Integer>> eventsFeatures = new ArrayList<List<Integer>>();
	
	public static List<Room> rooms = new ArrayList<Room>();
	public static List<Event> events = new ArrayList<Event>();
	public static List<Student> students = new ArrayList<Student>();
	
	public static void main(String[] args) {
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(FILENAME);
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
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Individual ind1 = new Individual();
		
		ind1.createPhenotype();
		System.out.println(ind1.getRoom(0).getSlots());
		System.out.println(ind1.getClashes());
		ind1.saveSolution();
		
		for (Event event : events) {
			System.out.println("Event: " + event.getId() + " Students: " + event.getStudents().size());
			
			for (Room room : event.getSuitableRooms()) {
				System.out.print(room.getId() + ", ");
			}
			System.out.println();
		}
		
		for (Room room : rooms) {
			System.out.println("ROOM: " + room.getId() + " Spaces: " + room.getSpaces());
		}
	}

}
