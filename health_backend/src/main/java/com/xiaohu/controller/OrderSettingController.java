package com.xiaohu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaohu.constant.MessageConstant;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.OrderSetting;
import com.xiaohu.service.OrderSettingService;
import com.xiaohu.util.POIUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pages/orderSetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;
    @RequestMapping("/upload.do")
    public Result uploadOrderSetting(@RequestParam("excelFile") MultipartFile excelFile){
           //使用POI解析表数据
        try {
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> orderSettings = new ArrayList<>();
            for (String[] strings : list) {
                //将表里的数据封装为pojo对象
                OrderSetting orderSetting = new OrderSetting(new Date(strings[0]), Integer.parseInt(strings[1]));
                orderSettings.add(orderSetting);
            }
            orderSettingService.add(orderSettings);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }
    //根据月份查询对应的预约设置数据
    @RequestMapping("/getOrderSettingByMonth.do")
    public Result getOrderSettingByMonth(String date){//date格式为：yyyy-MM
        try{
            List<Map> list = orderSettingService.getOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    //根据日期设置对应的预约设置数据
    @RequestMapping("/editNumberByDate.do")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        try{
            orderSettingService.editNumberByDate(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }
}
