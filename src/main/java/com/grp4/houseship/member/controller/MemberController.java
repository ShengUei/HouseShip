package com.grp4.houseship.member.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.member.model.MemberService;
import com.grp4.houseship.member.model.Role;
import com.grp4.houseship.member.model.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping(path = "/admin/member")
public class MemberController {
	
	@Autowired
	MemberService memberService;
	@Autowired
	RoleService roleService;
	
	
	//-----------------------------------------從這邊開始是後台--------------------------------------
//	@GetMapping
//	public String searchAllMemberMain() {
//	
//		return "/admin/member/member_viewAll";
//		
//	}

	//改用ajax寫
	@GetMapping/*(path="/findall.controller")*/
//	@ResponseBody
	public String searchAllMember(Model model) {
		
		model.addAttribute("memberList", memberService.findAll());
		
		return "/admin/member/member_viewAll";
	}
	
	@GetMapping(path = "/insertmember.controller")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewHouseForm(Member member, Model model) {
        
		
        return "/admin/member/InsertMemberData";
    }
	
	@PostMapping(path = "/insertmember.controller")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addNewHouse(Member member, Model model) {
		//如果資料庫沒有該帳號
        if(memberService.findByAccount(member.getAccount())==null) {
			//加密存進dB
        	String bcEncode1 = new BCryptPasswordEncoder().encode(member.getHashed_pwd());
			member.setHashed_pwd(bcEncode1);
			//enabled帳號
			member.setEnabled(true);
			//給定角色
			Set<Role> roles = new HashSet<Role>();
			roles.add(roleService.findById(3));
			roles.add(roleService.findById(2));
			member.setRoles(roles);
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
		@PreAuthorize("hasRole('ROLE_ADMIN')")
	    public String updateMemberConfirm(@PathVariable("memberaccount") String account, Model model) {
	        //叫出每一列的role物件
			
	        List<Role> roleList = roleService.findAll();
	        model.addAttribute("roleList", roleList);
	        System.out.println(memberService.findByAccount(account).isEnabled()+"123456");
	        model.addAttribute("member", memberService.findByAccount(account));
	        model.addAttribute("account", account);
	        return "/admin/member/UpdateMemberData";
	    }
		//維持原本form:form寫法
	    @PostMapping(path = "/updatemember.controller/{memberaccount}")
		@PreAuthorize("hasRole('ROLE_ADMIN')")
	    public String updateMember(@PathVariable("memberaccount") String account,@ModelAttribute("member") Member member) {
	        System.out.println("============Go to Update=============");
	        System.out.println(member.getAccount()+" "+member.getUser_id()+" "+member.getBirthday());
	        //update匯進來的物件沒有user_id(表單沒這欄),所以要透過account去資料庫撈user_id
	        member.setUser_id(memberService.findByAccount(member.getAccount()).getUser_id());
	        System.out.println(member.isEnabled()+"前");
	        Boolean status = memberService.update(member);
	        System.out.println(member.isEnabled()+"後");
	        return "redirect:/admin/member";
	    }
	   
	    @GetMapping(path = "/deletemember.controller/{memberaccount}")
		@PreAuthorize("hasRole('ROLE_ADMIN')")
	    public String deleteMemberConfirm(@PathVariable("memberaccount") String account, Model model) {
	    	List<Role> roleList = roleService.findAll();
		    model.addAttribute("roleList", roleList);
	        model.addAttribute("member", memberService.findByAccount(account));
	        model.addAttribute("account", account);
	        return "/admin/member/DeleteByAccount";
	    }
	   
	    @PostMapping(path = "/deletemember.controller/{memberaccount}")
		@PreAuthorize("hasRole('ROLE_ADMIN')")
	    public String deleteMember(@PathVariable("memberaccount") String account,@ModelAttribute("member") Member member ) {
	        member.setEnabled(false);
	        member.setUser_id(memberService.findByAccount(member.getAccount()).getUser_id());
	        
	        memberService.update(member);
	        return "redirect:/admin/member";
	    }
	    
	    @GetMapping(path = "/restoremember.controller/{memberaccount}")
		@PreAuthorize("hasRole('ROLE_ADMIN')")
	    public String restoreAccount(@PathVariable("memberaccount") String account, Model model) {
	    	List<Role> roleList = roleService.findAll();
		    model.addAttribute("roleList", roleList);
	        model.addAttribute("member", memberService.findByAccount(account));
	        model.addAttribute("account", account);
	        return "/admin/member/restoreAccount";
	    }
	   
	    @PostMapping(path = "/restoremember.controller/{memberaccount}")
		@PreAuthorize("hasRole('ROLE_ADMIN')")
	    public String restoreAccount(@PathVariable("memberaccount") String account,@ModelAttribute("member") Member member ) {
	        member.setEnabled(true);
	        member.setUser_id(memberService.findByAccount(member.getAccount()).getUser_id());
	        
	        memberService.update(member);
	        return "redirect:/admin/member";
	    }
	    
//	    private void sendEmailForSuspension(Member member, String siteURL) throws UnsupportedEncodingException, MessagingException {
//			String toAddress = member.getEmail();
//		    String fromAddress = "eeit139.grp4@gmail.com";
//		    String senderName = "HouseShip";
//		    String subject = "您的帳號已被停權";
//		    String content = "親愛的使用者,<br>"
//		            + "基於某種原因,您的Houseship帳號已被停權<br>"
//		            + "若您使用<br>"
//		            + "感謝您,<br>"
//		            + "HouseShip.";
//		    
//		    MimeMessage message = mailSender.createMimeMessage();
//		    MimeMessageHelper helper = new MimeMessageHelper(message);
//		     
//		    helper.setFrom(fromAddress, senderName);
//		    //在這邊設定用戶email
//		    helper.setTo(toAddress);
//		    helper.setSubject(subject);
//		     
////		    content = content.replace("[[name]]", member.getFirstname()+member.getLastname());
//		    
//		     
//		    content = content.replace("[[URL]]", verifyURL);
//		     
//		    helper.setText(content, true);
//		     
//		    mailSender.send(message);
//		}
	
	
	
	
	
	
	
	
	

}
