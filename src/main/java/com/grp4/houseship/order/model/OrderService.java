package com.grp4.houseship.order.model;

import com.grp4.houseship.coupon.model.Coupon;
import com.grp4.houseship.coupon.model.CouponStatus;
import com.grp4.houseship.member.model.Member;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository ordersRepo;

    @Autowired
    private OderDetailRepository detailRepo;

    public Long getDataCount() {
        return ordersRepo.count();
    }

    //新增
    public Order save(Order newOrder){
        return ordersRepo.save(newOrder);
    }

    public Order update(Order order){
        return ordersRepo.save(order);
    }

    //修改入住資料
    public Order updateDetail(int orderId, Order updateOrder){
        Optional<Order> optional = ordersRepo.findById(orderId);
        if (optional.isPresent()){
            Order order = optional.get();
            order.getOrderDetail().setGuest(updateOrder.getOrderDetail().getGuest());
            order.getOrderDetail().setGuestNum(updateOrder.getOrderDetail().getGuestNum());
            order.getOrderDetail().setCheckInTime(updateOrder.getOrderDetail().getCheckInTime());
            order.getOrderDetail().setNote(updateOrder.getOrderDetail().getNote());
            return ordersRepo.save(order);
        }return null;
    }

    public void saveAll(List<Order> orderList){ ordersRepo.saveAll(orderList); }

    //查詢
    public Order findById(int orderId){
        Optional<Order> optional = ordersRepo.findById(orderId);
        if (optional.isPresent()){
            return optional.get();
        }return null;
    }

    public List<Order> findAll(){
        return ordersRepo.findAll();
    }

    public List<Order> findByMember(Member member){
        return ordersRepo.findAllByMember(member);
    }

    //會員查詢訂單
    public List<Order> findAllByMemberAndStatus(Member member, OrderStatus status){
        return ordersRepo.findAllByMemberAndStatus(member,status);
    }

    //會員查詢訂單:分頁
    public Page<Order> findAllByMemberAndStatusPage(Member member, OrderStatus status, Pageable pageable){
        return ordersRepo.findAllByMemberAndStatus(member, status, pageable);
    }

    public Page<Order> findAllByMemberAndStatusOrStatus(Member member, OrderStatus status1, OrderStatus status2, Pageable pageable){
        return ordersRepo.findAllByMemberAndStatusOrStatus(member, status1, status2, pageable);
    }

    //更新訂單狀態
    public void orderStatusUpdate(){
        List<Order> allOrder = findAll();
        Date today = new Date();
        for (Order order : allOrder){
            Date checkOutDate = new Date( order.getOrderDetail().getCheckOutDate().getTime() + (1000*60*60*24) );
            if (today.after(checkOutDate)){
                order.setStatus(OrderStatus.Finish);
            }
            if (order.getStatus() == OrderStatus.UnCheck){
                order.setStatus(OrderStatus.Cancel);
            }
        }
        saveAll(allOrder);
    }

}
