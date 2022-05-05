package com.grp4.houseship.order.model;

public enum OrderStatus {
    //已付款
    Check("已付款"),
    //取消
    Cancel("已取消"),
    //變更
    Update("已變更");

    private String description;

    OrderStatus(String desc){
        this.description = desc;
    }

    public String getDescription(){
        return description;
    }
}