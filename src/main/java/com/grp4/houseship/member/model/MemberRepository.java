package com.grp4.houseship.member.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//因為沒有設自動生成的id為pk,所以就沒有多寫自定義的方法了
		
public interface MemberRepository extends JpaRepository<Member, String> {
	@Query(value = "SELECT m from Member m WHERE m.account = :account")
	public Optional<Member> findByAccount(@Param("account") String account);
	
	//用取得的驗證碼找對應的member物件(for 註冊驗證)
	public Member findByVerificationCode(String code);
	
	//用戶輸入信箱以獲取忘記密碼驗證信,所以需要用信箱找到對應entity(for 忘記密碼)
	//第三方也有用到
	public Member findByEmail(String email); 
	
	//用取得的驗證碼找對應的member物件(for 忘記密碼)
	public Member findByResetPasswordToken(String token);
}


