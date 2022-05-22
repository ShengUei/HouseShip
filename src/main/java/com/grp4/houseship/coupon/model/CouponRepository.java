package com.grp4.houseship.coupon.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    public Optional<Coupon> findByCouponCode(String code);

    public List<Coupon> findAllByStatus(CouponStatus status);
}
