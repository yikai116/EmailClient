package com.exercise.p.emailclient.dto.param;

public class User {
   private String email;
   private String password;
   private String checkCode;

    public User() {
        email = "";
        password = "";
        checkCode = "";
    }

    public String getEmail() {
       return this.email;
   }
    public String getPassword() {
       return this.password;
   }
    public String getCheckCode() {
        return checkCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }
}