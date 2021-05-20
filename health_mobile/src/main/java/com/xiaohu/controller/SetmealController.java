package com.xiaohu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaohu.constant.MessageConstant;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.Setmeal;
import com.xiaohu.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pages/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @RequestMapping("/getAllSetmeal.do")
    public Result getAllSetmeal(){
        List<Setmeal> setmealList = setmealService.findAll();
        if (setmealList!=null){
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmealList);
        }else {
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_SUCCESS);
        }
    }
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        Setmeal setmeal = setmealService.findById(id);
        if (setmeal==null){
            return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }else {
            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,setmeal);
        }
    }
}
