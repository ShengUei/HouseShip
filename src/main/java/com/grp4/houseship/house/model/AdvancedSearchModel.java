package com.grp4.houseship.house.model;

import java.util.Map;

public class AdvancedSearchModel {

    private Integer houseType;
    private Double[] priceZone;
    private boolean greaterPrice;
    private Map<String, Boolean> houseOffers;
    private Map<String, Boolean> houseRules;

    public AdvancedSearchModel() {
    }

    public Integer getHouseType() {
        return houseType;
    }

    public void setHouseType(Integer houseType) {
        this.houseType = houseType;
    }

    public Double[] getPriceZone() {
        return priceZone;
    }

    public void setPriceZone(Double[] priceZone) {
        this.priceZone = priceZone;
    }

    public boolean isGreaterPrice() {
        return greaterPrice;
    }

    public void setGreaterPrice(boolean greaterPrice) {
        this.greaterPrice = greaterPrice;
    }

    public Map<String, Boolean> getHouseOffers() {
        return houseOffers;
    }

    public void setHouseOffers(Map<String, Boolean> houseOffers) {
        this.houseOffers = houseOffers;
    }

    public Map<String, Boolean> getHouseRules() {
        return houseRules;
    }

    public void setHouseRules(Map<String, Boolean> houseRules) {
        this.houseRules = houseRules;
    }
}
