package com.xiaohu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaohu.constant.MessageConstant;
import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.CheckItem;

import com.xiaohu.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pages/checkitem")
public class CheckItemController {
    //查找dubbo服务接口
    @Reference
    private CheckItemService checkItemService;

    @RequestMapping(value = "/add.do")
    public Result add(@RequestBody CheckItem checkItem) {
        Boolean flag = checkItemService.add(checkItem);
        if (flag) {
            return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
        } else {
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/pageQuery.do")
    public PageResult pageQuery(@RequestBody QueryPageBean queryPageBean) {
        return checkItemService.pageQuery(queryPageBean);
    }

    @RequestMapping(value = "/delete.do")
    public Result delete(String id) {
        int flag = checkItemService.deleteById(Integer.parseInt(id));
        if (flag > 0) {
            return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } else {
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem) {
        Boolean flag = checkItemService.edit(checkItem);
        if (flag) {
            return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } else {
            return new Result(true, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/findById.do")
    public Result findById(String id) {
        CheckItem checkItem = checkItemService.findById(Integer.parseInt(id));
        if (checkItem!=null){
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItem);
        }else {
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL,null);
        }
    }
    @RequestMapping("/findAll.do")
    public Result findAll() {
        List<CheckItem> checkItems = checkItemService.findAll();
        if (checkItems == null) {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_FAIL);
        } else {
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItems);
        }
    }
}