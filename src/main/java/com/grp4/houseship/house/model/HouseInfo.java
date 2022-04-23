package com.grp4.houseship.house.model;

import javax.persistence.*;

import com.grp4.houseship.member.model.Member;

import java.io.Serializable;

@Entity
@Table(name = "houseinfo")
public class HouseInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "HOUSENO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int houseNo;
	
	@Transient
	private String account;
	
	@Column(name = "H_TITLE")
	private String h_title;
	
	@Column(name = "H_ADDRESS")
	private String h_address;
	
	@Column(name = "H_TYPE")
	private int h_type;
	
	@Column(name = "H_ABOUT")
	private String h_about;
	
	@Column(name = "H_PRICE")
	private double h_price;
	
	@Transient
	private int offersNo;
	
	@Transient
	private int rulesNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT", referencedColumnName = "ACCOUNT")
	private Member member;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "OFFERSNO", referencedColumnName = "OFFERSNO")
	private HouseOffers houseOffers;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "RULESNO", referencedColumnName = "RULESNO")
	private HouseRules houseRules;

//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy ="houseInfo")
//	private Set<HousePhotos> housePhotos;
	
	public HouseInfo() {
	}

	public HouseInfo(String h_title, String h_address, int h_type, String h_about, double h_price) {
		this.h_title = h_title;
		this.h_address = h_address;
		this.h_type = h_type;
		this.h_about = h_about;
		this.h_price = h_price;
	}

	public int getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(int houseNo) {
		this.houseNo = houseNo;
	}

	public String getH_title() {
		return h_title;
	}

	public void setH_title(String h_title) {
		this.h_title = h_title;
	}

	public String getH_address() {
		return h_address;
	}

	public void setH_address(String h_address) {
		this.h_address = h_address;
	}

	public int getH_type() {
		return h_type;
	}

	public void setH_type(int h_type) {
		this.h_type = h_type;
	}

	public String getH_about() {
		return h_about;
	}

	public void setH_about(String h_about) {
		this.h_about = h_about;
	}

	public double getH_price() {
		return h_price;
	}

	public void setH_price(double h_price) {
		this.h_price = h_price;
	}

	public int getOffersNo() {
		return offersNo;
	}

	public void setOffersNo(int offersNo) {
		this.offersNo = offersNo;
	}

	public int getRulesNo() {
		return rulesNo;
	}

	public void setRulesNo(int rulesNo) {
		this.rulesNo = rulesNo;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public HouseOffers getHouseOffers() {
		return houseOffers;
	}

	public void setHouseOffers(HouseOffers houseOffers) {
		this.houseOffers = houseOffers;
	}

	public HouseRules getHouseRules() {
		return houseRules;
	}

	public void setHouseRules(HouseRules houseRules) {
		this.houseRules = houseRules;
	}

//	public Set<HousePhotos> getHousePhotos() {
//		return housePhotos;
//	}
//
//	public void setHousePhotos(Set<HousePhotos> housePhotos) {
//		this.housePhotos = housePhotos;
//	}

}