package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.param.Email;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by p on 2017/12/13.
 */

public interface AccountModel {
    @PUT("mail/")
    Call<MyResponse> addAcount(
            @Body Email email);

    @GET("mail/")
    Call<MyResponse<ArrayList<com.exercise.p.emailclient.dto.data.Email>>> getAcounts();

    @PUT("/")
    Call<MyResponse> updateAcounts(
            @Body Email email);

    @PUT("/")
    Call<MyResponse> deleteAcount(
            @Body Email email);
}
