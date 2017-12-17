package com.exercise.p.emailclient.dto.data;

/**
 * Created by p on 2017/12/5.
 */

public class UserInfoResponse {
    /**
     * userName : MailBoxResponse
     * email : 1234567890@qq.com
     * accessToken : bClFg4hi7IM5h4mh
     * root : false
     */

    private String userName;
    private String email;
    private String accessToken;
    private boolean root;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isRoot() {
        return root;
    }

    public void setRoot(boolean root) {
        this.root = root;
    }
}
