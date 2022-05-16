package com.grp4.houseship.house.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.grp4.houseship.email.service.EmailService;
import com.grp4.houseship.house.model.*;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.order.model.OrderDetail;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

@Controller
public class HouseUserInterfaceController {

    @Autowired
    private HouseService houseService;

    @Autowired
    private EmailService emailService;
    
    @GetMapping(path = "/search")
    public String search() {
        return "/ui/house/searchResults";
    }

    @GetMapping(path = "/search/{city}")
    public String searchByCity(@PathVariable("city") String city, HttpSession session) {
        session.setAttribute("locationCity", city);
        return "/ui/house/searchResults";
    }

    @GetMapping(path = "/api/house/search-result/{page}")
    @ResponseBody
    public ResponseEntity<List<HouseInfo>> searchAllHouses(HttpSession session, @PathVariable("page") Integer page) {
        List<HouseInfo> houseList;
        Pageable pageable = PageRequest.of(page - 1, 5);
        String locationCity = (String) session.getAttribute("locationCity");
        Page<HouseInfo> houseInfoPage;
        if(locationCity != null) {
            houseInfoPage = houseService.searchAllByCity(locationCity, pageable);
        } else {
            houseInfoPage = houseService.searchAll(pageable);
        }
        session.setAttribute("totalPages", houseInfoPage.getTotalPages());
        session.setAttribute("totalElements", houseInfoPage.getTotalElements());
        session.setAttribute("currentPage", page);
        houseList = houseInfoPage.getContent();
        session.setAttribute("houseList", houseList);

        session.removeAttribute("locationCity");

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if(houseList.isEmpty()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (houseList, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(path = "/api/house/totalpages")
    @ResponseBody
    public ResponseEntity<Map<String, Integer>> getTotalPages(HttpSession session) {
        Integer totalPages = (Integer) session.getAttribute("totalPages");
        Integer currentPage = (Integer) session.getAttribute("currentPage");
        Integer totalElements;
        if(session.getAttribute("totalElements") instanceof Long) {
            totalElements = ((Long) session.getAttribute("totalElements")).intValue();
        } else {
            totalElements = (Integer) session.getAttribute("totalElements");
        }
        if(totalPages == null || currentPage == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Integer> pageMap = new HashMap<>();
        pageMap.put("totalPages", totalPages);
        pageMap.put("totalElements", totalElements);
        pageMap.put("currentPage", currentPage);

        return new ResponseEntity<>(pageMap, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(path = "/api/house/advanced-search-result/{page}")
    @ResponseBody
    public ResponseEntity<List<HouseInfo>> advancedSearchAllHouses(@RequestBody AdvancedSearchModel advancedSearchModel,
                                                                    @PathVariable("page") Integer page
                                                                    , HttpSession session) {
        List<HouseInfo> houseList;
        StringBuilder sb = new StringBuilder();
        Pageable pageable = PageRequest.of(page - 1, 5);

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

        Page<HouseInfo> houseInfoPage = houseService.advanceSearch(sb.toString(), pageable);

        houseList = houseInfoPage.getContent();

        session.setAttribute("houseList", houseList);
        session.setAttribute("totalPages", houseInfoPage.getTotalPages());
        session.setAttribute("totalElements", houseInfoPage.getTotalElements());
        session.setAttribute("currentPage", page);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if(houseList.isEmpty()) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<> (houseList, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(path = "/housedetails/{houseid}")
    public String houseDetails(@PathVariable("houseid") int houseid, Model model, HttpSession session) {
        HouseInfo houseInfo = houseService.searchById(houseid);
        if(houseInfo != null) {
            model.addAttribute("houseInfo", houseInfo);
            session.setAttribute("houseInfo", houseInfo);
            OrderDetail orderDetail = new OrderDetail(houseInfo);
            model.addAttribute("orderDetail", orderDetail);
        } else {
            model.addAttribute("errMsg", "查無資料");
        }
        return "/ui/house/house-details";
    }

    @GetMapping(path = "/account/host/ownedhouse")
    public String ownedHouse(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");

        model.addAttribute("ownedList", houseService.searchByAccount(member));
        return "/ui/house/ownedhouse";
    }

    @GetMapping(path = "/account/host/addnewhouse")
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

    @PostMapping(path = "/account/host/addnewhouse")
    public String addNewHouse(Model model,
                            HttpSession session,
                            @ModelAttribute("houseInfo") HouseInfo houseInfo,
                            @RequestParam("photos") MultipartFile[] photos) {

        Member member = (Member) session.getAttribute("member");
        houseInfo.setMember(member);
        houseInfo.setStatus(true);

        Geometry geometry = addressToLatLng(houseInfo.getCity() + houseInfo.getH_address());
        houseInfo.setLat(geometry.location.lat);
        houseInfo.setLng(geometry.location.lng);

        List<HousePhotos> photosList = savePhoto(model, photos);

        if(photosList == null) {
            return "/ui/house/add-new-house";
        }

        houseInfo.setHousePhotos(photosList);

        boolean insertStatue = houseService.insert(houseInfo);
        if(insertStatue) {
            String title = "親愛的: " + houseInfo.getMember().getFirstname() + houseInfo.getMember().getLastname() + " 您好\n"
                            + "您在 HouseShip 上新增的 '"+ houseInfo.getH_title() + "' 房屋已成功上架";
            try {
                emailService.sendHouseMail(houseInfo, "ui/house/email_house_success", title);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            return "redirect:/account/host/ownedhouse";
        }
        model.addAttribute("errMsg", "新增失敗");
        return "/ui/house/add-new-house";
    }

    @GetMapping(path = "/account/host/updatehouse/{houseid}")
    public String updateHousePage(@PathVariable("houseid") int houseid, Model model, HttpSession session) {
        HouseInfo houseInfo = houseService.searchById(houseid);
        session.setAttribute("houseNo", houseInfo.getHouseNo());
        session.setAttribute("createdDate", houseInfo.getCreatedDate());
        session.setAttribute("tempPhotoList", houseInfo.getHousePhotos());
        model.addAttribute("houseInfo", houseInfo);
        return "/ui/house/update-house";
    }

    @PostMapping(path = "/account/host/updatehouse")
    public String updateHouse(Model model,
                              HttpSession session,
                              @ModelAttribute("houseInfo") HouseInfo houseInfo,
                              @RequestParam("photos") MultipartFile[] photos) {
        List<HousePhotos> photosList;
        Member member = (Member) session.getAttribute("member");
        houseInfo.setMember(member);
        houseInfo.setStatus(true);

        Integer houseNo = (Integer) session.getAttribute("houseNo");
        houseInfo.setHouseNo(houseNo);
        Date createdDate = (Date) session.getAttribute("createdDate");
        houseInfo.setCreatedDate(createdDate);
        Geometry geometry = addressToLatLng(houseInfo.getCity() + houseInfo.getH_address());
        houseInfo.setLat(geometry.location.lat);
        houseInfo.setLng(geometry.location.lng);

        if (photos.length == 1) {
            photosList = (List<HousePhotos>) session.getAttribute("tempPhotoList");
        } else {
            photosList = savePhoto(model, photos);
            if(photosList == null) {
                return "/ui/house/update-house";
            }
        }

        houseInfo.setHousePhotos(photosList);

        boolean insertStatue = houseService.update(houseNo,houseInfo);
        session.removeAttribute("houseNo");
        session.removeAttribute("createdDate");
        if(insertStatue) {
            return "redirect:/account/host/ownedhouse";
        }
        model.addAttribute("errMsg", "修改失敗");
        return "/ui/house/update-house";
    }

    @PostMapping(path = "/account/host/cancelhouse/{houseid}")
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

    @PostMapping(path = "/account/host/enablehouse/{houseid}")
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

    @GetMapping(path = "/api/map")
    public String showMap(HttpSession session) {
        HouseInfo houseInfo = (HouseInfo) session.getAttribute("houseInfo");
        if(houseInfo != null) {
            return "/ui/house/map-house-detail";
        }
        return "/ui/house/map";
    }

    @GetMapping(path = "/api/map/latlng")
    @ResponseBody
    public List<HouseInfo> searchLatAndLng(HttpSession session) {
        List<HouseInfo> houseList;
        HouseInfo houseInfo = (HouseInfo) session.getAttribute("houseInfo");
        if(houseInfo != null) {
            houseList = new ArrayList<>();
            houseList.add(houseInfo);
            session.removeAttribute("houseInfo");
        } else {
            houseList = (List<HouseInfo>) session.getAttribute("houseList");
            session.removeAttribute("houseList");
        }
        return houseList;
    }

    @GetMapping(path = "/api/images/{directory}/{fileName}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable("directory")String directory, @PathVariable("fileName") String fileName) {
        String pathname = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\images\\" + directory + "\\" + fileName;
        try {
            byte[] byteArray = FileUtils.readFileToByteArray(new File(pathname));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(byteArray, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private List<HousePhotos> savePhoto(Model model, MultipartFile[] photos) {
        if(photos != null && photos.length > 0 && photos.length <= 5) {
            List<HousePhotos> photosList = new ArrayList<>();
            String fileName, pathname;
            for(MultipartFile photo : photos){
                HousePhotos housePhotos = new HousePhotos();
                if(photo.getSize() <= 500000) {
                    fileName = String.format("%s\\%s.%s", "house", Instant.now().toEpochMilli(), photo.getContentType().split("/")[1]);
                    try {
                        pathname = "C:\\Users\\" + System.getProperty("user.name") + "\\Desktop\\images\\" + fileName;
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

    private Geometry addressToLatLng(String address) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyAfx3SJ3744XiZVKLLoLTrAK2_ymXJ8R4E")
                .build();
        try {
            GeocodingResult[] results =  GeocodingApi.geocode(context, address).await();
            return results[0].geometry;
        } catch (IOException | InterruptedException | ApiException e) {
            e.printStackTrace();
            return null;
        } finally {
            context.shutdown();
        }

    }

}
