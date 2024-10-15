package com.example.ordering.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.ordering.dto.request.SubmitOrderRequest;
import com.example.ordering.dto.response.SubmitOrderResponse;
import com.example.ordering.entity.Order;
import com.example.ordering.event.OrderCreatedEvent;
import com.example.ordering.event.OrderEvent;

@Service
public class OrderHandlerService {
	private final OrderService orderService;
	private final EventPublisherService eventPublisher;
	
	public OrderHandlerService(OrderService orderService, EventPublisherService eventPublisher) {
		this.orderService = orderService;
		this.eventPublisher = eventPublisher;
	}

	@Transactional(isolation = Isolation.DEFAULT , propagation = Propagation.REQUIRES_NEW)
	public SubmitOrderResponse handleRequest(SubmitOrderRequest request) {
		var order = orderService.saveOrder(request.getOrder());
		var event = getOrderCreatedEvent(order);
		eventPublisher.publishEvent(event);
		return new SubmitOrderResponse(order.getOrderId(),"success");
	}

	private OrderEvent getOrderCreatedEvent(Order order) {
		var customerId = order.getCustomerId();
		var orderId = order.getOrderId();
		return new OrderCreatedEvent(orderId,customerId);
	}
}
