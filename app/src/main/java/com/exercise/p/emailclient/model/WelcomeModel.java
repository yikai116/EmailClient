package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.UserInfo;

import retrofit2.Call;
import retrofit2.http.GET;

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
    Call<MyResponse<UserInfo>> verToken();
}
