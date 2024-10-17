package com.example.crm.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Repository;

import com.example.crm.document.CustomerReadModel;
import com.example.crm.es.CustomerAddressesChangedEvent;
import com.example.crm.es.CustomerCreatedEvent;
import com.example.crm.es.CustomerEvent;
import com.example.crm.es.CustomerPhonesChangedEvent;
import com.example.crm.es.CustomerRemovedEvent;
import com.example.crm.repository.CustomerReadModelRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
public class EventSourcingReadModelSnapshotService {
	private final CustomerReadModelRepository customerDocumentRepository;
	private final ObjectMapper objectMapper;

	public EventSourcingReadModelSnapshotService(CustomerReadModelRepository customerDocumentRepository,
			ObjectMapper objectMapper) {
		this.customerDocumentRepository = customerDocumentRepository;
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics="crm-es-events", groupId = "customer-service-readmodel")
	public void listenEventSourcing(String eventAsJson) {
		System.out.println("new replication customer event has just arrived: %s".formatted(eventAsJson));
		try {
			CustomerEvent event = objectMapper.readValue(eventAsJson, CustomerEvent.class);
			switch (event) {
			case CustomerCreatedEvent cce -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cce));
				var customer = new CustomerReadModel();
				customer.setIdentity(cce.getIdentity());
				customer.setFullname(cce.getFullname());
				customer.setAddresses(cce.getAddresses());
				customer.setPhones(cce.getPhones());
				customerDocumentRepository.save(customer);
			}
			case CustomerRemovedEvent cre -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cre));
				customerDocumentRepository.deleteById(cre.getCustomerIdentity());
			}
			case CustomerAddressesChangedEvent cace -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cace));
				customerDocumentRepository.findById(cace.getCustomerIdentity())
				                          .ifPresent(cust -> {
				                        	 cust.setAddresses(cace.getAddresses());
				                        	 customerDocumentRepository.save(cust);
				                           });
			}
			case CustomerPhonesChangedEvent cpce -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cpce));
				customerDocumentRepository.findById(cpce.getCustomerIdentity())
							              .ifPresent(cust -> {
							              	 cust.setPhones(cpce.getPhones());
				                        	 customerDocumentRepository.save(cust);
						              });
			}
			default -> {
				throw new IllegalArgumentException("Unexpected value: " + event);
			}
			}
		} catch (JsonProcessingException e) {
			System.out.println("Exception while deserializing the json: %s".formatted(e.getMessage()));
		}
		
	}
}
