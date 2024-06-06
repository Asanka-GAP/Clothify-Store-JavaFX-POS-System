package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.ProductBo;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.ProductDaoImpl;
import org.example.entity.CustomerEntity;
import org.example.entity.ProductEntity;
import org.example.model.Product;
import org.example.util.DaoType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

    public ObservableList<Product> getAllProducts() {
        ObservableList<ProductEntity> productEntities = productDaoImpl.searchAll();
        ObservableList<Product> productsList = FXCollections.observableArrayList();

        productEntities.forEach(productEntity -> {
            productsList.add(new ObjectMapper().convertValue(productEntity,Product.class));
        });

        return productsList;
    }


    public Product getProductById(String id) {
        ProductEntity productEntity = productDaoImpl.search(id);
        return new ObjectMapper().convertValue(productEntity, Product.class);
    }


    public boolean updateProduct(Product product) {
        return productDaoImpl.update(new ObjectMapper().convertValue(product,ProductEntity.class));
    }

    public boolean deleteProduct(String id) {
        return productDaoImpl.delete(id);
    }


    public ObservableList<String> getAllProductIds() {
        return productDaoImpl.searchAllIds();
    }

    //------------------------------------------------------------------------------
    public BufferedImage byteArrayToBufferedImage(byte[] byteArray) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        return ImageIO.read(bais);
    }

    public InputStream bufferedImageToInputStream(BufferedImage bufferedImage) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage,"png",baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }


    public static byte[] bufferedImageToByteArray(BufferedImage bufferedImage, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, format, baos);
        return baos.toByteArray();
    }



}
