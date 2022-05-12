package com.grp4.houseship.member.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {
	

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private Member member;
	
	public UserDetailsImpl(Member member) {
		this.member = member;
	}
	
	@Override
	//用來取得當前使用者有哪些許可權(角色)
	public Collection<? extends GrantedAuthority> getAuthorities() {
		//透過hibernate的多對多映射,member bean給中介表user_id,進而找到對應的role物件(那一列資料)
		//找到的所有role物件會被裝進member的roles屬性(類型是set),我們再把它裝進這邊的roles變數
		Set<Role> roles = member.getRoles();
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		
		//遍歷roles裡的物件,並用role bean的getName()取得角色名稱,再封裝進authorities這個放user info的物件裡
		//結論是"放進系統的的是該位user的所有角色名稱"
		for(Role role:roles) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}
		
		return authorities;
	}
	
	@Override
	public String getPassword() {
		
		return member.getHashed_pwd();
	}
	
	@Override
	public String getUsername() {
		
		return member.getAccount();
	}
	
	@Override
	public boolean isAccountNonExpired() {
	
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		
		return member.isEnabled();
	}
	
}
