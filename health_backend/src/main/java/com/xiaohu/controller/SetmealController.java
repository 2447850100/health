package com.xiaohu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xiaohu.constant.MessageConstant;
import com.xiaohu.entity.PageResult;
import com.xiaohu.entity.QueryPageBean;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.Setmeal;
import com.xiaohu.service.SetmealService;
import com.xiaohu.util.JedisUtil;
import com.xiaohu.util.QiniuUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/pages/setmeal")
public class SetmealController {
    @Reference
    private SetmealService setmealService;
    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal,Integer[]checkgroupIds){
        Boolean flag = setmealService.add(setmeal, checkgroupIds);
        if (flag){
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        }else {
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }
    @RequestMapping("/queryPage.do")
    public PageResult queryPage(@RequestBody QueryPageBean queryPageBean){
        return setmealService.pageQuery(queryPageBean);
    }

    /**
     * 文件上传
     * MultipartFile springMVC文件上传组件
     * @return
     */
    @RequestMapping("/upload.do")
    public Result uploadFile(@RequestParam("imgFile") MultipartFile imgFile){
        String filename = null;
        Jedis jedis = null;
        if (!StringUtils.isEmpty(imgFile))
        {
            //原始文件名
            String originalFilename = imgFile.getOriginalFilename();
            //文件不为空
            int index = Objects.requireNonNull(originalFilename).lastIndexOf(".");
            //.jpg
            String extention = originalFilename.substring(index - 1);
            //用UUID生成文件名
            filename = UUID.randomUUID().toString()+extention;
            try {
                QiniuUtils.upload2Qiniu(imgFile.getBytes(),filename);
                //将上传文件存入redis集合
                jedis = JedisUtil.getResource();
                jedis.sadd("setmealPic_big",filename);

            } catch (IOException e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
            }finally {
                assert jedis != null;
                jedis.close();
            }
        }
        return new Result(true,MessageConstant.PIC_UPLOAD_SUCCESS,filename);
    }
    @RequestMapping("/delete.do")
    public Result delete(@RequestParam("id") String id){
        int res = setmealService.deleteById(Integer.parseInt(id));
        if (res>0){
            return new Result(true,"删除套餐成功~");
        }else {
            return new Result(false,"删除套餐失败~");
        }
    }
    @RequestMapping("/findSetmealByGroupId.do")
    public Result findSetmealByGroupId(Integer id){
        List<Integer> list = setmealService.findSetmealByGroupId(id);
        if (list!=null){
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }else {
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Setmeal setmeal, Integer[]checkgroupIds){
        Boolean flag = setmealService.edit(setmeal, checkgroupIds);
        if (flag){
            return new Result(true,"编辑套餐成功~");
        }else {
            return new Result(false,"编辑套餐失败~");
        }
    }
    @RequestMapping("/findImageBySetmealId.do")
    public Setmeal findImageBySetmealId(Integer id){

        return setmealService.findById(id);
    }
}
