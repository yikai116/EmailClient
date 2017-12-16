package com.exercise.p.emailclient.view;

import com.exercise.p.emailclient.dto.data.MailPreviewResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p on 2017/12/15.
 */

public interface MainView {
    void updateDrawer();
    void toAddAccountActivity();
    void showProgress(boolean show);
    void showMessage(String msg);
    void setData(ArrayList<MailPreviewResponse> mails);
    void deleteSuccess();
}
