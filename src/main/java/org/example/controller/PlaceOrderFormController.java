package org.example.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.example.bo.BoFactory;
import org.example.bo.custom.impl.CustomerBoImpl;
import org.example.bo.custom.impl.OrderBoImpl;
import org.example.bo.custom.impl.PlaceOrderBoImpl;
import org.example.bo.custom.impl.ProductBoImpl;
import org.example.model.Customer;
import org.example.model.Product;
import org.example.util.BoType;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlaceOrderFormController implements Initializable {

    public JFXComboBox proIdComboBox;
    public JFXTextField productNameTxt;
    public JFXTextField cusNameTxt;
    public JFXTextField cusEmailTxt;
    public JFXTextField cusAddressTxt;
    CustomerBoImpl customerBoImpl = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    OrderBoImpl orderBoImpl = BoFactory.getInstance().getBo(BoType.ORDER);
    PlaceOrderBoImpl placeOrderBoImpl = BoFactory.getInstance().getBo(BoType.CART);

    @FXML
    private TableColumn<?, ?> amountCol;

    @FXML
    private Text availableQTYTxt;

    @FXML
    private TableColumn<?, ?> cartIdCol;

    @FXML
    private TableView<?> cartTable;

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

    boolean isCustomerSelect,isProductSelect,isQtyValid;
    @FXML
    void addToCartOnAction(MouseEvent event) {

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

        cusIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            isCustomerSelect = true;
            Customer customer = customerBoImpl.getUserById(newValue);
            cusNameTxt.setText(customer.getName());
            cusAddressTxt.setText(customer.getAddress());
            cusEmailTxt.setText(customer.getEmail());
        });
        proIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            isProductSelect = true;
            Product product = placeOrderBoImpl.getProductById((String) newValue);
            productNameTxt.setText(product.getName());
            availableQTYTxt.setText(Integer.toString(product.getQty()));
            categoryTxt.setText(product.getCategory());
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

}
