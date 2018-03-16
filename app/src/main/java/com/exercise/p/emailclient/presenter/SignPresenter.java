package com.exercise.p.emailclient.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.UserInfoResponse;
import com.exercise.p.emailclient.dto.param.User;
import com.exercise.p.emailclient.dto.param.UserSignUp;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.model.SignModel;
import com.exercise.p.emailclient.utils.FormatUtils;
import com.exercise.p.emailclient.view.SignView;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/5.
 */

public class SignPresenter {
    private SignModel signModel;
    private SignView view;

    private String cookie = null;

    public SignPresenter(SignView view) {
        signModel = RetrofitInstance.getRetrofit().create(SignModel.class);
        this.view = view;
    }

    public void sign(User user) {
        if (!FormatUtils.emailFormat(user.getEmail())) {
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
        Call<MyResponse<UserInfoResponse>> call = signModel.signIn(user, cookie);
        call.enqueue(new Callback<MyResponse<UserInfoResponse>>() {
            @Override
            public void onResponse(Call<MyResponse<UserInfoResponse>> call, Response<MyResponse<UserInfoResponse>> response) {
                view.showProgress(false);
                MyResponse<UserInfoResponse> myResponse = response.body();
                if (myResponse.getCode() != 200) {
                    view.showMessage(myResponse.getMessage());
                } else {
                    GlobalInfo.user = myResponse.getData();
                    GlobalInfo.authorization = "Bearer " + GlobalInfo.user.getAccessToken();
                    view.toMainActivity();
                }
            }

            @Override
            public void onFailure(Call<MyResponse<UserInfoResponse>> call, Throwable t) {
                view.showProgress(false);
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });
    }

    public void getCheckImg() {
        Call<ResponseBody> call = signModel.getCheckImg(String.valueOf(Math.random()));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                cookie = response.headers().get("Set-Cookie");
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    view.showCheckImg(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showMessage("网络错误，请稍后再试");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });
    }

    public void signUp(UserSignUp user) {
        if (!FormatUtils.emailFormat(user.getEmail())) {
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
        if (user.getUserName().length() == 0) {
            view.showMessage("用户名错误");
            return;
        }
        view.showProgress(true);
        Call<MyResponse<Object>> call = signModel.signUp(user, cookie);
        call.enqueue(new Callback<MyResponse<Object>>() {
            @Override
            public void onResponse(Call<MyResponse<Object>> call, Response<MyResponse<Object>> response) {
                view.showProgress(false);
                MyResponse<Object> myResponse = response.body();
                view.showMessage(myResponse.getMessage());
                if (myResponse.getCode() == 200) {
                    view.toMainActivity();
                }
            }

            @Override
            public void onFailure(Call<MyResponse<Object>> call, Throwable t) {
                view.showProgress(false);
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });
    }
}
