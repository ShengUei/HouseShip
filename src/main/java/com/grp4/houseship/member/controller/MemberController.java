package com.grp4.houseship.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;

@Controller

@RequestMapping(path = "/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;
	
	
	@GetMapping
	public String searchAllMemberMain(Model model) {
		
		
		return "/member/member_viewAll";
		
	}
	
	//改用ajax寫
	@GetMapping(path="/findall.controller")
	@ResponseBody
	public List<Member> searchAllMember(Model model) {
		
		
		return memberService.findAll();
	}
	
	@GetMapping(path = "/insertmember.controller")
    public String addNewHouseForm(Member member, Model model) {
        
		
        return "/member/InsertMemberData";
    }
	//新增加密步驟
	@PostMapping(path = "/insertmember.controller")
    public String addNewHouse(Member member, Model model) {
		//如果資料庫沒有該帳號
        if(memberService.findByAccount(member.getAccount())==null) {
			//加密存進dB
//        	String bcEncode1 = new BCryptPasswordEncoder().encode(member.getHashed_pwd());
        	String bcEncode1 = "123456789";
			member.setHashed_pwd(bcEncode1);
			boolean statu = memberService.insert(member);
			
			if(statu==true) {	
				return "redirect:/member";
			}else {
				return "/member/ErrorPage";
			}
        }else {
        	return "此新增帳號已被註冊";
        }
    }
		
		@GetMapping (path = "/updatemember.controller/{memberaccount}")
	    public String updateMemberConfirm(@PathVariable("memberaccount") String account, Model model) {
	        model.addAttribute("member", memberService.findByAccount(account));
	        model.addAttribute("account", account);        
	        return "/member/UpdateMemberData";
	    }
		//維持原本form:form寫法
	    @PostMapping(path = "/updatemember.controller/{memberaccount}")
	    public String updateMember(@PathVariable("memberaccount") String account,@ModelAttribute("member") Member member) {
	        System.out.println("============Go to Update=============");
	        Boolean status = memberService.update(member);
	        System.out.println(status);
	        return "redirect:/member";
	    }
	   
	    @GetMapping(path = "/deletemember.controller/{memberaccount}")
	    public String deleteMemberConfirm(@PathVariable("memberaccount") String account, Model model) {
	        model.addAttribute("member", memberService.findByAccount(account));
	        model.addAttribute("account", account);
	        return "/member/DeleteByAccount";
	    }
	   
	    @PostMapping(path = "/deletemember.controller/{memberaccount}")
	    public String deleteMember(@PathVariable("memberaccount") String account,@ModelAttribute("member") Member member ) {
	        memberService.delete(account);
	        return "redirect:/member";
	    }
	
	
	
	
	
	
	
	
	

}
