package com.grp4.houseship.coupon.model;

public enum CouponStatus {
    Enabled("啟用"),
    Disabled("停用"),
    Expired("過期");

    private String description;
    CouponStatus(String decs){
        this.description = decs;
    }

    public String getDescription() {
        return description;
    }
}
