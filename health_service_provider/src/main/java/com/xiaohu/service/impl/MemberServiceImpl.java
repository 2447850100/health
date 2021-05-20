package com.xiaohu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.xiaohu.dao.MemberDao;
import com.xiaohu.pojo.Member;
import com.xiaohu.service.MemberService;
import com.xiaohu.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Transactional
@Service(interfaceClass = MemberService.class)
public class MemberServiceImpl implements MemberService {
   @Autowired
   private MemberDao memberDao;
    @Override
    public Member login(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        //如果使通过表单注册，将密码进行MD5算法加密
        if (!Objects.isNull(password)){
           password = MD5Utils.md5(password);
           member.setPassword(password);
        }
        memberDao.add(member);
    }

    @Override
    public List<Integer> findCountByMonths(List<String> monthsList) {
        List<Integer> list = new ArrayList<>();
        monthsList.forEach(month->{
            //因为controller层将天数截取了，数据库格式为yyyy-MM-dd
            month = month + "-31";
           Integer count = memberDao.findMemberCountBeforeDate(month);
           if (Objects.nonNull(count)){
               list.add(count);
           }
        });
        return list;
    }
}
