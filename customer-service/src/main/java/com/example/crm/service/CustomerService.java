package com.example.crm.service;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.crm.document.CustomerDocument;
import com.example.crm.repository.CustomerDocumentRepository;

@Service
public class CustomerService {
	private final CustomerDocumentRepository customerDocumentRepository;
	
	public CustomerService(CustomerDocumentRepository customerDocumentRepository) {
		this.customerDocumentRepository = customerDocumentRepository;
	}

	public CustomerDocument findById(String identity) {
		return customerDocumentRepository.findById(identity).orElseThrow(() -> new IllegalArgumentException("Cannot find the customer (%s)".formatted(identity)));
	}

	public List<CustomerDocument> findAllByPage(int pageNo, int pageSize) {
		return customerDocumentRepository.findAll(PageRequest.of(pageNo, pageSize));
	}

	public CustomerDocument acquireCustomer(CustomerDocument customer) {
		return customerDocumentRepository.insert(customer);
	}

	public CustomerDocument updateCustomer(CustomerDocument customer) {
		return customerDocumentRepository.save(customer);
	}

	public CustomerDocument releaseCustomer(String identity) {
		var customer = customerDocumentRepository.findById(identity).orElseThrow(() -> new IllegalArgumentException("Cannot find the customer (%s)".formatted(identity)));
		customerDocumentRepository.delete(customer);
		return customer;
	}

}
