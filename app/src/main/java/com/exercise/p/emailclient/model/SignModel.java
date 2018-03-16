package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.UserInfoResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by p on 2017/12/5.
 */

public interface SignModel {
    /**
     * 登录
     * @param param 用户信息
     * @return 返回结果信息
     */
    @POST("user/auth")
    Call<MyResponse<UserInfoResponse>> signIn(
            @Body com.exercise.p.emailclient.dto.param.User user,
            @Header("Cookie") String cookie
    );

    @POST("user/register")
    Call<MyResponse<Object>> signUp(
            @Body com.exercise.p.emailclient.dto.param.UserSignUp user,
            @Header("Cookie") String cookie
    );

    @GET("user/authCode")
    Call<ResponseBody> getCheckImg(
            @Query("a") String a
    );
}
