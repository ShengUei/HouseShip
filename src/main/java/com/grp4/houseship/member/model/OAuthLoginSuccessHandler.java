package com.grp4.houseship.member.model;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
@Component
public class OAuthLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired MemberService memberService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) throws ServletException, IOException {
		//登入成功後第一件事是把email和ClientName提供給processOAuthPostLogin,讓他註冊新的Member物件
		OAuth2UserImpl oauth2Member = (OAuth2UserImpl)authentication.getPrincipal();
		memberService.processOAuthPostLogin(oauth2Member.getEmail(),oauth2Member.getOauth2ClientName(),oauth2Member.getName());
		
		
		//取出代表當前使用者的principal物件,取得存在其內部的email,透過這個email找尋Member物件(前兩行剛註冊完),並存到session中
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		OAuth2UserImpl oauth2UserImpl = (OAuth2UserImpl) principal;
		Member member = memberService.findByEmail(oauth2UserImpl.getEmail());
		
		request.getSession().setAttribute("member", member);
		
		
		response.sendRedirect("/houseship/");
	}
}
