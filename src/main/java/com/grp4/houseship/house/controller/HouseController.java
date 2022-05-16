package com.grp4.houseship.house.controller;

import com.grp4.houseship.email.service.EmailService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.Member;

import javax.mail.MessagingException;


@Controller
@RequestMapping(path = "/admin/house")
public class HouseController {

    @Autowired
    private HouseService houseServiece;

    @Autowired
    private EmailService emailService;

    @GetMapping
    public String searchAllHouse(Model model) {
        model.addAttribute("houseInfoList", houseServiece.searchAll());
        return "/admin/house/HouseInfo";
    }
    
    @GetMapping (path = "/housedetail/{houseid}")
    public String searchHouseDetail(@PathVariable("houseid") int houseid, Model model) {
        model.addAttribute("houseInfo", houseServiece.searchById(houseid));
        return "/admin/house/HouseDetail";
    }

    @GetMapping(path = "/addnewhouse")
    public String addNewHouseForm(Model model) {
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setH_price(1);
        houseInfo.setH_type(1);
        model.addAttribute("houseInfo", houseInfo);
        return "/admin/house/CreateHouseInfo";
    }

    @PostMapping(path = "/addnewhouse")
    public String addNewHouse(@ModelAttribute("houseInfo") HouseInfo houseInfo) {
        Member member = new Member();
        member.setAccount("admin");
        houseInfo.setMember(member);
        houseServiece.insert(houseInfo);
        return "redirect:/house";
    }

    @GetMapping (path = "/updatehouse/{houseid}")
    public String updateHouseConfirm(@PathVariable("houseid") int houseid, Model model) {
        HouseInfo houseInfo = houseServiece.searchById(houseid);
        model.addAttribute("houseInfo", houseInfo);
        return "/admin/house/UpdateHouseInfo";
    }

    @PostMapping(path = "/updatehouse/{houseid}")
    public String updateHouse(@PathVariable("houseid") int houseid, @ModelAttribute("houseInfo") HouseInfo houseInfo) {
    	Member member = new Member();
        member.setAccount("admin");
        houseInfo.setMember(member);
        houseInfo.setHouseNo(houseid);
        houseServiece.update(houseid, houseInfo);
        return "redirect:/house";
    }

    @GetMapping(path = "/deletehouse/{houseid}")
    public String deleteHouseConfirm(@PathVariable("houseid") int houseid, Model model) {
        model.addAttribute("houseInfo", houseServiece.searchById(houseid));
        return "/admin/house/DeleteHouseInfo";
    }

    @PostMapping(path = "/deletehouse/{houseid}")
    public ResponseEntity<String> deleteHouse(@PathVariable("houseid") int houseid) {
        HouseInfo houseInfo = houseServiece.searchById(houseid);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        boolean deleteStatue = houseServiece.delete(houseid);
        if (deleteStatue) {
            String title = "親愛的: " + houseInfo.getMember().getFirstname() + houseInfo.getMember().getLastname() + " 您好\n"
                    + "您在 HouseShip 上刊登的 '"+ houseInfo.getH_title() + "' 房屋已遭系統刪除";
            try {
                emailService.sendHouseMail(houseInfo, "ui/house/email_delete_house_success", title);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return new ResponseEntity<>("{\"message\": \"刪除成功\"}", responseHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"message\": \"刪除失敗\"}", responseHeaders, HttpStatus.BAD_REQUEST);
    }

}
