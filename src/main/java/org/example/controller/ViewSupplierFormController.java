package org.example.controller;

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
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.SupplierBoImpl;
import org.example.model.Supplier;
import org.example.util.BoType;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewSupplierFormController implements Initializable {

    public TableColumn empIdCol;
    @FXML
    private Text companyTxt;

    @FXML
    private Text emailTxt;

    @FXML
    private TableColumn<?, ?> supCompanyCol;

    @FXML
    private TableColumn<?, ?> supEmailCol;

    @FXML
    private TableColumn<?, ?> supIdCol;

    @FXML
    private Text supIdTxt;

    @FXML
    private TableColumn<?, ?> supNameCol;

    @FXML
    private Text supNameTxt;

    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    private Text totalTxt;

    @FXML
    private AnchorPane viewSupplierWindow;

    SupplierBoImpl supplierBo = BoFactory.getInstance().getBo(BoType.SUPPLIER);

    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();

    boolean isRowSelected;
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
        sceneSwitch.switchScene(viewSupplierWindow,"viewCustomer-form.fxml");
    }

    @FXML
    void manageEmployeeBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewSupplierWindow,"adminDashBoard-form.fxml");
    }

    @FXML
    void orderDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewSupplierWindow,"viewOrder-form.fxml");
    }

    @FXML
    void productDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewSupplierWindow,"viewProduct-form.fxml");
    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewSupplierWindow,"viewOrder-form.fxml");
    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("Are you sure want to Sign Out..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            sceneSwitch.switchScene(viewSupplierWindow, "dashBoard-form.fxml");
        }
    }

    @FXML
    void suppliersDetailsBtnAction(ActionEvent event) {

    }

    @FXML
    void tableMouseClickedOnAction(MouseEvent event) {

        try {
            int index = supplierTable.getSelectionModel().getSelectedIndex();

            isRowSelected = true;

            if(index < 0){
                return;
            }
            String id = supIdCol.getCellData(index).toString();
            String name = supNameCol.getCellData(index).toString();
            String email = supEmailCol.getCellData(index).toString();
            String company = supCompanyCol.getCellData(index).toString();

            supIdTxt.setText(id);
            supNameTxt.setText(name);
            emailTxt.setText(email);
            companyTxt.setText(company);

        }catch (Exception e){}

    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewSupplierWindow,"adminDashBoard-form.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        supIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        supNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        supEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        supCompanyCol.setCellValueFactory(new PropertyValueFactory<>("company"));
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("empId"));

        supplierTable.setItems(supplierBo.getAllSuppliers());

        supIdTxt.setText("");
        supNameTxt.setText("");
        emailTxt.setText("");
        companyTxt.setText("");
        isRowSelected =false;
    }

    public void reportViewOnAction(ActionEvent actionEvent) throws IOException {
        if (isRowSelected){
            File file = new File("D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store" +
                    "\\src\\main\\resources\\reportPdf\\supplierReport\\"+supIdTxt.getText()+".pdf");

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
}
