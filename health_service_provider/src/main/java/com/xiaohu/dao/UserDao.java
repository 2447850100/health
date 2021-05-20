package com.xiaohu.dao;

import com.xiaohu.pojo.User;

public interface UserDao {
    public User findByUserName(String userName);
}
