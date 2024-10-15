package com.example.ordering.outbox.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.ordering.event.OrderEvent;
import com.example.ordering.outbox.entity.OutboxEvent;
import com.example.ordering.outbox.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OutboxService {
	private final OutboxRepository outboxRepository;
	private final KafkaTemplate<Long, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final String eventTopicName;
	
	public OutboxService(OutboxRepository outboxRepository, 
			KafkaTemplate<Long, String> kafkaTemplate, 
			ObjectMapper objectMapper, @Value("${eventTopicName}") String eventTopicName) {
		this.outboxRepository = outboxRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
		this.eventTopicName = eventTopicName;
	}

	@EventListener
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void handleOrderEvent(OrderEvent event) {
		try {
			var eventAsJson = objectMapper.writeValueAsString(event);
			var outboxEvent = new OutboxEvent(event.getOrderId(), eventAsJson);
			outboxRepository.save(outboxEvent);
		} catch (JsonProcessingException e) {
			System.out.println("Error while serializing to JSON: %s".formatted(e.getMessage()));
		}
	}
	
	@Scheduled(fixedRate = 1_000)
	@Transactional
	public void sendEvents() {
		outboxRepository.findAll(PageRequest.of(0, 1))
		                .forEach( event -> {
		                		kafkaTemplate.send(eventTopicName, event.getPayload())
		                		.thenAcceptAsync(result -> {
		                			outboxRepository.delete(event);		                			
		                		})
		                		.exceptionallyAsync(e -> {
		                			event.incrementTries();
		                			// consider dead-letter implementation if number of tries > 10
		                			outboxRepository.save(event);
		                			return null;
		                		});
		                }); 
	}
}
