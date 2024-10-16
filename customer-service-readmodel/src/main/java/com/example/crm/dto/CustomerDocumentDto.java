package com.example.crm.dto;

import java.util.List;

public class CustomerDocumentDto {
	private String identity;
	private String fullname;
	private List<Address> addresses;
	private List<Phone> phones;

	public CustomerDocumentDto() {
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

	@Override
	public String toString() {
		return "CustomerDocumentDto [identity=" + identity + ", fullname=" + fullname + ", addresses=" + addresses
				+ ", phones=" + phones + "]";
	}

}
