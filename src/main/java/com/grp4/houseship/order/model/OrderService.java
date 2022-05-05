package com.grp4.houseship.order.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public Order findById(int orderId){
        Optional<Order> optional = ordersRepo.findById(orderId);
        if (optional.isPresent()){
            return optional.get();
        }return null;
    }

    public Order update(Order order){
        return ordersRepo.save(order);
    }

}
