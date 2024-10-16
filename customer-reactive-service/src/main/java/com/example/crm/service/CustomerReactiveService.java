package com.example.crm.service;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.crm.document.CustomerDocument;
import com.example.crm.event.CustomerAddressesChangedEvent;
import com.example.crm.event.CustomerCreatedEvent;
import com.example.crm.event.CustomerPhonesChangedEvent;
import com.example.crm.event.CustomerRemovedEvent;
import com.example.crm.repository.CustomerDocumentReactiveRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerReactiveService {
	private final CustomerDocumentReactiveRepository customerDocumentRepository;
	private final CustomerEventPublisherService publisherService;

	public CustomerReactiveService(CustomerDocumentReactiveRepository customerDocumentRepository,
			CustomerEventPublisherService publisherService) {
		this.customerDocumentRepository = customerDocumentRepository;
		this.publisherService = publisherService;
	}

	public Mono<CustomerDocument> findById(String identity) {
		return customerDocumentRepository.findById(identity);
	}

	public Flux<CustomerDocument> findAllByPage(int pageNo, int pageSize) {
		return customerDocumentRepository.findAll(PageRequest.of(pageNo, pageSize));
	}

	public Mono<CustomerDocument> acquireCustomer(CustomerDocument customer) {
		var insertedDocument = customerDocumentRepository.insert(customer);
		publisherService.publishEvent(new CustomerCreatedEvent(customer));
		return insertedDocument;
	}

	public Mono<CustomerDocument> updateCustomer(CustomerDocument customer) {
		String identity = customer.getIdentity();
		customerDocumentRepository.findById(identity)
				.doOnSuccess( existingCustomer -> {
					if(!existingCustomer.getAddresses().containsAll(customer.getAddresses()))
						publisherService.publishEvent(new CustomerAddressesChangedEvent(identity, customer.getAddresses()));
					if(!existingCustomer.getPhones().containsAll(customer.getPhones()))
						publisherService.publishEvent(new CustomerPhonesChangedEvent(identity, customer.getPhones()));					
				}).subscribe();
		return customerDocumentRepository.save(customer);
	}

	public Mono<CustomerDocument> releaseCustomer(String identity) {
		return customerDocumentRepository.findById(identity)
				  .doOnSuccess( retrievedCustomer -> {
			          customerDocumentRepository.delete(retrievedCustomer).subscribe();
			          publisherService.publishEvent(new CustomerRemovedEvent(identity));
		           });
	}

	public Mono<CustomerDocument> patchCustomer(String identity, Map<String, Object> patchRequest) {
		return customerDocumentRepository.findById(identity).doOnSuccess(
				customer -> patchRequest.entrySet().forEach(entry -> {
								var field = entry.getKey();
								var value = entry.getValue();
								switch (field) {
									case "addresses" -> {
										publisherService.publishEvent(new CustomerAddressesChangedEvent(identity, value));
									}
									case "phones" -> {
										publisherService.publishEvent(new CustomerPhonesChangedEvent(identity, value));
									}
									default -> {
										throw new IllegalArgumentException("Unexpected value: " + field);
									}
								}
				})
		);
	}

}
