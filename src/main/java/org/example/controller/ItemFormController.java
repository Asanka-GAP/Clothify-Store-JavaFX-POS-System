package org.example.controller;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.example.model.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ItemFormController {
    public Text nameTxt;
    public Text priceTxt;
    public ImageView imageView;
    Product product2;

    public void setData(Product product) throws IOException {
        product2 = product;

        nameTxt.setText(product.getName());
        priceTxt.setText("Rs."+Double.toString(product.getPrice())+"0");
        byte[] data;
        try{
            data = Base64.getDecoder().decode(new String(product.getImage()));
        }catch (Exception e){
            return;
        }

        String savePath = "C:\\Users\\HP\\Pictures\\ClothifyStore\\image.jpg";
        FileOutputStream fileOutputStream = new FileOutputStream(savePath);
        fileOutputStream.write(data);
        fileOutputStream.close();

        Image image1 = new Image(savePath);
        imageView.setImage(image1);
    }

    public void mouseClickedOnAction(MouseEvent mouseEvent) {

//        ImageWallFormController instance = new ImageWallFormController();
//
//        instance.nameTxt.setText(nameTxt.getText());
//        if (product2.getQty()<=0){
//            instance.availableTxt.setText("Not Available");
//        }
//        instance.priceTxt.setText(priceTxt.getText());
    }
}
