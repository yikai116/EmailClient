package com.exercise.p.emailclient.presenter;

import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.Email;
import com.exercise.p.emailclient.dto.data.FolderResponse;
import com.exercise.p.emailclient.model.AccountModel;
import com.exercise.p.emailclient.model.EmailModel;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.view.MainView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/15.
 */

public class MainPresenter {
    private MainView view;
    private AccountModel accountModel;
    private EmailModel emailModel;

    public MainPresenter(MainView view) {
        this.view = view;
        accountModel = RetrofitInstance.getRetrofitWithToken().create(AccountModel.class);
        emailModel = RetrofitInstance.getRetrofitWithToken().create(EmailModel.class);
    }

    public void getAccounts() {
        view.showProgress(true);
        Call<MyResponse<ArrayList<Email>>> accountCall = accountModel.getAccounts();
        accountCall.enqueue(new Callback<MyResponse<ArrayList<Email>>>() {
            @Override
            public void onResponse(Call<MyResponse<ArrayList<Email>>> call, Response<MyResponse<ArrayList<Email>>> response) {
                view.showProgress(false);
                MyResponse<ArrayList<Email>> myResponse = response.body();
                if ((myResponse != null)) {
                    if (myResponse.getCode() == 200) {
                        GlobalInfo.accounts.clear();
                        GlobalInfo.accounts.addAll(myResponse.getData());
                        view.updateDrawer();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<ArrayList<Email>>> call, Throwable t) {
                t.printStackTrace();
                view.showMessage("网络连接错误");
                view.showProgress(false);
            }
        });
    }

    public void delete(final int id) {
        view.showProgress(true);
        List<Integer> list = new ArrayList<>();
        list.add(id);
        Call<MyResponse> call = accountModel.deleteAcount(list);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                view.showProgress(false);
                try {
                    Log.i(SignActivity.TAG, "delete server response: " + response.code());
                    if (response.body().getCode() == 200) {
                        view.showMessage("删除成功");
                        for (Email email : GlobalInfo.accounts) {
                            if (email.getId() == id) {
                                GlobalInfo.accounts.remove(email);
                                break;
                            }
                        }
                        view.updateDrawer();
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

    public void getEmail(int id) {
        Log.i(SignActivity.TAG,"get Folder id :" + id);
        view.showProgress(true);
        Call<MyResponse<List<FolderResponse>>> accountCall = emailModel.getFolder(id);
        accountCall.enqueue(new Callback<MyResponse<List<FolderResponse>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<FolderResponse>>> call, Response<MyResponse<List<FolderResponse>>> response) {
                view.showProgress(false);
                Log.i(SignActivity.TAG,"get Folder server code :" + response.code());
                MyResponse<List<FolderResponse>> myResponse = response.body();
                if ((myResponse != null)) {
                    if (myResponse.getCode() == 200) {
                        Log.i(SignActivity.TAG,"get Folder");
                    }else {
                        Log.i(SignActivity.TAG,"get Folder code :" + myResponse.getCode());
                    }
                }
                else {
                    Log.i(SignActivity.TAG,"get Folder null");
                }
            }

            @Override
            public void onFailure(Call<MyResponse<List<FolderResponse>>> call, Throwable t) {
                t.printStackTrace();
                view.showMessage("网络连接错误");
                view.showProgress(false);
            }
        });
    }
}
