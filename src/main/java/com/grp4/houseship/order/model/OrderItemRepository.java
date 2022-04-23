package com.grp4.houseship.order.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

	public List<OrderItem> findByChkinDateBetween(String date1, String date2);
	public List<OrderItem> findAllByOrderinfo(OrderInfo orderInfo);
}
