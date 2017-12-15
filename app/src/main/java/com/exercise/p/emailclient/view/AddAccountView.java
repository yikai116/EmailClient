package com.exercise.p.emailclient.view;

/**
 * Created by p on 2017/12/13.
 */

public interface AddAccountView {
    void showProgress(boolean show);
    void showMessage(String msg);
    void finishActivity();
}
