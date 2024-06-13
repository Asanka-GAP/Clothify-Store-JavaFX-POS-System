package org.example.controller;

import javafx.collections.ObservableList;
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
import org.example.bo.custom.impl.PlaceOrderBoImpl;
import org.example.model.Cart;
import org.example.model.Product;
import org.example.util.BoType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewProductFormController implements Initializable {

    @FXML
    private Text availableQTYTxt;

    @FXML
    private Text categoryTxt;

    @FXML
    private TableColumn<?, ?> priceCol;

    @FXML
    private Text priceTxt;

    @FXML
    private TableColumn<?, ?> proCategoryCol;

    @FXML
    private TableColumn<?, ?> proIdCol;

    @FXML
    private TableColumn<?, ?> proNameCol;

    @FXML
    private TableColumn<?, ?> proQTYCol;

    @FXML
    private TableColumn<?, ?> proSizeCol;

    @FXML
    private Text productIdTxt;

    @FXML
    private Text productNameTxt;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private Text totalTxt;

    @FXML
    private AnchorPane viewProductWindow;

    PlaceOrderBoImpl placeOrderBo = BoFactory.getInstance().getBo(BoType.CART);
    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        productIdTxt.setText("");
        productNameTxt.setText("");
        categoryTxt.setText("");
        priceTxt.setText("");
        availableQTYTxt.setText("");

        try {
            placeOrderBo.generateProductChartReport();
        } catch (JRException e) {
            System.out.println(e);
        }

        ObservableList<Product> allProducts = placeOrderBo.getAllProducts();
        productTable.setItems(allProducts);
        proIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        proCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        proNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        proQTYCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        proSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\report\\ProductView.jrxml";

        JasperReport report = null;
        try {
            report = JasperCompileManager.compileReport(path);
            String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\productSummaryReport\\ProductReport.pdf";

            List<Product> list = new ArrayList<Product>();

            allProducts.forEach(product -> {
                list.add(product);
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
        sceneSwitch.switchScene(viewProductWindow,"viewCustomer-form.fxml");
    }

    @FXML
    void manageEmployeeBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewProductWindow,"adminDashBoard-form.fxml");
    }

    @FXML
    void orderDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewProductWindow,"viewOrder-form.fxml");
    }

    @FXML
    void productDetailsBtnAction(ActionEvent event) {

    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewProductWindow,"viewOrder-form.fxml");
    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("Are you sure want to Sign Out..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            SceneSwitchController.getInstance().switchScene(viewProductWindow, "dashBoard-form.fxml");
        }
    }

    @FXML
    void suppliersDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewProductWindow,"viewSupplier-form.fxml");
    }

    @FXML
    void tableMouseClickedAction(MouseEvent event) {


        try{
            int index = productTable.getSelectionModel().getSelectedIndex();
            if(index < 0){
                return;
            }
            String id = proIdCol.getCellData(index).toString();

            Product product = placeOrderBo.getProductById(id);
            productIdTxt.setText(id);
            productNameTxt.setText(product.getName());
            categoryTxt.setText(product.getCategory());
            priceTxt.setText(Double.toString(product.getPrice()));
            availableQTYTxt.setText(Integer.toString(product.getQty()));

        }catch (Exception e){}

    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewProductWindow,"adminDashBoard-form.fxml");
    }


    public void viewReportOnAction(ActionEvent actionEvent) throws IOException {


        File file = new File("D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\productSummaryReport\\ProductReport.pdf");
        if (file.exists()){
            if (Desktop.isDesktopSupported()){
                Desktop.getDesktop().open(file);
            }else {

            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Report Not Found..!!!").show();
        }
    }

    public void viewProgressOnAction(ActionEvent actionEvent) throws IOException{

        File file = new File("D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\productSummaryReport\\ProductChart.pdf");
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
