package com.grp4.houseship.order.ecpay;

import com.grp4.houseship.order.model.Order;
import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutALL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EcpayPayment {

    public static AllInOne all;

    public static void initial(){
        all = new AllInOne("");
    }

    public static String genAioCheckOutALL(Order order){
        //初始化
        initial();
        //交易日期格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //實體化交易物件
        AioCheckOutALL obj = new AioCheckOutALL();
        //填入所需參數
        obj.setMerchantTradeNo("ordernb"+String.valueOf(order.getOrderId()));
        obj.setMerchantTradeDate(dateFormat.format(order.getOrderTime()));
        obj.setTotalAmount(String.valueOf( order.getPay().intValue() ));
        obj.setTradeDesc("Houseship 訂房");
        obj.setItemName(order.getOrderDetail().getHouseInfo().getH_title());
        obj.setReturnURL("http://211.23.128.214:5000");
        obj.setOrderResultURL("http://localhost:8080/houseship/order/payResultCheck");
        obj.setNeedExtraPaidInfo("N");
        String form = all.aioCheckOut(obj, null);
        return form;
    }
}
