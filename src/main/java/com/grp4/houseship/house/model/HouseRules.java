package com.grp4.houseship.house.model;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "houserules")
public class HouseRules implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "RULESNO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rulesNo;

	@Column(name = "CHECK_IN")
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date check_in;

	@Column(name = "CHECK_OUT")
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date check_out;

	@Column(name = "SMOKING")
	private boolean smoking;
	
	@Column(name = "PET")
	private boolean pet;
	
	public HouseRules() {
	}

	public HouseRules(Time check_in, Time check_out, boolean smoking, boolean pet) {
		this.check_in = check_in;
		this.check_out = check_out;
		this.smoking = smoking;
		this.pet = pet;
	}

	public int getRulesNo() {
		return rulesNo;
	}

	public void setRulesNo(int rulesNo) {
		this.rulesNo = rulesNo;
	}

	public Date getCheck_in() {
		return check_in;
	}

	public void setCheck_in(Date check_in) {
		this.check_in = check_in;
	}

	public Date getCheck_out() {
		return check_out;
	}

	public void setCheck_out(Date check_out) {
		this.check_out = check_out;
	}
	
	public boolean isSmoking() {
		return smoking;
	}

	public void setSmoking(boolean smoking) {
		this.smoking = smoking;
	}

	public boolean isPet() {
		return pet;
	}

	public void setPet(boolean pet) {
		this.pet = pet;
	}

}
