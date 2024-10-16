package com.example.crm.outbox.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crm.event.CustomerEvent;
import com.example.crm.outbox.entity.OutboxEvent;
import com.example.crm.outbox.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(name = "messagingSystem", havingValue = "kafka")
public class OutboxKafkaService {
	private final OutboxRepository outboxRepository;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final String eventTopicName;
	
	public OutboxKafkaService(OutboxRepository outboxRepository, 
			KafkaTemplate<String, String> kafkaTemplate, 
			ObjectMapper objectMapper, @Value("${eventTopicName}") String eventTopicName) {
		this.outboxRepository = outboxRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
		this.eventTopicName = eventTopicName;
	}

	@EventListener
	public void handleOrderEvent(CustomerEvent event) {
		try {
			String eventAsJson = objectMapper.writeValueAsString(event);
			var outboxEvent = new OutboxEvent(event.getEventId(), eventAsJson);
			System.out.println(outboxEvent);
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
		                		kafkaTemplate.send(eventTopicName,event.getEventId(),event.getPayload())
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
