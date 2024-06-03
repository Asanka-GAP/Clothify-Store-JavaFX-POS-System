package org.example.dao.custom.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.dao.custom.UserDao;
import org.example.entity.UserEntity;
import org.example.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    @Override
    public UserEntity search(String s) throws SQLException {
        UserEntity userEntity = null;
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE email=?",s);
            while (resultSet.next()){
                userEntity= new UserEntity(resultSet.getString(1)
                                            ,resultSet.getString(2)
                                            ,resultSet.getString(3)
                                            ,resultSet.getString(4)
                                            ,resultSet.getString(5)
                                            ,resultSet.getString(6)
                );
            }
            return userEntity;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ObservableList<UserEntity> searchAll() throws SQLException {
        ObservableList<UserEntity> list= FXCollections.observableArrayList();

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user");

            while (resultSet.next()){
                list.add(new UserEntity(resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)));
            }
            return list;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean insert(UserEntity userEntity) throws SQLException {
        try {
            boolean isInsert = CrudUtil.execute("INSERT INTO user VALUES (?,?,?,?,?,?)",userEntity.getId(),
                    userEntity.getName(),
                    userEntity.getEmail(),
                    userEntity.getPassword(),
                    userEntity.getRole(),
                    userEntity.getAddress());
            return isInsert;
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
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
