package com.xiaohu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xiaohu.dao.CheckGroupDao;
import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.pojo.CheckGroup;
import com.xiaohu.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public Boolean add(CheckGroup checkGroup,Integer[]checkItemIds) {
        Map<String, Integer>hashMap =  new HashMap<>();
        int res = 0;
        checkGroupDao.add(checkGroup);
        Integer id = checkGroup.getId();
        res = getAssociate(checkItemIds, res, hashMap, id);
        return res >0;
    }

    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findAllByPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public int deleteById(Integer id) {
        CheckGroup checkGroup = new CheckGroup();
        checkGroup.setId(id);
        checkGroupDao.deleteByCheckgroupId(id);
        return checkGroupDao.delete(checkGroup);
    }

    @Override
    public Boolean edit(CheckGroup checkGroup,Integer[]checkitemIds) {
        int res = 0;
        checkGroup.setCode(checkGroup.getCode());
        checkGroup.setHelpCode(checkGroup.getHelpCode());
        checkGroupDao.update(checkGroup);
        //先清理旧关系，再建立新关系
        Map<String, Integer> hashmap = new HashMap<>();
        Integer id = checkGroup.getId();
        checkGroupDao.deleteByCheckgroupId(id);
        res = getAssociate(checkitemIds, res, hashmap, id);
        return res>0;
    }

    /**
     * 建立关联关系
     * @param checkitemIds
     * @param res
     * @param hashmap
     * @param id
     * @return
     */
    private int getAssociate(Integer[] checkitemIds, int res, Map<String, Integer> hashmap, Integer id) {
        if (checkitemIds != null && checkitemIds.length > 0) {
            for (Integer checkitemId : checkitemIds) {
                hashmap.put("checkGroupId", id);
                hashmap.put("checkItemId", checkitemId);
                res = checkGroupDao.setCheckGroupAndCheckItem(hashmap);
            }
        }
        return res;
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    @Override
    public List<Integer> findCheckItemByGroupId(Integer id) {
        return  checkGroupDao.findCheckItemById(id);
    }
}
