package com.example.shipping.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {

	
	@KafkaListener(topics = {"${eventTopicName}"},groupId = "shipping-service")
	public void handleOrderEvent(String event) {
		System.out.println("New order has arrived: %s".formatted(event));
	}
}
