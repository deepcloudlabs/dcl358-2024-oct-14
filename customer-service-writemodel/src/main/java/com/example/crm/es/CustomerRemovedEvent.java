package com.example.crm.es;

public class CustomerRemovedEvent extends CustomerEvent {

	public CustomerRemovedEvent() {
	}

	public CustomerRemovedEvent(String customerIdentity) {
		super(customerIdentity);
	}

	@Override
	public String toString() {
		return "CustomerRemovedEvent []";
	}

}
