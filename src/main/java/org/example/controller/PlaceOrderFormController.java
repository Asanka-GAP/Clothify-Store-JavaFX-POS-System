package org.example.controller;

import com.jfoenix.controls.JFXButton;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.bo.custom.impl.OrderBoImpl;
import org.example.bo.custom.impl.PlaceOrderBoImpl;
import org.example.model.*;
import org.example.util.BoType;

import javax.mail.MessagingException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

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
    public JFXButton reportViewBtn;
    CustomerBoImpl customerBoImpl = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    PlaceOrderBoImpl placeOrderBoImpl = BoFactory.getInstance().getBo(BoType.CART);
    OrderBoImpl orderBoImpl = BoFactory.getInstance().getBo(BoType.ORDER);

    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();

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
     String productId,customerId,selectdColPID,orderid;
    boolean isAlreadyAdd =false;
    int index;
    int invoiceCid = 1;
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
    void manageCustomersBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(placeOrderWindow,"customer-form.fxml");
    }

    @FXML
    void manageOrdersBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(placeOrderWindow,"manageOrder-form.fxml");
    }

    @FXML
    void manageProductsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(placeOrderWindow,"product-form.fxml");
    }

    @FXML
    void manageSuppliersBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(placeOrderWindow,"supplier-form.fxml");
    }

    @FXML
    void placeOrderOnAction(MouseEvent event) throws JRException, MessagingException {


        String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\report\\invoice_1.jrxml";

        Map<String,Object> parameters = new HashMap();


        JasperReport report = JasperCompileManager.compileReport(path);

        orderid = orderIdtxt.getText();

        Date date = new Date();
        String id = EmployeeData.getInstance().getId();
        Order order = new Order(orderIdtxt.getText(),customerId,"Pending",date,Double.parseDouble(totalTxt.getText()),id);

        String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\orderReport\\"+orderIdtxt.getText()+".pdf";

        Customer customer = customerBoImpl.getUserById(customerId);
        parameters.put("cusId",customerId);
        parameters.put("cusName",customer.getName());
        parameters.put("email",customer.getEmail());
        parameters.put("address",customer.getAddress());
        parameters.put("orderId",orderIdtxt.getText());
        parameters.put("total",Double.parseDouble(totalTxt.getText()));

        EmployeeData instance = EmployeeData.getInstance();

        parameters.put("empId",instance.getId());
        parameters.put("empName",instance.getName());


        boolean isSaved = orderBoImpl.saveOrder(order);


       ObservableList<OrderHasItem> orderHasItemObservableList =FXCollections.observableArrayList();
        List<Cart> list = new ArrayList<Cart>();

        cartList.forEach(orderHasItem -> {

            Product product1 = placeOrderBoImpl.getProductById(orderHasItem.getProductId());

             Cart cart = new Cart(invoiceCid++,product1.getId(),product1.getName(),orderHasItem.getQty(),orderHasItem.getAmount());

            list.add(cart);


            orderHasItemObservableList.add(new OrderHasItem(oid++, orderIdtxt.getText(),orderHasItem.getProductId(),orderHasItem.getQty(),orderHasItem.getAmount()));
        });

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
        JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint,savePath);


            invoiceCid = 1;

        boolean isSavedOrderDetails = placeOrderBoImpl.saveOrderDetails(orderHasItemObservableList);
        if (isSavedOrderDetails && isSaved){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed..!!!");
            alert.setContentText("Order Placed Successfully");
            alert.showAndWait();

            File file = new File(savePath);
            placeOrderBoImpl.sendEmail(customer.getEmail(),"Your order Details",file);

            new Alert(Alert.AlertType.INFORMATION,"Email sent").show();

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
    void signOutBtnMouseClicked(MouseEvent event) throws IOException {

        Alert alert =new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("Are you sure want to sign out ?");
        Optional<ButtonType> type = alert.showAndWait();

        if (type.get()==ButtonType.OK){
            sceneSwitch.switchScene(placeOrderWindow,"dashBoard-form.fxml");
        }

    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(placeOrderWindow,"customer-form.fxml");
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


    public void reportViewOnAction(ActionEvent actionEvent) throws IOException {
        File file = new File("D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\orderReport\\"+orderid+".pdf");

        if (file.exists()){
            if (Desktop.isDesktopSupported()){
                Desktop.getDesktop().open(file);
            }else {

            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Report Not Found..!!!").show();
        }
    }
}
