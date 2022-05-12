package com.grp4.houseship.coupon.model;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.Optional;
import java.util.function.Function;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

    public Optional<Coupon> findByCouponCode(String code);
}
