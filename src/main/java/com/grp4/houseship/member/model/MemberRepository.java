package com.grp4.houseship.member.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//因為沒有設自動生成的id為pk,所以就沒有多寫自定義的方法了

public interface MemberRepository extends JpaRepository<Member, String> {
	 
	public  Optional<Member> findByAccount(String account);
}

