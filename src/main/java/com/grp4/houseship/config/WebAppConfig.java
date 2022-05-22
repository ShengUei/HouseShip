package com.grp4.houseship.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/signinPage").setViewName("ui/member/signIn");
//		registry.addViewController("/welcomePage").setViewName("index");
//		registry.addViewController("/logout").setViewName("loginResult");
		//搭配script.js內的"SignOut"
		registry.addViewController("/SignOut").setViewName("logout");
		registry.addViewController("/403").setViewName("ui/member/403page");
	}
	
	//資料夾名字都小寫
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//讓被放在WEB-INF下的資源能直接被讀取,不然系統預設WEB-INF下的會擋
		//  "/WEB-INF/"resources/css/表示路徑位於8081/WEB-INF/resources/css/的檔案都能讀(有contextpath則在加一層)
		//  "/css/**"表示url如果是8081/css/底下的都能顯示
//		registry.addResourceHandler("/css/**").addResourceLocations("/WEB-INF/resources/css/");
//		registry.addResourceHandler("/js/**").addResourceLocations("/WEB-INF/resources/js/");
		
	}
    
}
