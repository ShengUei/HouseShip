package com.grp4.houseship.index.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.grp4.houseship.forum.model.ForumService;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.MemberService;
import com.grp4.houseship.order.model.OrderService;

@Controller
public class IndexController {
	
	@Autowired
	private MemberService memberService;

    @Autowired
    private HouseService houseService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private ForumService forumservice;
    
    @GetMapping(path = {"/"})
    public String signIn() {
    	return "signIn";
    }

    @GetMapping(path = {"/home", "/welcomePage"})
    public String home(Model model) {
        model.addAttribute("memberNumOfData", memberService.getDataCount());
        model.addAttribute("houseNumOfData", houseService.getDataCount());
        model.addAttribute("orderNumOfData", orderService.getDataCount());
        model.addAttribute("forumNumOfData", forumservice.getDataCount());
        return "index";
    }
}
