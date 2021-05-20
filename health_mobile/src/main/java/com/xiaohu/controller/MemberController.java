package com.xiaohu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.xiaohu.constant.MessageConstant;
import com.xiaohu.constant.RedisMessageConstant;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.Member;
import com.xiaohu.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/pages/login")
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    @RequestMapping("/check.do")
    public Result login(HttpServletResponse response, @RequestBody Map<String, String> map){
        String telephone = map.get("telephone");
        String validateCode = map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (Objects.isNull(codeInRedis) || !codeInRedis.equalsIgnoreCase(validateCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        Member member= memberService.login(telephone);
        if (!Objects.isNull(member)){
            //客户端浏览器写入Cookie 内容为手机号
            Cookie cookie = new Cookie("member_telepnone_login",telephone);
            cookie.setPath("/");
            //有效期为30天
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            //将会员信息保存到redis中
            //redis不能存取javaBean,先进行序列号转换为json对象
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS,member);
        }else {
            member=new Member();
            member.setRegTime(new Date());
            member.setPhoneNumber(telephone);
            memberService.add(member);
            return new Result(false,"新用户将自动注册");
        }
    }
}
