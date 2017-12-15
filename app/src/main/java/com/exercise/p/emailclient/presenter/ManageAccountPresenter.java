package com.exercise.p.emailclient.presenter;

import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.Email;
import com.exercise.p.emailclient.model.AccountModel;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.view.ManageAccountView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/15.
 */

public class ManageAccountPresenter {
    private AccountModel accountModel;
    private ManageAccountView view;

    public ManageAccountPresenter(ManageAccountView view) {
        this.view = view;
        accountModel = RetrofitInstance.getRetrofitWithToken().create(AccountModel.class);
    }

    public void delete(ArrayList<Email> accounts) {
        view.showProgress(true);
        List<Integer> list = new ArrayList<>();
        for (Email email : accounts) {
            list.add(email.getId());
        }
        Call<MyResponse> call = accountModel.deleteAcount(list);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                view.showProgress(false);
                try {
                    Log.i(SignActivity.TAG, "response: " + response.code());
                    if (response.body().getCode() == 200) {
                        view.showMessage("删除成功");
                        GlobalInfo.Main2ManageIschange = true;
                        view.deleteSuccess();
                    } else {
                        view.showMessage("抱歉，发生错误");
                    }
                } catch (Exception e) {
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
