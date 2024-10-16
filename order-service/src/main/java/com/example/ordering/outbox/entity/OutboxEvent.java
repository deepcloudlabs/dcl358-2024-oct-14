package com.example.ordering.outbox.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "outevents")
public class OutboxEvent {
	@Id
	private long eventId;
	@Column(columnDefinition = "varchar(256)")
	private String payload;
	private int tries;

	public OutboxEvent() {
	}

	public OutboxEvent(long eventId, String payload) {
		this.eventId = eventId;
		this.payload = payload;
	}

	public long getEventId() {
		return eventId;
	}

	public void setEventId(long eventId) {
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
