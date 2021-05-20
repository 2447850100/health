package com.xiaohu.dao;

import com.github.pagehelper.Page;

import com.xiaohu.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
    int add(CheckItem checkItem);
    List<CheckItem> findAll();
   CheckItem findById(Integer id);
   int update(CheckItem checkItem);
   int delete(CheckItem checkItem);
   Page<CheckItem> findAllByPage(String queryString);
}
