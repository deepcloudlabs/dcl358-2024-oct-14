package com.example.crm.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.crm.document.CustomerReadModel;
import com.example.crm.dto.CustomerDocumentDto;
import com.example.crm.repository.CustomerReadModelRepository;

@Service
public class CustomerQueryService {
	private final CustomerReadModelRepository customerReadModelRepository;
	private final ModelMapper modelMapper;
	
	public CustomerQueryService(CustomerReadModelRepository customerDocumentRepository, ModelMapper modelMapper) {
		this.customerReadModelRepository = customerDocumentRepository;
		this.modelMapper = modelMapper;
	}

	public CustomerDocumentDto findById(String identity) {
		CustomerReadModel customerReadModel = customerReadModelRepository.findById(identity)
				.orElseThrow(() -> new IllegalArgumentException("Cannot find the customer (%s)".formatted(identity)));
		return modelMapper.map(customerReadModel,CustomerDocumentDto.class);
	}

	public List<CustomerDocumentDto> findAllByPage(int pageNo, int pageSize) {
		return customerReadModelRepository.findAll(PageRequest.of(pageNo, pageSize))
				                          .stream()
				                          .map(document -> modelMapper.map(document,CustomerDocumentDto.class))
				                          .toList();
	}

}
