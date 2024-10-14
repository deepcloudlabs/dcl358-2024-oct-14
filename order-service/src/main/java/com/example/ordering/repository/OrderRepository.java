package com.example.ordering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ordering.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
