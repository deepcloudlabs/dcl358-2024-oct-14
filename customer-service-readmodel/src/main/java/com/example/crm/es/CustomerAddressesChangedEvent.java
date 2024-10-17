package com.example.crm.es;

import java.util.List;

import com.example.crm.document.Address;

public class CustomerAddressesChangedEvent extends CustomerEvent {
	private List<Address> addresses;

	public CustomerAddressesChangedEvent() {
	}

	public CustomerAddressesChangedEvent(String customerIdentity, List<Address> addresses) {
		super(customerIdentity);
		this.addresses = addresses;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		return "CustomerAddressesChangedEvent [addresses=" + addresses + "]";
	}

}
