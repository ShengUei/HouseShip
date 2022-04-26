package com.grp4.houseship.house.controller;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseOffers;
import com.grp4.houseship.house.model.HouseRules;
import com.grp4.houseship.house.model.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/house")
public class HouseUserInterfaceController {

    @Autowired
    private HouseService houseService;

    @GetMapping(path = "/housedetails/{houseid}")
    public String houseDetails(@PathVariable("houseid") int houseid, Model model) {
        HouseInfo houseInfo = houseService.searchById(houseid);
        model.addAttribute("houseInfo", houseInfo);
        return "/ui/house/house-details";
    }

    @GetMapping(path = "/host/addnewhouse")
    public String addNewhouse(Model model) {
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setH_type(1);
        model.addAttribute("houseInfo", houseInfo);
        return "/ui/house/add-new-house";
    }


}
