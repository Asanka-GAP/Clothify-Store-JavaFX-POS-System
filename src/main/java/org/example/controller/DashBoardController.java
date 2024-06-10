package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.example.bo.BoFactory;
import org.example.bo.SuperBo;
import org.example.bo.custom.impl.UserBoImpl;
import org.example.dao.custom.impl.UserDaoImpl;
import org.example.entity.UserEntity;
import org.example.util.BoType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.beans.Encoder;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashBoardController implements Initializable {
    public JFXTextField emailField;
    public JFXPasswordField passwordField;
    public JFXButton signinBtn;
    public Text forgotPasswordLink;
    public Rectangle panel2;
    public JFXButton signinBtn2;
    public Rectangle panel3;
    public JFXTextField emailField2;
    public JFXButton otpBtn;
    public JFXPasswordField passwordField1;
    public JFXPasswordField reEnterPasswordField;
    public JFXTextField otpTextField;
    public JFXButton resetBtn;
    public Text resetTxt;
    public Text errormsg;
    public Text validmsg;
    public AnchorPane dashboardWindow;
    public FontAwesomeIconView iconCart;
    public Text titleTxt;
    public FontAwesomeIconView mainCartIcon;
    public Text mainTitle;
    public Text notification;

    private int otp;

    UserBoImpl userBoImpl=new UserBoImpl();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetPasswordField(false);
        errormsg.setVisible(false);
        validmsg.setVisible(false);

    }

    public void signinBtnAction(ActionEvent actionEvent) throws IOException {

        userBoImpl=BoFactory.getInstance().getBo(BoType.USER);
        UserEntity userEntity = userBoImpl.getUserByEmail(emailField.getText());


        if (userEntity==null){
            new Alert(Alert.AlertType.ERROR,"Invalid Email Address or Password").show();
            return;
        }
        String password = userBoImpl.passwordDecrypt(userEntity.getPassword());

            if (userEntity.getRole().equals("Admin") && password.equals(passwordField.getText())){
                System.out.println("Logged");
                try {
                    SceneSwitchController.getInstance().switchScene(dashboardWindow,"adminDashBoard-form.fxml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else if (userEntity.getRole().equals("Employee") && password.equals(passwordField.getText())){
                EmployeeData instance = EmployeeData.getInstance();
                instance.setId(userEntity.getId());
                instance.setName(userEntity.getName());
                instance.setEmail(userEntity.getEmail());
                SceneSwitchController.getInstance().switchScene(dashboardWindow,"placeOrder-form.fxml");
            } else if (userEntity.getId()==null) {
                System.out.println("Null");
            } else{
                new Alert(Alert.AlertType.ERROR,"Invalid Password").show();
            }
    }

    public void forgotPasswordClickAction(MouseEvent mouseEvent) {
        UserBoImpl userBoImpl = BoFactory.getInstance().getBo(BoType.USER);
        UserEntity userEntity = userBoImpl.getUserByEmail(emailField.getText());

        if (userEntity==null){
            new Alert(Alert.AlertType.ERROR,"Please enter your correct email address").show();
            return;
        }
        resetPasswordField(true);
        emailField2.setText(emailField.getText());
        emailField2.setDisable(true);

    }

    public void signinBtn2Action(ActionEvent actionEvent) {
        errormsg.setVisible(false);
        resetPasswordField(false);
    }

    public void otpBtnAction(ActionEvent actionEvent) {

        Random random = new Random();
        otp = random.nextInt(999999)+100000;
        System.out.println(otp);

        try {
            userBoImpl.sendEmail(emailField2.getText(),Integer.toString(otp));
            new Alert(Alert.AlertType.INFORMATION,"Send Email Successfully").show();
        } catch (MessagingException e) {
            new Alert(Alert.AlertType.ERROR,"Access Denided..your Email is invalid").show();
        }

    }


    
    private void resetPasswordField(boolean b){
        panel2.setVisible(b);
        panel3.setVisible(b);
        passwordField1.setVisible(b);
        reEnterPasswordField.setVisible(b);
        emailField2.setVisible(b);
        signinBtn2.setVisible(b);
        otpBtn.setVisible(b);
        otpTextField.setVisible(b);
        resetBtn.setVisible(b);
        resetTxt.setVisible(b);
        iconCart.setVisible(b);
        titleTxt.setVisible(b);
        mainCartIcon.setVisible(!b);
        mainTitle.setVisible(!b);
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {

        try {
            if (passwordField1.getText().equals(reEnterPasswordField.getText())){
                if (otp==Integer.parseInt(otpTextField.getText())){
                    boolean isUpdatePassword = userBoImpl.isUpdatePassword(emailField2.getText(),passwordField1.getText());
                    if (isUpdatePassword){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Reset Password");
                        alert.setContentText("Password reset Successfully");
                        alert.showAndWait();
                        passwordField1.setText("");
                        reEnterPasswordField.setText("");
                        otpTextField.setText("");
                    }
                }else {
                    new Alert(Alert.AlertType.ERROR,"Incorrect OTP, Please Check your OTP").show();
                }

            }else {
                new Alert(Alert.AlertType.ERROR,"Password & Confirmation Password does not match..!!").show();
            }
        }catch (Exception e){
            System.out.println(e);
            new Alert(Alert.AlertType.ERROR,"Invalid OTP").show();
        }

    }





    public void newPasswordKeyReleased(KeyEvent keyEvent) {
        boolean isValidPassword = userBoImpl.passwordValidate(passwordField1.getText());
        if (!isValidPassword){
            errormsg.setVisible(true);
            validmsg.setVisible(false);
            reEnterPasswordField.setDisable(true);
        }else{
            reEnterPasswordField.setDisable(false);
            errormsg.setVisible(false);
            validmsg.setVisible(true);
        }
    }

    public void closeBtnAction(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setContentText("Are you sure want to exit..?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            System.exit(0);
        }
    }
}
