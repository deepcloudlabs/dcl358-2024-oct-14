package com.example.crm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.crm.es.CustomerEvent;

public interface CustomerEventSourceRepository extends MongoRepository<CustomerEvent, String>{

}
