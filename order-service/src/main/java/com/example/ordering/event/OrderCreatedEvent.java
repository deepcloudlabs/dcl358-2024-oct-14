package com.example.ordering.event;

public class OrderCreatedEvent extends OrderEvent {
	private long orderId;
	private String customerId;

	public OrderCreatedEvent() {
	}

	public OrderCreatedEvent(long orderId, String customerId) {
		this.orderId = orderId;
		this.customerId = customerId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Override
	public String toString() {
		return "OrderCreatedEvent [orderId=" + orderId + ", customerId=" + customerId + "]";
	}

}
