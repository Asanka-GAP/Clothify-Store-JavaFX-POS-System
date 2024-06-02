package org.example.controller.entityController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.crudUtil.CrudUtil;
import org.example.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntityController {

    private static UserEntityController instance;

    private UserEntityController(){}

    public static UserEntityController getInstance(){
        if (instance==null){
            return instance = new UserEntityController();
        }
        return instance;
    }
    public ObservableList<User> getUserByEmail(String email){
        ObservableList<User> list = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE email=?",email);

            while (resultSet.next()){

                list.add(new User(resultSet.getString(1)
                        ,resultSet.getString(2)
                        ,resultSet.getString(3)
                        ,resultSet.getString(4)
                        ,resultSet.getString(5)
                        ,resultSet.getString(6)));

            }

            return list;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
        return null;
    }
}
