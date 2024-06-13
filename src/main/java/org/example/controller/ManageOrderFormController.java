package org.example.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.bo.custom.impl.OrderBoImpl;
import org.example.bo.custom.impl.PlaceOrderBoImpl;
import org.example.model.*;
import org.example.util.BoType;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

public class ManageOrderFormController implements Initializable {

    public Text productNameTxt;
    public Text productIdTxt;
    public Text cusAddressTxt;
    public Text cusEmailTxt;
    public Text cusNameTxt;
    public Text cusIdTxt;
    public Text orderIDtxt;
    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private Text availableQTYTxt;

    @FXML
    private TableColumn<?, ?> cartIdCol;

    @FXML
    private TableView<OrderHasItem> cartTable;

    @FXML
    private Text categoryTxt;

    @FXML
    private Text dateLbl;

    @FXML
    private Text errorMsgtxt;

    @FXML
    private AnchorPane manageOrderWindow;

    @FXML
    private TableColumn<?, ?> orderIdCol;

    @FXML
    private JFXTextField orderingQtyTxt;

    @FXML
    private Text pId;

    @FXML
    private Text priceTxt;


    @FXML
    private TableColumn<?, ?> productNameCol;


    @FXML
    private TableColumn<?, ?> qtyCol;

    @FXML
    private Text timeLbl;

    @FXML
    private Text totalTxt;
    private boolean isRowSelect, isQtyValid;
    int index, seletedRowQty, selectedCartId;
    String selectdColPID;

    double selectedAmount;
    int invoiceCid = 1;
    int count = 0;
    PlaceOrderBoImpl placeOrderBoImpl = BoFactory.getInstance().getBo(BoType.CART);
    OrderBoImpl orderBoImpl = BoFactory.getInstance().getBo(BoType.ORDER);
    CustomerBoImpl customerBoImpl = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        cartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        cartTable.setItems(placeOrderBoImpl.getAllOrderedProducts());

        isQtyValid = false;
        errorMsgtxt.setVisible(false);
        isRowSelect = false;
        loadDateTime();
    }

    @FXML
    void closeBtnAction(MouseEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure want to exit..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }

    }

    @FXML
    void deleteOrderOnAction(MouseEvent event) throws JRException, MessagingException {
        if (isRowSelect) {
            Alert alertMain = new Alert(Alert.AlertType.CONFIRMATION);
            alertMain.setTitle("Remove an item");
            alertMain.setContentText("Are you sure want to remove this item ?");
            Optional<ButtonType> type1 = alertMain.showAndWait();
            if (type1.get()==ButtonType.OK) {

                ObservableList<OrderHasItem> allOrderedProducts = placeOrderBoImpl.getAllOrderedProducts();

                allOrderedProducts.forEach(orderHasItem -> {
                    if (orderIDtxt.getText().equals(orderHasItem.getOrderId())) {
                        count++;
                    }
                });

                if (count > 1) {

                    boolean isIncreseQty = placeOrderBoImpl.increaseQtyOfProduct(productIdTxt.getText(), seletedRowQty);
                    boolean isDecreaseAmount = placeOrderBoImpl.decreaseAmountByOrderId(orderIDtxt.getText(), selectedAmount);
                    boolean isRemoveFromCart = placeOrderBoImpl.removeFromCart(orderIDtxt.getText(), productIdTxt.getText());

                    if (isIncreseQty && isDecreaseAmount && isRemoveFromCart) {
                        new Alert(Alert.AlertType.INFORMATION, "Removed Successfully").show();

                        String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\report\\invoice_1.jrxml";
                        Map<String, Object> parameters = new HashMap();
                        JasperReport report = JasperCompileManager.compileReport(path);

                        String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\orderReport\\" + orderIDtxt.getText() + ".pdf";

                        parameters.put("cusId", cusIdTxt.getText());
                        parameters.put("cusName", cusNameTxt.getText());
                        parameters.put("email", cusEmailTxt.getText());
                        parameters.put("address", cusAddressTxt.getText());
                        parameters.put("orderId", orderIDtxt.getText());
                        parameters.put("total", Double.parseDouble(totalTxt.getText()));

                        EmployeeData instance = EmployeeData.getInstance();

                        parameters.put("empId", instance.getId());
                        parameters.put("empName", instance.getName());

                        List<Cart> list = new ArrayList<Cart>();

                        ObservableList<OrderHasItem> productIdsByOrderId = placeOrderBoImpl.getProductIdsByOrderId(orderIDtxt.getText());

                        productIdsByOrderId.forEach(orderHasItem -> {
                            Product product1 = placeOrderBoImpl.getProductById(orderHasItem.getProductId());

                            Cart cart = new Cart(invoiceCid++, product1.getId(), product1.getName(), orderHasItem.getQty(), orderHasItem.getAmount());

                            list.add(cart);
                        });

                        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
                        JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
                        JasperExportManager.exportReportToPdfFile(jasperPrint, savePath);

                        invoiceCid = 1;

                        File file = new File(savePath);
                        placeOrderBoImpl.sendEmail(cusEmailTxt.getText(),"Your order Details",file);

                        new Alert(Alert.AlertType.INFORMATION,"Email sent").show();

                        cartTable.setItems(placeOrderBoImpl.getAllOrderedProducts());

                        System.out.println("OK");
                    }

                } else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Delete Entire Order");
                    alert.setContentText("Are you sure want wo delete this whole order ?");
                    Optional<ButtonType> type = alert.showAndWait();

                    if (type.get() == ButtonType.OK) {

                        ObservableList<OrderHasItem> productIdList = placeOrderBoImpl.getProductIdsByOrderId(orderIDtxt.getText());

                        boolean isUpdateQty = placeOrderBoImpl.increseQty(productIdList);

                        boolean isDeleted = orderBoImpl.deleteOrderById(orderIDtxt.getText());

                        boolean isDeleted2 = placeOrderBoImpl.deleteById(orderIDtxt.getText());

                        if (isDeleted2 && isDeleted && isUpdateQty) {
                            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                            alert1.setTitle("Order Deleted");
                            alert1.setContentText("Order Deleted Successfully..!!!");
                            alert1.showAndWait();
                            cartTable.setItems(placeOrderBoImpl.getAllOrderedProducts());
                        }
                    }
                }
            }
        }else {
            new Alert(Alert.AlertType.ERROR, "Please select the row if you want to remove").show();
        }
    }

    @FXML
    void manageCustomersBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(manageOrderWindow, "customer-form.fxml");
    }

    @FXML
    void manageOrdersBtnAction(ActionEvent event) {

    }

    @FXML
    void manageProductsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(manageOrderWindow, "product-form.fxml");
    }

    @FXML
    void manageSuppliersBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(manageOrderWindow, "supplier-form.fxml");
    }

    @FXML
    void orderQTYKeyReleased(KeyEvent event) {

        if (isRowSelect) {
            try {
                int qty = Integer.parseInt(orderingQtyTxt.getText());
                errorMsgtxt.setVisible(false);
                isQtyValid = true;
                if (qty > Integer.parseInt(availableQTYTxt.getText()) || qty < 1) {
                    isQtyValid = false;
                    errorMsgtxt.setVisible(true);
                }
            } catch (Exception e) {
                isQtyValid = false;
                errorMsgtxt.setVisible(true);
            }
        }

    }

    @FXML
    void placeSectionBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(manageOrderWindow, "placeOrder-form.fxml");
    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(manageOrderWindow, "placeOrder-form.fxml");
    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("Are you sure want to Sign Out..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            SceneSwitchController.getInstance().switchScene(manageOrderWindow, "dashBoard-form.fxml");
        }
    }

    @FXML
    void tableMouseClickedAction(MouseEvent event) {
        index = cartTable.getSelectionModel().getSelectedIndex();

        try {
            isRowSelect = true;
            selectdColPID = productNameCol.getCellData(index).toString();
            String orderId = orderIdCol.getCellData(index).toString();
            seletedRowQty = (int) qtyCol.getCellData(index);
            selectedCartId = (int) cartIdCol.getCellData(index);
            selectedAmount = (double) amountCol.getCellData(index);

            Order order = orderBoImpl.getOrderById(orderId);
            Customer customer = customerBoImpl.getUserById(order.getCusId());
            Product product = placeOrderBoImpl.getProductById(selectdColPID);

            cusIdTxt.setText(customer.getId());
            cusNameTxt.setText(customer.getName());
            cusEmailTxt.setText(customer.getEmail());
            cusAddressTxt.setText(customer.getAddress());

            orderIDtxt.setText(order.getId());
            totalTxt.setText(Double.toString(order.getAmount())+"0");

            productIdTxt.setText(product.getId());
            productNameTxt.setText(product.getName());
            priceTxt.setText(Double.toString(product.getPrice()));
            availableQTYTxt.setText(Integer.toString(product.getQty()));
            categoryTxt.setText(product.getCategory());
            isRowSelect = true;
            if (index < 0) {
                return;
            }
        } catch (Exception e) {
        }

    }

    @FXML
    void updateOrderOnAction(MouseEvent event) throws JRException, MessagingException {

        if (isRowSelect && isQtyValid) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Order");
            alert.setContentText("Are you want to Update this order ?");
            Optional<ButtonType> type = alert.showAndWait();

            if (type.get() == ButtonType.OK) {
                boolean isUpdateQty = placeOrderBoImpl.updateNewQty(selectdColPID, Integer.parseInt(orderingQtyTxt.getText()));
                double newAmount = Double.parseDouble(priceTxt.getText()) * Integer.parseInt(orderingQtyTxt.getText());

                boolean isUpdateOrderAmount = placeOrderBoImpl.updateOrderAmount(orderIDtxt.getText(), newAmount);
                boolean isUpdateCart = placeOrderBoImpl.updateCartById(selectedCartId, Integer.parseInt(orderingQtyTxt.getText()), newAmount);

                if (isUpdateOrderAmount && isUpdateCart && isUpdateQty) {
                    cartTable.setItems(placeOrderBoImpl.getAllOrderedProducts());
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Cart Update");
                    alert1.setContentText("Your Update Successfully");
                    alert1.showAndWait();
                    orderingQtyTxt.setText("");

                    ObservableList<OrderHasItem> allOrderedProducts = placeOrderBoImpl.getProductIdsByOrderId(orderIDtxt.getText());


                    String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\report\\invoice_1.jrxml";
                    Map<String, Object> parameters = new HashMap();
                    JasperReport report = JasperCompileManager.compileReport(path);

                    String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\orderReport\\" + orderIDtxt.getText() + ".pdf";

                    parameters.put("cusId", cusIdTxt.getText());
                    parameters.put("cusName", cusNameTxt.getText());
                    parameters.put("email", cusEmailTxt.getText());
                    parameters.put("address", cusAddressTxt.getText());
                    parameters.put("orderId", orderIDtxt.getText());
                    parameters.put("total", Double.parseDouble(totalTxt.getText()));

                    EmployeeData instance = EmployeeData.getInstance();

                    parameters.put("empId", instance.getId());
                    parameters.put("empName", instance.getName());

                    List<Cart> list = new ArrayList<Cart>();

                    allOrderedProducts.forEach(orderHasItem -> {
                        Product product1 = placeOrderBoImpl.getProductById(orderHasItem.getProductId());

                        Cart cart = new Cart(invoiceCid++, product1.getId(), product1.getName(), orderHasItem.getQty(), orderHasItem.getAmount());

                        list.add(cart);
                    });

                    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
                    JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource);
                    JasperExportManager.exportReportToPdfFile(jasperPrint, savePath);

                    invoiceCid = 1;

                    File file = new File(savePath);
                    placeOrderBoImpl.sendEmail(cusEmailTxt.getText(),"Your order Details",file);

                    new Alert(Alert.AlertType.INFORMATION,"Email sent").show();


                }
            }
        }
    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) {

    }

    private void loadDateTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateLbl.setText(format.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime localTime = LocalTime.now();
            timeLbl.setText(
                    localTime.getHour() + " : " + localTime.getMinute() + " : " + localTime.getSecond()
            );
        }),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void deleteWholeOrderOnAction(MouseEvent mouseEvent) {

        if (isRowSelect) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Entire Order");
            alert.setContentText("Are you sure want wo delete this whole order ?");
            Optional<ButtonType> type = alert.showAndWait();

            if (type.get() == ButtonType.OK) {

                ObservableList<OrderHasItem> productIdList = placeOrderBoImpl.getProductIdsByOrderId(orderIDtxt.getText());

                boolean isUpdateQty = placeOrderBoImpl.increseQty(productIdList);

                boolean isDeleted = orderBoImpl.deleteOrderById(orderIDtxt.getText());

                boolean isDeleted2 = placeOrderBoImpl.deleteById(orderIDtxt.getText());

                if (isDeleted2 && isDeleted && isUpdateQty) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Order Deleted");
                    alert1.setContentText("Order Deleted Successfully..!!!");
                    alert1.showAndWait();
                    cartTable.setItems(placeOrderBoImpl.getAllOrderedProducts());
                }
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please select the row if you want to delete").show();
        }
    }
}
