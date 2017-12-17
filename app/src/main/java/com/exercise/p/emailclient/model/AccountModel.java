package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.MailBoxResponse;
import com.exercise.p.emailclient.dto.param.MailBox;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by p on 2017/12/13.
 */

public interface AccountModel {
    @PUT("mail/")
    Call<MyResponse> addAcount(
            @Body MailBox mailBox);

    @GET("mail/")
    Call<MyResponse<ArrayList<MailBoxResponse>>> getAccounts();

    @POST("mail/{boxId}")
    Call<MyResponse> updateAcounts(
            @Path("boxId") int boxId,
            @Body MailBox mailBox);

    @POST("mail/delete")
    Call<MyResponse> deleteAcount(
            @Body List<Integer> accounts);
}
