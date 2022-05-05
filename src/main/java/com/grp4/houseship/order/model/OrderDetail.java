package com.grp4.houseship.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grp4.houseship.house.model.HouseInfo;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import javax.validation.constraints.*;

@Entity
@Table(name = "orderdetail")
public class OrderDetail {

    @Id
    @Column(name = "BOOKNO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookNo;

    @ManyToOne
    @JoinColumn(name = "HOUSENO", referencedColumnName = "HOUSENO")
    private HouseInfo houseInfo;

    @Column(name = "CHECKINDATE")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkInDate;

    @Column(name = "CHECKOUTDATE")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkOutDate;

    @Column(name = "HOUSEPRICE")
    private Double housePrice;

    //預計入住時間
    @Temporal(TemporalType.TIME)
    @DateTimeFormat(pattern = "HH:mm")
    @Column(name = "CHECKINTIME")
    private Date checkInTime;

    //入住人數
    @Column(name = "GUESTNUM")
    private int guestNum;

    //其他備註
    @Column(name = "NOTE")
    private String note;

    //宣告OneToOne關係
    @OneToOne(cascade = {CascadeType.PERSIST ,CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "GUESTID")
    private Guest guest;

    //連結Order表
    @OneToOne(mappedBy = "orderDetail")
    private Order order;

    //建構子
    public OrderDetail(){}

    public OrderDetail(HouseInfo houseInfo) {
        this.houseInfo = houseInfo;
        this.housePrice = houseInfo.getH_price();
    }

    public OrderDetail(HouseInfo houseInfo, Date checkInDate, Date checkOutDate, int guestNum) {
        this.houseInfo = houseInfo;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestNum = guestNum;
    }

    public int getBookNo() {
        return bookNo;
    }

    public void setBookNo(int bookNo) {
        this.bookNo = bookNo;
    }

    public HouseInfo getHouseInfo() {
        return houseInfo;
    }

    public void setHouseInfo(HouseInfo houseInfo) {
        this.houseInfo = houseInfo;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Double getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(Double housePrice) {
        this.housePrice = housePrice;
    }

    public Date getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Date checkInTime) {
        this.checkInTime = checkInTime;
    }

    public int getGuestNum() {
        return guestNum;
    }

    public void setGuestNum(int guestNum) {
        this.guestNum = guestNum;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }
}
