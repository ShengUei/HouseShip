package com.grp4.houseship.forum.model;//package com.grp4.houseship.forum.model;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.transaction.Transactional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.grp4.houseship.member.model.Member;
//
//@Service
//@Transactional
//public class MessageService {
//	@Autowired
//	private MessageRepository messageRepository;
//
////-----------------Insert-------------------------------------------
//	public Message insert(Message forum) {
//		return messageRepository.save(forum);
//	}
//	
//
////------------------Delete----------------------------------------------
//	public boolean delete(int mid) {
//		Optional<Message> id = messageRepository.findById(mid);
//		if (id.isPresent()) {
//			messageRepository.deleteById(mid);
//			return true;
//		}
//		return false;
//	}
//
////------------------Update----------------------------------------------
//	public boolean update(Integer fid, Message forum) {
//		Optional<Message> op1 = messageRepository.findById(fid);
//		if (op1.isPresent()) {
//			messageRepository.save(forum);
//			return true;
//		}
//		return false;
//	}
//
////-----------------QueryById---------------------------------------------
//	public Message findById(Integer fid) {
//		Optional<Message> op1 = messageRepository.findById(fid);
//		if (op1.isPresent()) {
//			return op1.get();
//
//		}
//		return null;
//
//	}
//
////------------------QueryAll-------------------------------------------
//	public List<Message> findAll() {
//		return messageRepository.findAll();
//	}
//
//	public List<Message> findByAccount(Member account) {
//
//		return messageRepository.findByMember(account);
//	}
//}
