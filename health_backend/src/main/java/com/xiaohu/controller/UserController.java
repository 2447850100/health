package com.xiaohu.controller;

import com.xiaohu.constant.MessageConstant;
import com.xiaohu.entity.Result;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/pages/user")
public class UserController {
    @RequestMapping("/getUserName.do")
    public Result getUserName(){
        //当spring security完成认证后，会将当前用户信息保存到上下文
        SecurityContext securityContext = SecurityContextHolder.getContext();
        User user = (User) securityContext.getAuthentication().getPrincipal();
        System.out.println(user);
        if (Objects.nonNull(user)){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        }else {
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
