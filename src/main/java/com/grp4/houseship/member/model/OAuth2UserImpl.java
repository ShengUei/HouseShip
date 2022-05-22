package com.grp4.houseship.member.model;


import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

//這個類用來存---OAuth2memberService跟Google要來的資料
public class OAuth2UserImpl implements OAuth2User {

	 private String oauth2ClientName;
	 private OAuth2User oauth2User;
	 
	 @Autowired
	 RoleService roleService;
	 
	 public OAuth2UserImpl(OAuth2User oauth2User, String oauth2ClientName) {
	       this.oauth2User = oauth2User;
	       this.oauth2ClientName = oauth2ClientName;
	}
	
	@Override
	public Map<String, Object> getAttributes() {
		 return oauth2User.getAttributes();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
		//在這邊直接給定角色,不然系統預設的角色跟我自定義的腳色不同,有擋的頁面會進不去
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		return authorities;
		//roles.add(roleService.findById(3));	 
	}

	@Override
	public String getName() {
        return oauth2User.getAttribute("name");
	}
	
	public String getEmail() {
        return oauth2User.<String>getAttribute("email");     
    }
 
    public String getOauth2ClientName() {
        return this.oauth2ClientName;
    }

    
    

}
