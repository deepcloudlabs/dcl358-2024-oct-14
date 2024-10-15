package com.example.ordering.service;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.ordering.entity.Order;
import com.example.ordering.entity.OrderStatus;
import com.example.ordering.repository.OrderRepository;

@Repository
public class OrderService {
	private final OrderRepository orderRepository;

	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Order saveOrder(Order order) {
		order.setStatus(OrderStatus.CREATED);
		return orderRepository.save(order);
	}

}
