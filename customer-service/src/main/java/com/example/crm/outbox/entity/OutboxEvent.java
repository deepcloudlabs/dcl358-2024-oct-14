package com.example.crm.outbox.entity;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="outbox-events")
public class OutboxEvent {
	@Id
	private String eventId;
	private String payload;
	private int tries;

	public OutboxEvent() {
	}

	public OutboxEvent(String eventId, String payload) {
		this.eventId = eventId;
		this.payload = payload;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public int getTries() {
		return tries;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}

	public void incrementTries() {
		this.tries++;
	}

	@Override
	public int hashCode() {
		return Objects.hash(eventId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OutboxEvent other = (OutboxEvent) obj;
		return eventId == other.eventId;
	}

	@Override
	public String toString() {
		return "OutboxEvent [eventId=" + eventId + ", payload=" + payload + ", tries=" + tries + "]";
	}

}
