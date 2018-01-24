package honours_project;

import java.util.List;

public class Slot {
	
	private int roomId;
	private int slotId;
	private List<Integer> possibleEvents; //list of suitable events
	private Event allocatedEvent;
	
	public Slot(int roomId, int slotId, List<Integer> events) {
		this.roomId = roomId;
		this.slotId = slotId;
		possibleEvents = events;
	}

	public List<Integer> getPossibleEvents() {
		return possibleEvents;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public int getSlotId() {
		return slotId;
	}

	public void setSlotId(int slotId) {
		this.slotId = slotId;
	}

	public Event getAllocatedEvent() {
		return allocatedEvent;
	}

	public void setAllocatedEvent(Event alloactedEvent) {
		this.allocatedEvent = alloactedEvent;
	}
	
	
}
