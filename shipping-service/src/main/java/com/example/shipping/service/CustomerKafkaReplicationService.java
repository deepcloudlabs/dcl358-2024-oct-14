package com.example.shipping.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.crm.event.CustomerAddressPhonesChangedEvent;
import com.example.crm.event.CustomerAddressesChangedEvent;
import com.example.crm.event.CustomerCreatedEvent;
import com.example.crm.event.CustomerEvent;
import com.example.crm.event.CustomerRemovedEvent;
import com.example.shipping.document.Address;
import com.example.shipping.document.AddressType;
import com.example.shipping.document.Phone;
import com.example.shipping.document.PhoneType;
import com.example.shipping.repository.CustomerDocumentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(name="messagingSystem", havingValue = "kafka")
public class CustomerKafkaReplicationService {
	private final ObjectMapper objectMapper;
	private final CustomerDocumentRepository customerDocumentRepository;

	public CustomerKafkaReplicationService(ObjectMapper objectMapper,
			CustomerDocumentRepository customerDocumentRepository) {
		this.objectMapper = objectMapper;
		this.customerDocumentRepository = customerDocumentRepository;
	}

	@KafkaListener(topics = { "${customerEventTopicName}" }, groupId = "shipping-service")
	public void listenCustomerEvents(String eventAsJson) {
		System.out.println("new replication customer event has just arrived: %s".formatted(eventAsJson));
		try {
			var event = objectMapper.readValue(eventAsJson, CustomerEvent.class);
			switch (event) {
			case CustomerCreatedEvent cce -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cce));
				customerDocumentRepository.save(cce.getCustomer());
			}
			case CustomerRemovedEvent cre -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cre));
				customerDocumentRepository.deleteById(cre.getIdentity());
			}
			case CustomerAddressesChangedEvent cace -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cace));
				customerDocumentRepository.findById(cace.getIdentity())
				                          .ifPresent(cust -> {
				                        	 cust.setAddresses(extractAddresses(cace.getEventData()));
				                        	 customerDocumentRepository.save(cust);
				                           });
			}
			case CustomerAddressPhonesChangedEvent cpce -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cpce));
				customerDocumentRepository.findById(cpce.getIdentity())
							              .ifPresent(cust -> {
							              	 cust.setPhones(extractPhones(cpce.getEventData()));
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

	private List<Address> extractAddresses(Object eventData) {
		return ((ArrayList<?>) eventData).stream()
				                         .map(LinkedHashMap.class::cast)
				                         .map( addressMap -> {
			var address = new Address();
			address.setType(AddressType.valueOf(addressMap.get("type").toString()));
			address.setCity(addressMap.get("city").toString());
			address.setCountry(addressMap.get("country").toString());
			address.setLine(addressMap.get("line").toString());
			address.setZipCode(addressMap.get("zipCode").toString());
			return address;
		}).toList();
	}
	
	private List<Phone> extractPhones(Object eventData) {
		return ((ArrayList<?>) eventData).stream()
				.map(LinkedHashMap.class::cast)
				.map( phoneMap -> {
					var phone = new Phone();
					phone.setType(PhoneType.valueOf(phoneMap.get("type").toString()));
					phone.setCountryCode(phoneMap.get("countryCode").toString());
					phone.setNumber(phoneMap.get("number").toString());
					return phone;
				}).toList();
	}
}
