package com.example.crm.outbox.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.crm.event.CustomerEvent;
import com.example.crm.outbox.entity.OutboxEvent;
import com.example.crm.outbox.repository.OutboxReactiveRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OutboxKafkaService {
	private final OutboxReactiveRepository outboxRepository;
	private final ReactiveKafkaProducerTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final String eventTopicName;
	
	public OutboxKafkaService(OutboxReactiveRepository outboxRepository, 
			ReactiveKafkaProducerTemplate<String, String> kafkaTemplate, 
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
			outboxRepository.save(outboxEvent).subscribe();
		} catch (JsonProcessingException e) {
			System.out.println("Error while serializing to JSON: %s".formatted(e.getMessage()));
		}
	}
	
	@Scheduled(fixedRate = 1_000)
	public void sendEvents() {
		outboxRepository.findAll(PageRequest.of(0, 1))
		                .subscribe( event -> {
		                		kafkaTemplate.send(eventTopicName,event.getEventId(),event.getPayload())
		                		.doOnSuccess(result -> {
		                			outboxRepository.delete(event).subscribe();		                			
		                		})
		                		.doOnError(e -> {
		                			event.incrementTries();
		                			// consider dead-letter implementation if number of tries > 10
		                			outboxRepository.save(event).subscribe();
		                		}).subscribe();
		                }); 
	}
}
