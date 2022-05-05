package com.grp4.houseship.member.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;
import com.grp4.houseship.member.model.Role;
import com.grp4.houseship.member.model.RoleService;
import com.grp4.houseship.member.model.UserDetailsImpl;


@Controller
@RequestMapping(path = "/ui/member")
public class UserMemberController {
	
	@Autowired
	MemberService memberService;
	@Autowired
	RoleService roleService;
	
	
	//進入註冊畫面
	@GetMapping(path="/register.controller")
	public String Register() {
		
		return "/ui/member/Register";
		
	}
	//接收user註冊資料(帳號、密碼、email),新增至資料庫
	@PostMapping(path="/register.controller")
	public String Register(Member member, Model model) {
		//如果資料庫沒有該帳號
        if(memberService.findByAccount(member.getAccount())==null) {
			//密碼加密
        	String bcEncode1 = new BCryptPasswordEncoder().encode(member.getHashed_pwd());
			member.setHashed_pwd(bcEncode1);
			//給予角色(user),3就是user角色的id
			Set<Role> roles = new HashSet<Role>();
			roles.add(roleService.findById(3));
			member.setRoles(roles);
			//存
			boolean statu = memberService.insert(member);
			
			if(statu==true) {	
				return "redirect:/home";
			}else {
				//待更改(如果註冊失敗要導到哪)
				return "/member/ErrorPage";
			}
        }else {
        	//待更改(如果已有該帳號要導到哪)
        	return "此帳號已被註冊";
        }
    }
	
	@GetMapping(path="/accountmanager.controller")
	public String accountManager() {
		
		return "/ui/member/accountManager";
		
	}
	//用來讓user填寫註冊時可以null的欄位
	@PostMapping(path="/accountmanager.controller")
	public String accountManager(@RequestParam("firstname") String firstname,
								 @RequestParam("lastname") String lastname,
								 @RequestParam("birthday") String birthday,
								 @RequestParam("phone") String phone,
								 @RequestParam("m_address") String m_address,
								 @RequestParam("mempic") String mempic,
								 HttpSession session){
		
		Object memberObject = session.getAttribute("member");
		if(memberObject != null) {
			if(memberObject instanceof Member) {
				Member member = (Member) memberObject;
					member.setFirstname(firstname);
					member.setLastname(lastname);
					member.setBirthday(birthday);
					member.setPhone(phone);
					member.setM_address(m_address);
					member.setMempic(mempic);
					memberService.update(member);
					
					return "redirect:/home";
				}
		}
		System.out.println("當前使用者為空");
		return "redirect:/home";
			
		
	 
		
		
		
	}

}
