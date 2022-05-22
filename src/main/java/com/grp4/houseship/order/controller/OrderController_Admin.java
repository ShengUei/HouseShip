package com.grp4.houseship.order.controller;

import com.grp4.houseship.coupon.model.Coupon;
import com.grp4.houseship.coupon.model.CouponService;
import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;
import com.grp4.houseship.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/order")
public class OrderController_Admin {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private CouponService couponService;

    @GetMapping
    public String OrderAdminMain(Model model) {
        orderService.orderStatusUpdate();
        model.addAttribute("orderList", orderService.findAllValid());
        //新增物件
        Order order = new Order();
        OrderDetail orderDetail = new OrderDetail();
        Guest guest = new Guest();
        orderDetail.setGuest(guest);
        order.setOrderDetail(orderDetail);
        model.addAttribute("order", order);
        return "/admin/order/order_admin_main";
    }

    //查進行中訂單
    @GetMapping("/checkOrder")
    public String OrderAdminMain_Check(Model model) {
        orderService.orderStatusUpdate();
        model.addAttribute("orderList", orderService.findAllByStatus(OrderStatus.Check));
        //新增物件
        Order order = new Order();
        OrderDetail orderDetail = new OrderDetail();
        Guest guest = new Guest();
        orderDetail.setGuest(guest);
        order.setOrderDetail(orderDetail);
        model.addAttribute("order", order);
        return "/admin/order/order_admin_main";
    }

    //查已完成訂單
    @GetMapping("/finishOrder")
    public String OrderAdminMain_Finish(Model model) {
        orderService.orderStatusUpdate();
        model.addAttribute("orderList", orderService.findAllByStatus(OrderStatus.Finish));
        //新增物件
        Order order = new Order();
        OrderDetail orderDetail = new OrderDetail();
        Guest guest = new Guest();
        orderDetail.setGuest(guest);
        order.setOrderDetail(orderDetail);
        model.addAttribute("order", order);
        return "/admin/order/order_admin_main";
    }

    //查已取消訂單
    @GetMapping("/cancelOrder")
    public String OrderAdminMain_Cancel(Model model) {
        orderService.orderStatusUpdate();
        model.addAttribute("orderList", orderService.findAllByStatus(OrderStatus.Cancel));
        //新增物件
        Order order = new Order();
        OrderDetail orderDetail = new OrderDetail();
        Guest guest = new Guest();
        orderDetail.setGuest(guest);
        order.setOrderDetail(orderDetail);
        model.addAttribute("order", order);
        return "/admin/order/order_admin_main";
    }

    //編輯入住
    @GetMapping("/edit/{orderId}")
    public String OrderAdminEditForm(@PathVariable("orderId") int orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "/admin/order/order_admin_edit";
    }

    @PostMapping("/postedit/{orderId}")
    public String OrderAdminEditPost(@PathVariable("orderId") int orderId, @ModelAttribute Order order) {
        orderService.updateDetail(orderId, order);
        return "redirect:/admin/order";
    }

    @GetMapping("/cancelOrder/{orderId}")
    public String OrderAdminCancel(@PathVariable("orderId") int orderId) {
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.Cancel);
        orderService.update(order);
        return "redirect:/admin/order";
    }

    @GetMapping("/activeOrder/{orderId}")
    public String OrderAdminActive(@PathVariable("orderId") int orderId) {
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.Check);
        orderService.update(order);
        return "redirect:/admin/order";
    }

    //新增前驗證帳號和房源
    @PostMapping("/checkAccount/{account}")
    @ResponseBody
    public String processInsertCheckAccount(@PathVariable("account") String account) {
        if (memberService.findByAccount(account) != null) {
            return "ok";
        }
        return null;
    }

    @PostMapping("/checkHouse/{houseNo}")
    @ResponseBody
    public HouseInfo processInsertCheckHouse(@PathVariable("houseNo") int houseNo) {
        return houseService.searchById(houseNo);
    }

    //新增
    @PostMapping("/adminInsert")
    public String insertOrder(@ModelAttribute Order order, @RequestParam(name = "newOrder_house") String newOrder_house,
                              @RequestParam(name = "inputAccount") String account, @RequestParam(name = "newOrder_couponNo") String newOrder_couponNo) {
        int houseNo = Integer.parseInt(newOrder_house);
        HouseInfo houseInfo = houseService.searchById(houseNo);
        Member member = memberService.findByAccount(account);

        if (newOrder_couponNo != null) {
            int couponNo = Integer.parseInt(newOrder_couponNo);
            Coupon coupon = couponService.findById(couponNo);
            order.setCoupon(coupon);
        }

        order.setMember(member);
        order.setStatus(OrderStatus.Check);
        order.getOrderDetail().setHouseInfo(houseInfo);
        orderService.save(order);
        return "redirect:/admin/order";
    }

    //訂單無效
    @PostMapping("/adminInvalid")
    public String invalidOrder(@RequestBody ArrayList<Integer> listid){
        List<Order> orderList = orderService.findAllByOrderId(listid);
        for (Order order : orderList){
            order.setStatus(OrderStatus.Invalid);
        }
        orderService.saveAll(orderList);
        return "redirect:/admin/order";
    }
}
