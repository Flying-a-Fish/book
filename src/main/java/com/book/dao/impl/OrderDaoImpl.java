package com.book.dao.impl;

import com.book.dao.OrderDao;
import com.book.entity.Order;
import org.hibernate.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lixuy on 2016/5/10.
 */
@Component
public class OrderDaoImpl extends BaseDaoImpl implements OrderDao {
    @Override
    public Order get(String s) {
        return (Order) getCurrentSession().get(Order.class, s);
    }

    @Override
    public List<Order> list() {
        return  getCurrentSession().createQuery("from Order").list();
    }

    @Override
    public String save(Order entity) {
        return (String)getCurrentSession().save(entity);
    }

    @Override
    public void saveOrUpdate(Order entity) {
        getCurrentSession().saveOrUpdate(entity);
    }

    @Override
    public void delete(String s) {
        Order order  = get(s);
        if(order != null){
            getCurrentSession().delete(order);
        }
    }

    /**
     * 根据用户Id查用户的订单
     * @param userId 用户Id
     * @return 订单列表
     */
    @Override
    public List<Order> getByUserId(String userId) {
        Query query = getCurrentSession().createQuery("from Order where buyUser =:userId order by uts desc");
        query.setString("userId", userId);
        return  query.list();
    }

    @Override
    public String getBaseSql() {
        return "from Order";
    }
}
