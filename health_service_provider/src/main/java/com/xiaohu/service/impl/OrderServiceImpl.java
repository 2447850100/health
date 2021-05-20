package com.xiaohu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.xiaohu.constant.MessageConstant;
import com.xiaohu.dao.MemberDao;
import com.xiaohu.dao.OrderDao;
import com.xiaohu.dao.OrderSettingDao;
import com.xiaohu.entity.Result;
import com.xiaohu.pojo.Member;
import com.xiaohu.pojo.Order;
import com.xiaohu.pojo.OrderSetting;
import com.xiaohu.service.OrderService;
import com.xiaohu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;

    @Override
    public Result order(Map<String, Object> map) {
        Order newOrder = null;
        String orderDate = (String) map.get("orderDate");
        //检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有则无法进行预约预约
        try {
            OrderSetting orderSetting = orderSettingDao.findByOrderDate(DateUtils.parseString2Date(orderDate));
            if (orderSetting == null) {
                return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
            }
            //2.检查用户所选择的预约日期是否已经预约满了
            //可预约人数
            int peopleNum = orderSetting.getNumber();
            //已预约人数
            int reservationsNum = orderSetting.getReservations();
            if (reservationsNum >= peopleNum) {
                //已经约满
                return new Result(false, MessageConstant.ORDER_FULL);
            }
            //3.判断用户是否重复预约(同一个用户在同一天约了同一个套餐)
            Member member = memberDao.findByTelephone((String) map.get("telephone"));
            if (member != null) {
                //会员id
                Integer memberId = member.getId();
                //预约日期
                Date oder_date = DateUtils.parseString2Date(orderDate);
                //套餐id
                String setmealId = (String) map.get("setmealId");
                //封装为order对象
                Order order = new Order(memberId, oder_date, Integer.parseInt(setmealId));
                List<Order> orderList = orderDao.findByCondition(order);
                if (orderList != null && orderList.size() > 0) {
                    //重复预约
                    return new Result(false, MessageConstant.HAS_ORDERED);
                } else {
                    //不是会员，注册为会员
                    member = new Member();
                    member.setName((String) map.get("name"));
                    member.setPhoneNumber((String) map.get("telephone"));
                    member.setIdCard((String) map.get("idCard"));
                    member.setSex((String) map.get("sex"));
                    member.setRegTime(new Date());
                    memberDao.add(member);
                }
                //5.预约成功,更新当日的已预约人数
                newOrder = new Order();
                newOrder.setMemberId(member.getId());
                newOrder.setOrderDate(oder_date);
                newOrder.setOrderType((String) map.get("orderType"));
                newOrder.setOrderStatus(Order.ORDERSTATUS_NO);
                newOrder.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
                orderDao.add(newOrder);
                //更新当然已预约人数+1
                orderSetting.setReservations(orderSetting.getReservations() + 1);
                orderSettingDao.editReservationsByOrderDate(orderSetting);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return new Result(true, MessageConstant.ORDER_SUCCESS, newOrder);
    }

    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            String date = (String) map.get("orderDate");
            try {
                Date string2Date = DateUtils.parseString2Date(date);
                map.put("orderDate", string2Date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
}
