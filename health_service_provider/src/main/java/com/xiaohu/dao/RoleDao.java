package com.xiaohu.dao;

import com.xiaohu.pojo.Role;

import java.util.Set;

public interface RoleDao {
    Set<Role> findUserById(Integer userId);
}
