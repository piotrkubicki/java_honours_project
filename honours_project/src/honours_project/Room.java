package honours_project;

import java.util.List;

public class Room {
	private int roomId;
	private Event[] slots = new Event[Evolution.slotsNumber];
	private List<Integer> features;
	private int spaces;
	
	public Room(int roomId, List<Integer> features, int spaces) {
		this.roomId = roomId;
		this.features = features;
		this.spaces = spaces;
		
		clearSlots();
	}
	
	public Room() {
		clearSlots();
	};
	
	public int getId() {
		return roomId;
	}

	public Event[] getSlots() {
		return slots;
	}

	public List<Integer> getFeatures() {
		return features;
	}

	public void setFeatures(List<Integer> features) {
		this.features = features;
	}
	
	public void setSlot(int slot, Event event) {
		slots[slot] = event;
	}
	
	public int getSpaces() {
		return spaces;
	}

	public void setSpaces(int spaces) {
		this.spaces = spaces;
	}

	public Event getSlot(int slot) {
		return slots[slot];
	}
	
	private void clearSlots() {
		slots = new Event[Evolution.slotsNumber];
	}
}
