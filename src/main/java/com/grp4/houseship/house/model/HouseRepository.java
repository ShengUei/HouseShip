package com.grp4.houseship.house.model;

import com.grp4.houseship.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    //空房查詢
    String queryDate = "select * from houseinfo where houseNo not in (\n" +
            "select houseno\n" +
            "from orderDetail\n" +
            "where (checkInDate <= :date1 and checkOutDate > :date1) \n" +
            "or (checkInDate < :date2 AND checkOutDate >= :date2 )\n" +
            "or (checkInDate >= :date1 AND checkInDate <= :date2)\n" +
            ")" ;

    String queryDate_count = "select count(*) from houseinfo where houseNo not in (\n" +
            "select houseno\n" +
            "from orderDetail\n" +
            "where (checkInDate <= :date1 and checkOutDate > :date1) \n" +
            "or (checkInDate < :date2 AND checkOutDate >= :date2 )\n" +
            "or (checkInDate >= :date1 AND checkInDate <= :date2)\n" +
            ")" ;

    String queryDateAndCity = "select * from houseinfo where houseNo not in (\n" +
            "select houseno\n" +
            "from orderDetail\n" +
            "where (checkInDate <= :date1 and checkOutDate > :date1) \n" +
            "or (checkInDate < :date2 AND checkOutDate >= :date2 )\n" +
            "or (checkInDate >= :date1 AND checkInDate <= :date2)\n" +
            ")and houseinfo.h_city = :city";

    String queryDateAndCity_count = "select count(*) from houseinfo where houseNo not in (\n" +
            "select houseno\n" +
            "from orderDetail\n" +
            "where (checkInDate <= :date1 and checkOutDate > :date1) \n" +
            "or (checkInDate < :date2 AND checkOutDate >= :date2 )\n" +
            "or (checkInDate >= :date1 AND checkInDate <= :date2)\n" +
            ")and houseinfo.h_city = :city";

    @Query(value = queryDate,countQuery = queryDate_count, nativeQuery = true)
    Page<HouseInfo> findByDateBetween(@Param("date1") String date1, @Param("date2") String date2, Pageable pageable) ;

    @Query(value = queryDateAndCity, countQuery = queryDateAndCity_count, nativeQuery = true)
    Page<HouseInfo> findByDateBetweenAndCity(@Param("date1") String date1, @Param("date2") String date2, @Param("city") String city, Pageable pageable) ;

    //確認房源與日期可提供
    String queryDateAndHouse = "select * from houseinfo where houseNo not in (\n" +
            "select houseno\n" +
            "from orderDetail\n" +
            "where (checkInDate <= :date1 and checkOutDate > :date1) \n" +
            "or (checkInDate < :date2 AND checkOutDate >= :date2 )\n" +
            "or (checkInDate >= :date1 AND checkInDate <= :date2)\n" +
            ") and houseNo = :houseNo" ;

    @Query(value = queryDateAndHouse, nativeQuery = true)
    HouseInfo findByDateAndHouseNo(@Param("date1") String date1, @Param("date2") String date2, @Param("houseNo") int houseNo) ;


}
