package org.example.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.bo.custom.impl.OrderBoImpl;
import org.example.bo.custom.impl.PlaceOrderBoImpl;
import org.example.model.*;
import org.example.util.BoType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewOrderFormController implements Initializable {

    public JFXButton reportViewBtn;
    @FXML
    private Text orderDateTxt;

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
    private Text cusAddressTxt;

    @FXML
    private Text cusEmailTxt;

    @FXML
    private Text cusIdTxt;

    @FXML
    private Text cusNameTxt;

    @FXML
    private Text orderIDtxt;

    @FXML
    private TableColumn<?, ?> orderIdCol;

    @FXML
    private Text priceTxt;

    @FXML
    private Text productIdTxt;

    @FXML
    private TableColumn<?, ?> productNameCol;

    @FXML
    private Text productNameTxt;

    @FXML
    private TableColumn<?, ?> qtyCol;

    @FXML
    private Text totalTxt;

    @FXML
    private AnchorPane viewOrderWindow;

    PlaceOrderBoImpl placeOrderBo = BoFactory.getInstance().getBo(BoType.CART);
    OrderBoImpl orderBo = BoFactory.getInstance().getBo(BoType.ORDER);
    CustomerBoImpl customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);

    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();

    String id;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        cartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        orderIdCol.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        reportViewBtn.setDisable(true);

        cartTable.setItems(placeOrderBo.getAllOrderedProducts());

        List<Long> orderCount = orderBo.getOrderCount();
        List<String> empIds = orderBo.getEmpIds();

        String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store" +
                "\\src\\main\\resources\\report\\EmpOrderChart.jrxml";

        JasperReport report = null;
        try {
            report = JasperCompileManager.compileReport(path);
            String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store" +
                    "\\src\\main\\resources\\reportPdf\\orderReport\\BestEmployeeReport.pdf";

            List<EmployeeProgress> list = new ArrayList<EmployeeProgress>();

            orderCount.forEach(count -> {
                EmployeeProgress employeeProgress = new EmployeeProgress();
                employeeProgress.setCount(count);
                list.add(employeeProgress);
            });

            empIds.forEach(id -> {
                int index = empIds.indexOf(id);
                EmployeeProgress employeeProgress = list.get(index);
                employeeProgress.setId(id);
                list.set(index,employeeProgress);
            });


            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,null,dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint,savePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
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
    void customerDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewOrderWindow,"viewCustomer-form.fxml");
    }

    @FXML
    void manageEmployeeBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewOrderWindow,"adminDashBoard-form.fxml");
    }

    @FXML
    void orderDetailsBtnAction(ActionEvent event) throws IOException {

    }

    @FXML
    void productDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewOrderWindow,"viewProduct-form.fxml");
    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewOrderWindow,"viewOrder-form.fxml");
    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("Are you sure want to Sign Out..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            SceneSwitchController.getInstance().switchScene(viewOrderWindow, "dashBoard-form.fxml");
        }
    }

    @FXML
    void suppliersDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewOrderWindow,"viewSupplier-form.fxml");
    }

    @FXML
    void tableMouseClickedAction(MouseEvent event) {
        int index = cartTable.getSelectionModel().getSelectedIndex();


        try{
            String orderId = orderIdCol.getCellData(index).toString();
            String productId = productNameCol.getCellData(index).toString();
            Product product = placeOrderBo.getProductById(productId);

            id = orderId;

            reportViewBtn.setDisable(false);

            Order order = orderBo.getOrderById(orderId);
            Customer customer = customerBo.getUserById(order.getCusId());

            cusAddressTxt.setText(customer.getAddress());
            cusIdTxt.setText(customer.getId());
            cusEmailTxt.setText(customer.getEmail());
            cusNameTxt.setText(customer.getName());

            orderIDtxt.setText(orderId);
            orderDateTxt.setText(String.valueOf(order.getDate()));
            totalTxt.setText("Rs. "+Double.toString(order.getAmount())+"0");
            productIdTxt.setText(productId);
            availableQTYTxt.setText(Integer.toString(product.getQty()));
            productNameTxt.setText(product.getName());
            categoryTxt.setText(product.getCategory());
            priceTxt.setText("Rs. "+Double.toString(product.getPrice())+"0");




        }catch (Exception e){}

    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewOrderWindow,"adminDashBoard-form.fxml");
    }


    public void reportViewOnAction(ActionEvent actionEvent) throws IOException {
        File file = new File("D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store" +
                "\\src\\main\\resources\\reportPdf\\orderReport\\"+id+".pdf");

        if (file.exists()){
            if (Desktop.isDesktopSupported()){
                Desktop.getDesktop().open(file);
            }else {

            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Report Not Found..!!!").show();
        }

    }

    public void bestEmpViewOnAction(ActionEvent actionEvent) throws IOException {
        File file = new File("D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store" +
                "\\src\\main\\resources\\reportPdf\\orderReport\\BestEmployeeReport.pdf");

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
