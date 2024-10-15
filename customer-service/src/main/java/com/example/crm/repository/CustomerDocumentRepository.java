package com.example.crm.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.crm.document.CustomerDocument;

public interface CustomerDocumentRepository extends MongoRepository<CustomerDocument, String> {
	@Query("{}")
	List<CustomerDocument> findAll(PageRequest page);
}
