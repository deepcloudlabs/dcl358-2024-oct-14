package com.example.shipping.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.example.crm.event.CustomerAddressPhonesChangedEvent;
import com.example.crm.event.CustomerAddressesChangedEvent;
import com.example.crm.event.CustomerCreatedEvent;
import com.example.crm.event.CustomerEvent;
import com.example.crm.event.CustomerRemovedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerReplicationService {
	private final ObjectMapper objectMapper;

	public CustomerReplicationService(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = { "${customerEventTopicName}" }, groupId = "shipping-service")
	public void listenCustomerEvents(String eventAsJson) {
		System.out.println("new replication customer event has just arrived: %s".formatted(eventAsJson));
		try {
			var event = objectMapper.readValue(eventAsJson, CustomerEvent.class);
			switch (event) {
			case CustomerCreatedEvent cce -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cce));					
			}
			case CustomerRemovedEvent cre -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cre));					
			}
			case CustomerAddressesChangedEvent cace -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cace));					
			}
			case CustomerAddressPhonesChangedEvent cpce -> {
				System.out.println("new replication customer event has just arrived: %s".formatted(cpce));					
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
