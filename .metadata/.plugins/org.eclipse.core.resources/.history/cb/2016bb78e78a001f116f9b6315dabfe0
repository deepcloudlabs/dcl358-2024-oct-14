package com.example.crm.event;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public abstract class CustomerEvent {
	private String eventId = UUID.randomUUID().toString();
	private CustomerEventType type;
	private long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);

	public CustomerEvent(CustomerEventType type) {
		this.type = type;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public CustomerEventType getType() {
		return type;
	}

	public void setType(CustomerEventType type) {
		this.type = type;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "CustomerEvent [eventId=" + eventId + ", type=" + type + ", timestamp=" + timestamp + "]";
	}

}
