package org.example.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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

public class PlaceOrderFormController implements Initializable {

    public JFXComboBox proIdComboBox;
    public JFXTextField productNameTxt;
    public JFXTextField cusNameTxt;
    public JFXTextField cusEmailTxt;
    public JFXTextField cusAddressTxt;
    public Text priceTxt;
    public Text pId;
    public Text timeLbl;
    public Text dateLbl;
    public FontAwesomeIconView binBtn;
    CustomerBoImpl customerBoImpl = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    PlaceOrderBoImpl placeOrderBoImpl = BoFactory.getInstance().getBo(BoType.CART);
    OrderBoImpl orderBoImpl = BoFactory.getInstance().getBo(BoType.ORDER);

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
    private JFXComboBox<String> cusIdComboBox;

    @FXML
    private TableColumn<?, ?> cusNameCol;

    @FXML
    private Text empIdLable;

    @FXML
    private Text errorMsgtxt;

    @FXML
    private Text orderIdtxt;

    @FXML
    private JFXTextField orderingQtyTxt;

    @FXML
    private AnchorPane placeOrderWindow;


    @FXML
    private TableColumn<?, ?> productNameCol;

    @FXML
    private TableColumn<?, ?> qtyCol;

    @FXML
    private Text totalTxt;

    ObservableList<OrderHasItem> cartList = FXCollections.observableArrayList();

    ObservableList<Product> productsList = FXCollections.observableArrayList();
    boolean isCustomerSelect,isProductSelect,isQtyValid,isRowSelect;
     int cid =1;
     String productId,customerId,selectdColPID;
    boolean isAlreadyAdd =false;
    int index;
    Product product;
    int oid,seletedRowQty;

    int num =1;
    @FXML
    void addToCartOnAction(MouseEvent event) {

        if (Integer.parseInt(orderingQtyTxt.getText())>Integer.parseInt(availableQTYTxt.getText())){
            new Alert(Alert.AlertType.ERROR,"Your ordering quantity is out of exceed").show();
        }else {

            if (isQtyValid && isProductSelect && isCustomerSelect) {
                int qty = Integer.parseInt(orderingQtyTxt.getText());
                double totalAmount = Double.parseDouble(priceTxt.getText()) * qty;

                double total = Double.parseDouble(totalTxt.getText());
                total += totalAmount;
                totalTxt.setText(Double.toString(total) + "0");

                for (int i = 0; i < cartList.size(); i++) {
                    OrderHasItem orderHasItem = cartList.get(i);
                    if (orderHasItem.getProductId().equals(pId.getText())) {
                        double amount = orderHasItem.getAmount();
                        int newQty = orderHasItem.getQty();
                        newQty += qty;
                        amount += totalAmount;

                        OrderHasItem updateOrderHasItem = new OrderHasItem(orderHasItem.getId(), cusNameTxt.getText(), pId.getText(), newQty, amount);

                        cartList.set(i, updateOrderHasItem);
                        isAlreadyAdd = true;
                        break;
                    }
                }
                if (!isAlreadyAdd) {
                    OrderHasItem cart = new OrderHasItem(cid++, cusNameTxt.getText(), pId.getText(), qty, totalAmount);
                    cartList.add(cart);
                }
                cartTable.setItems(cartList);
                isAlreadyAdd = false;

                productsList.forEach(product1 -> {
                    if (product1.getId().equals(pId.getText())) {
                        int newQ = product1.getQty();
                        newQ -= Integer.parseInt(orderingQtyTxt.getText());
                        product1.setQty(newQ);
                        availableQTYTxt.setText(Integer.toString(product1.getQty()));
                    }
                });

            }
            orderingQtyTxt.setText("");
        }

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
    void placeOrderOnAction(MouseEvent event) {



        Date date = new Date();
        Order order = new Order(orderIdtxt.getText(),customerId,"Pending",date,Double.parseDouble(totalTxt.getText()));

       boolean isSaved = orderBoImpl.saveOrder(order);


        ObservableList<OrderHasItem> orderHasItemObservableList =FXCollections.observableArrayList();

        cartList.forEach(orderHasItem -> {

            orderHasItemObservableList.add(new OrderHasItem(oid++, orderIdtxt.getText(),orderHasItem.getProductId(),orderHasItem.getQty(),orderHasItem.getAmount()));
        });


        boolean isSavedOrderDetails = placeOrderBoImpl.saveOrderDetails(orderHasItemObservableList);
        if (isSavedOrderDetails && isSaved){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed..!!!");
            alert.setContentText("Order Placed Successfully");
            alert.showAndWait();
            productsList = placeOrderBoImpl.getAllProducts();
            orderIdtxt.setText(placeOrderBoImpl.generateOrderId());
            cartList.clear();
            cartTable.setItems(null);
            oid= placeOrderBoImpl.getLatestCartId();
            cid = 1;
            cusIdComboBox.setDisable(false);
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
    void userBtnMouseClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loadDateTime();
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        cartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));

        oid = placeOrderBoImpl.getLatestCartId();
        productsList = placeOrderBoImpl.getAllProducts();

        pId.setVisible(false);
        isRowSelect = false;
        isAlreadyAdd =false;
        priceTxt.setVisible(false);
        errorMsgtxt.setVisible(false);
        isCustomerSelect = false;
        isProductSelect = false;
        cusIdComboBox.setItems(customerBoImpl.getAllCustomerIds());
        proIdComboBox.setItems(placeOrderBoImpl.getProductIds());
        cusEmailTxt.setDisable(true);
        cusAddressTxt.setDisable(true);
        cusEmailTxt.setDisable(true);
        productNameTxt.setDisable(true);
        cusNameTxt.setDisable(true);

        orderIdtxt.setText(placeOrderBoImpl.generateOrderId());

        cusIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            isCustomerSelect = true;
            Customer customer = customerBoImpl.getUserById(newValue);
            cusNameTxt.setText(customer.getName());
            cusAddressTxt.setText(customer.getAddress());
            cusEmailTxt.setText(customer.getEmail());
            customerId = newValue;
            cusIdComboBox.setDisable(true);
        });
        proIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            isProductSelect = true;

            productsList.forEach(product1 -> {
                if (product1.getId().equals((String) newValue)){
                    product = product1;
                }
            });

            productNameTxt.setText(product.getName());
            availableQTYTxt.setText(Integer.toString(product.getQty()));
            categoryTxt.setText(product.getCategory());
            priceTxt.setText(Double.toString(product.getPrice()));
            productId = product.getId();
            pId.setText(product.getId());
            priceTxt.setVisible(true);


        });
    }

    public void orderQTYKeyReleased(KeyEvent keyEvent) {

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

    public void deleteFromCartOnAction(MouseEvent mouseEvent) {

        if (isRowSelect){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove From Cart");
            alert.setContentText("Are you sure want to remove this item");
            Optional<ButtonType> type = alert.showAndWait();

            if (type.get()==ButtonType.OK) {
                cartList.remove(index);
                cartTable.setItems(cartList);
                isRowSelect = false;
                productsList.forEach(product1 -> {
                    if (product1.getId().equals(selectdColPID)) {
                        int newQ = product1.getQty();
                        newQ += seletedRowQty;
                        product1.setQty(newQ);
                        availableQTYTxt.setText("");
                    }
                });
                return;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not Row Selected");
        alert.setContentText("Please select the row if you want to remove");
        alert.showAndWait();
    }

    public void tableMouseClickedAction(MouseEvent mouseEvent) {
         index = cartTable.getSelectionModel().getSelectedIndex();

        selectdColPID = productNameCol.getCellData(index).toString();
        seletedRowQty = (int) qtyCol.getCellData(index);
         isRowSelect = true;
        if(index < 0){
            return;
        }
    }

    private void loadDateTime(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateLbl.setText(format.format(date));

        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO,e->{
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


}
