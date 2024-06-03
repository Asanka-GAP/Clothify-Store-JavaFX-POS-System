package org.example.dao;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public interface CrudDao<T,S> extends SuperDao{

    T search(S s) throws SQLException;

    ObservableList<T> searchAll() throws SQLException;

    boolean insert(T t) throws SQLException;

    boolean update(T t) throws SQLException;

    boolean delete(S s);
}
