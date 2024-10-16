package com.example.om.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name="orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long orderId;
    @OneToMany(cascade = {CascadeType.PERSIST},fetch = FetchType.EAGER)
    private List<OrderItem> items = new ArrayList<>();
    private double total;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private String customerId;
    
    public void addOrderItem(OrderItem item) {
    	items.add(item);
    	total += item.getSubTotal();
    }
}
