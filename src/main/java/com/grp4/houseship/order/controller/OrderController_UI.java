package com.grp4.houseship.order.controller;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.order.ecpay.EcpayPayment;
import com.grp4.houseship.order.model.*;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
//@RequestMapping("/house/housedetails/{houseid}")
public class OrderController_UI {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HouseService houseService;

    @GetMapping("/new.form")
    public String newOrder(Model model) {
        HouseInfo houseInfo = houseService.searchById(2);
        OrderDetail orderDetail = new OrderDetail(houseInfo);
        model.addAttribute("orderDetail", orderDetail);
        return "/newOrder";
    }

    @PostMapping("/house/housedetails/{houseid}/booking")
    public String tryOrder(@PathVariable("houseid") int houseid,@ModelAttribute OrderDetail orderDetail, Model model){
        HouseInfo houseInfo = houseService.searchById(houseid);
        model.addAttribute("houseInfo", houseInfo);
        orderDetail.setGuest(new Guest());
        orderDetail.setHouseInfo(houseInfo);
        Order order = new Order(orderDetail);
        model.addAttribute("order", order);
        return "ui/order/checkOut";
    }

    @PostMapping("/house/housedetails/{houseid}/checkout")
    @ResponseBody
    public String createNewOrder(@PathVariable("houseid") int houseid, @ModelAttribute Order order, HttpSession session){
        HouseInfo houseInfo = houseService.searchById(houseid);
        order.getOrderDetail().setHouseInfo(houseInfo);
        Member member = (Member) session.getAttribute("member");
        if (member != null){
            order.setMember(member);
            order.setStatus(OrderStatus.UnCheck);
            Order order_uncheck = orderService.save(order);
            String ecPayForm = EcpayPayment.genAioCheckOutALL(order_uncheck);
            return ecPayForm;
        }return "no";
    }

    @PostMapping("/order/payResultCheck")
    public String payResultCheck(@RequestParam("RtnCode") int RtnCode, @RequestParam("MerchantTradeNo") String MerchantTradeNo){
        System.out.println(RtnCode);
        System.out.println(MerchantTradeNo.substring(7));
        //回傳值為1 表示付款成功
        if (RtnCode == 1){
            int orderId = Integer.parseInt(MerchantTradeNo.substring(7));
            //更新訂單狀態
            Order order = orderService.findById(orderId);
            order.setStatus(OrderStatus.Check);
            System.out.println(order.getMember().getAccount());
            orderService.update(order);
        }
        return "redirect:/home";
    }

    @GetMapping ("order/getMemberData")
    @ResponseBody
    public Member getMemberData(HttpSession session){
        Member member = (Member) session.getAttribute("member");
        return member;
    }
}
