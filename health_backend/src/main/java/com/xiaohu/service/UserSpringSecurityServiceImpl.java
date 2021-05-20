package com.xiaohu.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaohu.pojo.Permission;
import com.xiaohu.pojo.Role;
import com.xiaohu.pojo.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component
public class UserSpringSecurityServiceImpl implements UserDetailsService {
    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findUser(username);
        if (Objects.isNull(user)){
            System.out.println("user为空");
            return null;
        }
        List<GrantedAuthority> list = new ArrayList<>();
        //动态为当前用户授权
        Set<Role> roles = user.getRoles();
        roles.forEach((role -> {
            //遍历角色集合，为用户授予角色
            list.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissionSet = role.getPermissions();
            permissionSet.forEach(permission -> {
                //遍历权限集合，为用户授权
                list.add(new SimpleGrantedAuthority(permission.getKeyword()));
            });
        }));
        //创建框架user，框架自动调用
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username,user.getPassword(),list);

        return securityUser;
    }
}
