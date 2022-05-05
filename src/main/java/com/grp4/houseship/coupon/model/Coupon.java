package com.grp4.houseship.coupon.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
//import javax.validation.constraints.Max;
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

//    @Max(value = 1, message = "折扣乘數應小於1")
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

}
