package com.grp4.houseship.house.controller;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/house")
public class HouseUserInterfaceController {

    @Autowired
    private HouseService houseService;

    @GetMapping(path = "house-details")
    public String houseDetails(Model model) {
        HouseInfo houseInfo = houseService.searchById(2);
        model.addAttribute("houseInfo", houseInfo);
        return "/ui/house/house-details";
    }


}
