package com.example.crm.controller;

import java.util.Map;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.crm.document.CustomerDocument;
import com.example.crm.service.CustomerReactiveService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@CrossOrigin
@Validated
public class CustomerRestController {
	private final CustomerReactiveService customerService;
	
	
	public CustomerRestController(CustomerReactiveService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("{identity}")
	public Mono<CustomerDocument> getCustomerById(@PathVariable String identity) {
		return customerService.findById(identity);
	}

	@GetMapping(params= {"pageNo","pageSize"})
	public Flux<CustomerDocument> getCustomers(@RequestParam int pageNo,@RequestParam int pageSize){
		return customerService.findAllByPage(pageNo,pageSize);
		
	}
	
	@PostMapping
	public Mono<CustomerDocument> createCustomer(@RequestBody CustomerDocument customer) {
		return customerService.acquireCustomer(customer);
	}
	
	@PutMapping("{identity}")
	public Mono<CustomerDocument> updateCustomer(@PathVariable String identity,@RequestBody CustomerDocument customer) {
		return customerService.updateCustomer(customer);
	}

	@PatchMapping("{identity}")
	public Mono<CustomerDocument> patchCustomer(@PathVariable String identity,@RequestBody Map<String,Object> patchRequest) {
		return customerService.patchCustomer(identity,patchRequest);
	}
	
	
	@DeleteMapping("{identity}")
	public Mono<CustomerDocument> removeCustomerById(@PathVariable String identity) {
		return customerService.releaseCustomer(identity);
	}
}
