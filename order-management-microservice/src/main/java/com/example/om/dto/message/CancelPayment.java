package com.example.om.dto.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CancelPayment {
	private String customerId;
	private long orderId;
	private double total;
}
