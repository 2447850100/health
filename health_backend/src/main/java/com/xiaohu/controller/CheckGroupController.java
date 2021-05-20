package com.xiaohu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaohu.constant.MessageConstant;
import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.CheckGroup;
import com.xiaohu.service.CheckGroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/pages/checkgroup")
public class CheckGroupController {
    @Reference
    private CheckGroupService checkGroupService;
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckGroup checkGroup,Integer[]checkitemIds){
        Boolean flag = checkGroupService.add(checkGroup,checkitemIds);
        if (flag){
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        }else {
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/pageQuery.do")
    public PageResult pageQuery(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.pageQuery(queryPageBean);
    }
    @RequestMapping("/findCheckItemByGroupId.do")
    public Result findCheckItemByGroupId(Integer id){
       List<Integer> checkItemList =  checkGroupService.findCheckItemByGroupId(id);
       if (checkItemList==null){
           return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
       }else {
           return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
       }
    }
    @RequestMapping("/edit.do")
    public Result editGroup(@RequestBody CheckGroup checkGroup,Integer[]checkitemIds){
        Boolean flag = checkGroupService.edit(checkGroup,checkitemIds);
        if (flag){
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }else {
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/delete.do")
    public Result deleteGroup(String id){
        int res = checkGroupService.deleteById(Integer.parseInt(id));
        if (res>0){
            return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }else {
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/findAll.do")
    public Result findAllCheckGroup(){
        List<CheckGroup> groupList = checkGroupService.findAll();
        if (groupList==null){
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }else {
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,groupList);
        }
    }
}
