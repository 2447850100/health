package com.xiaohu.service;

import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    public Boolean add(CheckItem checkItem);
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public int deleteById(Integer id);
    public Boolean edit(CheckItem checkItem);
    public CheckItem findById(Integer id);
    public List<CheckItem> findAll();
}
