package com.grp4.houseship.house.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "housephotos")
public class HousePhotos implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "PHOTONO")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int photoNo;

//	@Column(name = "HOUSENO")
	@Transient
	private int houseNo;
	
	@Column(name = "PHOTOPATH")
	private String photoPath;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "HOUSENO", referencedColumnName = "HOUSENO")
//	private HouseInfo houseInfo;

	public HousePhotos() {
	}

	public HousePhotos(String photoPath) {
		this.photoPath = photoPath;
	}

	public int getPhotoNo() {
		return photoNo;
	}

	public void setPhotoNo(int photoNo) {
		this.photoNo = photoNo;
	}

	public int getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(int houseNo) {
		this.houseNo = houseNo;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

//	public HouseInfo getHouseInfo() {
//		return houseInfo;
//	}
//
//	public void setHouseInfo(HouseInfo houseInfo) {
//		this.houseInfo = houseInfo;
//	}
	
}
