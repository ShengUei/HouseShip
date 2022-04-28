package com.grp4.houseship.house.controller;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseOffers;
import com.grp4.houseship.house.model.HouseRules;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        return "/ui/house/add-new-house-2";
    }

    @PostMapping(path = "/host/addnewhouse")
    public String addNewHouse(@ModelAttribute("houseInfo") HouseInfo houseInfo,
                              @RequestParam("city") String city,
                              @RequestParam("address") String address,
                              @RequestParam("photos") MultipartFile[] photos) {
        Member member = new Member();
        member.setAccount("admin");
        houseInfo.setMember(member);
        houseInfo.setH_address(city + address);
        houseService.insert(houseInfo);
        return "redirect:/house";
    }


}
