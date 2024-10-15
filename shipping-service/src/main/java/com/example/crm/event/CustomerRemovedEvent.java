package com.example.crm.event;

public class CustomerRemovedEvent extends CustomerEvent {

	private String identity;

	public CustomerRemovedEvent() {
	}

	public CustomerRemovedEvent(CustomerEventType type) {
		super(type);
	}

	public CustomerRemovedEvent(String identity) {
		super(CustomerEventType.CUSTOMER_REMOVED);
		this.identity = identity;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	@Override
	public String toString() {
		return "CustomerRemovedEvent [identity=" + identity + "]";
	}

}
