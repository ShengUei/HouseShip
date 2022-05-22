package com.grp4.houseship.coupon.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CouponService {

    @Autowired
    private CouponRepository couponRepo;

    public Long getDataCount() {
        return couponRepo.count();
    }

    //查詢優惠券
    public Coupon checkCoupon(String code){
        isValidUpdate();
        Optional<Coupon> optional = couponRepo.findByCouponCode(code);
        if (optional.isPresent()){
            Coupon coupon = optional.get();
            //確認為啟用狀態
            if (coupon.getStatus().equals(CouponStatus.Enabled)){
                return coupon;
            }return null;
        }
        return null;
    }

    public Coupon findById(int couponNo){
        Optional<Coupon> optional = couponRepo.findById(couponNo);
        if (optional.isPresent()){
            Coupon coupon = optional.get();
            //確認為啟用狀態
            if (coupon.getStatus().equals(CouponStatus.Enabled)){
                return coupon;
            }return null;
        }
        return null;
    }

    public Coupon findByIdOrigin(int couponNo){
        Optional<Coupon> optional = couponRepo.findById(couponNo);
        if (optional.isPresent()){
            Coupon coupon = optional.get();
            return coupon;
        }
        return null;
    }

    //查全部
    public List<Coupon> findAll(){
        return couponRepo.findAll();
    }

    //狀態查詢全部
    public List<Coupon> findAllByStatus(CouponStatus status){
        return couponRepo.findAllByStatus(status);
    }

    //新增或更新
    public Coupon save(Coupon coupon){
        return couponRepo.save(coupon);
    }

    //更新全部
    public List<Coupon> saveAll(List<Coupon> allCoupon){
        return couponRepo.saveAll(allCoupon);
    }


    //更新優惠券狀態
    public void isValidUpdate(){
        List<Coupon> allCoupon = findAll();
        Date today = new Date();
        for (Coupon coupon : allCoupon){
            Date endDate = new Date( coupon.getEndDate().getTime() + (1000*60*60*24) );
            if (today.after(endDate)){
                coupon.setStatus(CouponStatus.Expired);
            }
        }
        saveAll(allCoupon);
    }

}
