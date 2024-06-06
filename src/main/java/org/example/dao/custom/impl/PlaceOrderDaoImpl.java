package org.example.dao.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import org.example.dao.DaoFactory;
import org.example.dao.custom.PlaceOrderDao;
import org.example.entity.OrderHasItemEntity;
import org.example.model.OrderHasItem;
import org.example.util.DaoType;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class PlaceOrderDaoImpl implements PlaceOrderDao {

    ProductDaoImpl productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
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

//        Session session = HibernateUtil.getSession();
//        session.getTransaction().begin();
//        session.persist((OrderHasItem));
        return true;
    }

    @Override
    public boolean update(OrderHasItemEntity orderHasItemEntity) {
        return false;
    }

    @Override
    public boolean delete(Integer integer) {
        return false;
    }

    public boolean saveAll(ObservableList<OrderHasItem> orderHasItemObservableList) {

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        orderHasItemObservableList.forEach(orderHasItem -> {
            OrderHasItemEntity orderHasItemEntity = new ObjectMapper().convertValue(orderHasItem, OrderHasItemEntity.class);

            productDao.updateQty(orderHasItemEntity.getProductId(),orderHasItemEntity.getQty());
            session.persist(orderHasItemEntity);
        });
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public int getLatestId() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Query query = session.createQuery("SELECT id FROM order_has_items ORDER BY id DESC LIMIT 1");
        int id = (int) query.uniqueResult();

        session.close();

        return id;
    }
}
