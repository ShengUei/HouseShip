package com.grp4.houseship.house.controller;

import com.grp4.houseship.house.model.AdvancedSearchModel;
import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import com.grp4.houseship.member.model.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping(path = "/house")
public class HouseUserInterfaceController {

    @Autowired
    private HouseService houseService;
    
    @GetMapping(path = "/search")
    public String search() {
        return "/ui/house/searchResults";
    }

    @GetMapping(path = "/api/search-result")
    @ResponseBody
    public ResponseEntity<List<HouseInfo>> searchAllHouses() {
        List<HouseInfo> houseList = houseService.searchAll();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if(houseList.isEmpty()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (houseList, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(path = "/api/advanced-search-result")
    @ResponseBody
    public ResponseEntity<List<HouseInfo>> advancedSearchAllHouses(@RequestBody AdvancedSearchModel advancedSearchModel) {
        List<HouseInfo> houseList;
        if(advancedSearchModel.isGreaterPrice() == true) {
            houseList = houseService.findByPriceGreaterThan(3000);
        }else {
            Double[] priceZone = advancedSearchModel.getPriceZone();
            houseList = houseService.findByPriceBetween(priceZone[0], priceZone[1]);
        }

        Iterator<HouseInfo> iterator = houseList.iterator();

        while (iterator.hasNext()) {
            HouseInfo next = iterator.next();
            if (advancedSearchModel.getHouseType() != 0) {
                if(next.getH_type() != advancedSearchModel.getHouseType()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseOffers().containsKey("wifi")) {
                if(!next.getHouseOffers().isWifi()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseOffers().containsKey("tv")) {
                if(!next.getHouseOffers().isTv()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseOffers().containsKey("refrigerator")) {
                if(!next.getHouseOffers().isRefrigerator()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseOffers().containsKey("aircon")) {
                if(!next.getHouseOffers().isAircon()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseOffers().containsKey("microwave")) {
                if(!next.getHouseOffers().isMicrowave()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseOffers().containsKey("kitchen")) {
                if(!next.getHouseOffers().isKitchen()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseOffers().containsKey("washer")) {
                if(!next.getHouseOffers().isWasher()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseRules().containsKey("smoking")) {
                if(next.getHouseRules().isSmoking()) {
                    iterator.remove();
                    continue;
                }
            }
            if(advancedSearchModel.getHouseRules().containsKey("pet")) {
                if(!next.getHouseRules().isPet()) {
                    iterator.remove();
                    continue;
                }
            }
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if(houseList.isEmpty()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (houseList, responseHeaders, HttpStatus.OK);
    }

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
        houseInfo.setH_price(100);
        model.addAttribute("houseInfo", houseInfo);
        return "/ui/house/add-new-house";
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
