package org.example.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import org.example.bo.custom.impl.ProductBoImpl;
import org.example.model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class ImageWallFormController implements Initializable {

    public Text nameTxt;
    public Text priceTxt;
    public ImageView imageView;
    public Text availableTxt;
    public Text qtyTxt;
    public ScrollPane scrollPane;
    public GridPane gridPane;
    @FXML
    private AnchorPane imageWallWindow;
    int column =1;
    int row = 1;

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
        ObservableList<Product> products = new ProductBoImpl().getAllProducts();


            try {
                for (int i=0; i<products.size();i++) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/view/item-form.fxml"));
                    AnchorPane anchorPane = fxmlLoader.load();
                    ItemFormController itemFormController = fxmlLoader.getController();

                    itemFormController.setData(products.get(i));

                    if (column == 4) {
                        column = 1;
                        row++;
                    }

                    gridPane.setMinWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxWidth(Region.USE_PREF_SIZE);

                    gridPane.minHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.setMaxWidth(Region.USE_PREF_SIZE);

                    gridPane.add(anchorPane, column++, row);
                    GridPane.setMargin(anchorPane, new Insets(10));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }



    }

    public void signInBtnMouseClicked(MouseEvent mouseEvent) throws IOException {
        sceneSwitch.switchScene(imageWallWindow,"dashBoard-form.fxml");
    }

    public void homeBtnMouseClicked(MouseEvent mouseEvent) throws IOException {
        sceneSwitch.switchScene(imageWallWindow,"landingPage-form.fxml");
    }
}
