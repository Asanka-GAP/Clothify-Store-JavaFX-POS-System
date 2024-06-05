package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.custom.ProductDao;
import org.example.entity.ProductEntity;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public ProductEntity search(String s) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("FROM product WHERE id=:id");
        query.setParameter("id",s);
        ProductEntity product = (ProductEntity) query.uniqueResult();
        session.close();

        return product;
    }

    @Override
    public ObservableList<ProductEntity> searchAll() {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        List<ProductEntity> list = session.createQuery("FROM product").list();
        session.close();

        ObservableList<ProductEntity> productEntities = FXCollections.observableArrayList();
        list.forEach(productEntity -> {
            productEntities.add(productEntity);
        });

        return productEntities;
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
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE product SET name =:name,qty =:qty,size =:size,category= :category,image= :image WHERE id =:id");
        query.setParameter("id",productEntity.getId());
        query.setParameter("name",productEntity.getName());
        query.setParameter("qty",productEntity.getQty());
        query.setParameter("size",productEntity.getSize());
        query.setParameter("image",productEntity.getImage());
        query.setParameter("category",productEntity.getCategory());

        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return i>0;
    }

    @Override
    public boolean delete(String s) {
        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("DELETE FROM product WHERE id= :id");
        query.setParameter("id",s);
        int i = query.executeUpdate();
        session.getTransaction().commit();
        session.close();

        return i>0;
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
