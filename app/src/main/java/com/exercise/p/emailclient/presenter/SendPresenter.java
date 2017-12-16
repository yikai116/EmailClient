package com.exercise.p.emailclient.presenter;

import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.UserInfo;
import com.exercise.p.emailclient.dto.param.Mail;
import com.exercise.p.emailclient.model.EmailModel;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.utils.FormatUtils;
import com.exercise.p.emailclient.view.SendView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/16.
 */

public class SendPresenter {
    EmailModel model;
    SendView view;

    public SendPresenter(SendView view) {
        this.view = view;
        this.model = RetrofitInstance.getRetrofitWithToken().create(EmailModel.class);
    }

    public void send(Mail mail){
        view.showProgress(true);
        if (!FormatUtils.emailFormat(mail.getTo())) {
            view.showMessage("邮箱格式错误");
            return;
        }
        Call<MyResponse> call = model.sendEmail(GlobalInfo.activeId,mail);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                try {
                    view.showProgress(false);
                    Log.i(SignActivity.TAG,"submit email server response: " + response.code());
                    if (response.body().getCode() == 200) {
                        view.showMessage("发送成功");
                        view.finishActivity();
                    }
                    else {
                        view.showMessage("抱歉，发生错误");
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    view.showMessage("抱歉，发生错误");
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                view.showProgress(false);
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });
    }
}
