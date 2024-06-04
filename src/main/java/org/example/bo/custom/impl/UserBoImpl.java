package org.example.bo.custom.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.bo.custom.UserBo;
import org.example.controller.DashBoardController;
import org.example.dao.DaoFactory;
import org.example.dao.custom.impl.UserDaoImpl;
import org.example.entity.UserEntity;
import org.example.model.User;
import org.example.util.DaoType;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserBoImpl implements UserBo {
    UserDaoImpl userDaoImpl = DaoFactory.getInstance().getDao(DaoType.USER);


    public boolean insertUser(User user){

        UserEntity userEntity = new ObjectMapper().convertValue(user, UserEntity.class);
        return userDaoImpl.insert(userEntity);
    }

    public UserEntity getUserByEmail(String email) {
        return userDaoImpl.search(email);
    }

    public boolean isUpdatePassword(String email,String password){
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
    public boolean isValidEmail(String email){
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    public ObservableList<String> getAllUserIds(){
        ObservableList<UserEntity> list = userDaoImpl.searchAll();

        ObservableList<String> idList = FXCollections.observableArrayList();

        list.forEach(userEntity -> {
            idList.add(userEntity.getId());
        });
        return idList;
    }

    public User getUserById(String id){
        UserEntity userEntity = userDaoImpl.searchById(id);
        return new ObjectMapper().convertValue(userEntity,User.class);
    }
    public ObservableList<User> getAllUsers(){
        ObservableList<UserEntity> list = userDaoImpl.searchAll();
        ObservableList<User> userList =FXCollections.observableArrayList();

        list.forEach(userEntity -> {
            userList.add(new ObjectMapper().convertValue(userEntity,User.class));
        });
        return userList;
    }

    public boolean updateUser(User user){
        UserEntity userEntity = new ObjectMapper().convertValue(user, UserEntity.class);

        return userDaoImpl.update(userEntity);
    }
    public boolean deleteUserById(String id){
        return userDaoImpl.delete(id);
    }

    public String generateEmployeeId() {

        String lastEmployeeId = userDaoImpl.getLatestId();

        int number = Integer.parseInt(lastEmployeeId.split("U")[1]);
        number++;
        return String.format("U%04d", number);
    }
}
