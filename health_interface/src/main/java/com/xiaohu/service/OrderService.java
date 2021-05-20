package com.xiaohu.service;

import com.xiaohu.entity.Result;
import com.xiaohu.pojo.Order;

import java.util.Map;

public interface OrderService {
    public Result order(Map<String, Object>map);

    Map findById(Integer id);
}
