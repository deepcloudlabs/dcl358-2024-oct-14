package com.example.crm.controller;

import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.example.crm.dto.CustomerDocumentDto;
import com.example.crm.service.CustomerQueryService;

@RestController
@RequestScope
@RequestMapping("/customers")
@CrossOrigin
@Validated
public class CustomerQueryRestController {
	private final CustomerQueryService customerService;
	
	
	public CustomerQueryRestController(CustomerQueryService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("{identity}")
	public CustomerDocumentDto getCustomerById(@PathVariable String identity) {
		return customerService.findById(identity);
	}

	@GetMapping(params= {"pageNo","pageSize"})
	public List<CustomerDocumentDto> getCustomers(@RequestParam int pageNo,@RequestParam int pageSize){
		return customerService.findAllByPage(pageNo,pageSize);
		
	}
	
}
