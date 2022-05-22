package com.grp4.houseship.order.model;

public enum OrderStatus {
    //已付款
    UnCheck("未付款"),
    Check("已付款"),
    //取消
    Cancel("已取消"),
    //變更
    Update("已變更"),
    //已完成
    Finish("已完成"),

    Invalid("無效");

    private String description;

    OrderStatus(String desc){
        this.description = desc;
    }

    public String getDescription(){
        return description;
    }
}