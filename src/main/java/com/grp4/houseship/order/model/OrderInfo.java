package com.grp4.houseship.order.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grp4.houseship.member.model.Member;



@Entity @Table(name = "orderinfo")
@Component
public class OrderInfo {

	
	@Id @Column(name = "ORDERID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderid;
	
	@Transient
	private String account;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ACCOUNT" )
	private Member member;
	
	@Column(name = "ORDERDATE")
	private String orderDate;
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//	private String orderDate;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "orderinfo")
	private List<OrderItem> items;	
	
	@PrePersist
	public void prePersist() {
		LocalDateTime time = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		orderDate = time.format(formatter);
	}
	
	@Transient
	private String date;
	@Transient
	private String time;
	@Transient
	private int itemCount;

	public OrderInfo() {
	}
	
	public OrderInfo(Member member) {
		super();
		this.member = member;
	}

	public OrderInfo(String account) {
		this.account = account;
	}

	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public String getAccount() {
		return member.getAccount() ;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getDate() {
		return this.orderDate.substring(0, 10);
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return this.orderDate.substring(11, 19);
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getItemCount() {
//		return itemCount;
		return items.size();
	}

	public void setItemCount(int itemCount) {
		this.itemCount = itemCount;
	}
	
	


}
