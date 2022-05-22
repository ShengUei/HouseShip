package com.grp4.houseship.member.model;



import java.io.UnsupportedEncodingException;

import java.util.List;
import java.util.Optional;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
    private JavaMailSender mailSender;
	 
	@Autowired
	private RoleService roleService;
	
	public List<Member> findAll(){
		return memberRepository.findAll();
	}
	
	
	public Member findByAccount(String account) {
		//直接用findById就能找到欄位名為account的值,應該是透過Bean的設定@Id @Column(name="account")有關
		//至於不是@Id的屬性是否還能找到就不知道了
		//至少老師在已經設id為主鍵的情況下,在repository又重寫了一個findByName()?
		Optional<Member> member = memberRepository.findByAccount(account);
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
	
	//以下for註冊及其驗證寫得
	
	//為了javamail寫得(註冊的controller方法改用這個方法新增資料)
	public boolean register(Member member,String siteURL) throws UnsupportedEncodingException, MessagingException {
		
		Member resultBean = findByAccount(member.getAccount());
		if(resultBean==null) {
			memberRepository.save(member);
			sendVerificationEmailForRegister(member, siteURL);
			
			return true;
		}
		return false;
		
	}
		
	//因為註冊後要接著寄驗證信,所以才寫在這(service)	
	//為了javamail寫得,用來寄信給user(只有上面的register()會呼叫它)
	private void sendVerificationEmailForRegister(Member member, String siteURL) throws UnsupportedEncodingException, MessagingException {
		String toAddress = member.getEmail();
	    String fromAddress = "eeit139.grp4@gmail.com";
	    String senderName = "HouseShip";
	    String subject = "請協助我們完成您的註冊流程";
	    String content = "親愛的使用者,<br>"
	            + "請點擊下方連結進行驗證:<br>"
	            + "<h3><a href=\"[[URL]]\" target=\"_self\">驗證</a></h3>"
	            + "感謝您,<br>"
	            + "HouseShip.";
	    
	    MimeMessage message = mailSender.createMimeMessage();
	    MimeMessageHelper helper = new MimeMessageHelper(message);
	     
	    helper.setFrom(fromAddress, senderName);
	    //在這邊設定用戶email
	    helper.setTo(toAddress);
	    helper.setSubject(subject);
	     
//	    content = content.replace("[[name]]", member.getFirstname()+member.getLastname());
	    String verifyURL = siteURL + "/ui/member/verifymember.controller?code=" + member.getVerificationCode();
	     
	    content = content.replace("[[URL]]", verifyURL);
	     
	    helper.setText(content, true);
	     
	    mailSender.send(message);
	}
	//for註冊成功後,用戶到他的信箱點擊驗證連結後流程所需的方法
	public boolean verify(String verificationCode) {
		Member member = memberRepository.findByVerificationCode(verificationCode);
		
		//如果用驗證碼找到的member物件是null(沒有註冊成功)或者它的啟用狀態是true(應該是false,註冊controller有設定)則表示驗證沒有成功
		//有找到物件且取用狀態是true表示找到的是其他已經驗證過的帳號吧?
		if(member == null || member.isEnabled()) {
			return false;
		//如果有透過驗證碼找到member物件且其啟用狀態為false,則把該使用者的驗證碼歸零為null,且啟用狀態設定為true
		}else {
			member.setVerificationCode(null);
		    member.setEnabled(true);
		    memberRepository.save(member);
		    return true;
		}
		
		
	}
	
	
	//---------------------------------------------以下for忘記密碼-----------------------------------------------------------
	
	public void updateResetPasswordToken(String token, String email)  {
        //透過email找到member物件
		Member member = memberRepository.findByEmail(email);
        if (member != null) {
        	//把controller隨機產生的的token塞給這個member物件
            member.setResetPasswordToken(token);
            memberRepository.save(member);
        } else {
            //throw new CustomerNotFoundException("Could not find any customer with the email " + email); 之後看有沒有要寫
        }
    }
	
	//這裡的參數token是來自用戶點擊驗證連結後送到controller再送來的,透過這個token取得對應物件返回給controller
	public Member getByResetPasswordToken(String token) {
        return memberRepository.findByResetPasswordToken(token);
    }
	
	//重設密碼
	public void updatePassword(Member member, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        member.setHashed_pwd(encodedPassword);
        //完成驗證且更新密碼跑完整個流程後,就能把忘記密碼驗證碼了欄位值去掉
        member.setResetPasswordToken(null);
        memberRepository.save(member);
    }
	//---------------------------------------------------------for第三方----------------------------------------------------------------------
	//for 第三方登入(登入成功後幫user註冊not null或必須有特定值的欄位)
		public void processOAuthPostLogin(String email,String oauth2ClientName,String username) {
			Member existMember = memberRepository.findByEmail(email);
			//如果帳號不存在則什麼也沒發生,successHandler那邊
			if(existMember == null) {
				Member newMember = new Member();
				newMember.setAccount(username);	//帳號其實也是,應該設成email,不然user根本不知道他有這組帳號
				newMember.setHashed_pwd(""); //pwd是not null一定要設,正確做法應該是帳密拉出去另外建表,這邊只能先寫死了
				//給Email
				newMember.setEmail(email);
				//給角色
//				Set<Role> roles = new HashSet<Role>();
//				roles.add(roleService.findById(3));
//				newMember.setRoles(roles);
				//給帳號狀態(啟用)
				newMember.setEnabled(true);
				//是從本地登入、還是透過GOOGLE或FB登入會寫在這欄
				newMember.setAuthType(AuthenticationType.valueOf(oauth2ClientName.toUpperCase()));
				
				memberRepository.save(newMember);
			}
	}
	//註冊時驗證帳號是否重複有用到
	public Member findByEmail(String email) {
		
		Member member = memberRepository.findByEmail(email);
		
		return member;
		
	}
	
	
	public Long getDataCount() {
		return memberRepository.count();
	}

}

