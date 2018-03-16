package com.exercise.p.emailclient.dto.param;

/**
 * Created by p on 2018/3/16.
 */

public class UserSignUp {
    private String email;
    private String userName;
    private String password;
    private String checkCode;

    public UserSignUp() {
        this.email = "";
        this.userName = "";
        this.password = "";
        this.checkCode = "";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}
