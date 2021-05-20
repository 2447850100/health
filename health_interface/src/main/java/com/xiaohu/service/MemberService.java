package com.xiaohu.service;

import com.xiaohu.pojo.Member;

import java.util.List;
import java.util.Map;

public interface MemberService {
    public Member login(String telephone);

    void add(Member member);

    List<Integer> findCountByMonths(List<String> monthsList);
}
