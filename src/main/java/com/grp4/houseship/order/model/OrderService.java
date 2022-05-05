package com.grp4.houseship.order.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    public boolean save(Order newOrder){
        if (newOrder.getOrderDetail() != null){
            System.out.println("有訂單明細");
            ordersRepo.save(newOrder);
            return true;
        }return false;
    }

}
