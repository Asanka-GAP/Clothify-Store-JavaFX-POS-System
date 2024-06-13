package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.bo.custom.PlaceOrderBo;
import org.example.controller.DashBoardController;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.OrderDaoImpl;
import org.example.dao.custom.impl.PlaceOrderDaoImpl;
import org.example.dao.custom.impl.ProductDaoImpl;
import org.example.entity.OrderHasItemEntity;
import org.example.entity.ProductEntity;
import org.example.model.OrderHasItem;
import org.example.model.Product;
import org.example.model.ProductSummary;
import org.example.util.DaoType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlaceOrderBoImpl implements PlaceOrderBo {

    PlaceOrderDaoImpl placeOrderDao = DaoFactory.getInstance().getDao(DaoType.CART);
    ProductDaoImpl productDao = DaoFactory.getInstance().getDao(DaoType.PRODUCT);
    OrderDaoImpl orderDao = DaoFactory.getInstance().getDao(DaoType.ORDER);
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

    public ObservableList<OrderHasItem> getAllOrderedProducts() {
        return placeOrderDao.getAll();
    }

    public boolean deleteById(String id) {
        return placeOrderDao.deleteByOrderId(id);
    }

    public ObservableList<OrderHasItem> getProductIdsByOrderId(String id) {

        return placeOrderDao.getProductIdsByOrderId(id);
    }


    public boolean increseQty(ObservableList<OrderHasItem> productIdList) {
        return productDao.increseQty(productIdList);
    }

    public boolean updateNewQty(String id,int qty) {
        productDao.updateQty(id,qty);
        return true;
    }

    public boolean updateOrderAmount(String id, double newAmount) {
        return orderDao.updateAmountById(id,newAmount);
    }

    public boolean updateCartById(int id, int i, double newAmount) {
        return placeOrderDao.updateQtyAndAmount(id,i,newAmount);
    }

    public void sendEmail(String receiveEmail,String text,File file) throws MessagingException {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myEmail = "asankapradeep0830@gmail.com";
        String password = "tkmkeibffwnpwjcp";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail,password);
            }
        });

        Message message = prepareMessage(session,myEmail,receiveEmail,text,file);
        Transport.send(message);
    }

    public Message prepareMessage(Session session, String myEmail, String receiveEmail, String text,File file) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO,new InternetAddress[]{
                    new InternetAddress(receiveEmail)
            });
            message.setSubject("Your Order");
            message.setText(text);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.attachFile(file);
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            return message;
        }catch (Exception e){
            Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE,null,e);
        }
        return null;
    }


    public boolean increaseQtyOfProduct(String id, int qty) {

        return productDao.updateQtyOfProduct(id,qty);
    }

    public boolean decreaseAmountByOrderId(String id, double amount) {
        return orderDao.deacreseAmount(id,amount);
    }

    public boolean removeFromCart(String oId, String pId) {
        return placeOrderDao.removeItem(oId,pId);
    }

    public void generateProductChartReport() throws JRException {
        String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\report\\ProductChart.jrxml";
        JasperReport report = JasperCompileManager.compileReport(path);

        String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\productSummaryReport\\ProductChart.pdf";

        List<ProductSummary> list = new ArrayList<ProductSummary>();

        ObservableList<String> observableList1 = productDao.searchAllIds();

        ObservableList<ProductSummary> maxQty = placeOrderDao.findMaxQty(observableList1);

        maxQty.forEach(productSummary -> {
            list.add(productSummary);
        });

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report,null,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,savePath);

    }
}
