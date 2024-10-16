package com.example.ordering.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import com.example.ordering.dto.request.SubmitOrderRequest;
import com.example.ordering.dto.response.SubmitOrderResponse;
import com.example.ordering.service.OrderHandlerService;

@RestController
@RequestScope
@RequestMapping("/orders")
@CrossOrigin
@Validated
public class OrderRestController {
	private final OrderHandlerService orderHandlerService;
	
	public OrderRestController(OrderHandlerService orderHandlerService) {
		this.orderHandlerService = orderHandlerService;
	}

	@PostMapping
	public SubmitOrderResponse submitOrder(@RequestBody @Validated SubmitOrderRequest request) {
		return orderHandlerService.handleRequest(request);
	}
}
