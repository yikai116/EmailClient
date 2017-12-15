package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.param.Email;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
            @Body Email email);

    @GET("mail/")
    Call<MyResponse<ArrayList<com.exercise.p.emailclient.dto.data.Email>>> getAccounts();

    @POST("mail/{boxId}")
    Call<MyResponse> updateAcounts(
            @Path("boxId") int boxId,
            @Body Email email);

    @POST("mail/delete")
    Call<MyResponse> deleteAcount(
            @Body List<Integer> accounts);
}
