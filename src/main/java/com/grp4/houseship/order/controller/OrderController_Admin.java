package com.grp4.houseship.order.controller;

import com.grp4.houseship.member.model.MemberService;
import com.grp4.houseship.order.model.Order;
import com.grp4.houseship.order.model.OrderService;
import com.grp4.houseship.order.model.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/order")
public class OrderController_Admin {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String OrderAdminMain(Model model){
        orderService.orderStatusUpdate();
        model.addAttribute("orderList", orderService.findAll());
        return "/admin/order/order_admin_main";
    }

    //編輯入住
    @GetMapping("/edit/{orderId}")
    public String OrderAdminEditForm(@PathVariable("orderId") int orderId, Model model){
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "/admin/order/order_admin_edit";
    }

    @PostMapping("/postedit/{orderId}")
    public String OrderAdminEditPost(@PathVariable("orderId") int orderId, @ModelAttribute Order order){
        orderService.updateDetail(orderId, order);
        return "redirect:/admin/order";
    }

    @GetMapping("/cancelOrder/{orderId}")
    public String OrderAdminCancel(@PathVariable("orderId") int orderId){
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.Cancel);
        orderService.update(order);
        return "redirect:/admin/order";
    }

    @GetMapping("/activeOrder/{orderId}")
    public String OrderAdminActive(@PathVariable("orderId") int orderId){
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.Check);
        orderService.update(order);
        return "redirect:/admin/order";
    }

    @PostMapping("/checkAccount/{account}")
    @ResponseBody
    public String processInsertCheckAccount(@PathVariable("account") String account) {
        if (memberService.findByAccount(account) != null) {
            return "ok";
        }
        return null;
    }
}
