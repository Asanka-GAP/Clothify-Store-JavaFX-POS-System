package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.example.db.DBConnection;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Base64;
import java.util.Random;
import java.util.ResourceBundle;

public class AdminDashBoardController implements Initializable {



    public JFXTextField empNametxt;
    public JFXTextField empAddresstxt;
    public JFXTextField empEmailtxt;
    public Text empIdtxt;
    public JFXButton addEmployeeBtn;
    public JFXButton clearBtn;
    public JFXButton viewBtn;
    public Text errorMsgtxt;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateCustomerId();
        errorMsgtxt.setVisible(false);
    }

    public void generateCustomerId() {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user ORDER BY id DESC LIMIT 1");

            while (resultSet.next()){
                String lastOrderId = resultSet.getString(1);
                int number = Integer.parseInt(lastOrderId.split("U")[1]);
                number++;
                 empIdtxt.setText(String.format("U%04d", number));

            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void addEmployeeBtnAction(ActionEvent actionEvent) {

        Random random = new Random();
        int p = random.nextInt(99999999)+10000000;

        String encrypt = Integer.toString(p);

        String password = Base64.getEncoder().encodeToString(encrypt.getBytes(StandardCharsets.UTF_8));

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO user VALUES (?,?,?,?,?,?)");
            preparedStatement.setString(1,empIdtxt.getText());
            preparedStatement.setString(2,empNametxt.getText());
            preparedStatement.setString(3,empEmailtxt.getText());
            preparedStatement.setString(4,password);
            preparedStatement.setString(5,"Employee");
            preparedStatement.setString(6,empAddresstxt.getText());


            if (!empNametxt.getText().equals("") && isValidEmail(empEmailtxt.getText()) && !empAddresstxt.getText().equals("")){

                System.out.println(password);
                preparedStatement.execute();
                generateCustomerId();
                empAddresstxt.setText("");
                empEmailtxt.setText("");
                empNametxt.setText("");
            }else {
                new Alert(Alert.AlertType.ERROR,"Somthing Wrong..!!!").show();
            }


        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }


    }

    public void clearBtnAction(ActionEvent actionEvent) {
    }

    public void viewBtnAction(ActionEvent actionEvent) {
    }

    private boolean isValidEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public void emailAddressKeyReleased(KeyEvent keyEvent) {
        if (!isValidEmail(empEmailtxt.getText())){
            errorMsgtxt.setVisible(true);
        }else{
            errorMsgtxt.setVisible(false);
        }
    }
}
