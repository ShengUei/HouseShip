package com.grp4.houseship.house.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HouseRepository extends JpaRepository<HouseInfo, Integer> {

    @Query("from HouseInfo where h_price > :price")
    List<HouseInfo> findByH_priceGreaterThan(double price);

    @Query("from HouseInfo where h_price between :min and :max")
    List<HouseInfo> findByH_priceBetween(double min, double max);

}
