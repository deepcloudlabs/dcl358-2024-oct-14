package com.example.crm.es;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Document(collection="customer-es-events")
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
	@Type(value=CustomerCreatedEvent.class,name="CUSTOMER_CREATED"),
	@Type(value=CustomerRemovedEvent.class,name="CUSTOMER_REMOVED"),
	@Type(value=CustomerAddressesChangedEvent.class,name="CUSTOMER_ADDRESS_CHANGED"),
	@Type(value=CustomerPhonesChangedEvent.class,name="CUSTOMER_PHONE_CHANGED")	
})
public abstract class CustomerEvent {
	@Id
	private String eventId = UUID.randomUUID().toString();
	private long timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
	private String customerIdentity;

	public CustomerEvent() {
	}

	public CustomerEvent(String customerIdentity) {
		this.customerIdentity = customerIdentity;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getCustomerIdentity() {
		return customerIdentity;
	}

	public void setCustomerIdentity(String customerIdentity) {
		this.customerIdentity = customerIdentity;
	}

	@Override
	public String toString() {
		return "CustomerEvent [eventId=" + eventId + ", timestamp=" + timestamp + ", customerIdentity="
				+ customerIdentity + "]";
	}

}
