package com.exercise.p.emailclient.dto;

public class User {
   private String account;
   private String psw;
   public User(String account, String psw) {
       this.account = account;
       this.psw = psw;
   }
   public String getAccount() {
       return this.account;
   }
   public String getPsw() {
       return this.psw;
   }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }
}