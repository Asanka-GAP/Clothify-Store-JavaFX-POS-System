package org.example.dao.custom.impl;

import javafx.collections.ObservableList;
import org.example.dao.custom.OrderDao;
import org.example.entity.OrderEntity;

public class OrderDaoImpl implements OrderDao {
    @Override
    public OrderEntity search(String s) {
        return null;
    }

    @Override
    public ObservableList<OrderEntity> searchAll() {
        return null;
    }

    @Override
    public boolean insert(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }
}
