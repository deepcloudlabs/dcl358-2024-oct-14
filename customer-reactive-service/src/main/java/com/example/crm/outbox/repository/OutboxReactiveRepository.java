package com.example.crm.outbox.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.crm.outbox.entity.OutboxEvent;

import reactor.core.publisher.Flux;

public interface OutboxReactiveRepository extends ReactiveMongoRepository<OutboxEvent, String> {
	@Query("{}")
	Flux<OutboxEvent> findAll(PageRequest page);
}
