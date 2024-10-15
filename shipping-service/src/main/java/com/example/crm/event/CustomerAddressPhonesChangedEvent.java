package com.example.crm.event;

public class CustomerAddressPhonesChangedEvent extends CustomerEvent {
	private String identity;
	private Object eventData;

	public CustomerAddressPhonesChangedEvent() {
	}

	public CustomerAddressPhonesChangedEvent(CustomerEventType type) {
		super(type);
	}

	public CustomerAddressPhonesChangedEvent(String identity, Object value) {
		super(CustomerEventType.PHONE_CHANGED_EVENT);
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
		return "CustomerAddressPhonesChangedEvent [identity=" + identity + ", eventData=" + eventData + "]";
	}

}
