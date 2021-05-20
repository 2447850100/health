package com.xiaohu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaohu.dao.PermissionDao;
import com.xiaohu.dao.RoleDao;
import com.xiaohu.dao.UserDao;
import com.xiaohu.pojo.Permission;
import com.xiaohu.pojo.Role;
import com.xiaohu.pojo.User;
import com.xiaohu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Override
    public User findUser(String username) {
        User user = userDao.findByUserName(username);
        if (Objects.isNull(user)){
            return null;
        }
        Integer userId = user.getId();
        //根据用户查询所有的角色
        Set<Role> roles =  roleDao.findUserById(userId);
        roles.forEach(role -> {
            Integer roleId = role.getId();
            //根据角色查询权限
           Set<Permission> permissionSet = permissionDao.findByRoleId(roleId);
           //将权限设置该角色
          role.setPermissions(permissionSet);
        });
        //让用户关联角色
        user.setRoles(roles);
        return user;
    }
}
