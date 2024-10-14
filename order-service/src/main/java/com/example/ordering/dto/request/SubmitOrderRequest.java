package com.example.ordering.dto.request;

import com.example.ordering.entity.Order;

public class SubmitOrderRequest {
	private Order order;

	public SubmitOrderRequest() {
	}

	public SubmitOrderRequest(Order order) {
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "SubmitOrderRequest [order=" + order + "]";
	}

}
