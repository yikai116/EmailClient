package com.exercise.p.emailclient.presenter;

import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.param.Email;
import com.exercise.p.emailclient.model.AccountModel;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.utils.FormatUtils;
import com.exercise.p.emailclient.view.AddAccountView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/13.
 */

public class AddAccountPresenter {
    private AccountModel model;
    private AddAccountView view;

    public AddAccountPresenter(AddAccountView view) {
        model = RetrofitInstance.getRetrofitWithToken().create(AccountModel.class);
        this.view = view;
    }

    public void submitAccount(final Email email){
        if (!FormatUtils.emailFormat(email.getAccount())) {
            view.showMessage("邮箱格式错误");
            return;
        }
        if (email.getPassword().length() < 6) {
            view.showMessage("密码格式错误");
            return;
        }
        view.showProgress(true);
        Call<MyResponse> call = model.addAcount(email);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                try {
                    view.showProgress(false);
                    Log.i(SignActivity.TAG,"submit account server response: " + response.code());
                    if (response.body().getCode() == 200) {
                        view.showMessage("添加成功");
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
