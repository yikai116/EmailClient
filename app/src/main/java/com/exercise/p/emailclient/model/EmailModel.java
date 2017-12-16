package com.exercise.p.emailclient.model;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.FolderResponse;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.dto.param.Email;
import com.exercise.p.emailclient.dto.param.Mail;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by p on 2017/12/15.
 */

public interface EmailModel {

    @GET("mail/{boxId}")
    Call<MyResponse<List<FolderResponse>>> getFolder(
            @Path("boxId") int boxId);

    @GET("mail/{boxId}/{folderId}")
    Call<MyResponse<List<MailPreviewResponse>>> updateFolder(
            @Path("boxId") int boxId, @Path("folderId") int folderId);

    @POST("mail/{boxId}/{folderId}/delete")
    Call<MyResponse> deleteEmails(
            @Path("boxId") int boxId,
            @Path("folderId") int folderId,
            @Body List<Integer> ids);

    @PUT("mail/{boxId}")
    Call<MyResponse> sendEmail(
            @Path("boxId") int boxId,
            @Body Mail mail);
}
