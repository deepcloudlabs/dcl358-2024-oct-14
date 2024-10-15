package com.example.crm.event;

public class CustomerAddressesChangedEvent extends CustomerEvent {

	private String identity;
	private Object eventData;

	public CustomerAddressesChangedEvent() {
	}

	public CustomerAddressesChangedEvent(CustomerEventType type) {
		super(type);
	}

	public CustomerAddressesChangedEvent(String identity, Object value) {
		super(CustomerEventType.ADDRESS_CHANGED_EVENT);
		this.identity = identity;
		this.eventData = value;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public Object getEventData() {
		return eventData;
	}

	public void setEventData(Object eventData) {
		this.eventData = eventData;
	}

	@Override
	public String toString() {
		return "CustomerAddressesChangedEvent [identity=" + identity + ", eventData=" + eventData + "]";
	}

}
