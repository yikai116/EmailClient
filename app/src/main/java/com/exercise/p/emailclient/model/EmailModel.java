package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.FolderResponse;
import com.exercise.p.emailclient.dto.param.Email;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by p on 2017/12/15.
 */

public interface EmailModel {

    @GET("mail/{boxId}")
    Call<MyResponse<List<FolderResponse>>> getFolder(
            @Path("boxId") int boxId);
}
