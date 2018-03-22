package honours_project;

import java.util.List;

public class Room {
	private int roomId;
	private Slot[] slots;
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

	public Slot[] getSlots() {
		return slots;
	}

	public List<Integer> getFeatures() {
		return features;
	}

	public void setFeatures(List<Integer> features) {
		this.features = features;
	}
	
	public void setSlot(int index, Slot slot) {
		slots[index] = slot;
	}
	
	public int getSpaces() {
		return spaces;
	}

	public void setSpaces(int spaces) {
		this.spaces = spaces;
	}

	public Slot getSlot(int slot) {
		return slots[slot];
	}
	
	private void clearSlots() {
		slots = new Slot[Evolution.slotsNumber];
		
		for (int i = 0; i < Evolution.slotsNumber; i++) {
			slots[i] = new Slot(roomId, i);
		}
	}
}
