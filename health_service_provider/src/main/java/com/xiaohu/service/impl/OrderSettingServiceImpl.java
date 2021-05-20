package com.xiaohu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaohu.dao.OrderSettingDao;
import com.xiaohu.pojo.OrderSetting;
import com.xiaohu.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService{
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Override
    public void add(List<OrderSetting> list) {
        if (list!=null&&list.size()>0){
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经进行了预约设置
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate>0){
                    //已经进行了预约设置，执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //执行添加操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
            Date orderDate = orderSetting.getOrderDate();
            //根据日期查询是否已经进行了预约设置
            long count = orderSettingDao.findCountByOrderDate(orderDate);
            if(count > 0){
                //当前日期已经进行了预约设置，需要执行更新操作
                orderSettingDao.editNumberByOrderDate(orderSetting);
            }else{
                //当前日期没有就那些预约设置，需要执行插入操作
                orderSettingDao.add(orderSetting);
            }

    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        String begin = date + "-1";//2019-6-1
        String end = date + "-31";//2019-6-31
        Map<String,String> map = new HashMap<>();
        map.put("begin",begin);
        map.put("end",end);
        //调用DAO，根据日期范围查询预约设置数据
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<>();
        if(list != null && list.size() > 0){
            for (OrderSetting orderSetting : list) {
                Map<String,Object> m = new HashMap<>();
                m.put("date",orderSetting.getOrderDate().getDate());//获取日期数字（几号）
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }
}
