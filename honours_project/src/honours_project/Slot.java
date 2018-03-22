package honours_project;

import java.util.List;

public class Slot {
	
	private int roomId;
	private int slotId;
	private Event allocatedEvent = null;
	
	public Slot(int roomId, int slotId) {
		this.roomId = roomId;
		this.slotId = slotId;
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
