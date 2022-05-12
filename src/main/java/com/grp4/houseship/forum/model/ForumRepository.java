package com.grp4.houseship.forum.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ForumRepository extends JpaRepository<Forum, Integer> {

//	@Query("from forum where account = account")
//	List<Forum> findMyForum(String account);

}