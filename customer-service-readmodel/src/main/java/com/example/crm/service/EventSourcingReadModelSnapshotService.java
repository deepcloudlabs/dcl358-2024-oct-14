package com.example.crm.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Repository;

@Repository
public class EventSourcingReadModelSnapshotService {

	@KafkaListener(topics="crm-es-events", groupId = "customer-service-readmodel")
	public void listenEventSourcing(String event) {
		
	}
}
