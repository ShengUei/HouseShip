package com.grp4.houseship.coupon.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.Date;

@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @Column(name = "COUPONNO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int couponNo;

    @Column(name = "COUPONCODE")
    private String couponCode;

    @Column(name = "TITLE")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private CouponStatus status;

    @Max(value = 1, message = "折扣乘數應小於1")
    @Column(name = "DISCOUNT")
    private double discount;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "STARTDATE")
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "ENDDATE")
    private Date endDate;


    public Coupon() {
    }

    public Coupon(String couponCode, String title, CouponStatus status, double discount, Date startDate, Date endDate) {
        this.couponCode = couponCode;
        this.title = title;
        this.status = status;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(int couponNo) {
        this.couponNo = couponNo;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CouponStatus getStatus() {
        return status;
    }

    public void setStatus(CouponStatus status) {
        this.status = status;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
