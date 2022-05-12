package com.grp4.houseship.order.model;

import com.grp4.houseship.coupon.model.Coupon;

import com.grp4.houseship.member.model.Member;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "ORDERID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderId;

    //會員才能下單
    @ManyToOne
    @JoinColumn(name = "ACCOUNT", referencedColumnName = "ACCOUNT")
    private Member member;

    @OneToOne(cascade = {CascadeType.PERSIST ,CascadeType.REFRESH, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinColumn(name = "BOOKNO")
    private OrderDetail orderDetail;

    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "ORDERTIME")
    private Date orderTime;

    @Column(name = "PAY")
    private Double pay;

    @ManyToOne
    @JoinColumn(name = "COUPONNO", referencedColumnName = "COUPONNO")
    private Coupon coupon;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private OrderStatus status;

    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "MODIFIEDTIME")
    private Date modifiedTime;

    @LastModifiedBy
    @Column(name = "MODIFIEDBY")
    private String modifiedBy;


    //建構子
    public Order(){}

    public Order(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }
    public Order(Member member, OrderDetail orderDetail) {
        this.member = member;
        this.orderDetail = orderDetail;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Double getPay() {
        return pay;
    }

    public void setPay(Double pay) {
        this.pay = pay;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupon coupon) {
        this.coupon = coupon;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
