package com.grp4.houseship.house.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.grp4.houseship.member.model.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "houseinfo")
public class HouseInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "HOUSENO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int houseNo;
	
	@Transient
	private String account;

	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATEDDATE")
	private Date createdDate;

	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LASTMODIFIEDDATE")
	private Date lastModifiedDate;
	
	@Column(name = "H_TITLE")
	private String h_title;

	@Column(name = "H_CITY")
	private String city;
	
	@Column(name = "H_ADDRESS")
	private String h_address;

	@Column(name = "LAT")
	private Double lat;

	@Column(name = "LNG")
	private Double lng;
	
	@Column(name = "H_TYPE")
	private int h_type;
	
	@Column(name = "H_ABOUT")
	private String h_about;
	
	@Column(name = "H_PRICE")
	private double h_price;

	@Column(name = "STATUS")
	private boolean status;
	
	@Transient
	private int offersNo;
	
	@Transient
	private int rulesNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACCOUNT", referencedColumnName = "ACCOUNT")
//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Member member;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "OFFERSNO", referencedColumnName = "OFFERSNO")
	private HouseOffers houseOffers;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "RULESNO", referencedColumnName = "RULESNO")
	private HouseRules houseRules;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "HOUSENO", referencedColumnName = "HOUSENO")
	private List<HousePhotos> housePhotos;
	
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

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
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

	public List<HousePhotos> getHousePhotos() {
		return housePhotos;
	}

	public void setHousePhotos(List<HousePhotos> housePhotos) {
		this.housePhotos = housePhotos;
	}
}