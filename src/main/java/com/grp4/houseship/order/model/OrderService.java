package com.grp4.houseship.order.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class OrderService {

	@Autowired
	private OrderInfoRepository orderinfoRepo;
	
	@Autowired
	private OrderItemRepository orderitemRepo;
	
	public Long getDataCount() {
		return orderinfoRepo.count();
	}
	
	public OrderInfo findByOrderId(int orderid) {
		Optional<OrderInfo> option = orderinfoRepo.findById(orderid);
		if (option.isPresent()) {
			return option.get();			
		}return null;
	}
	
	public List<OrderInfo> findAll(){
		return orderinfoRepo.findAll();
	}
	
	public List<OrderInfo> findAllById(List<Integer> listid){
		return orderinfoRepo.findAllById(listid);
	}
	
	public List<OrderInfo> findByChkindate(String date1, String date2){
		List<OrderItem> items = orderitemRepo.findByChkinDateBetween(date1, date2);
		List<Integer> orderidList = new ArrayList<Integer>();
		for (OrderItem item : items) {
			orderidList.add(item.getOrderid());
		}
		return orderinfoRepo.findAllById(orderidList);
	}
	
	public OrderInfo insertOrder(OrderInfo orderinfo) {
		return orderinfoRepo.save(orderinfo);
	}
	
	public OrderItem inserItem(OrderItem orderitem) {
		return orderitemRepo.save(orderitem);
	}
	
	public OrderItem findByItemid(int itemid) {
		Optional<OrderItem> option = orderitemRepo.findById(itemid);
		if (option.isPresent()) {
			return option.get();			
		}return null;
	}
	
	public List<OrderItem> findItemByOrder(OrderInfo orderInfo){
		return orderitemRepo.findAllByOrderinfo(orderInfo);
	}
	
	public OrderItem updateItem(OrderItem item) {
		System.out.println("1===================");
		OrderItem updateitem = findByItemid(item.getItemid());
		updateitem.setChkinDate(item.getChkinDate());
		updateitem.setChkoutDate(item.getChkoutDate());
		updateitem.setVid(item.getVid());
		System.out.println("2===================");
		return orderitemRepo.save(updateitem);
	}
	
	public void deleteItem(int id) {
		orderitemRepo.deleteById(id);
	}
	
	public void deleteAllItems(List<OrderItem> items) {
		orderitemRepo.deleteAll(items);
	}
	
	public void deleteOrder(int id) {
		orderinfoRepo.deleteById(id);
	}
	
	public void deleteSelection(List<Integer> listid) {
		orderinfoRepo.deleteAllById(listid);
	}
}
