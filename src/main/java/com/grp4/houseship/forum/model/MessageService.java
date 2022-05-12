package com.grp4.houseship.forum.model;//package com.grp4.houseship.forum.model;
//
//import java.util.List;
//import java.util.Optional;
//
//import javax.transaction.Transactional;
//
//import org.apache.logging.log4j.message.Message;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//@Transactional
//public class MessageService {
//	@Autowired
//	private MessageRepository messageRepository;
//
////-----------------Insert-------------------------------------------
//	public Message insert(Message message) {
//		return messageRepository.save(message);
//	}
//
////------------------Delete----------------------------------------------
//	public boolean delete(int fid) {
//		Optional<Message> id = Optional.empty();
//		if (id.isPresent()) {
//			messageRepository.deleteById(fid);
//			return true;
//		}
//		return false;
//	}
//
////------------------Update----------------------------------------------
//	public Message update(Integer fid, Message message) {
////		forumRepository.s
//		return messageRepository.save(message);
//	}
//
////-----------------QueryById---------------------------------------------
//	public message findById(Integer fid) {
//		Optional<message> op1 = messageRepository.findById(fid);
//		if (op1.isPresent()) {
//			return op1.get();
//
//		}
//		return null;
//
//	}
//
////------------------QueryAll-------------------------------------------
//	public List<message> findAll() {
//		return messageRepository.findAll();
//	}
//}
