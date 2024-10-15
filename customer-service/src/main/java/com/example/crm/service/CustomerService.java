package com.example.crm.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.crm.document.CustomerDocument;
import com.example.crm.event.CustomerCreatedEvent;
import com.example.crm.event.CustomerRemovedEvent;
import com.example.crm.repository.CustomerDocumentRepository;

@Service
public class CustomerService {
	private final CustomerDocumentRepository customerDocumentRepository;
	private final CustomerEventPublisherService publisherService;
	
	public CustomerService(CustomerDocumentRepository customerDocumentRepository, CustomerEventPublisherService publisherService) {
		this.customerDocumentRepository = customerDocumentRepository;
		this.publisherService = publisherService;
	}

	public CustomerDocument findById(String identity) {
		return customerDocumentRepository.findById(identity).orElseThrow(() -> new IllegalArgumentException("Cannot find the customer (%s)".formatted(identity)));
	}

	public List<CustomerDocument> findAllByPage(int pageNo, int pageSize) {
		return customerDocumentRepository.findAll(PageRequest.of(pageNo, pageSize));
	}

	public CustomerDocument acquireCustomer(CustomerDocument customer) {
		var insertedDocument = customerDocumentRepository.insert(customer);
		publisherService.publishEvent(new CustomerCreatedEvent(insertedDocument));
		return insertedDocument;
	}

	public CustomerDocument updateCustomer(CustomerDocument customer) {
		CustomerDocument updatedCustomer = customerDocumentRepository.save(customer);
		return updatedCustomer;
	}

	public CustomerDocument releaseCustomer(String identity) {
		var customer = customerDocumentRepository.findById(identity).orElseThrow(() -> new IllegalArgumentException("Cannot find the customer (%s)".formatted(identity)));
		customerDocumentRepository.delete(customer);
		publisherService.publishEvent(new CustomerRemovedEvent(identity));
		return customer;
	}

}
