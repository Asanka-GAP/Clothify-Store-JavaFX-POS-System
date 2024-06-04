package org.example.dao.custom.impl;

import javafx.collections.ObservableList;
import org.example.dao.custom.ProductDao;
import org.example.entity.ProductEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ProductDaoImpl implements ProductDao {
    @Override
    public ProductEntity search(String s) {
        return null;
    }

    @Override
    public ObservableList<ProductEntity> searchAll() {
        return null;
    }

    @Override
    public boolean insert(ProductEntity productEntity) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(productEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public boolean update(ProductEntity productEntity) {
        return false;
    }

    @Override
    public boolean delete(String s) {
        return false;
    }

    public String getLatestId() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();

        Query query = session.createQuery("SELECT id FROM product ORDER BY id DESC LIMIT 1");
        String id = (String) query.uniqueResult();

        session.close();

        return id;
    }
}
