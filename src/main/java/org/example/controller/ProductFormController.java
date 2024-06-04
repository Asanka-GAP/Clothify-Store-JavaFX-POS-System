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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.bo.custom.impl.ProductBoImpl;
import org.example.model.Customer;
import org.example.model.Product;

import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    public JFXComboBox productIdComboBox;
    public ImageView imageView;
    public Text sizeError;
    public JFXComboBox categoryComboBox;
    @FXML
    private JFXButton actionBtn;

    @FXML
    private JFXButton addProductBtn;

    @FXML
    private JFXTextField categoryTxt;

    @FXML
    private TableColumn<?, ?> cusAddressCol;

    @FXML
    private Text cusIdLable;

    @FXML
    private AnchorPane customerWindow;

    @FXML
    private JFXButton deleteBtn;

    @FXML
    private Text errorMsgtxt;

    @FXML
    private TableColumn<?, ?> proCategoryCol;

    @FXML
    private TableColumn<?, ?> proIdCol;

    @FXML
    private Text proIdTxt;

    @FXML
    private TableColumn<?, ?> proImgCol;

    @FXML
    private TableColumn<?, ?> proNameCol;

    @FXML
    private TableColumn<?, ?> proQTYCol;

    @FXML
    private TableColumn<?, ?> proSizeCol;

    @FXML
    private JFXTextField productNameTxt;

    @FXML
    private TableView<?> productTable;

    @FXML
    private JFXTextField qtyTxt;

    @FXML
    private JFXTextField sizeTxt;

    @FXML
    private JFXButton updateBtn;

    ProductBoImpl productBoImpl = new ProductBoImpl();

    byte[] image;
    String category;
    @FXML
    void actionBtnAction(ActionEvent event) {

    }

    @FXML
    void addProductOnAction(ActionEvent event) {

        if (!productNameTxt.getText().equals("") && !qtyTxt.getText().equals("") && !sizeTxt.getText().equals("")){
            Product product = new Product(proIdTxt.getText(),productNameTxt.getText(),Integer.parseInt(sizeTxt.getText()),Integer.parseInt(qtyTxt.getText()),category,image);
            boolean isAdd = productBoImpl.addProduct(product);
            if (isAdd){
                proIdTxt.setText(productBoImpl.generateProductId());
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
    void deleteBtnAction(ActionEvent event) {

    }

    @FXML
    void imageUploadOnAction(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload an Image");
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG Image","*.jpg"));

        File file = fileChooser.showOpenDialog(new Stage());
        image = new byte[(int)file.length()];
        System.out.println(image);

        Image photo = new Image(file.getPath());

        imageView.setImage(photo);

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
    void placeSectionBtnAction(ActionEvent event) {

    }

    @FXML
    void qtyKeyReleased(KeyEvent event) {
        try {
            Integer.parseInt(qtyTxt.getText());
            errorMsgtxt.setVisible(false);
        }catch (Exception e){
            errorMsgtxt.setVisible(true);
        }


    }

    @FXML
    void shoppingBagBtnMouseClicked(MouseEvent event) {

    }

    @FXML
    void signOutBtnMouseClicked(MouseEvent event) {

    }

    @FXML
    void updateOnAction(ActionEvent event) {

    }

    @FXML
    void userBtnMouseClicked(MouseEvent event) {

    }

    private ObservableList<String> categoryLoad(){
        ObservableList<String> list =FXCollections.observableArrayList();
        list.add("Gents");
        list.add("Ladies");
        list.add("Kids");
        list.add("Unisex");

        return list;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        proIdTxt.setText(productBoImpl.generateProductId());
        updateBtn.setVisible(false);
        deleteBtn.setVisible(false);
        productIdComboBox.setVisible(false);
        errorMsgtxt.setVisible(false);
        sizeError.setVisible(false);
        categoryComboBox.setItems(categoryLoad());
        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            category = (String) newValue;
            System.out.println(category);
        });
    }

    public void sizeKeyReleased(KeyEvent keyEvent) {
        try {
            Integer.parseInt(sizeTxt.getText());
            sizeError.setVisible(false);
        }catch (Exception e){
            sizeError.setVisible(true);
        }
    }
}
