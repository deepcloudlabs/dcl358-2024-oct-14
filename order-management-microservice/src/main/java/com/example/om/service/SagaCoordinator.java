package com.example.om.service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.om.dto.message.CancelInventoryMessage;
import com.example.om.dto.message.CancelPayment;
import com.example.om.dto.message.InventoryItem;
import com.example.om.dto.message.InventoryMessage;
import com.example.om.dto.message.InventoryResponseMessage;
import com.example.om.dto.message.InventoryStatus;
import com.example.om.dto.message.Payment;
import com.example.om.dto.message.PaymentResponseMessage;
import com.example.om.entity.Order;
import com.example.om.entity.OrderStatus;
import com.example.om.repository.OrderRepository;
import com.example.om.saga.Compansation;
import com.example.om.saga.OrderAction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

@Service
public class SagaCoordinator {
	private final Logger logger = LoggerFactory.getLogger(SagaCoordinator.class);

	private final OrderRepository orderRepository;
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;
	private final Map<OrderAction,Method> compansators = new ConcurrentHashMap<>();
	
	public SagaCoordinator(OrderRepository orderRepository, KafkaTemplate<String, String> kafkaTemplate,
			ObjectMapper objectMapper) {
		this.orderRepository = orderRepository;
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
	}

	@PostConstruct
	public void loadCompansators() {
		for (var method : SagaCoordinator.class.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Compansation.class)) {
				var compansation = method.getAnnotation(Compansation.class);
				compansators.put(compansation.action(), method);	
			}
		}
	}
	
	@Transactional
	public Order createOrder(Order order) throws Exception {
		order.setStatus(OrderStatus.CREATED);
		var savedOrder = orderRepository.save(order);
		var payment= Payment.builder()
				.customerId(order.getCustomerId())
				.orderId(order.getOrderId())
				.total(order.getTotal())
				.build();
		kafkaTemplate.send("order-payment", objectMapper.writeValueAsString(payment));
		return savedOrder;		
	}

	@Transactional
	@KafkaListener(topics="order-payment-response")
	public void listenPaymentResponseMessage(String paymentResponseMesssage) throws Exception { 
		var responseMessage= objectMapper.readValue(paymentResponseMesssage, PaymentResponseMessage.class);
		if (responseMessage.getStatus().equals("success")) {
			orderRepository.findById(responseMessage.getOrderId()).ifPresent(this::sendOrderToInventory);			
		}
	}

	private void sendOrderToInventory(Order order) {
		if (order.getStatus() == OrderStatus.CREATED) {
			try {
				order.setStatus(OrderStatus.PAYMENT);
				orderRepository.save(order);
				List<InventoryItem> items = order.getItems().stream().map(item -> InventoryItem.builder().sku(item.getSku()).quantity(item.getQuantity()).build()).toList();
				var inventoryMessage = InventoryMessage.builder()
						.orderId(order.getOrderId())
						.items(items)
						.build();
				kafkaTemplate.send("order-inventory", objectMapper.writeValueAsString(inventoryMessage));
			} catch (JsonProcessingException e) {
				logger.error("Error while converting object to json: %s".formatted(e.getMessage()));
			}		            		   
		}		
	}
	
	@Transactional
	@KafkaListener(topics="order-inventory-response")
	public void listenInventoryResponseMessage(String inventoryResponseMesssage) throws Exception { 
		var responseMessage= objectMapper.readValue(inventoryResponseMesssage, InventoryResponseMessage.class);
		orderRepository.findById(responseMessage.getOrderId())
		.ifPresent(order -> {
			if (order.getStatus() == OrderStatus.PAYMENT ) {
				if (responseMessage.getInventoryStatus() == InventoryStatus.IN_STOCK)
					order.setStatus(OrderStatus.SENT);
			    else if (responseMessage.getInventoryStatus() == InventoryStatus.NOT_IN_STOCK) {
			    	order.setStatus(OrderStatus.NOT_IN_STOCK);
			    	cancelInventory(order);
			    	cancelPayment(order);
			    }
				orderRepository.save(order);
			}
		});
	}
	

	@Compansation(action=OrderAction.VALIDATE_PAYMENT)
	public void cancelPayment(Order order) {
		var cancelPayment= CancelPayment.builder()
				.customerId(order.getCustomerId())
				.orderId(order.getOrderId())
				.total(order.getTotal())
				.build();
		try {
			kafkaTemplate.send("order-payment", objectMapper.writeValueAsString(cancelPayment));
		} catch (JsonProcessingException e) {
			logger.error("Error while converting object to json: %s".formatted(e.getMessage()));
		}	
	}

	@Compansation(action=OrderAction.DROP_FROM_INVENTORY)
	public void cancelInventory(Order order) {
		   orderRepository.findById(order.getOrderId())
		    .ifPresent( retrievedOrder -> {
		    	List<InventoryItem> items = retrievedOrder.getItems().stream().map(item -> InventoryItem.builder().sku(item.getSku()).quantity(item.getQuantity()).build()).toList();
		    	var cancelInventoryMessage = CancelInventoryMessage.builder()
		    			.orderId(order.getOrderId())
		    			.items(items)
		    			.build();
		    	try {
					kafkaTemplate.send("order-inventory", objectMapper.writeValueAsString(cancelInventoryMessage));
				} catch (JsonProcessingException e) {
					logger.error("Error while converting object to json: %s".formatted(e.getMessage()));
				}		    	
		    });
	}

	@Compansation(action=OrderAction.CREATE_ORDER)
	public void cancelOrder(Order order) {
		var orderId = order.getOrderId();
		Consumer<Order> changeOrderStatusToCanceled = anOrder -> anOrder.setStatus(OrderStatus.CANCELED);
		Consumer<Order> saveOrder = orderRepository::save;
		orderRepository.findById(orderId)
				       .ifPresent( changeOrderStatusToCanceled.andThen(saveOrder) );
	}
}
