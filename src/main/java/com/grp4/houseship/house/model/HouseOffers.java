package com.grp4.houseship.house.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "houseoffers")
public class HouseOffers implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "OFFERSNO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int offersNo;
	
	@Column(name = "WIFI")
	private boolean wifi;
	
	@Column(name = "TV")
	private boolean tv;
	
	@Column(name = "KITCHEN")
	private boolean kitchen;
	
	@Column(name = "REFRIGERATOR")
	private boolean refrigerator;
	
	@Column(name = "MICROWAVE")
	private boolean microwave;
	
	@Column(name = "AIRCON")
	private boolean aircon;
	
	@Column(name = "WASHER")
	private boolean washer;
	
	public HouseOffers() {
	}

	public HouseOffers(boolean wifi, boolean tv, boolean kitchen, boolean refrigerator, boolean microwave,
			boolean aircon, boolean washer) {
		this.wifi = wifi;
		this.tv = tv;
		this.kitchen = kitchen;
		this.refrigerator = refrigerator;
		this.microwave = microwave;
		this.aircon = aircon;
		this.washer = washer;
	}

	public int getOffersNo() {
		return offersNo;
	}

	public void setOffersNo(int offersNo) {
		this.offersNo = offersNo;
	}

	public boolean isWifi() {
		return wifi;
	}

	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}

	public boolean isTv() {
		return tv;
	}

	public void setTv(boolean tv) {
		this.tv = tv;
	}

	public boolean isKitchen() {
		return kitchen;
	}

	public void setKitchen(boolean kitchen) {
		this.kitchen = kitchen;
	}

	public boolean isRefrigerator() {
		return refrigerator;
	}

	public void setRefrigerator(boolean refrigerator) {
		this.refrigerator = refrigerator;
	}

	public boolean isMicrowave() {
		return microwave;
	}

	public void setMicrowave(boolean microwave) {
		this.microwave = microwave;
	}

	public boolean isAircon() {
		return aircon;
	}

	public void setAircon(boolean aircon) {
		this.aircon = aircon;
	}

	public boolean isWasher() {
		return washer;
	}

	public void setWasher(boolean washer) {
		this.washer = washer;
	}

}
