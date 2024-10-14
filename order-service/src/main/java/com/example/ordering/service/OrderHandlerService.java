package com.example.ordering.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ordering.dto.request.SubmitOrderRequest;
import com.example.ordering.dto.response.SubmitOrderResponse;
import com.example.ordering.entity.OrderStatus;
import com.example.ordering.event.OrderCreatedEvent;
import com.example.ordering.repository.OrderRepository;

@Service
public class OrderHandlerService {
	private final OrderRepository orderRepository;
	private final ApplicationEventPublisher eventPublisher;
	
	public OrderHandlerService(OrderRepository orderRepository, ApplicationEventPublisher eventPublisher) {
		this.orderRepository = orderRepository;
		this.eventPublisher = eventPublisher;
	}

	@Transactional
	public SubmitOrderResponse handleRequest(SubmitOrderRequest request) {
		var order = request.getOrder();
		order.setStatus(OrderStatus.CREATED);
		var customerId = order.getCustomerId();
		var savedOrder = orderRepository.saveAndFlush(order);
		var orderId = savedOrder.getOrderId();
		var event = new OrderCreatedEvent(orderId,customerId);
		eventPublisher.publishEvent(event);
		return new SubmitOrderResponse(orderId,"success");
	}

}
