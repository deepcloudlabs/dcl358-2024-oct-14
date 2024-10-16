package com.example.crm.dto;

import java.util.Objects;

public class Address {
	private AddressType type;
	private String country;
	private String city;
	private String line;
	private String zipCode;

	public Address() {
	}

	public AddressType getType() {
		return type;
	}

	public void setType(AddressType type) {
		this.type = type;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, country, line, type, zipCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city) && Objects.equals(country, other.country)
				&& Objects.equals(line, other.line) && type == other.type && Objects.equals(zipCode, other.zipCode);
	}

	@Override
	public String toString() {
		return "Address [type=" + type + ", country=" + country + ", city=" + city + ", line=" + line + ", zipCode="
				+ zipCode + "]";
	}

}
