package com.grp4.houseship.order.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.stereotype.Component;

import com.grp4.houseship.house.model.HouseInfo;

@Entity
@Table(name = "orderitem")
@Component
public class OrderItem implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id @Column(name = "itemid")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int itemid;
	
	@Column(name = "chkindate")
	private String chkinDate;
	
	@Column(name = "chkoutdate")
	private String chkoutDate;
	
	@Column(name = "vid")
	private Integer vid;
	
	@Transient
	private int orderid;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "orderid")
	private OrderInfo orderinfo;
	
	@Transient
	private int houseno;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "houseno")
	private HouseInfo houseinfo;
	
	public OrderItem() {
	}
	
	public OrderItem(OrderInfo orderinfo, HouseInfo houseinfo) {
		this.orderinfo = orderinfo;
		this.houseinfo = houseinfo;
	}
	
	public OrderItem(OrderInfo orderinfo) {
		this.orderinfo = orderinfo;
	}


	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public String getChkinDate() {
		return chkinDate;
	}

	public void setChkinDate(String chkinDate) {
		this.chkinDate = chkinDate;
	}

	public String getChkoutDate() {
		return chkoutDate;
	}

	public void setChkoutDate(String chkoutDate) {
		this.chkoutDate = chkoutDate;
	}

	public int getOrderid() {
		return orderinfo.getOrderid();
	}
	

	public void setVid(Integer vid) {
		this.vid = vid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public OrderInfo getOrderinfo() {
		return orderinfo;
	}

	public void setOrderinfo(OrderInfo orderinfo) {
		this.orderinfo = orderinfo;
	}

	public HouseInfo getHouseinfo() {
		return houseinfo;
	}

	public void setHouseinfo(HouseInfo houseinfo) {
		this.houseinfo = houseinfo;
	}

	public int getHouseno() {
		return houseinfo.getHouseNo();
//		return houseno;
	}
	
	public int getInputHouseno() {
		return this.houseno;
	}

	public void setHouseno(int houseno) {
		this.houseno = houseno;
	}

	public int getVid() {
		if (vid == null) {
			return 0;
		}
		return vid;
	}

	public void setVid(int vid) {
		this.vid = vid;
	}
	
	

}
