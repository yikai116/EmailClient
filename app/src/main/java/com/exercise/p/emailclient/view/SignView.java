package com.exercise.p.emailclient.view;

import android.graphics.Bitmap;

/**
 * Created by p on 2017/12/5.
 */

public interface SignView {
    void showMessage(String msg);
    void toMainActivity();
    void showProgress(boolean show);
    void showCheckImg(Bitmap bitmap);
}
