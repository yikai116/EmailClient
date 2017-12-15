package com.exercise.p.emailclient.presenter;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.Email;
import com.exercise.p.emailclient.model.AccountModel;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.view.MainView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/15.
 */

public class MainPresenter {
    private MainView view;
    private AccountModel accountModel;

    public MainPresenter(MainView view) {
        this.view = view;
        accountModel = RetrofitInstance.getRetrofitWithToken().create(AccountModel.class);
    }

    public void init(final boolean changeAccount) {
        view.showProgress(true);
        Call<MyResponse<ArrayList<Email>>> accountCall = accountModel.getAcounts();
        accountCall.enqueue(new Callback<MyResponse<ArrayList<Email>>>() {
            @Override
            public void onResponse(Call<MyResponse<ArrayList<Email>>> call, Response<MyResponse<ArrayList<Email>>> response) {
                MyResponse<ArrayList<Email>> myResponse = response.body();
                if ((myResponse != null)) {
                    if (myResponse.getCode() == 200) {
                        GlobalInfo.accounts.clear();
                        GlobalInfo.accounts.addAll(myResponse.getData());
                        if (GlobalInfo.accounts.size() == 0) {
                            view.toAddAccountActivity();
                        } else {
                            GlobalInfo.currentEmail = GlobalInfo.accounts.get(0);
                            view.updateDrawer();
                            if (changeAccount) {
                                // todo
                                view.showProgress(false);
                            } else {
                                view.showProgress(false);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<ArrayList<Email>>> call, Throwable t) {
                t.printStackTrace();
                view.showMessage("网络连接错误");
            }
        });
    }
}
