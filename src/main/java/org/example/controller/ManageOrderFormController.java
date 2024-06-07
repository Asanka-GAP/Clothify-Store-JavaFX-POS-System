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
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.bo.custom.impl.OrderBoImpl;
import org.example.bo.custom.impl.PlaceOrderBoImpl;
import org.example.model.Customer;
import org.example.model.Order;
import org.example.model.OrderHasItem;
import org.example.model.Product;
import org.example.util.BoType;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private boolean isRowSelect,isQtyValid;
    int index,seletedRowQty,selectedCartId;
    String selectdColPID;

    PlaceOrderBoImpl placeOrderBoImpl = BoFactory.getInstance().getBo(BoType.CART);
    OrderBoImpl orderBoImpl = BoFactory.getInstance().getBo(BoType.ORDER);
    CustomerBoImpl customerBoImpl = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        cartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        cartTable.setItems(placeOrderBoImpl.getAllOrderedProducts());

        isQtyValid =false;
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
    void deleteOrderOnAction(MouseEvent event) {

    }

    @FXML
    void manageCustomersBtnAction(ActionEvent event) {

    }

    @FXML
    void manageOrdersBtnAction(ActionEvent event) {

    }

    @FXML
    void manageProductsBtnAction(ActionEvent event) {

    }

    @FXML
    void manageSuppliersBtnAction(ActionEvent event) {

    }

    @FXML
    void orderQTYKeyReleased(KeyEvent event) {

        if (isRowSelect){
            try {
                int qty = Integer.parseInt(orderingQtyTxt.getText());
                errorMsgtxt.setVisible(false);
                isQtyValid = true;
                if (qty>Integer.parseInt(availableQTYTxt.getText()) || qty<1){
                    isQtyValid =false;
                    errorMsgtxt.setVisible(true);
                }
            }catch (Exception e){
                isQtyValid = false;
                errorMsgtxt.setVisible(true);
            }
        }

    }

    @FXML
    void placeSectionBtnAction(ActionEvent event) {

    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) {

    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) {

    }

    @FXML
    void tableMouseClickedAction(MouseEvent event) {
        index = cartTable.getSelectionModel().getSelectedIndex();

        try {
            isRowSelect = true;
            selectdColPID = productNameCol.getCellData(index).toString();
            String orderId = orderIdCol.getCellData(index).toString();
            seletedRowQty = (int) qtyCol.getCellData(index);
            selectedCartId =(int) cartIdCol.getCellData(index);

            Order order = orderBoImpl.getOrderById(orderId);
            Customer customer = customerBoImpl.getUserById(order.getCusId());
            Product product = placeOrderBoImpl.getProductById(selectdColPID);

            cusIdTxt.setText(customer.getId());
            cusNameTxt.setText(customer.getName());
            cusEmailTxt.setText(customer.getEmail());
            cusAddressTxt.setText(customer.getAddress());

            orderIDtxt.setText(order.getId());

            productIdTxt.setText(product.getId());
            productNameTxt.setText(product.getName());
            priceTxt.setText(Double.toString(product.getPrice()));
            availableQTYTxt.setText(Integer.toString(product.getQty()));
            categoryTxt.setText(product.getCategory());
            isRowSelect = true;
            if (index < 0) {
                return;
            }
        }catch (Exception e){}

    }

    @FXML
    void updateOrderOnAction(MouseEvent event) {

        if (isRowSelect && isQtyValid){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Update Order");
            alert.setContentText("Are you want to Update this order ?");
            Optional<ButtonType> type = alert.showAndWait();

            if (type.get()==ButtonType.OK){
                boolean isUpdateQty = placeOrderBoImpl.updateNewQty(selectdColPID,Integer.parseInt(orderingQtyTxt.getText()));
                double newAmount = Double.parseDouble(priceTxt.getText())*Integer.parseInt(orderingQtyTxt.getText());

                boolean isUpdateOrderAmount = placeOrderBoImpl.updateOrderAmount(orderIDtxt.getText(),newAmount);
                boolean isUpdateCart = placeOrderBoImpl.updateCartById(selectedCartId,Integer.parseInt(orderingQtyTxt.getText()),newAmount);

                if (isUpdateOrderAmount && isUpdateCart && isUpdateQty){
                    cartTable.setItems(placeOrderBoImpl.getAllOrderedProducts());
                    Alert alert1= new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Cart Update");
                    alert1.setContentText("Your Update Successfully");
                    alert1.showAndWait();
                }
            }
        }
    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) {

    }
    private void loadDateTime(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateLbl.setText(format.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime localTime = LocalTime.now();
            timeLbl.setText(
                    localTime.getHour()+" : "+localTime.getMinute()+" : "+localTime.getSecond()
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

                ObservableList<OrderHasItem> productIdList = FXCollections.observableArrayList();

                productIdList = placeOrderBoImpl.getProductIdsByOrderId(orderIDtxt.getText());


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
        }else{
            new Alert(Alert.AlertType.ERROR,"Please select the row if you want to delete").show();
        }
    }
}
