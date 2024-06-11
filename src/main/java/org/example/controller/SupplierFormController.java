package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.ProductBoImpl;
import org.example.bo.custom.impl.SupplierBoImpl;
import org.example.model.Cart;
import org.example.model.OrderHasItem;
import org.example.model.Product;
import org.example.model.Supplier;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();
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
    void addSupplierBtnAction(ActionEvent event) throws JRException {

        if (!supNametxt.getText().equals("") && !supCompanytxt.getText().equals("") && !supEmailtxt.equals("")) {

            String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\report\\Supplier.jrxml";

            Map<String,Object> parameters = new HashMap();

            parameters.put("supId",supIdTxt.getText());
            parameters.put("supName",supNametxt.getText());
            parameters.put("email",supEmailtxt.getText());
            parameters.put("company",supCompanytxt.getText());

            JasperReport report = JasperCompileManager.compileReport(path);

            String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\supplierReport\\"+supIdTxt.getText()+".pdf";

            ObservableList<Product> productList = new ProductBoImpl().getProductBySupId(supIdTxt.getText());
            List<Product> list = new ArrayList<Product>();

            productList.forEach(product -> {
                list.add(product);
            });

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters,dataSource);
            JasperExportManager.exportReportToPdfFile(jasperPrint,savePath);



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
        }else {
            new Alert(Alert.AlertType.WARNING,"Please select the row if you wanna delete");
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
    void shoppingBagBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(supplierManageWindow,"placeOrder-form.fxml");
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

    public void placeSectionBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(supplierManageWindow,"placeOrder-form.fxml");
    }

    public void manageOrdersBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(supplierManageWindow,"manageOrder-form.fxml");
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

    public void manageCustomersBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(supplierManageWindow,"customer-form.fxml");
    }

    public void manageProductsBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(supplierManageWindow,"product-form.fxml");
    }
}
