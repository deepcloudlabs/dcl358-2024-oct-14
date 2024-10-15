package com.example.shipping.document;

import java.util.Objects;

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
	public int hashCode() {
		return Objects.hash(countryCode, number, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Phone other = (Phone) obj;
		return Objects.equals(countryCode, other.countryCode) && Objects.equals(number, other.number)
				&& type == other.type;
	}

	@Override
	public String toString() {
		return "Phone [type=" + type + ", countryCode=" + countryCode + ", number=" + number + "]";
	}

}
