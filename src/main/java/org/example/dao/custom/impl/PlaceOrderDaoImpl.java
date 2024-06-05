package org.example.dao.custom.impl;

import javafx.collections.ObservableList;
import org.example.dao.custom.PlaceOrderDao;
import org.example.entity.OrderHasItemEntity;

public class PlaceOrderDaoImpl implements PlaceOrderDao {
    @Override
    public OrderHasItemEntity search(Integer integer) {
        return null;
    }

    @Override
    public ObservableList<OrderHasItemEntity> searchAll() {
        return null;
    }

    @Override
    public boolean insert(OrderHasItemEntity orderHasItemEntity) {
        return false;
    }

    @Override
    public boolean update(OrderHasItemEntity orderHasItemEntity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }
}
