package com.example.crm.event;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
	@Type(value=CustomerCreatedEvent.class,name="CUSTOMER_CREATED"),
	@Type(value=CustomerRemovedEvent.class,name="CUSTOMER_REMOVED"),
	@Type(value=CustomerAddressesChangedEvent.class,name="CUSTOMER_ADDRESS_CHANGED"),
	@Type(value=CustomerAddressPhonesChangedEvent.class,name="CUSTOMER_PHONE_CHANGED")	
})
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
