package com.xiaohu.service;

import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    public Boolean add(Setmeal setmeal, Integer[]checkgroupIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public int deleteById(Integer id);
    public Boolean edit(Setmeal setmeal,Integer[]checkgroupIds);
    public Setmeal findById(Integer id);
    public List<Setmeal> findAll();

    /**
     * 通过GroupId查询关系setmealId
     * @param id
     * @return
     */
    public List<Integer> findSetmealByGroupId(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
