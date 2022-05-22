package com.grp4.houseship.member.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OAuthMemberService extends DefaultOAuth2UserService {
	
	@Autowired
	MemberService memberService;
	
	@Override
	//用戶選擇第三方登入後,用戶輸入提出的請求就是參數裡的userRequest,在此之前用戶應該已經跟第三方驗證完,取得google給的token
	//透過這個token我們就能跟google要資料,是這樣嗎?zzz
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		//
		//Member member = memberService.findByEmail();
		//透過userRequest參數能取得ClientName
		String clientName = userRequest.getClientRegistration().getClientName();
		//利用繼承來的方法和userRequest參數能取得代表用戶的OAuth2User物件
        OAuth2User user =  super.loadUser(userRequest);
        //用取得的用戶物件和用戶名new一個OAuth2UserImpl傳給系統,我們就能以OAuth2UserImpl的方法去跟google要資料?
        //同時也會建立這個user的Authentication和principle?
        return new OAuth2UserImpl(user, clientName);
	}
}
