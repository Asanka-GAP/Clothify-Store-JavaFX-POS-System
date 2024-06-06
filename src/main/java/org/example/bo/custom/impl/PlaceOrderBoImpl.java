package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.PlaceOrderBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.OrderDaoImpl;
import org.example.dao.custom.impl.PlaceOrderDaoImpl;
import org.example.dao.custom.impl.ProductDaoImpl;
import org.example.entity.ProductEntity;
import org.example.model.OrderHasItem;
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

    public ObservableList<Product> getAllProducts() {
        ObservableList<ProductEntity> list = productDao.searchAll();
        ObservableList<Product> products = FXCollections.observableArrayList();

        list.forEach(productEntity -> {
            products.add(new ObjectMapper().convertValue(productEntity, Product.class));
        });
        return products;
    }

    public String generateOrderId() {
        String id = new OrderDaoImpl().getLatestOrderId();

        int number = Integer.parseInt(id.split("X")[1]);
        number++;
        return String.format("X%04d", number);
    }
    public boolean saveOrderDetails(ObservableList<OrderHasItem> orderHasItemObservableList) {
        return placeOrderDao.saveAll(orderHasItemObservableList);
    }

    public int getLatestCartId(){
        return placeOrderDao.getLatestId()+1;
    }
}
