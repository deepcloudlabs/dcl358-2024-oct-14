package com.example.crm.es;

import java.util.List;

import com.example.crm.document.Phone;

public class CustomerPhonesChangedEvent extends CustomerEvent {
	private List<Phone> phones;

	public CustomerPhonesChangedEvent() {
	}

	public CustomerPhonesChangedEvent(String customerIdentity, List<Phone> phones) {
		super(customerIdentity);
		this.phones = phones;
	}

	public List<Phone> getPhones() {
		return phones;
	}

	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}

	@Override
	public String toString() {
		return "CustomerPhonesChangedEvent [phones=" + phones + "]";
	}

}
