package com.grp4.houseship.forum.model;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ForumService {
	@Autowired
	private ForumRepository forumRepository;

	public Long getDataCount() {
		return forumRepository.count();
	}

//-----------------Insert-------------------------------------------
	public Forum insert(Forum forum) {
		return forumRepository.save(forum);
	}

//------------------Delete----------------------------------------------
	public boolean delete(int fid) {
		Optional<Forum> id = forumRepository.findById(fid);
		if (id.isPresent()) {
			forumRepository.deleteById(fid);
			return true;
		}
		return false;
	}

//------------------Update----------------------------------------------
	public Forum update(Integer fid, Forum forum) {
//		forumRepository.s
		return forumRepository.save(forum);
	}

//-----------------QueryById---------------------------------------------
	public Forum findById(Integer fid) {
		Optional<Forum> op1 = forumRepository.findById(fid);
		if (op1.isPresent()) {
			return op1.get();

		}
		return null;

	}

//------------------QueryAll-------------------------------------------
	public List<Forum> findAll() {
		return forumRepository.findAll();
	}

//	public List<Forum> findmyForums(String account) {
//		return forumRepository.findMyForum(account);
//	}

}
