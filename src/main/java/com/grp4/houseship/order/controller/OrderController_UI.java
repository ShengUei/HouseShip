package com.grp4.houseship.order.controller;

import com.grp4.houseship.email.service.EmailService;
import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.order.ecpay.EcpayPayment;
import com.grp4.houseship.order.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController_UI {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HouseService houseService;

    @Autowired
    private EmailService emailService;

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
    public String payResultCheck(@RequestParam("RtnCode") int RtnCode, @RequestParam("MerchantTradeNo") String MerchantTradeNo) throws MessagingException {
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
            //傳送email
            String title = "您在 HouseShip 上預約 '"+ order.getOrderDetail().getHouseInfo().getH_title() + "' 已完成";
            emailService.sendOrderMail(order, "ui/order/email_order_success", title);
        }
        return "redirect:/home";
    }

    @GetMapping ("order/getMemberData")
    @ResponseBody
    public Member getMemberData(HttpSession session){
        Member member = (Member) session.getAttribute("member");
        return member;
    }

    //會員查詢訂單
    @GetMapping("/order/myorder/{pageNo}")
    public String viewMyOrders(@PathVariable("pageNo") int pageNo, HttpSession session, Model model){
        Member member = (Member) session.getAttribute("member");
        orderService.orderStatusUpdate();
        PageRequest pageable = PageRequest.of(pageNo - 1,4);
        Page<Order> checkpage = orderService.findAllByMemberAndStatusOrStatus(member, OrderStatus.Check, OrderStatus.Update, pageable);
        Page<Order> finishpage = orderService.findAllByMemberAndStatusPage(member, OrderStatus.Finish, pageable);
        Page<Order> cancelpage = orderService.findAllByMemberAndStatusPage(member, OrderStatus.Cancel, pageable);
        model.addAttribute("checkpage", checkpage);
        model.addAttribute("checkOrders", checkpage.getContent());

        model.addAttribute("finishpage", finishpage);
        model.addAttribute("finishOrders", finishpage.getContent());

        model.addAttribute("cancelpage", cancelpage);
        model.addAttribute("cancelOrders", cancelpage.getContent());

        return "ui/order/account_order";
    }

    //房客編輯入住資料
    @GetMapping("/order/myorder/edit/{orderId}")
    public String myOrderEdit(@PathVariable("orderId") int orderId, Model model){
        Order order = orderService.findById(orderId);
        model.addAttribute("order",order);
        return "/ui/order/account_order_edit";
    }

    @PostMapping("/order/myorder/postedit/{orderId}")
    public String myOrderEditPost(@PathVariable("orderId") int orderId, @ModelAttribute Order order){
        orderService.updateDetail(orderId, order);
        return "redirect:/order/myorder/1";
    }

    //房客取消訂單
    @GetMapping("/order/myorder/cancel/{orderId}")
    public String myOrderCancel(@PathVariable("orderId") int orderId, Model model){
        Order order = orderService.findById(orderId);
        order.setStatus(OrderStatus.Cancel);
        orderService.update(order);
        return "redirect:/order/myorder/1";
    }


    //以下刪除

    @GetMapping("/order/sendEmail")
    @ResponseBody
    public String trySendEmail() throws MessagingException {
        Member member = new Member();
        member.setFirstname("Lucy");
        emailService.sendMail(member);
        return "email sent!";
    }

    @GetMapping("/order/tryview")
    public String tryview(Model model){
       Order order = orderService.findById(34);
       model.addAttribute("order", order);
        return "ui/order/email_order_success";
    }

    @GetMapping("/order/tryview/sendmail")
    @ResponseBody
    public String tryviewSend() throws MessagingException {
        Order order = orderService.findById(44);

        String title = "您在 HouseShip 上預約 '"+ order.getOrderDetail().getHouseInfo().getH_title() + "' 已完成";
        emailService.sendOrderMail(order, "ui/order/email_order_success", title);
        return "email sent!";
    }



}
