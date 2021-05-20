package com.xiaohu.dao;

import com.github.pagehelper.Page;
import com.xiaohu.pojo.CheckGroup;



import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    int add(CheckGroup checkGroup);
    List<CheckGroup> findAll();
   CheckGroup findById(Integer id);
   int update(CheckGroup checkGroup);
   int delete(CheckGroup checkGroup);
   Page<CheckGroup> findAllByPage(String queryString);

    /**
     * 关联表映射
     * @param hashMap
     * @return
     */
    int setCheckGroupAndCheckItem(Map<String, Integer> hashMap);

     List<Integer> findCheckItemById(Integer id);

    /**
     * 删除旧关系表
     * @param checkgroupId
     */
    void deleteByCheckgroupId(Integer checkgroupId);
}
