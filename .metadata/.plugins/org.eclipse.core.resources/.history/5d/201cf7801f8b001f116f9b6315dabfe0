package com.example.crm.event;

import com.example.shipping.document.CustomerDocument;

public class CustomerCreatedEvent extends CustomerEvent {
	private CustomerDocument customer;
	
	
	public CustomerCreatedEvent() {
	}

	public CustomerCreatedEvent(CustomerEventType type) {
		super(type);
	}

	public CustomerCreatedEvent(CustomerDocument customer) {
		super(CustomerEventType.CUSTOMER_CREATED);
		this.customer = customer;
	}

	public CustomerDocument getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDocument customer) {
		this.customer = customer;
	}

	@Override
	public String toString() {
		return "CustomerCreatedEvent [customer=" + customer + "]";
	}

}
