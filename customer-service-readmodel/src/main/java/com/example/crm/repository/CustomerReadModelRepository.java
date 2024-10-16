package com.example.crm.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.crm.document.CustomerReadModel;

public interface CustomerReadModelRepository extends MongoRepository<CustomerReadModel, String> {

}
