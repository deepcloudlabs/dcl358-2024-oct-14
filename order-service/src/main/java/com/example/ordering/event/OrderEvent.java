package com.example.ordering.event;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class OrderEvent {
	private static AtomicInteger SEQUENCE = new AtomicInteger();
	private int sequenceId;
	private long timestamp;

	public OrderEvent() {
		timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
		sequenceId = SEQUENCE.getAndIncrement();
	}

	public OrderEvent(int sequenceId, long timestamp) {
		this.sequenceId = sequenceId;
		this.timestamp = timestamp;
	}

	public int getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public abstract long getOrderId();

	public abstract String getCustomerId();
}
