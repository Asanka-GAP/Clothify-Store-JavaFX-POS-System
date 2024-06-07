package org.example.dao.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.DaoFactory;
import org.example.dao.custom.PlaceOrderDao;
import org.example.entity.OrderHasItemEntity;
import org.example.model.OrderHasItem;
import org.example.util.DaoType;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

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

    public ObservableList<OrderHasItem> getAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        List<OrderHasItemEntity> list = session.createQuery("FROM order_has_items").list();
        session.close();
        ObservableList<OrderHasItem> observableList = FXCollections.observableArrayList();

        list.forEach(orderHasItemEntity -> {
            observableList.add(new ObjectMapper().convertValue(orderHasItemEntity, OrderHasItem.class));
        });

        return observableList;
    }

    public boolean deleteByOrderId(String id) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM order_has_items WHERE orderId=:id");
        query.setParameter("id",id);
        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return i>0;
    }

    public ObservableList<OrderHasItem> getProductIdsByOrderId(String id) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM order_has_items WHERE orderId=:id");
        query.setParameter("id",id);
        List<OrderHasItemEntity> list = query.list();
        session.close();

        ObservableList<OrderHasItem> observableList=FXCollections.observableArrayList();

        list.forEach(s -> {
            observableList.add(new ObjectMapper().convertValue(s, OrderHasItem.class));
        });

        return observableList;
    }

    public boolean updateQtyAndAmount(int id, int qty, double newAmount) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE order_has_items SET qty=qty+:qty, amount=amount+:amount WHERE id=:id");
        query.setParameter("id",id);
        query.setParameter("qty",qty);
        query.setParameter("amount",newAmount);

        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return  i>0;
    }
}
