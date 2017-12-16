package com.exercise.p.emailclient.view;

/**
 * Created by p on 2017/12/16.
 */

public interface SendView {
    void showProgress(boolean show);
    void showMessage(String msg);
    void finishActivity();
}
