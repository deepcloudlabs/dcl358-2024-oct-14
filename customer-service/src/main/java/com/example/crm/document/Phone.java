package com.example.crm.document;

public class Phone {
	private PhoneType type;
	private String countryCode;
	private String number;

	public Phone() {
	}

	public PhoneType getType() {
		return type;
	}

	public void setType(PhoneType type) {
		this.type = type;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Phone [type=" + type + ", countryCode=" + countryCode + ", number=" + number + "]";
	}

}
