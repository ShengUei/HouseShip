package com.grp4.houseship.order.model;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findAllByMember(Member member);

//    public List<Order> findAllByOrderId(List<Integer> listid);

    public List<Order> findAllByMemberAndStatus(Member member, OrderStatus status);

    public Page<Order> findAllByMemberAndStatus(Member member, OrderStatus status, Pageable pageable);

    public Page<Order> findAllByMemberAndStatusOrStatus(Member member, OrderStatus status1,OrderStatus status2, Pageable pageable);

    public List<Order> findAllByStatus(OrderStatus status);

    public List<Order> findAllByStatusOrStatusOrStatus(OrderStatus status1, OrderStatus status2, OrderStatus status3);

    String queryByHouse1 = "select * from orders ord join orderDetail de on ord.bookNo = de.bookNo\n" +
            "where de.houseNo in (:houseList) and status = :status";

    @Query(value = queryByHouse1, nativeQuery = true)
    public List<Order> findAllByHouseAndStatus(@Param("houseList") List<HouseInfo> houseInfoList, @Param("status") String status);

    String queryByHouse = "select * from orders ord join orderDetail de on ord.bookNo = de.bookNo\n" +
            "where de.houseNo in (:houseList) and (status = :status1 or status = :status2)";

    @Query(value = queryByHouse, nativeQuery = true)
    public List<Order> findAllByHouse(@Param("houseList") List<HouseInfo> houseInfoList, @Param("status1") String status1, @Param("status2") String status2);

}
