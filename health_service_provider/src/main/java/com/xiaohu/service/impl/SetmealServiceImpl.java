package com.xiaohu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiaohu.dao.SetmealDao;
import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.pojo.Setmeal;
import com.xiaohu.service.SetmealService;
import com.xiaohu.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService{
    @Autowired
    private SetmealDao setmealDao;
    private Jedis jedis;
    @Override
    public Boolean add(Setmeal setmeal, Integer[] checkgroupIds) {
        HashMap<String, Integer> map = new HashMap<>();
        int res = 0;
        setmealDao.add(setmeal);
        for (Integer checkgroupId : checkgroupIds) {
            map.put("checkgroupId",checkgroupId);
            map.put("setmealId",setmeal.getId());
            res = setmealDao.setSetmealAndCheckItem(map);
        }
        //将图片保存到redis
        String img = setmeal.getImg();
        jedis = JedisUtil.getResource();
        jedis.sadd("setmealPic_smell",img);
        jedis.close();
        return res>0;
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<Setmeal> setmealPage = setmealDao.findAllByPage(queryPageBean.getQueryString());
        return new PageResult(setmealPage.getTotal(),setmealPage.getResult());
    }

    @Override
    public int deleteById(Integer id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setId(id);
        //删除关系表主键
       setmealDao.deleteByCheckgroupId(id);
        return setmealDao.delete(setmeal);
    }

    @Override
    public Boolean edit(Setmeal setmeal, Integer[] checkgroupIds) {
        jedis = JedisUtil.getResource();
        jedis.sadd("setmealPic_smell",setmeal.getImg());

        //是否修改了图片，
        setmealDao.update(setmeal);
        int res = 0;
        HashMap<String, Integer> hashMap = new HashMap<>();
        setmealDao.deleteByCheckgroupId(setmeal.getId());
        if (checkgroupIds!=null) {
            for (Integer checkgroupId : checkgroupIds) {
                hashMap.put("checkgroupId", checkgroupId);
                hashMap.put("setmealId", setmeal.getId());
                res = setmealDao.setSetmealAndCheckItem(hashMap);
            }
        }
        jedis.close();
        return res>0;
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
}

    @Override
    public List<Integer> findSetmealByGroupId(Integer id) {
        return setmealDao.findCheckgroupById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
      List<Map<String, Object>> list = setmealDao.findSetmealCount();

        return list;
    }
}
