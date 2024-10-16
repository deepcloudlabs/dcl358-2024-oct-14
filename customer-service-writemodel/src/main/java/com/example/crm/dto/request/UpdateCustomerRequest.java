package com.example.crm.dto.request;

import java.util.List;

public record UpdateCustomerRequest(
		String identity,
		String fullname,
		List<Address> addresses,
		List<Phone> phones
) {}
