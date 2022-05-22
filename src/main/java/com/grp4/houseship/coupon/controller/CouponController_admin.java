package com.grp4.houseship.coupon.controller;

import com.grp4.houseship.coupon.model.Coupon;
import com.grp4.houseship.coupon.model.CouponService;
import com.grp4.houseship.coupon.model.CouponStatus;
import com.grp4.houseship.email.service.EmailService;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;

@Controller
@RequestMapping("/admin/coupon")
public class CouponController_admin {

    @Autowired
    private CouponService couponService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MemberService memberService;

    @GetMapping
    public String couponAdmin(Model model){
        couponService.isValidUpdate();
        model.addAttribute("couponList", couponService.findAll());
        return "/admin/coupon/admin_coupon_main";
    }

    //查詢所有啟用優惠券
    @GetMapping("/active")
    public String couponAdminActive(Model model){
        couponService.isValidUpdate();
        model.addAttribute("couponList", couponService.findAllByStatus(CouponStatus.Enabled));
        return "/admin/coupon/admin_coupon_main";
    }

    //查詢所有停用優惠券
    @GetMapping("/disable")
    public String couponAdminDisable(Model model){
        couponService.isValidUpdate();
        model.addAttribute("couponList", couponService.findAllByStatus(CouponStatus.Disabled));
        return "/admin/coupon/admin_coupon_main";
    }

    @GetMapping("/expired")
    public String couponAdminExpired(Model model){
        couponService.isValidUpdate();
        model.addAttribute("couponList", couponService.findAllByStatus(CouponStatus.Expired));
        return "/admin/coupon/admin_coupon_main";
    }

    @GetMapping("/suspendCoupon/{couponNo}")
    public String suspendCoupon(@PathVariable("couponNo") String couponNoInput){
        int couponNo = Integer.parseInt(couponNoInput);
        Coupon coupon = couponService.findByIdOrigin(couponNo);
        if (coupon != null) {
            coupon.setStatus(CouponStatus.Disabled);
            couponService.save(coupon);
        }
        return "redirect:/admin/coupon";
    }

    @GetMapping("/activeCoupon/{couponNo}")
    public String activeCoupon(@PathVariable("couponNo") String couponNoInput){
        int couponNo = Integer.parseInt(couponNoInput);
        Coupon coupon = couponService.findByIdOrigin(couponNo);
        if (coupon != null) {
            coupon.setStatus(CouponStatus.Enabled);
            couponService.save(coupon);
        }
        return "redirect:/admin/coupon";
    }

    //新增表單
    @GetMapping("/insertCoupon")
    public String insertCoupon(Model model){
        Coupon coupon = new Coupon();
        model.addAttribute("coupon", coupon);
        return "/admin/coupon/admin_coupon_new";
    }

    @PostMapping("/postinsert")
    public String postinsert(@ModelAttribute Coupon coupon){
        coupon.setStatus(CouponStatus.Enabled);
        couponService.save(coupon);
        return "redirect:/admin/coupon";
    }

    //修改
    @GetMapping("/edit/{couponNo}")
    public String CouponAdminEditForm(@PathVariable("couponNo") int couponNo, Model model) {
        Coupon coupon = couponService.findByIdOrigin(couponNo);
        model.addAttribute("coupon", coupon);
        return "/admin/coupon/admin_coupon_edit";
    }

    @PostMapping("/postEdit/{couponNo}")
    public String postEdit(@PathVariable("couponNo") int couponNo,@ModelAttribute Coupon updateCoupon){
        Coupon coupon = couponService.findByIdOrigin(couponNo);
        if (coupon != null) {
            couponService.save(updateCoupon);
        }
        return "redirect:/admin/coupon";
    }

    @GetMapping("/send/{couponNo}")
    @ResponseBody
    public Boolean sendMailToMember(@PathVariable("couponNo") int couponNo) throws MessagingException {
        Coupon coupon = couponService.findByIdOrigin(couponNo);
        List<Member> memberList = memberService.findAll();

        String title = "HouseShip 優惠報你知";
        String template = "/admin/coupon/email_coupon_send";
        Boolean isMailSent = true;

        for (Member member : memberList){
           isMailSent = emailService.sendCouponMail(coupon, member, template, title);
        }

        return isMailSent;
    }
}
