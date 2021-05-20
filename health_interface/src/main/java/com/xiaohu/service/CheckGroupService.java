package com.xiaohu.service;

import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.pojo.CheckGroup;
import java.util.List;

public interface CheckGroupService {
    public Boolean add(CheckGroup checkGroup,Integer[]checkItemIds);

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    public PageResult pageQuery(QueryPageBean queryPageBean);
    public int deleteById(Integer id);
    public Boolean edit(CheckGroup checkGroup,Integer[]checkitemIds);
    public CheckGroup findById(Integer id);
    public List<CheckGroup> findAll();

    /**
     * 通过GroupId查询关系checkitemid
     * @param id
     * @return
     */
    public List<Integer> findCheckItemByGroupId(Integer id);
}
