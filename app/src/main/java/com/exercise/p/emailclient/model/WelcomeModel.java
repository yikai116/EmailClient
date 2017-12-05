package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.Sign;
import com.exercise.p.emailclient.dto.param.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by p on 2017/12/5.
 */

public interface WelcomeModel {
    /**
     * 登录
     * @param param 用户信息
     * @return 返回结果信息
     */
    @GET("user/auth")
    Call<MyResponse<Sign>> verToken();
}
