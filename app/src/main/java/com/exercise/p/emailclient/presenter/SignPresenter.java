package com.exercise.p.emailclient.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.Sign;
import com.exercise.p.emailclient.dto.param.User;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.model.SignModel;
import com.exercise.p.emailclient.view.SignView;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/5.
 */

public class SignPresenter {
    private SignModel model;
    private SignView view;

    private String cookie = null;

    public SignPresenter(SignView view) {
        model = RetrofitInstance.getRetrofit().create(SignModel.class);
        this.view = view;
    }

    public void sign(User user) {
        if (!emailFormat(user.getEmail())) {
            view.showMessage("邮箱格式错误");
            return;
        }
        if (user.getPassword().length() < 6) {
            view.showMessage("密码格式错误");
            return;
        }
        if (user.getCheckCode().length() < 4) {
            view.showMessage("验证码格式错误");
            return;
        }
        view.showProgress(true);
        Call<MyResponse<Sign>> call = model.signIn(user, cookie);
        call.enqueue(new Callback<MyResponse<Sign>>() {
            @Override
            public void onResponse(Call<MyResponse<Sign>> call, Response<MyResponse<Sign>> response) {
                view.showProgress(false);
                MyResponse<Sign> myResponse = response.body();
                if (myResponse.getCode() != 200) {
                    view.showMessage(myResponse.getMessage());
                } else {
                    GlobalInfo.user = myResponse.getData();
                    view.toMainActivity();
                }
            }
            @Override
            public void onFailure(Call<MyResponse<Sign>> call, Throwable t) {
                view.showProgress(false);
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });
    }

    public void getCheckImg() {
        Call<ResponseBody> call = model.getCheckImg(String.valueOf(Math.random()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                cookie = response.headers().get("Set-Cookie");
                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                view.showCheckImg(bitmap);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });
    }

    private boolean emailFormat(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

}
