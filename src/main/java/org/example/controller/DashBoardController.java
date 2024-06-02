package org.example.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.example.db.DBConnection;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
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

    //-------------------------------------------------------
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

    private int otp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        resetPasswordField(false);
        errormsg.setVisible(false);
        validmsg.setVisible(false);
    }

    public void signinBtnAction(ActionEvent actionEvent) throws IOException {

        SceneSwitchController.getInstance().switchScene(dashboardWindow,"adminDashBoard-form.fxml");

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM user WHERE email='"+emailField.getText()+"'");


            while (resultSet.next()){

                String password = new String(Base64.getDecoder().decode(resultSet.getString(4)));

                if (resultSet.getString(5).equals("Admin") && password.equals(passwordField.getText())){
                    System.out.println("Logged");
                }else{
                    System.out.println("Employee");
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e);
        }
    }

    public void forgotPasswordClickAction(MouseEvent mouseEvent) {
        resetPasswordField(true);

    }

    public void signinBtn2Action(ActionEvent actionEvent) {
        resetPasswordField(false);
    }

    public void otpBtnAction(ActionEvent actionEvent) {

        Random random = new Random();
        otp = random.nextInt(99999)+10000;


        try {
            sendEmail(emailField2.getText(),Integer.toString(otp));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
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
    }

    public void resetBtnOnAction(ActionEvent actionEvent) {

    }

    private void sendEmail(String receiveEmail,String text) throws MessagingException {
        Properties properties = new Properties();

        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");

        String myEmail = "asankapradeep0830@gmail.com";
        String password = "tkmkeibffwnpwjcp";

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myEmail,password);
            }
        });

        Message message = prepareMessage(session,myEmail,emailField2.getText(),text);

        if (message!=null){
            new Alert(Alert.AlertType.INFORMATION,"Send Email Successfully").show();
        }else{
            new Alert(Alert.AlertType.ERROR,"Please Try Again").show();
        }
        Transport.send(message);
    }

    private Message prepareMessage(Session session, String myEmail, String receiveEmail, String text) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmail));
            message.setRecipients(Message.RecipientType.TO,new InternetAddress[]{
                    new InternetAddress(receiveEmail)
            });
            message.setSubject("OTP CODE");
            message.setText(text);

            return message;
        }catch (Exception e){
            Logger.getLogger(DashBoardController.class.getName()).log(Level.SEVERE,null,e);
        }
        return null;
    }

    public void passwordValidate(String password){
        Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#%$!&]).{8,20})");
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()){
            errormsg.setVisible(true);
            validmsg.setVisible(false);
        }else{
            errormsg.setVisible(false);
            validmsg.setVisible(true);
        }
    }

    public void newPasswordKeyReleased(KeyEvent keyEvent) {
        passwordValidate(passwordField1.getText());
    }
}
