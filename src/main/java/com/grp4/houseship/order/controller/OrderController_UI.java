package com.grp4.houseship.order.controller;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.order.model.Order;
import com.grp4.houseship.order.model.OrderDetail;
import com.grp4.houseship.order.model.OrderStatus;
import com.grp4.houseship.order.model.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/house/housedetails/{houseid}")
public class OrderController_UI {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HouseService houseService;

    @PostMapping("/booking")
    public String tryOrder(@PathVariable("houseid") int houseid,@ModelAttribute OrderDetail orderDetail, Model model){
        HouseInfo houseInfo = houseService.searchById(houseid);
        model.addAttribute("houseInfo", houseInfo);
        model.addAttribute("orderDetail", orderDetail);
        Order order = new Order(orderDetail);
        model.addAttribute("order", order);
        return "ui/order/checkOut";
    }

}
