package com.grp4.houseship.house.controller;

import com.grp4.houseship.house.model.*;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.order.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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

    @GetMapping(path = "/search/{city}")
    public String searchByCity(@PathVariable("city") String city, HttpSession session) {
        session.setAttribute("locationCity", city);
        return "/ui/house/searchResults";
    }

    @GetMapping(path = "/api/search-result")
    @ResponseBody
    public ResponseEntity<List<HouseInfo>> searchAllHouses(HttpSession session) {
        List<HouseInfo> houseList;
        String locationCity = (String) session.getAttribute("locationCity");
        if(locationCity != null) {
            houseList = houseService.searchAllByCity(locationCity);
        } else {
            houseList = houseService.searchAll();
        }
        session.removeAttribute("locationCity");
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
        StringBuilder sb = new StringBuilder();

        if(advancedSearchModel.isGreaterPrice()) {
            sb.append("i.h_price > 3000");
        }else {
            Double[] priceZone = advancedSearchModel.getPriceZone();
            sb.append("i.h_price BETWEEN ").append(priceZone[0]).append(" AND ").append(priceZone[1]);
        }

        if (advancedSearchModel.getHouseType() != 0) {
            sb.append(" AND i.h_type = ").append(advancedSearchModel.getHouseType());
        }
        if(advancedSearchModel.getHouseOffers().containsKey("wifi")) {
            sb.append(" AND o.wifi = 1");
        }
        if(advancedSearchModel.getHouseOffers().containsKey("tv")) {
            sb.append(" AND o.tv = 1");
        }
        if(advancedSearchModel.getHouseOffers().containsKey("refrigerator")) {
            sb.append(" AND o.refrigerator = 1");
        }
        if(advancedSearchModel.getHouseOffers().containsKey("aircon")) {
            sb.append(" AND o.aircon = 1");
        }
        if(advancedSearchModel.getHouseOffers().containsKey("microwave")) {
            sb.append(" AND o.microwave = 1");
        }
        if(advancedSearchModel.getHouseOffers().containsKey("kitchen")) {
            sb.append(" AND o.microwave = 1");
        }
        if(advancedSearchModel.getHouseOffers().containsKey("washer")) {
            sb.append(" AND o.microwave = 1");
        }

        if(advancedSearchModel.getHouseRules().containsKey("smoking")) {
            sb.append(" AND r.smoking = 0");
        }
        if(advancedSearchModel.getHouseRules().containsKey("pet")) {
            sb.append(" AND r.pet = 1");
        }

        houseList = houseService.advanceSearch(sb.toString());

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
        if(houseInfo != null) {
            model.addAttribute("houseInfo", houseInfo);
            OrderDetail orderDetail = new OrderDetail(houseInfo);
            model.addAttribute("orderDetail", orderDetail);
        } else {
            model.addAttribute("errMsg", "查無資料");
        }
        return "/ui/house/house-details";
    }

    @GetMapping(path = "/host/ownedhouse")
    public String ownedHouse(HttpSession session, Model model) {
        Member member = new Member();
        member.setAccount("admin");

        model.addAttribute("ownedList", houseService.searchByAccount(member));
        return "/ui/house/ownedhouse";
    }

    @GetMapping(path = "/host/addnewhouse")
    public String addNewHousePage(Model model) {
        HouseInfo houseInfo = new HouseInfo();
        houseInfo.setH_type(1);
        houseInfo.setH_price(100);
        HouseRules houseRules = new HouseRules();
        try {
            houseRules.setCheck_in(new SimpleDateFormat("HH:mm").parse("15:00"));
            houseRules.setCheck_out(new SimpleDateFormat("HH:mm").parse("11:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        houseInfo.setHouseRules(houseRules);
        model.addAttribute("houseInfo", houseInfo);
        return "/ui/house/add-new-house";
    }

    @PostMapping(path = "/host/addnewhouse")
    public String addNewHouse(Model model,
                            @ModelAttribute("houseInfo") HouseInfo houseInfo,
                            @RequestParam("photos") MultipartFile[] photos) {
        Member member = new Member();
        member.setUser_id(1);
        member.setAccount("admin");
        houseInfo.setMember(member);
        houseInfo.setStatus(true);

        List<HousePhotos> photosList = savePhoto(model, photos);

        if(photosList == null) {
            return "/ui/house/add-new-house";
        }

        houseInfo.setHousePhotos(photosList);

        boolean insertStatue = houseService.insert(houseInfo);
        if(insertStatue) {
            return "redirect:/house/host/ownedhouse";
        }
        model.addAttribute("errMsg", "新增失敗");
        return "/ui/house/add-new-house";
    }

    @GetMapping(path = "/host/updatehouse/{houseid}")
    public String updateHousePage(@PathVariable("houseid") int houseid, Model model, HttpSession session) {
        HouseInfo houseInfo = houseService.searchById(houseid);
        session.setAttribute("tempPhotoList", houseInfo.getHousePhotos());
        model.addAttribute("houseInfo", houseInfo);
        return "/ui/house/update-house";
    }

    @PostMapping(path = "/host/updatehouse")
    public String updateHouse(Model model,
                              HttpSession session,
                              @ModelAttribute("houseInfo") HouseInfo houseInfo,
                              @RequestParam("photos") MultipartFile[] photos) {
        List<HousePhotos> photosList;
        Member member = new Member();
        member.setUser_id(1);
        member.setAccount("admin");
        houseInfo.setMember(member);
        houseInfo.setStatus(true);

        if (houseInfo.getHousePhotos() == null) {
            photosList = (List<HousePhotos>) session.getAttribute("tempPhotoList");
        } else {
            photosList = savePhoto(model, photos);
            if(photosList == null) {
                return "/ui/house/update-house";
            }
        }
        houseInfo.setHousePhotos(photosList);

        boolean insertStatue = houseService.insert(houseInfo);
        if(insertStatue) {
            return "redirect:/house/host/ownedhouse";
        }
        model.addAttribute("errMsg", "修改失敗");
        return "/ui/house/update-house";
    }

    @PostMapping(path = "/host/cancelhouse/{houseid}")
    @ResponseBody
    public ResponseEntity<String> cancelHouse(@PathVariable("houseid") int houseid) {
        boolean cancelStatue = houseService.cancel(houseid);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (cancelStatue) {
            return new ResponseEntity<>("{\"message\": \"下架成功\"}", responseHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"message\": \"下架失敗\"}", responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/host/enablehouse/{houseid}")
    @ResponseBody
    public ResponseEntity<String> enableHouse(@PathVariable("houseid") int houseid) {
        boolean enableStatue = houseService.enable(houseid);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (enableStatue) {
            return new ResponseEntity<>("{\"message\": \"上架成功\"}", responseHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("{\"message\": \"上架失敗\"}", responseHeaders, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/map")
    public String showMap() {
        return "/ui/house/map";
    }

    private List<HousePhotos> savePhoto(Model model, MultipartFile[] photos) {
        if(photos != null && photos.length > 0 && photos.length < 5) {
            List<HousePhotos> photosList = new ArrayList<>();
            String fileName, pathname;
            for(MultipartFile photo : photos){
                HousePhotos housePhotos = new HousePhotos();
                if(photo.getSize() <= 500000) {
                    fileName = String.format("%s.%s", Instant.now().toEpochMilli(), photo.getContentType().split("/")[1]);
                    try {
                        pathname = ResourceUtils.getURL("classpath:").getPath() + "static/images/house/" + fileName;
                        File file = new File(pathname);
                        photo.transferTo(file);
                        housePhotos.setPhotoPath(fileName);
                        photosList.add(housePhotos);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    model.addAttribute("errMsg", "照片大小必須小於5MB");
                    return null;
                }
            }
            return photosList;
        } else {
            model.addAttribute("errMsg", "照片最多只能上傳5張");
            return null;
        }
    }

}
