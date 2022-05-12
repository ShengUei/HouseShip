package com.grp4.houseship.member.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

import org.springframework.stereotype.Component;


//目前做到能按角色導入前台或後台首頁,但user原始請求的頁面就回不去了(被強制轉到登入頁面)
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	//protected final Log logger = LogFactory.getLog(this.getClass());

	//private RequestCache requestCache = new HttpSessionRequestCache();
	
	@Autowired
	MemberService memberService;
	
	//登入成功的話執行這個方法
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {
		
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		
		//成功登入還會有principal == null的可能性?有空弄懂
		if(principal != null && principal instanceof UserDetailsImpl) {
			
			//存放當前使用者物件到session中
			UserDetailsImpl userDetailsImpl = (UserDetailsImpl) principal;
			Member member = memberService.findByAccount(userDetailsImpl.getUsername());
			request.getSession().setAttribute("member", member);
			request.getSession().setAttribute("mempicPath",member.getMempic());
			System.out.println(member.getAccount());
		
		
			
			if(member.getRoles() instanceof Set) {
				
				Set<Role> roles = (Set<Role>)member.getRoles();
				//如果管理員有其他多的角色,沒設flag的話,因為無序可能會導到user首頁去(next()表示取第一個元素)
				Boolean flag = false;
				if(roles.size()>1) {
					flag = true;
				}
				String redirectURL = request.getContextPath();
				//有staff或admin角色就導到後台首頁
				for(Role role : roles) {
					if(role.toString().equals("ROLE_ADMIN")) {
						redirectURL += "/admin/home";
						response.sendRedirect(redirectURL);
						break;
					}else if(role.toString().equals("ROLE_STAFF")) {
						redirectURL += "/admin/home";
						response.sendRedirect(redirectURL);	
						break;
						//super.onAuthenticationSuccess(request, response, authentication);
					//沒有staff或admin角色就導到一般首頁
					}else if(role.toString().equals("ROLE_USER")){
							if(flag) {
								redirectURL += "/admin/home";
								response.sendRedirect(redirectURL);	
								break;
							}else {
								redirectURL += "/home";
								response.sendRedirect(redirectURL);
							}
							
					}else {
						System.out.println("沒有角色還能成功登入會員那就是bug,之後再看怎麼處理");
					}
				}
			}else {
				System.out.println("member.getRoles()沒有Set型態,無法強轉");
			}

		}
	}
}
	

