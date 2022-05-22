package com.grp4.houseship.forum.model;

import java.util.List;

import com.grp4.houseship.member.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRepository extends JpaRepository<Forum, Integer> {


	List<Forum> findByMember(Member member);
	
//	List<Forum> findByMessage()

}