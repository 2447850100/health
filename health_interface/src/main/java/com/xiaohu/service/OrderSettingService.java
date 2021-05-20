package com.xiaohu.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaohu.pojo.OrderSetting;

import java.util.List;
import java.util.Map;


public interface OrderSettingService {
    public void add(List<OrderSetting> list);

    void editNumberByDate(OrderSetting orderSetting);

    List<Map> getOrderSettingByMonth(String date);
}
