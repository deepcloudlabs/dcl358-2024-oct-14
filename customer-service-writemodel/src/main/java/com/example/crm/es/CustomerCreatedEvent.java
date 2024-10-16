package com.example.crm.es;

import java.util.List;

import com.example.crm.dto.request.Address;
import com.example.crm.dto.request.CreateCustomerRequest;
import com.example.crm.dto.request.Phone;

public class CustomerCreatedEvent extends CustomerEvent {
	private String identity;
	private String fullname;
	private List<Address> addresses;
	private List<Phone> phones;
	
	
	public CustomerCreatedEvent() {
	}

	public CustomerCreatedEvent(CreateCustomerRequest request) {
		super(request.identity());
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	
}
