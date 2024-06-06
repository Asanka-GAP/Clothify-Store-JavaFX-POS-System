package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import jakarta.persistence.Lob;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.bo.custom.impl.ProductBoImpl;
import org.example.model.Product;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.util.Optional;
import java.util.ResourceBundle;

public class ProductFormController implements Initializable {

    public ImageView imageView;
    public Text sizeError;
    public JFXComboBox categoryComboBox;
    public JFXTextField priceTxt;
    public Text priceError;
    public TableColumn priceCol;
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
    private TableView<Product> productTable;

    @FXML
    private JFXTextField qtyTxt;

    @FXML
    private JFXTextField sizeTxt;

    @FXML
    private JFXButton updateBtn;

    ProductBoImpl productBoImpl = new ProductBoImpl();

    byte[] image;
    String category;
    boolean isAction,isMouseClick,isPriceValid;
    @FXML
    void actionBtnAction(ActionEvent event) {

        isAction = true;
        addProductBtn.setVisible(false);
        actionBtn.setVisible(false);
        updateBtn.setVisible(true);
        deleteBtn.setVisible(true);
    }

    @FXML
    void addProductOnAction(ActionEvent event) {

            if (isPriceValid && !productNameTxt.getText().equals("") && !qtyTxt.getText().equals("") && !sizeTxt.getText().equals("") && image != null) {
                Product product = new Product(proIdTxt.getText(), productNameTxt.getText(), Integer.parseInt(sizeTxt.getText()), Integer.parseInt(qtyTxt.getText()), category,image,Double.parseDouble(priceTxt.getText()));
                boolean isAdd = productBoImpl.addProduct(product);
                if (isAdd) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Product Added");
                    alert.setContentText("Product Added successfully");
                    alert.showAndWait();
                    image = null;
                    productNameTxt.setText("");
                    qtyTxt.setText("");
                    sizeTxt.setText("");
                    priceTxt.setText("");
                    proIdTxt.setText(productBoImpl.generateProductId());
                    productTable.setItems(productBoImpl.getAllProducts());
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Something Missing");
                alert.setContentText("Please Check your Form again..!!");
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

        if (isMouseClick){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting");
            alert.setContentText("Are you sure want to delete this Product");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get()==ButtonType.OK){
                boolean isDelete = productBoImpl.deleteProduct(proIdTxt.getText());

                if (isDelete){
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Product Deleted");
                    alert1.setContentText("Product Deleted Successfully");
                    alert1.showAndWait();
                    proIdTxt.setText("");
                    productNameTxt.setText("");
                    qtyTxt.setText("");
                    sizeTxt.setText("");
                    priceTxt.setText("");
                    productTable.setItems(productBoImpl.getAllProducts());
                    image = null;
                }
            }
        }

    }

    @FXML
    void imageUploadOnAction(ActionEvent event) throws FileNotFoundException {

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

        isAction = false;
        addProductBtn.setVisible(true);
        actionBtn.setVisible(true);
        updateBtn.setVisible(false);
        deleteBtn.setVisible(false);
        proIdTxt.setText(productBoImpl.generateProductId());
        productNameTxt.setText("");
        qtyTxt.setText("");
        sizeTxt.setText("");
        priceTxt.setText("");
        image = null;
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
            addProductBtn.setDisable(false);
            updateBtn.setDisable(false);
        }catch (Exception e){
            errorMsgtxt.setVisible(true);
            addProductBtn.setDisable(true);
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
    void updateOnAction(ActionEvent event) {

        if (isPriceValid && isMouseClick && !productNameTxt.getText().equals("") && !qtyTxt.getText().equals("") && !sizeTxt.getText().equals("") && image!=null){
            Product product = new Product(proIdTxt.getText(),productNameTxt.getText(),Integer.parseInt(sizeTxt.getText()),Integer.parseInt(qtyTxt.getText()),category,image,Double.parseDouble(priceTxt.getText()));
            boolean isUpdate = productBoImpl.updateProduct(product);

            if (isUpdate){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Product Updated");
                alert.setContentText("Product Updated Successfully..!!!");
                alert.showAndWait();
                proIdTxt.setText("");
                productNameTxt.setText("");
                qtyTxt.setText("");
                sizeTxt.setText("");
                productTable.setItems(productBoImpl.getAllProducts());
                image = null;
            }
        }else {
            new Alert(Alert.AlertType.ERROR,"Something Missing").show();
        }
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


        isPriceValid = false;
        priceError.setVisible(false);
        isAction = false;
        isMouseClick = false;
        proIdTxt.setText(productBoImpl.generateProductId());
        updateBtn.setVisible(false);
        deleteBtn.setVisible(false);
        errorMsgtxt.setVisible(false);
        sizeError.setVisible(false);
        categoryComboBox.setItems(categoryLoad());
        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {
            category = (String) newValue;
            System.out.println(category);
        });
        proIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        proCategoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        proNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        proQTYCol.setCellValueFactory(new PropertyValueFactory<>("qty"));
        proSizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems(productBoImpl.getAllProducts());
    }

    public void sizeKeyReleased(KeyEvent keyEvent) {
        try {
            Integer.parseInt(sizeTxt.getText());
            sizeError.setVisible(false);
            addProductBtn.setDisable(false);
            updateBtn.setDisable(false);
        }catch (Exception e){
            sizeError.setVisible(true);
            addProductBtn.setDisable(true);
            updateBtn.setDisable(true);
        }
    }

    public void tableMouseClickedAction(MouseEvent mouseEvent) throws IOException {
        int index = productTable.getSelectionModel().getSelectedIndex();


        if(index < 0){
            return;
        }
        String id = proIdCol.getCellData(index).toString();

        if (isAction){
            Product product = productBoImpl.getProductById(id);
            productNameTxt.setText(product.getName());
            qtyTxt.setText(Integer.toString(product.getQty()));
            sizeTxt.setText(Integer.toString(product.getSize()));
            proIdTxt.setText(product.getId());
//            System.out.println(product.getImage());
//
//            image = product.getImage();
//            System.out.println(productBoImpl.getExampleByteArray());
//            BufferedImage bufferedImage = productBoImpl.byteArrayToBufferedImage(image);
//            InputStream inputStream = productBoImpl.bufferedImageToInputStream(bufferedImage);
//            Image image1 = new Image(inputStream);
//
//            imageView.setImage(image1);

            if (!product.getId().equals("")){
                isMouseClick = true;
            }
        }




    }

    public void priceKeyReleased(KeyEvent keyEvent) {
        try{
            Double.parseDouble(priceTxt.getText());
            priceError.setVisible(false);
            addProductBtn.setDisable(false);
            isPriceValid =true;

        }catch (Exception e){
            priceError.setVisible(true);
            addProductBtn.setDisable(true);
        }
    }
}
