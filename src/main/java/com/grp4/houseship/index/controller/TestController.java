package com.grp4.houseship.index.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class TestController {
	
	//先獲取一組加密密碼存到資料庫以便第一次登入
	//要先把 WebSecurityConfig 內的 .anyRequest().authenticated() 註釋掉
	//在http://localhost:8081/HouseShip/test這個url的browser畫面取得加密
	//之後每次用同樣的明文密碼和http://localhost:8081/HouseShip/test生成的隨機加密密碼都不一樣,因為系統有自動加鹽值
	//至於鹽值他是怎麼保存進而去驗證就不知道了他是怎麼做的
	//流程就是把hashedPwd這個生成的密碼存進資料庫,再用bb1227這個原文密碼就能登入
		@RequestMapping("/test")
		
		public String encrypt() {
			//bb1227可以改成自己的明文密碼(之後資料庫就看不到明文密碼用自己的比較容易記得)
			String hashedPwd = new BCryptPasswordEncoder().encode("P@ssw0rd");

			return hashedPwd;
		}
}
