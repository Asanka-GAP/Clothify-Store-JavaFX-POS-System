package org.example.bo.custom.impl;

import org.example.bo.custom.UserBo;
import org.example.controller.DashBoardController;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.UserDaoImpl;
import org.example.entity.UserEntity;
import org.example.util.DaoType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserBoImpl implements UserBo {
    UserDaoImpl userDaoImpl = DaoFactory.getInstance().getDao(DaoType.USER);

    public UserEntity getUserByEmail(String email) {
        try {
            return userDaoImpl.search(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUpdatePassword(String email,String password) throws SQLException {
        String encryptPassword=passwordEncrypt(password);
        return userDaoImpl.update(email,encryptPassword);
    }

    public String passwordEncrypt(String password){
        return new String(Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8)));
    }
    public String passwordDecrypt(String password){
        return new String(Base64.getDecoder().decode(password));
    }
    public boolean passwordValidate(String password){
        Pattern pattern = Pattern.compile("((?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#%$!&]).{8,20})");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();

    }
    public void sendEmail(String receiveEmail,String text) throws MessagingException {

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

        Message message = prepareMessage(session,myEmail,receiveEmail,text);
        Transport.send(message);
    }

    public Message prepareMessage(Session session, String myEmail, String receiveEmail, String text) {
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
}
