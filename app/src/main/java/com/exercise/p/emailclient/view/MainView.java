package com.exercise.p.emailclient.view;

/**
 * Created by p on 2017/12/15.
 */

public interface MainView {
    void updateDrawer();
    void toAddAccountActivity();
    void showProgress(boolean show);
    void showMessage(String msg);
}
