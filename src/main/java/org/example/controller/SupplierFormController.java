package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.SupplierBoImpl;
import org.example.model.Supplier;
import org.example.util.BoType;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplierFormController implements Initializable {

    public JFXButton updateBtn;
    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    private JFXButton actionBtn;

    @FXML
    private JFXButton addSupplierBtn;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private Text empIdLable;

    @FXML
    private Text errorMsgtxt;

    @FXML
    private TableColumn<?, ?> supCompanyCol;

    @FXML
    private JFXTextField supCompanytxt;

    @FXML
    private TableColumn<?, ?> supEmailCol;

    @FXML
    private JFXTextField supEmailtxt;

    @FXML
    private TableColumn<?, ?> supIdCol;

    @FXML
    private Text supIdTxt;

    @FXML
    private TableColumn<?, ?> supNameCol;

    @FXML
    private JFXTextField supNametxt;

    @FXML
    private JFXComboBox<?> supplierIdComboBox;

    @FXML
    private AnchorPane supplierManageWindow;
    boolean isRowSelect,isAction;
    String selectedSupId;

    SupplierBoImpl supllierBo= BoFactory.getInstance().getBo(BoType.SUPPLIER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        isAction =false;
        isRowSelect = false;
        updateBtn.setVisible(false);
        deleteBtn.setVisible(false);
        supplierIdComboBox.setVisible(false);
        supIdTxt.setText(supllierBo.generateSupplierId());
        errorMsgtxt.setVisible(false);

        supIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        supNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        supEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        supCompanyCol.setCellValueFactory(new PropertyValueFactory<>("company"));

        supplierTable.setItems(supllierBo.getAllSuppliers());
    }

    @FXML
    void actionBtnAction(ActionEvent event) {

        addSupplierBtn.setVisible(false);
        actionBtn.setVisible(false);
        isAction = true;
        updateBtn.setVisible(true);
        deleteBtn.setVisible(true);
    }

    @FXML
    void addSupplierBtnAction(ActionEvent event) {

        if (!supNametxt.getText().equals("") && !supCompanytxt.getText().equals("") && !supEmailtxt.equals("")) {

            Supplier supplier = new Supplier(supIdTxt.getText(),supNametxt.getText(),supEmailtxt.getText(),supCompanytxt.getText());
            boolean isAdded = supllierBo.addSupplier(supplier);

            if (isAdded) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Supplier");
                alert.setContentText("Supplier Added Successfully");
                alert.showAndWait();
                supIdTxt.setText(supllierBo.generateSupplierId());
                supNametxt.setText("");
                supEmailtxt.setText("");
                supCompanytxt.setText("");
                supplierTable.setItems(supllierBo.getAllSuppliers());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Something Missing");
            alert.setContentText("Please check your form again");
            alert.showAndWait();
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
    void deleteBtnAction(ActionEvent event) {
        if (isRowSelect){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Supplier deleting");
            alert.setContentText("Are you sure want to delete this Supplier ?");
            Optional<ButtonType> type = alert.showAndWait();

            if (type.get()==ButtonType.OK){
                boolean isDeleted = supllierBo.deleteSupplierById(selectedSupId);

                if (isDeleted){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Supplier deleted");
                    alert1.setContentText("Supplier Deleted Successfully");
                    alert1.showAndWait();
                    supIdTxt.setText("");
                    supNametxt.setText("");
                    supEmailtxt.setText("");
                    supCompanytxt.setText("");
                    supplierTable.setItems(supllierBo.getAllSuppliers());
                }

            }
        }

    }

    @FXML
    void emailAddressKeyReleased(KeyEvent event) {

        boolean isEmailValid = supllierBo.isValidEmail(supEmailtxt.getText());

        if (isEmailValid){
            errorMsgtxt.setVisible(false);
            addSupplierBtn.setDisable(false);
            updateBtn.setDisable(false);
        }else{
            errorMsgtxt.setVisible(true);
            addSupplierBtn.setDisable(true);
            updateBtn.setDisable(true);
        }

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

    public void updateBtnAction(ActionEvent actionEvent) {

        if (isRowSelect && !supNametxt.getText().equals("") && !supCompanytxt.getText().equals("") && !supEmailtxt.equals("")){
            Supplier supplier = new Supplier(supIdTxt.getText(),supNametxt.getText(),supEmailtxt.getText(),supCompanytxt.getText());
            boolean isUpdate = supllierBo.updateSupplier(supplier);

            if (isUpdate){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Supplier Update");
                alert.setContentText("Supplier updated Successfully..!!!");
                alert.showAndWait();
                supIdTxt.setText("");
                supNametxt.setText("");
                supEmailtxt.setText("");
                supCompanytxt.setText("");
                supplierTable.setItems(supllierBo.getAllSuppliers());
            }
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select the row if you wanna update");
        }

    }

    public void tableMouseClickedOnAction(MouseEvent mouseEvent) {
        int index = supplierTable.getSelectionModel().getSelectedIndex();

        try {
            if (isAction){
                isRowSelect = true;
                selectedSupId = supIdCol.getCellData(index).toString();
                 supIdTxt.setText(supIdCol.getCellData(index).toString());
                 supNametxt.setText(supNameCol.getCellData(index).toString());
                 supEmailtxt.setText(supEmailCol.getCellData(index).toString());
                 supCompanytxt.setText(supCompanyCol.getCellData(index).toString());
            }
        }catch (Exception e){

        }

    }

    public void placeSectionBtnAction(ActionEvent actionEvent) {
    }

    public void manageOrdersBtnAction(ActionEvent actionEvent) {
    }

    public void manageSuppliersBtnAction(ActionEvent actionEvent) {
        addSupplierBtn.setVisible(true);
        actionBtn.setVisible(true);
        isAction = false;
        updateBtn.setVisible(false);
        deleteBtn.setVisible(false);
        supIdTxt.setText(supllierBo.generateSupplierId());
        supNametxt.setText("");
        supEmailtxt.setText("");
        supCompanytxt.setText("");
    }

    public void manageCustomersBtnAction(ActionEvent actionEvent) {
    }

    public void manageProductsBtnAction(ActionEvent actionEvent) {

    }
}
