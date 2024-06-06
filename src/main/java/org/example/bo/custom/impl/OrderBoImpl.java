package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bo.custom.OrderBo;
import org.example.dao.custom.impl.OrderDaoImpl;
import org.example.entity.OrderEntity;
import org.example.model.Order;

public class OrderBoImpl implements OrderBo {
    public boolean saveOrder(Order order) {
        return new OrderDaoImpl().insert(new ObjectMapper().convertValue(order, OrderEntity.class));
    }
}
