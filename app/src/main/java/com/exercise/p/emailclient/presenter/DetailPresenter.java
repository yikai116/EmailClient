package com.exercise.p.emailclient.presenter;

import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.dto.param.Mail;
import com.exercise.p.emailclient.model.EmailModel;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.utils.FormatUtils;
import com.exercise.p.emailclient.view.DetailView;
import com.exercise.p.emailclient.view.SendView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/16.
 */

public class DetailPresenter {
    EmailModel model;
    DetailView view;

    public DetailPresenter(DetailView view) {
        this.view = view;
        this.model = RetrofitInstance.getRetrofitWithToken().create(EmailModel.class);
    }

    // 删除邮件
    public void deleteEmails(ArrayList<MailPreviewResponse> mails, int folderId) {
        view.showProgress(true);
        List<Integer> list = new ArrayList<>();
        for (MailPreviewResponse email : mails) {
            list.add(email.getId());
        }
        Call<MyResponse> call = model.deleteEmails(GlobalInfo.activeId, folderId, list);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                view.showProgress(false);
                try {
                    Log.i(SignActivity.TAG, "response: " + response.code());
                    if (response.body().getCode() == 200) {
                        view.showMessage("删除成功");
                        GlobalInfo.Main2DetailIsChange = true;
                        view.finishActivity();
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

    public void markAsSeen(int folderId, int mailId, final boolean finish){
        if (finish)
            view.showProgress(true);
        Call<MyResponse> call = model.markAsSeen(GlobalInfo.activeId, folderId, mailId);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                try {
                    Log.i(SignActivity.TAG, "mark response: " + response.code());
                    if (response.body().getCode() == 200) {
                        GlobalInfo.Main2DetailIsChange = true;
                        if (finish) {
                            view.showProgress(false);
                            view.showMessage("标记成功");
                            view.finishActivity();
                        }
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
