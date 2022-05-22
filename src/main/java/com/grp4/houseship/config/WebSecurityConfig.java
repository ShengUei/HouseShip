package com.grp4.houseship.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.grp4.houseship.member.model.CustomAccessDeniedHandler;
import com.grp4.houseship.member.model.LoginSuccessHandler;
import com.grp4.houseship.member.model.OAuthLoginSuccessHandler;
import com.grp4.houseship.member.model.OAuthMemberService;
import com.grp4.houseship.member.model.UserDetailsServiceImpl;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	//private UserDetailsServiceImpl userDetailsServiceImpl;
	
	 @Bean
	 public UserDetailsService userDetailsService() {
	        return new UserDetailsServiceImpl();
	 }
	 
	 @Bean
	 public BCryptPasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	 }
	 
	 @Bean
	 public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());
	         
	        return authProvider;
	 }
	 @Bean
	 public AccessDeniedHandler accessDeniedHandler(){
	     return new CustomAccessDeniedHandler();
	 }
	 
	 @Autowired
	 LoginSuccessHandler loginSuccessHandler;
	//for第三方登入用
	 @Autowired
	 OAuthMemberService oauthMemberService;
	//for第三方登入用
	 @Autowired
	 OAuthLoginSuccessHandler oauth2LoginSuccessHandler;
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
//		auth
//		.userDetailsService(userDetailsServiceImpl)
//		.passwordEncoder(new BCryptPasswordEncoder());
	
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		
	}
	//設定
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		//for第三方登入用
		.antMatchers("/oauth/**").permitAll()
//		.antMatchers(HttpMethod.GET,"/home/**").authenticated()
		.antMatchers(HttpMethod.GET,"/admin/member/**").hasAnyAuthority("ROLE_ADMIN","ROLE_STAFF")
		.antMatchers(HttpMethod.GET,"/ui/member/accountmanager.controller").hasAuthority("ROLE_USER")
//		.antMatchers(HttpMethod.GET,"/house/**").authenticated()
//		.antMatchers(HttpMethod.GET,"/order/**").authenticated()
//		.antMatchers(HttpMethod.GET,"/forum/**").authenticated()
		.antMatchers(HttpMethod.GET).permitAll()
//		.antMatchers(HttpMethod.POST,"/home/**").authenticated()
		.antMatchers(HttpMethod.POST,"/admin/member/**").hasAuthority("ROLE_ADMIN")
		.antMatchers(HttpMethod.POST,"/ui/member/accountmanager.controller").hasAuthority("ROLE_USER")
//		.antMatchers(HttpMethod.POST,"/house/**").authenticated()
//		.antMatchers(HttpMethod.POST,"/order/**").authenticated()
//		.antMatchers(HttpMethod.POST,"/forum/**").authenticated()
		.antMatchers(HttpMethod.POST).permitAll()
//		.anyRequest().authenticated()
		.and()
		.rememberMe().tokenValiditySeconds(86400).key("rememberMe-key")
		.and()
		.csrf().disable()
		.headers().frameOptions().sameOrigin()
		.and()
		//signIn.html的action必須是"/houseship/signinPage",spring security好像才能順利驗證
		.formLogin()
			.loginPage("/signinPage")
		//.defaultSuccessUrl("/welcomePage") 用下面的handler代替了
				.successHandler(loginSuccessHandler) //上面有為了這邊多一個@Autowired
				.and()
				.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
		.and()
		//for第三方登入用
		.oauth2Login()
			.loginPage("/signinPage")
				.userInfoEndpoint()
					.userService(oauthMemberService)
					.and()
					.successHandler(oauth2LoginSuccessHandler)
		.and()
		.logout()
		.logoutSuccessUrl("/");
	}
	
	
}
