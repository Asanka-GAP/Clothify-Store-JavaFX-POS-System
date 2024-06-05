package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.ObservableList;
import org.example.bo.custom.PlaceOrderBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.PlaceOrderDaoImpl;
import org.example.dao.custom.impl.ProductDaoImpl;
import org.example.entity.ProductEntity;
import org.example.model.Product;
import org.example.util.DaoType;

public class PlaceOrderBoImpl implements PlaceOrderBo {

    PlaceOrderDaoImpl placeOrderDao = DaoFactory.getInstance().getDao(DaoType.CART);
    ProductDaoImpl productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
    public ObservableList<String> getProductIds() {
        return productDao.searchAllIds();

    }

    public Product getProductById(String newValue) {
        ProductEntity productEntity = productDao.search(newValue);
        return new ObjectMapper().convertValue(productEntity, Product.class);
    }
}
