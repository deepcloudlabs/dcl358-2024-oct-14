package com.example.crm.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.crm.event.CustomerEvent;

@Service
public class CustomerEventPublisherService {
	private final ApplicationEventPublisher eventPublisher;

	public CustomerEventPublisherService(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void publishEvent(CustomerEvent event) {
		eventPublisher.publishEvent(event);
	}

}
