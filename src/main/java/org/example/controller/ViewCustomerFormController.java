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
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.model.Customer;
import org.example.util.BoType;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewCustomerFormController implements Initializable {

    public TableColumn empIdCol;
    @FXML
    private TableColumn<?, ?> cusAddressCol;

    @FXML
    private Text cusAddressTxt;

    @FXML
    private TableColumn<?, ?> cusEmailCol;

    @FXML
    private Text cusEmailTxt;

    @FXML
    private TableColumn<?, ?> cusIdCol;

    @FXML
    private Text cusIdTxt;

    @FXML
    private TableColumn<?, ?> cusNameCol;

    @FXML
    private Text cusNameTxt;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private Text totalTxt;

    @FXML
    private AnchorPane viewCustomerWindow;

    CustomerBoImpl customerBoImpl= BoFactory.getInstance().getBo(BoType.CUSTOMER);
    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();

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

    }

    @FXML
    void manageEmployeeBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewCustomerWindow,"adminDashBoard-form.fxml");
    }

    @FXML
    void orderDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewCustomerWindow,"viewOrder-form.fxml");
    }

    @FXML
    void productDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewCustomerWindow,"viewProduct-form.fxml");
    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewCustomerWindow,"viewOrder-form.fxml");
    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("Are you sure want to Sign Out..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            SceneSwitchController.getInstance().switchScene(viewCustomerWindow, "dashBoard-form.fxml");
        }
    }

    @FXML
    void suppliersDetailsBtnAction(ActionEvent event) throws IOException {
        sceneSwitch.switchScene(viewCustomerWindow,"viewSupplier-form.fxml");
    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) throws IOException {
        sceneSwitch.switchScene(viewCustomerWindow,"adminDashBoard-form.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cusIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cusNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cusEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        cusAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        empIdCol.setCellValueFactory(new PropertyValueFactory<>("empId"));

        customerTable.setItems(customerBoImpl.getAllCustomer());
        cusIdTxt.setText("");
        cusAddressTxt.setText("");
        cusNameTxt.setText("");
        cusEmailTxt.setText("");
    }

    public void mouseAction(MouseEvent mouseEvent) {
        try {
            int index = customerTable.getSelectionModel().getSelectedIndex();
            if(index < 0){
                return;
            }
            String id = cusIdCol.getCellData(index).toString();
            Customer customer = new CustomerBoImpl().getUserById(id);
            cusIdTxt.setText(customer.getId());
            cusAddressTxt.setText(customer.getAddress());
            cusNameTxt.setText(customer.getName());
            cusEmailTxt.setText(customer.getEmail());
        }catch (Exception e){}
    }
}
