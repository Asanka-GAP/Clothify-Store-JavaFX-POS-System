package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
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
import org.example.bo.custom.impl.UserBoImpl;
import org.example.db.DBConnection;
import org.example.model.User;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;

public class AdminDashBoardController implements Initializable {


    public JFXTextField empNametxt;
    public JFXTextField empAddresstxt;
    public JFXTextField empEmailtxt;
    public Text empIdtxt;
    public JFXButton addEmployeeBtn;

    public Text errorMsgtxt;
    public AnchorPane adminDashboardWindow;
    public TableView employeeTable;
    public TableColumn employeeIdCol;
    public TableColumn employeeNameCol;
    public TableColumn employeeEmailCol;
    public TableColumn employeeAddressCol;

    UserBoImpl userBoImpl = new UserBoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateCustomerId();
        errorMsgtxt.setVisible(false);
        employeeIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        employeeAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        try {
            employeeTable.setItems(userBoImpl.getAllUsers());
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public void generateCustomerId() {

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user ORDER BY id DESC LIMIT 1");

            while (resultSet.next()) {
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


    public void addEmployeeBtnAction(ActionEvent actionEvent) throws SQLException {

        Random random = new Random();
        int p = random.nextInt(99999999) + 10000000;

        String encrypt = Integer.toString(p);
        String password = userBoImpl.passwordEncrypt(encrypt);

        User user = new User(empIdtxt.getText(), empNametxt.getText(), empEmailtxt.getText(), password, "Employee", empAddresstxt.getText());

        if (!empNametxt.getText().equals("") && userBoImpl.isValidEmail(empEmailtxt.getText()) && !empAddresstxt.getText().equals("")) {

            boolean isInsert = userBoImpl.insertUser(user);

            if (isInsert) {
                employeeTable.setItems(userBoImpl.getAllUsers());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Employee Added");
                alert.setContentText("Employee Added Successfully..!");
                alert.showAndWait();
                generateCustomerId();
                empAddresstxt.setText("");
                empEmailtxt.setText("");
                empNametxt.setText("");
            }

        } else {
            new Alert(Alert.AlertType.ERROR, "Somthing Wrong..!!!").show();
        }


    }

    public void clearBtnAction(ActionEvent actionEvent) {
    }

    public void viewBtnAction(ActionEvent actionEvent) {
    }


    public void emailAddressKeyReleased(KeyEvent keyEvent) {
        boolean isValidEmail = userBoImpl.isValidEmail(empEmailtxt.getText());
        if (!isValidEmail) {
            errorMsgtxt.setVisible(true);
        } else {
            errorMsgtxt.setVisible(false);
        }
    }

    public void userBtnMouseClicked(MouseEvent mouseEvent) {
    }

    public void shoppingBagBtnMouseClicked(MouseEvent mouseEvent) {
    }

    public void signOutBtnMouseClicked(MouseEvent mouseEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sign Out");
        alert.setContentText("Are you sure want to Sign Out..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            SceneSwitchController.getInstance().switchScene(adminDashboardWindow, "dashBoard-form.fxml");
        }
    }

    public void customerDetailsBtnAction(ActionEvent actionEvent) {
    }

    public void suppliersDetailsBtnAction(ActionEvent actionEvent) {
    }

    public void productDetailsBtnAction(ActionEvent actionEvent) {
    }

    public void orderDetailsBtnAction(ActionEvent actionEvent) {
    }

    public void manageEmployeeBtnAction(ActionEvent actionEvent) {
    }

    public void actionBtnAction(ActionEvent actionEvent) {
    }

    public void closeBtnAction(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure want to exit..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }
}
