package com.grp4.houseship.house.model;

import com.grp4.houseship.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<HouseInfo, Integer> {

    Optional<HouseInfo> findByHouseNoAndStatusIsTrue(int id);

//    List<HouseInfo> findByStatusIsTrueOrderByCreatedDateDesc();

    Page<HouseInfo> findByStatusIsTrueOrderByCreatedDateDesc(Pageable pageable);

    Page<HouseInfo> findByCityAndStatusIsTrueOrderByCreatedDateDesc(String city, Pageable pageable);

    List<HouseInfo> findByMemberOrderByCreatedDateDesc(Member member);

    @Query("from HouseInfo where h_price > :price and status = true order by createdDate desc")
    List<HouseInfo> findByH_priceGreaterThan(double price);

    @Query("from HouseInfo where h_price between :min and :max and status = true order by createdDate desc")
    List<HouseInfo> findByH_priceBetween(double min, double max);

}
