package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import net.sf.jasperreports.engine.*;
import org.example.bo.custom.impl.UserBoImpl;
import org.example.model.User;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

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
    public JFXButton updayeEmployeeBtn;
    public JFXComboBox employeeIdComboBox;
    public JFXButton deleteBtn;
    public Text empIdLable;
    public JFXButton actionBtn;

    UserBoImpl userBoImpl = new UserBoImpl();
    String selectedId;
    SceneSwitchController sceneSwitch = SceneSwitchController.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeIdComboBox.setVisible(false);
        employeeIdComboBox.getSelectionModel().selectedItemProperty().addListener((observable,oldValue,newValue) -> {

                User user = userBoImpl.getUserById((String) newValue);

                empNametxt.setText(user.getName());
                empAddresstxt.setText(user.getAddress());
                empEmailtxt.setText(user.getEmail());

                selectedId = (String) newValue;

        });

        empIdtxt.setText(userBoImpl.generateEmployeeId());
        updayeEmployeeBtn.setVisible(false);
        deleteBtn.setVisible(false);

        errorMsgtxt.setVisible(false);
        employeeIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        employeeNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        employeeAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));


            employeeTable.setItems(userBoImpl.getAllUsers());
    }

    public void addEmployeeBtnAction(ActionEvent actionEvent) throws SQLException, JRException {

        Random random = new Random();
        int p = random.nextInt(99999999) + 10000000;

        String encrypt = Integer.toString(p);
        String password = userBoImpl.passwordEncrypt(encrypt);

        User user = new User(empIdtxt.getText(), empNametxt.getText(), empEmailtxt.getText(), password, "Employee", empAddresstxt.getText());

        if (!empNametxt.getText().equals("") && userBoImpl.isValidEmail(empEmailtxt.getText()) && !empAddresstxt.getText().equals("")) {

            String path = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\report\\Emp_Re.jrxml";

            Map<String,Object> parameters = new HashMap();
            StringBuffer name = new StringBuffer(user.getName());
            StringBuffer id = new StringBuffer(user.getId());
            StringBuffer address = new StringBuffer(user.getAddress());
            StringBuffer email = new StringBuffer(user.getEmail());
            parameters.put("empName",name);
            parameters.put("id",id);
            parameters.put("add",address);
            parameters.put("email",email);

            JasperReport report = JasperCompileManager.compileReport(path);
            JasperPrint jasperPrint = JasperFillManager.fillReport(report,parameters);

            String savePath = "D:\\Notes\\ICD\\StandAlone Application\\END\\Colthify-Store\\src\\main\\resources\\reportPdf\\"+user.getId()+".pdf";
            JasperExportManager.exportReportToPdfFile(jasperPrint,savePath);

            boolean isInsert = userBoImpl.insertUser(user);

            if (isInsert) {
                employeeTable.setItems(userBoImpl.getAllUsers());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Employee Added");
                alert.setContentText("Employee Added Successfully..!");
                alert.showAndWait();
                empIdtxt.setText(userBoImpl.generateEmployeeId());
                empAddresstxt.setText("");
                empEmailtxt.setText("");
                empNametxt.setText("");
            }

        } else {
            new Alert(Alert.AlertType.ERROR, "Somthing Wrong..!!!").show();
        }


    }


    public void emailAddressKeyReleased(KeyEvent keyEvent) {
        boolean isValidEmail = userBoImpl.isValidEmail(empEmailtxt.getText());
        if (!isValidEmail) {
            errorMsgtxt.setVisible(true);
            addEmployeeBtn.setDisable(true);
            updayeEmployeeBtn.setDisable(true);
        } else {
            addEmployeeBtn.setDisable(false);
            updayeEmployeeBtn.setDisable(false);
            errorMsgtxt.setVisible(false);
        }
    }

    public void userBtnMouseClicked(MouseEvent mouseEvent) {

    }

    public void shoppingBagBtnMouseClicked(MouseEvent mouseEvent) throws IOException {
        sceneSwitch.switchScene(adminDashboardWindow,"viewOrder-form.fxml");
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

    public void customerDetailsBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(adminDashboardWindow,"viewCustomer-form.fxml");
    }

    public void suppliersDetailsBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(adminDashboardWindow,"viewSupplier-form.fxml");
    }

    public void productDetailsBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(adminDashboardWindow,"viewProduct-form.fxml");
    }

    public void orderDetailsBtnAction(ActionEvent actionEvent) throws IOException {
        sceneSwitch.switchScene(adminDashboardWindow,"viewOrder-form.fxml");
    }

    public void manageEmployeeBtnAction(ActionEvent actionEvent) {
        empIdLable.setVisible(true);
        addEmployeeBtn.setVisible(true);
        actionBtn.setVisible(true);
        empIdtxt.setVisible(true);
        deleteBtn.setVisible(false);
        updayeEmployeeBtn.setVisible(false);
        employeeIdComboBox.setVisible(false);
        empNametxt.setText("");
        empAddresstxt.setText("");
        empEmailtxt.setText("");
        empIdtxt.setText(userBoImpl.generateEmployeeId());
    }

    public void actionBtnAction(ActionEvent actionEvent) {
        empIdLable.setVisible(false);
        addEmployeeBtn.setVisible(false);
        actionBtn.setVisible(false);
        empIdtxt.setVisible(false);
        deleteBtn.setVisible(true);
        updayeEmployeeBtn.setVisible(true);
        employeeIdComboBox.setVisible(true);
        employeeIdComboBox.setItems(userBoImpl.getAllUserIds());

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

    public void updateEmployeeBtnAction(ActionEvent actionEvent) {
        if (!empEmailtxt.getText().equals("") && !empAddresstxt.getText().equals("") && !empNametxt.getText().equals("")){
            User user = new User(selectedId,empNametxt.getText(),empEmailtxt.getText(),null,null,empAddresstxt.getText());
            boolean isUpdated = userBoImpl.updateUser(user);
            if (isUpdated){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Employee Update");
                alert.setContentText("Employee Updated Successfully");
                alert.showAndWait();
                empEmailtxt.setText("");
                empAddresstxt.setText("");
                empNametxt.setText("");
                employeeTable.setItems(userBoImpl.getAllUsers());
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Something Missing");
            alert.setContentText("Please Check your Form again..!!!");
            alert.showAndWait();
        }
    }

    public void deleteBtnAction(ActionEvent actionEvent) {

        if (!selectedId.equals("")){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting");
            alert.setContentText("Are you sure want to delete this Employee");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get()==ButtonType.OK){
                boolean isDeleted = userBoImpl.deleteUserById(selectedId);
                if (isDeleted){
                    Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                    alert2.setTitle("Employee Deleted");
                    alert2.setContentText("Employee deleted successfully");
                    alert2.showAndWait();
                    employeeTable.setItems(userBoImpl.getAllUsers());
                    employeeIdComboBox.setItems(userBoImpl.getAllUserIds());
                    empAddresstxt.setText("");
                    empNametxt.setText("");
                    empEmailtxt.setText("");
                }
            }
        }


    }

    public void tableMouseClick(MouseEvent mouseEvent) {
    }
}
