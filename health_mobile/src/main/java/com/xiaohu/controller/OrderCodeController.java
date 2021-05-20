package com.xiaohu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.xiaohu.constant.MessageConstant;
import com.xiaohu.constant.RedisMessageConstant;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.Order;
import com.xiaohu.service.OrderService;
import com.xiaohu.util.SMSUtils;
import com.xiaohu.util.ValidateCodeUtils;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/pages/order")
public class OrderCodeController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    @RequestMapping("/send4order.do")
    public Result send4order(@RequestParam("telephone") String telephone){
        //随机生成一个4位的手机验证码
        String code4String = ValidateCodeUtils.generateValidateCode4String(4);
        //给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code4String);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis(5分钟) 13812345678001
        String setex = jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 5*60, code4String);
        System.out.println(setex);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
    @RequestMapping("/submit.do")
    public Result submit(@RequestBody Map<String, Object> map){
        //获取电map封装的数据,电话和验证码
        String telephone =(String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //从redis获取保存的验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //将用户输入的验证码跟redis保存的验证码比对
        if ( Objects.isNull(code) || !code.equalsIgnoreCase(validateCode)){
                //不一致,返回错误信息结果集
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }

        //设置预约类型
        System.out.println(telephone);
        System.out.println(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        map.put("orderType",Order.ORDERTYPE_WEIXIN);
        Result result = orderService.order(map);
        if (result.isFlag()){
            //预约成功，发送短信
            try {
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,(String) map.get("orderDate"));
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
        return new Result(true,MessageConstant.ORDER_SUCCESS,result);
    }
    @RequestMapping("/findById.do")
    public Result findOrder(Integer id){
     Map map = orderService.findById(id);
     if (map!=null){
         return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
     }else {
         return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
     }
    }
    @RequestMapping("/send4Login.do")
    public Result send4Login(@RequestParam("telephone") String telephone){
        //随机生成一个4位的手机验证码
        String code6String = ValidateCodeUtils.generateValidateCode4String(6);
        //给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code6String);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码保存到redis(5分钟) 13812345678001
        String setex = jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5*60, code6String);
        System.out.println(setex);
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
