package com.grp4.houseship.member.model;



import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	public Long getDataCount() {
		return memberRepository.count();
	}
	 
	public List<Member> findAll(){
		return memberRepository.findAll();
	}
	
	public Member findByAccount(String account) {
		//直接用findById就能找到欄位名為account的值,應該是透過Bean的設定@Id @Column(name="account")有關
		//至於不是@Id的屬性是否還能找到就不知道了
		//至少老師在已經設id為主鍵的情況下,在repository又重寫了一個findByName()?
		Optional<Member> member = memberRepository.findById(account);
		if(member.isPresent()) {
			return member.get();
		}
		return null;
	}
	
	public Boolean insert(Member member) {
		
		Member resultBean = findByAccount(member.getAccount());
		if(resultBean==null) {
			memberRepository.save(member);
			return true;
		}
		return false;
		
	}
	
	public Boolean update(Member newMemberInfo) {
		
		Member resultBean = findByAccount(newMemberInfo.getAccount());
		if(resultBean!=null) {
			memberRepository.save(newMemberInfo);
			return true;
		}
		return false;
	}
	
	public Boolean delete(String account) {
		
		Member resultBean = findByAccount(account);
		if(resultBean!=null) {
			memberRepository.delete(resultBean);
			return true;
		}
		return false;
	}

}
