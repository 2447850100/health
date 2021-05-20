package com.xiaohu.jobs;

import com.xiaohu.util.JedisUtil;
import com.xiaohu.util.QiniuUtils;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * 自定义job 垃圾图片自动清理
 */
public class ClearImgJob {
    public void clearImg(){
        Jedis jedis = JedisUtil.getResource();
        //将redis大集合减去小集合的差值就是垃圾图片
        Set<String> setImgs = jedis.sdiff("setmealPic_big", "setmealPic_smell");
        for (String fileName : setImgs) {
            //将七牛云服务器的垃圾图片删除
            QiniuUtils.deleteFileFromQiniu(fileName);
            //将大集合中的垃圾图片名称删除
            jedis.srem("setmealPic_big",fileName);
            System.out.println("自动清理垃圾图片"+fileName);
        }
        jedis.close();
    }
}
