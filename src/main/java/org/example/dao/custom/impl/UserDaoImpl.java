package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.custom.UserDao;
import org.example.entity.UserEntity;
import org.example.util.CrudUtil;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {


    public UserEntity searchById(String id){
        Session session = HibernateUtil.getSession();
        session.getTransaction();

        Query query = session.createQuery("FROM user WHERE id=:id");
        query.setParameter("id",id);
        UserEntity userEntity = (UserEntity) query.uniqueResult();
        session.close();
        return userEntity;
    }

    @Override
    public UserEntity search(String s) throws SQLException {

        Session session = HibernateUtil.getSession();
        session.getTransaction();

        Query query = session.createQuery("FROM user WHERE email=:email");
        query.setParameter("email",s);
        UserEntity userEntity = (UserEntity) query.uniqueResult();
        session.close();
        return userEntity;
    }

    @Override
    public ObservableList<UserEntity> searchAll(){

        Session session = HibernateUtil.getSession();
        session.getTransaction();
        List<UserEntity> userList = session.createQuery("FROM user").list();
        ObservableList<UserEntity> list= FXCollections.observableArrayList();
        session.close();
        userList.forEach(userEntity -> {
            list.add(userEntity);
        });
        return list;

    }

    @Override
    public boolean insert(UserEntity userEntity){

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        session.persist(userEntity);
        session.getTransaction().commit();
        session.close();
        return true;
    }

    public boolean update(String email,String password){

        Session session = HibernateUtil.getSession();
        session.getTransaction().begin();
        Query query = session.createQuery("UPDATE user SET password =:p WHERE email =:e");
        query.setParameter("p",password);
        query.setParameter("e",email);
        int i = query.executeUpdate();
        System.out.println(i);

        session.getTransaction().commit();
        session.close();
        return i>0;
    }
    @Override
    public boolean update(UserEntity userEntity) throws SQLException {
        try {
            return CrudUtil.execute("UPDATE user SET name=?,address=?,email=? WHERE id=?",userEntity.getName()
                    ,userEntity.getAddress(),userEntity.getEmail(),userEntity.getId());

        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        try {

            return CrudUtil.execute("DELETE * FROM WHERE id=?",id);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }
}
