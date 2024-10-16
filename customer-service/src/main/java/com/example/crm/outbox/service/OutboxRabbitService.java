package com.example.crm.outbox.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.crm.event.CustomerEvent;
import com.example.crm.outbox.entity.OutboxEvent;
import com.example.crm.outbox.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@ConditionalOnProperty(name = "messagingSystem", havingValue = "rabbit")
public class OutboxRabbitService {
	private final OutboxRepository outboxRepository;
	private final RabbitTemplate rabbitTemplate;
	private final ObjectMapper objectMapper;
	private final String exchangeName;
	
	public OutboxRabbitService(OutboxRepository outboxRepository, 
			RabbitTemplate rabbitTemplate, 
			ObjectMapper objectMapper, @Value("${exchangeName}") String exchangeName) {
		this.outboxRepository = outboxRepository;
		this.rabbitTemplate = rabbitTemplate;
		this.objectMapper = objectMapper;
		this.exchangeName = exchangeName;
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
		                	try {
		                		rabbitTemplate.convertAndSend(exchangeName,null,event.getPayload());
		                		outboxRepository.delete(event);		                			
		                	}catch(AmqpException e) {
		                		event.incrementTries();
		                		outboxRepository.save(event);
		                		// consider dead-letter implementation if number of tries > 10	                		
		                	}
		                }); 
	}
}
