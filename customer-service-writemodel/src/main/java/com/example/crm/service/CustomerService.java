package com.example.crm.service;

import java.util.Objects;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.crm.dto.request.CreateCustomerRequest;
import com.example.crm.dto.request.UpdateCustomerRequest;
import com.example.crm.dto.response.CreateCustomerResponse;
import com.example.crm.dto.response.DeleteCustomerResponse;
import com.example.crm.dto.response.UpdateCustomerResponse;
import com.example.crm.es.CustomerCreatedEvent;
import com.example.crm.es.CustomerAddressesChangedEvent;
import com.example.crm.es.CustomerPhonesChangedEvent;
import com.example.crm.es.CustomerRemovedEvent;
import com.example.crm.repository.CustomerEventSourceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CustomerService {
	private final CustomerEventSourceRepository eventSourceRepository;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	public CustomerService(CustomerEventSourceRepository eventSourceRepository,
			KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
		this.eventSourceRepository = eventSourceRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	public CreateCustomerResponse acquireCustomer(CreateCustomerRequest request) {
		try {
			var event = new CustomerCreatedEvent();
			var insertedEvent = eventSourceRepository.insert(event);
			var eventAsJson = objectMapper.writeValueAsString(insertedEvent);
			kafkaTemplate.send("crm-es-events", request.identity(), eventAsJson);
			return new CreateCustomerResponse("success");
		} catch (JsonProcessingException e) {
			System.out.println("Error while serializing to json: %s".formatted(e.getMessage()));
			return new CreateCustomerResponse("fails");
		}
	}

	public UpdateCustomerResponse updateCustomer(UpdateCustomerRequest request) {
		String identity = request.identity();
		if (Objects.nonNull(request.addresses())) {
			try {
				var event = new CustomerAddressesChangedEvent(identity, request.addresses());
				var insertedEvent = eventSourceRepository.insert(event);
				var eventAsJson = objectMapper.writeValueAsString(insertedEvent);
				kafkaTemplate.send("crm-es-events", request.identity(), eventAsJson);
			} catch (JsonProcessingException e) {
				System.out.println("Error while serializing to json: %s".formatted(e.getMessage()));
				return new UpdateCustomerResponse("fails");
			}
		}
		if (Objects.nonNull(request.phones())) {
			try {
				var event = new CustomerPhonesChangedEvent(identity, request.phones());
				var insertedEvent = eventSourceRepository.insert(event);
				var eventAsJson = objectMapper.writeValueAsString(insertedEvent);
				kafkaTemplate.send("crm-es-events", request.identity(), eventAsJson);
			} catch (JsonProcessingException e) {
				System.out.println("Error while serializing to json: %s".formatted(e.getMessage()));
				return new UpdateCustomerResponse("fails");
			}
		}
		return new UpdateCustomerResponse("success");
	}

	public DeleteCustomerResponse releaseCustomer(String identity) {
		try {
			var event = new CustomerRemovedEvent(identity);
			var insertedEvent = eventSourceRepository.insert(event);
			var eventAsJson = objectMapper.writeValueAsString(insertedEvent);
			kafkaTemplate.send("crm-es-events", identity, eventAsJson);
			return new DeleteCustomerResponse("success");
		} catch (JsonProcessingException e) {
			System.out.println("Error while serializing to json: %s".formatted(e.getMessage()));
			return new DeleteCustomerResponse("fails");
		}
	}

}
