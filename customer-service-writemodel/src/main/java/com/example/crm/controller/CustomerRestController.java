package com.example.crm.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.example.crm.dto.request.CreateCustomerRequest;
import com.example.crm.dto.request.UpdateCustomerRequest;
import com.example.crm.dto.response.CreateCustomerResponse;
import com.example.crm.dto.response.DeleteCustomerResponse;
import com.example.crm.dto.response.UpdateCustomerResponse;
import com.example.crm.service.CustomerService;

@RestController
@RequestScope
@RequestMapping("/customers")
@CrossOrigin
@Validated
public class CustomerRestController {
	private final CustomerService customerService;

	public CustomerRestController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping
	public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest request) {
		return customerService.acquireCustomer(request);
	}

	@PutMapping("{identity}")
	public UpdateCustomerResponse updateCustomer(@PathVariable String identity,
			@RequestBody UpdateCustomerRequest request) {
		return customerService.updateCustomer(request);
	}

	@DeleteMapping("{identity}")
	public DeleteCustomerResponse removeCustomerById(@PathVariable String identity) {
		return customerService.releaseCustomer(identity);
	}
}
