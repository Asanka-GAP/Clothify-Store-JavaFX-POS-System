package org.example.dao;
import javafx.collections.ObservableList;
import java.sql.SQLException;

public interface CrudDao<T,S> extends SuperDao{

    T search(S s);

    ObservableList<T> searchAll();

    boolean insert(T t);

    boolean update(T t);

    boolean delete(S s);
}
