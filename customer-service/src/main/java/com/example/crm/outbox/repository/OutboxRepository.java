package com.example.crm.outbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.crm.outbox.entity.OutboxEvent;

public interface OutboxRepository extends MongoRepository<OutboxEvent, String>{

}
