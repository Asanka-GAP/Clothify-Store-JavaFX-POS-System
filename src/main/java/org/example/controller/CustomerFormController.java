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
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.model.Customer;
import org.example.util.BoType;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    public Text cusIdTxt;
    @FXML
    private JFXButton actionBtn;

    @FXML
    private JFXButton addCusBtn;

    @FXML
    private TableColumn<?, ?> cusAddressCol;

    @FXML
    private JFXTextField cusAddressTxt;

    @FXML
    private TableColumn<?, ?> cusEmailCol;

    @FXML
    private JFXTextField cusEmailTxt;

    @FXML
    private TableColumn<?, ?> cusIdCol;

    @FXML
    private JFXComboBox<String> cusIdComboBox;

    @FXML
    private Text cusIdLable;

    @FXML
    private TableColumn<?, ?> cusNameCol;

    @FXML
    private JFXTextField cusNameTxt;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private AnchorPane customerWindow;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private Text errorMsgtxt;

    @FXML
    private Text orderIdtxt;

    @FXML
    private JFXButton updateCusBtn;

    String selectedId;
    CustomerBoImpl customerBoImpl = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateCusBtn.setVisible(false);
        deleteBtn.setVisible(false);
        cusIdComboBox.setVisible(false);
        errorMsgtxt.setVisible(false);
        cusIdTxt.setText(customerBoImpl.generateCustomerId());

        cusIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cusEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        cusAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        customerTable.setItems(customerBoImpl.getAllCustomer());

        cusIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            Customer user = customerBoImpl.getUserById((String) newValue);

            cusNameTxt.setText(user.getName());
            cusAddressTxt.setText(user.getAddress());
            cusEmailTxt.setText(user.getEmail());
            selectedId = (String) newValue;
        });
    }

    @FXML
    void actionBtnAction(ActionEvent event) {

        addCusBtn.setVisible(false);
        actionBtn.setVisible(false);
        cusIdTxt.setVisible(false);
        cusIdLable.setVisible(false);
        cusIdComboBox.setVisible(true);
        deleteBtn.setVisible(true);
        updateCusBtn.setVisible(true);
        cusIdComboBox.setItems(customerBoImpl.getAllCustomerIds());
    }

    @FXML
    void addCusOnAction(ActionEvent event) {
        if (!cusAddressTxt.getText().equals("") && !cusEmailTxt.getText().equals("") && !cusNameTxt.getText().equals("")){
            Customer customer = new Customer(cusIdTxt.getText(), cusNameTxt.getText(), cusEmailTxt.getText(), cusAddressTxt.getText());

            boolean isAdd = customerBoImpl.insertCustomer(customer);

            if (isAdd){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Added");
                alert.setContentText("Customer Added Successfully..!");
                alert.showAndWait();
                cusIdTxt.setText(customerBoImpl.generateCustomerId());
                cusAddressTxt.setText("");
                cusEmailTxt.setText("");
                cusNameTxt.setText("");
                customerTable.setItems(customerBoImpl.getAllCustomer());
            }
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
    void deleteBtnAction(ActionEvent event){
        if (!selectedId.equals("")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting");
            alert.setContentText("Are you sure want to delete this Customer");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get()==ButtonType.OK){
                boolean isDeleted = customerBoImpl.deleteCustomerById(selectedId);
                if (isDeleted){
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Employee Deleted");
                    alert2.setContentText("Employee deleted successfully");
                    cusNameTxt.setText("");
                    cusAddressTxt.setText("");
                    cusEmailTxt.setText("");
                    alert2.showAndWait();
                    customerTable.setItems(customerBoImpl.getAllCustomer());
                    cusIdComboBox.setItems(customerBoImpl.getAllCustomerIds());

                }
            }
        }

    }

    @FXML
    void emailAddressKeyReleased(KeyEvent event) {
        boolean isValidEmail = customerBoImpl.isValidEmail(cusEmailTxt.getText());
        if (!isValidEmail) {
            errorMsgtxt.setVisible(true);
            addCusBtn.setDisable(true);
            updateCusBtn.setDisable(true);
        } else {
            addCusBtn.setDisable(false);
            updateCusBtn.setDisable(false);
            errorMsgtxt.setVisible(false);
        }

    }

    @FXML
    void manageCustomersBtnAction(ActionEvent event) {

        addCusBtn.setVisible(true);
        actionBtn.setVisible(true);
        cusIdTxt.setVisible(true);
        cusIdLable.setVisible(true);
        cusIdComboBox.setVisible(false);
        deleteBtn.setVisible(false);
        updateCusBtn.setVisible(false);
        cusIdTxt.setText(customerBoImpl.generateCustomerId());
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
    void placeSectionBtnAction(ActionEvent event) {

    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) {

    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) {

    }

    @FXML
    void updateCusOnAction(ActionEvent event) {
        if (!cusEmailTxt.getText().equals("") && !cusAddressTxt.getText().equals("") && !cusNameTxt.getText().equals("")){
            Customer customer = new Customer(selectedId,cusNameTxt.getText(),cusEmailTxt.getText(),cusAddressTxt.getText());
            boolean isUpdated = customerBoImpl.updateCustomer(customer);
            if (isUpdated){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Customer Update");
                alert.setContentText("Customer Updated Successfully");
                alert.showAndWait();
                cusEmailTxt.setText("");
                cusAddressTxt.setText("");
                cusNameTxt.setText("");
                customerTable.setItems(customerBoImpl.getAllCustomer());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Something Missing");
            alert.setContentText("Please Check your Form again..!!!");
            alert.showAndWait();
        }
    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) {

    }

}
