package com.xiaohu.dao;

import com.github.pagehelper.Page;
import com.xiaohu.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {
    int add(Setmeal setmeal);
    List<Setmeal> findAll();
    Setmeal findById(Integer id);
    int update(Setmeal setmeal);
    int delete(Setmeal setmeal);
    Page<Setmeal> findAllByPage(String queryString);

    /**
     * 关联表映射
     * @param hashMap
     * @return
     */
    int setSetmealAndCheckItem(Map<String, Integer> hashMap);

    List<Integer> findCheckgroupById(Integer id);

    /**
     * 删除旧关系表
     * @param setmealId
     */
    void deleteByCheckgroupId(Integer setmealId);

    List<Map<String, Object>> findSetmealCount();
}
