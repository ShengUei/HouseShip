package com.grp4.houseship.order.model;

import com.grp4.houseship.member.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findAllByMember(Member member);

    public List<Order> findAllByMemberAndStatus(Member member, OrderStatus status);

    public Page<Order> findAllByMemberAndStatus(Member member, OrderStatus status, Pageable pageable);

    public Page<Order> findAllByMemberAndStatusOrStatus(Member member, OrderStatus status1,OrderStatus status2, Pageable pageable);
}
