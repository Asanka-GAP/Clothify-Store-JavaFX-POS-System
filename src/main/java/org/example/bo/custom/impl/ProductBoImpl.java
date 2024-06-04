package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bo.custom.ProductBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.ProductDaoImpl;
import org.example.entity.ProductEntity;
import org.example.model.Product;
import org.example.util.DaoType;

public class ProductBoImpl implements ProductBo {

    ProductDaoImpl productDaoImpl = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
    public boolean addProduct(Product product) {
        return productDaoImpl.insert(new ObjectMapper().convertValue(product, ProductEntity.class));
    }

    public String generateProductId() {
        String id = productDaoImpl.getLatestId();

        int number = Integer.parseInt(id.split("P")[1]);
        number++;
        return String.format("P%04d", number);
    }
}
