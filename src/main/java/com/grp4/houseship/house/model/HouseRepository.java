package com.grp4.houseship.house.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<HouseInfo, Integer> {

    Optional<HouseInfo> findByHouseNoAndStatusIsTrue(int id);

    List<HouseInfo> findByStatusIsTrueOrderByCreatedDateDesc();

    List<HouseInfo> findByCityAndStatusIsTrueOrderByCreatedDateDesc(String city);

    @Query("from HouseInfo where h_price > :price and status = true order by createdDate desc")
    List<HouseInfo> findByH_priceGreaterThan(double price);

    @Query("from HouseInfo where h_price between :min and :max and status = true order by createdDate desc")
    List<HouseInfo> findByH_priceBetween(double min, double max);

}
