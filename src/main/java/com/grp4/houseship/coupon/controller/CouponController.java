package com.grp4.houseship.coupon.controller;

import com.grp4.houseship.coupon.model.Coupon;
import com.grp4.houseship.coupon.model.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping("/check/{code}")
    @ResponseBody
    public Coupon checkCouponAvailable(@PathVariable("code") String code, Model model){
        System.out.println("code:" + code);
        Coupon coupon = couponService.checkCoupon(code);
        return coupon;
    }
}
