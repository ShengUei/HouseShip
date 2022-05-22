package com.grp4.houseship.house.controller;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.house.model.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoomSearchController {

    @Autowired
    private HouseService houseService;

    @GetMapping(path = "/checkAvailable/{date1}/{date2}/{houseNo}")
    @ResponseBody
    public HouseInfo searchByDate(@PathVariable("date1") String date1, @PathVariable("date2") String date2, @PathVariable("houseNo") int houseNo) {

        HouseInfo houseInfo = houseService.findByDateAndHouseNo(date1, date2, houseNo);
        return houseInfo;
    }

}
