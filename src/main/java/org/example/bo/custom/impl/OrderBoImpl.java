package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bo.custom.OrderBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.OrderDaoImpl;
import org.example.entity.OrderEntity;
import org.example.model.Order;
import org.example.util.DaoType;
public class OrderBoImpl implements OrderBo {

    OrderDaoImpl orderDaoImpl = DaoFactory.getInstance().getDao(DaoType.ORDER);
    public boolean saveOrder(Order order) {
        return new OrderDaoImpl().insert(new ObjectMapper().convertValue(order, OrderEntity.class));
    }

    public String getLatestOrderId() {
        return orderDaoImpl.getLatestOrderId();
    }

    public Order getOrderById(String orderId) {
        return new ObjectMapper().convertValue(orderDaoImpl.search(orderId),Order.class);
    }

    public boolean deleteOrderById(String id) {
        return orderDaoImpl.delete(id);
    }
}
