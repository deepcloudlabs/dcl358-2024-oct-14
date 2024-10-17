package com.example.crm.es;

import java.util.List;

import com.example.crm.document.Address;
import com.example.crm.document.Phone;

public class CustomerCreatedEvent extends CustomerEvent {
	private String identity;
	private String fullname;
	private List<Address> addresses;
	private List<Phone> phones;

	public CustomerCreatedEvent() {
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
