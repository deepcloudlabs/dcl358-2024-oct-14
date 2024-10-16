package com.example.crm.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

	@Bean
	ModelMapper createModelMapper() {
		var modelMapper = new ModelMapper();
		return modelMapper;
	}
}
