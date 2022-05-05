package com.grp4.houseship.member.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.grp4.houseship.member.model.Role;
import com.grp4.houseship.member.model.RoleService;

@Controller

@RequestMapping(path = "/admin/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;
	@Autowired
	RoleService roleService;
	
	
	//-----------------------------------------從這邊開始是後台--------------------------------------
	@GetMapping
	public String searchAllMemberMain() {
	
		return "/admin/member/member_viewAll";
		
	}
	
	//改用ajax寫
	@GetMapping(path="/findall.controller")
	@ResponseBody
	public List<Member> searchAllMember(Model model) {
		
		return memberService.findAll();
	}
	
	@GetMapping(path = "/insertmember.controller")
    public String addNewHouseForm(Member member, Model model) {
        
		
        return "/admin/member/InsertMemberData";
    }
	
	@PostMapping(path = "/insertmember.controller")
    public String addNewHouse(Member member, Model model) {
		//如果資料庫沒有該帳號
        if(memberService.findByAccount(member.getAccount())==null) {
			//加密存進dB
        	String bcEncode1 = new BCryptPasswordEncoder().encode(member.getHashed_pwd());
			member.setHashed_pwd(bcEncode1);
			boolean statu = memberService.insert(member);
			
			if(statu==true) {	
				return "redirect:/admin/member";
			}else {
				//待更改(如果新增失敗要導到哪)
				return "/admin/member/ErrorPage";
			}
        }else {
        	//待更改(如果已經有該帳號的物件要導到哪)
        	return "此新增帳號已被註冊";
        }
    }
		
		@GetMapping (path = "/updatemember.controller/{memberaccount}")
	    public String updateMemberConfirm(@PathVariable("memberaccount") String account, Model model) {
	        //叫出每一列的role物件
	        List<Role> roleList = roleService.findAll();
	        model.addAttribute("roleList", roleList);
	        model.addAttribute("member", memberService.findByAccount(account));
	        model.addAttribute("account", account);
	        System.out.println("here here here here here here here here here ");
	        return "/admin/member/UpdateMemberData";
	    }
		//維持原本form:form寫法
	    @PostMapping(path = "/updatemember.controller/{memberaccount}")
	    public String updateMember(@PathVariable("memberaccount") String account,@ModelAttribute("member") Member member) {
	        System.out.println("============Go to Update=============");
	        System.out.println(member.getAccount()+" "+member.getUser_id()+" "+member.getBirthday());
	        //update匯進來的物件沒有user_id(表單沒這欄),所以要透過account去資料庫撈user_id
	        member.setUser_id(memberService.findByAccount(member.getAccount()).getUser_id());
	        Boolean status = memberService.update(member);
	        System.out.println(status);
	        return "redirect:/admin/member";
	    }
	   
	    @GetMapping(path = "/deletemember.controller/{memberaccount}")
	    public String deleteMemberConfirm(@PathVariable("memberaccount") String account, Model model) {
	        model.addAttribute("member", memberService.findByAccount(account));
	        model.addAttribute("account", account);
	        return "/admin/member/DeleteByAccount";
	    }
	   
	    @PostMapping(path = "/deletemember.controller/{memberaccount}")
	    public String deleteMember(@PathVariable("memberaccount") String account,@ModelAttribute("member") Member member ) {
	        memberService.delete(account);
	        return "redirect:/admin/member";
	    }
	
	
	
	
	
	
	
	
	

}
