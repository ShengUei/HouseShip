package com.grp4.houseship.member.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;
import com.grp4.houseship.member.model.Role;
import com.grp4.houseship.member.model.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import net.bytebuddy.utility.RandomString;


@Controller
//@RequestMapping(path = "/ui/member")
public class UserMemberController {
	
	@Autowired
	MemberService memberService;
	@Autowired
	RoleService roleService;
	@Autowired
	private JavaMailSender mailSender;
	
	//--------------------------------------------------以下四個方法組合使用(for註冊)--------------------------------------------
	
	//進入註冊畫面
	@GetMapping(path="/register")
	public String Register() {
		
		return "/ui/member/Register";
		
	}
	
	//檢驗註冊時帳號是否重複
	@PostMapping(path="/ui/member/checkduplicateaccount.controller")
	@ResponseBody
	//從ajax接收到的會是一個json字符串{account:值},把它轉成json物件再取其值
	public ResponseEntity<String> CheckDuplicateAccount(@RequestBody Member m) {
		if(memberService.findByAccount(m.getAccount()) != null) {	
			return new ResponseEntity<String>("Y", HttpStatus.OK);
		
		}else {
			
			return new ResponseEntity<String>("N", HttpStatus.OK);
		}
		
	}
	
	//檢驗註冊時email是否重複
		@PostMapping(path="/ui/member/checkduplicateemail.controller")
		@ResponseBody
		//從ajax接收到的會是一個json字符串{account:值},把它轉成json物件再取其值
		public ResponseEntity<String> CheckDuplicateEmail(@RequestBody Member m) {
			if(memberService.findByEmail(m.getEmail()) != null) {	
				return new ResponseEntity<String>("Y", HttpStatus.OK);
			
			}else {
				
				return new ResponseEntity<String>("N", HttpStatus.OK);
			}
			
		}
	
	//接收user註冊資料(帳號、密碼、email),新增至資料庫
	@PostMapping(path="/ui/member/register.controller")
	@ResponseBody
	public ResponseEntity<String> Register(@RequestBody Member member, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		//如果資料庫沒有該帳號
		System.out.println("123");
		System.out.println(member.getAccount()+"456456456456456456546");
        if(memberService.findByAccount(member.getAccount())==null) {
			//密碼加密
        	String bcEncode1 = new BCryptPasswordEncoder().encode(member.getHashed_pwd());
			member.setHashed_pwd(bcEncode1);
			//製作驗證信所需的驗證碼
			String randomCode = RandomString.make(64);
			//把驗證碼設定給member物件
		    member.setVerificationCode(randomCode);
		    //把member的enabled屬性設為停用(因為尚未驗證)
		    member.setEnabled(false);
			//給予角色(user),3就是user角色的id
			Set<Role> roles = new HashSet<Role>();
			roles.add(roleService.findById(3));
			member.setRoles(roles);
			//存
			boolean statu = memberService.register(member,getSiteURL(request));
			
			if(statu==true) {	
				return new ResponseEntity<String>("Y", HttpStatus.OK);
			}else {
				//待更改(如果註冊失敗要導到哪)
				return new ResponseEntity<String>("N", HttpStatus.OK);
			}
        }
//			else {
//        	//待更改(如果已有該帳號要導到哪)
//        	return "此帳號已被註冊";
//        }
		return null;
    }
	
	private String getSiteURL(HttpServletRequest request) {
		String siteURL = request.getRequestURL().toString();
		return siteURL.replace(request.getServletPath(), "");
	}
	//用來驗證註冊獲得的驗證信,code是透過點擊信中的連結送過來的
	@GetMapping(path="/ui/member/verifymember.controller")
	public String verifyMember(@Param("code") String code) {
		if(memberService.verify(code)) {
			
			return "redirect:/";
		
		}else {
			
			return "redirect:/";
		}
	}
	
	
	//---------------------------------------------以下三個方法組合使用(上傳可null資料,含頭像)----------------------------------------------------
	//註冊時可以null的欄位,在這裡可以補填
	//取得資料庫圖片路徑並把路徑不需要的部分去掉,再放入model帶到前台顯示
	@GetMapping(path="/member/myinfo")
	public String accountManager(HttpSession session,Model model) throws FileNotFoundException {
		Object memberObject = session.getAttribute("member");
		if (memberObject instanceof Member) {
			Member member = (Member)session.getAttribute("member");
			//添加Member物件屬性
			model.addAttribute("member", member);
			//添加圖片連結(沒有if的話,圖片欄位值為null的會員會無法透過這個controller方法進入頁面)
			if(member.getMempic() != null) {
				String path = member.getMempic();
//				String path = member.getMempic().replace(ResourceUtils.getURL("classpath:").getPath() + "static/images/member/","");
				
				model.addAttribute("picPath", path);	
				System.out.println(path);
			}
		}else {
			System.out.println("memberObject沒有Member型態,無法強轉");
		}
		return "/ui/member/accountManager";
		
	}
	//更新各項會員資料;呼叫CreatePathForFile(),目的是用圖片原始檔名製造路徑,並把圖片存到該路徑
	@PostMapping(path="/ui/member/accountmanager.controller")
	public String accountManager(@RequestParam("firstname") String firstname,
								 @RequestParam("lastname") String lastname,
								 @RequestParam("birthday") String birthday,
								 @RequestParam("phone") String phone,
								 @RequestParam("m_address") String m_address,
								 @RequestParam("mempic") MultipartFile mempic,
								 HttpSession session,
								 HttpServletRequest request,
								 Model model) throws IllegalStateException, IOException{
		
		Object memberObject = session.getAttribute("member");
		if(memberObject != null) {
			if(memberObject instanceof Member) {
				Member member = (Member) memberObject;
					member.setFirstname(firstname);
					member.setLastname(lastname);
					member.setBirthday(birthday);
					member.setPhone(phone);
					member.setM_address(m_address);
					//把MultipartFile mempic傳給CreatePathForFile()方法,它再傳一個檔案路徑回來,這邊再把路徑存進資料庫
					// 測試後路徑是C:\Users\Student\AppData\Local\Temp\tomcat-docbase.8080.13391970009719460972\\uploadTempDir
					member.setMempic(CreatePathForFile(mempic,request));
					
					memberService.update(member);
					System.out.println(member.getMempic());
					
					return "redirect:/";
				}
		}
		System.out.println("當前使用者為空");
		return "redirect:/";
			
	}
	//讓accountManager()方法調用的方法,用來把檔案user上傳的檔案(圖)存到本地端,再把檔案路徑傳回去給accountManager()
	private String CreatePathForFile(MultipartFile mempic,HttpServletRequest request) throws IllegalStateException, IOException {
		//取得原始檔名
		String fileName = String.format("%s\\%s.%s", "member", Instant.now().toEpochMilli(), mempic.getContentType().split("/")[1]);
		//String fileName = mempic.getOriginalFilename();
		//建立完整路徑
		String pathname = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\images\\" + fileName;
		System.out.println(pathname);
//		String saveTempFileDir = ResourceUtils.getURL("classpath:").getPath() + "static/images/member/" ;
		//根據完整路徑實際建立資料夾物件
		File file = new File(pathname);
		//File saveTempDirFile = new File(saveTempFileDir);
		//用資料夾物件實際建立資料夾
		file.mkdirs();
		//saveTempDirFile.mkdirs();
		//加上原始檔名,獲得完整路徑
//		String saveFilePath = saveTempFileDir + fileName;
		//根據完整路徑建立一個檔案物件(同時也建立實際檔案)
//		File saveFile = new File(saveFilePath);
		//把圖檔內容灌到檔案物件(實際檔案)中
		mempic.transferTo(file);
		
		
		return fileName; 
		
	}
	
	//------------------------------------------------以下五個方法組合使用(for 忘記密碼)------------------------------------------------
	//進入頁面
	@GetMapping("/forgotpassword")
    public String showForgotPasswordForm() {
		return "/ui/member/forgotPassword";
    }
	//接收mail,製造驗證碼並隨附在信件中,call方法sendResetPasswordEmail(email, resetPasswordLink)把信件寄出
    @PostMapping("/ui/member/forgotpassword.controller")
    @ResponseBody
    public ResponseEntity<String> processForgotPassword(/*@RequestParam("email") String email,*/HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
    	//取得user輸入的email
    	String email = request.getParameter("email");
    	System.out.println(email);
    	//隨機產生驗證碼
        String token = RandomString.make(30);
        
//        try {
        	//給參數,以便讓updateResetPasswordToken()將隨機驗證碼輸入到資料庫
            memberService.updateResetPasswordToken(token, email);
            //弄出讓用戶點擊的驗證網址
            String resetPasswordLink = getSiteURL(request) + "/resetpassword?token=" + token;
            System.out.println(getSiteURL(request));
            sendResetPasswordEmail(email, resetPasswordLink);
//            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
             
//        } catch (CustomerNotFoundException ex) {
//            model.addAttribute("error", ex.getMessage());
//        } catch (UnsupportedEncodingException | MessagingException e) {
//            model.addAttribute("error", "Error while sending email");
//        }
             
            return new ResponseEntity<String>("Y", HttpStatus.OK);
    }
    //信件在這裡寫
    public void sendResetPasswordEmail(String recipientEmail, String link) throws UnsupportedEncodingException, MessagingException{
    	 MimeMessage message = mailSender.createMimeMessage();              
    	    MimeMessageHelper helper = new MimeMessageHelper(message);
    	     
    	    helper.setFrom("eeit139.grp4@gmail.com", "HouseShip");
    	    helper.setTo(recipientEmail);
    	     
    	    String subject = "重設密碼驗證流程";
    	     
    	    String content = "<p>您好,</p>"
    	            + "<p>系統已收到您重設密碼的請求</p>"
    	            + "<p>點擊連結重設密碼:</p>"
    	            + "<p><a href=\"" + link + "\">重設密碼</a></p>"
    	            + "<br>"
    	            + "<p>若您已重設密碼或無此需求,請忽略這封信件</p>";
    	     
    	    helper.setSubject(subject);
    	     
    	    helper.setText(content, true);
    	     
    	    mailSender.send(message);
    }  
     
    //透過驗證碼取得會員entity,有取到表示會員已經通過驗證
    @GetMapping("/resetpassword")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
    	//透過用戶點擊連結後傳來的token,memberService會傳回資料庫中帶有這個token的物件
    	System.out.println(token);
    	Member member = memberService.getByResetPasswordToken(token);
    	//將token帶到重置密碼頁面
    	model.addAttribute("token", token);
    	
    	if (member == null) {
//            model.addAttribute("message", "Invalid Token");
//            return "message";
    		System.out.println("沒有成功透過token取得member物件");
        }
        
        return "ui/member/resetPassword";
    }
    //更新密碼
    @PostMapping("/ui/member/resetpassword.controller")
    @ResponseBody
    public ResponseEntity<String> processResetPassword(HttpServletRequest request, Model model) {
    	 //透過token再次取得member物件
    	 String token = request.getParameter("token");
    	 Member member = memberService.getByResetPasswordToken(token);
    	 //取得新密碼,並加密
    	 String newPassword = request.getParameter("password");
    	 BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
         String encodedPassword = passwordEncoder.encode(newPassword);
    	 
    	 model.addAttribute("title", "Reset your password");
    	 
    	 if (member == null) {
//    	        model.addAttribute("message", "Invalid Token");
//    	        return "message";
    	        System.out.println("沒有成功透過token取得member物件");
    	        return new ResponseEntity<String>("N", HttpStatus.OK);
    	 }else {           
    		 	member.setHashed_pwd(encodedPassword);
    		 	//完成驗證且更新密碼跑完整個流程後,就能把忘記密碼驗證碼了欄位值去掉
    	        member.setResetPasswordToken(null);
    	        boolean statu = memberService.update(member);
    	        if(statu) {
    	        	return new ResponseEntity<String>("Y", HttpStatus.OK);
    	        }
    	         
    	       // model.addAttribute("message", "You have successfully changed your password.");
    	 }
    	     
    	 		return new ResponseEntity<String>("N", HttpStatus.OK);
    }

}
