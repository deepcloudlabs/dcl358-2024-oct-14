package com.example.ordering.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.ordering.event.OrderEvent;

@Service
public class EventPublisherService {
	private final ApplicationEventPublisher eventPublisher;

	public EventPublisherService(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void publishEvent(OrderEvent event) {
		eventPublisher.publishEvent(event);
	}
}
