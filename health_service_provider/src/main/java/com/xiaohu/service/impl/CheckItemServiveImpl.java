package com.xiaohu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.xiaohu.dao.CheckItemDao;
import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.pojo.CheckItem;
import com.xiaohu.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiveImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public Boolean add(CheckItem checkItem) {
        return checkItemDao.add(checkItem)>0;
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckItem>page = checkItemDao.findAllByPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }
    @Override
    public int deleteById(Integer id){
        CheckItem checkItem = new CheckItem();
        checkItem.setId(id);
      return checkItemDao.delete(checkItem);
    }

    @Override
    public Boolean edit(CheckItem checkItem) {
        checkItem.setCode(checkItem.getCode());
        return checkItemDao.update(checkItem)>0;
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
