package org.example.bo.custom.impl;

import org.example.bo.custom.UserBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.UserDaoImpl;
import org.example.entity.UserEntity;
import org.example.util.DaoType;

import java.sql.SQLException;

public class UserBoImpl implements UserBo {
    UserDaoImpl userDao = DaoFactory.getInstance().getDao(DaoType.USER);


    public UserEntity getUserByEmail(String email) {
        try {
            return userDao.search(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
