package org.example.dao.custom;

import org.example.dao.CrudDao;
import org.example.entity.OrderHasItemEntity;
import org.example.model.OrderHasItem;

public interface PlaceOrderDao extends CrudDao<OrderHasItemEntity,Integer> {
}
