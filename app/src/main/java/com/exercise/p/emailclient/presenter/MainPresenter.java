package com.exercise.p.emailclient.presenter;

import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.MailBoxResponse;
import com.exercise.p.emailclient.dto.data.FolderResponse;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.model.AccountModel;
import com.exercise.p.emailclient.model.EmailModel;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.view.MainView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by p on 2017/12/15.
 */

public class MainPresenter {
    private MainView view;
    private AccountModel accountModel;
    private EmailModel emailModel;

    public MainPresenter(MainView view) {
        this.view = view;
        accountModel = RetrofitInstance.getRetrofitWithToken().create(AccountModel.class);
        emailModel = RetrofitInstance.getRetrofitWithToken().create(EmailModel.class);
    }

    // 得到所有账号
    public void getAccounts() {
        view.showProgress(true);
        Call<MyResponse<ArrayList<MailBoxResponse>>> accountCall = accountModel.getAccounts();
        accountCall.enqueue(new Callback<MyResponse<ArrayList<MailBoxResponse>>>() {
            @Override
            public void onResponse(Call<MyResponse<ArrayList<MailBoxResponse>>> call, Response<MyResponse<ArrayList<MailBoxResponse>>> response) {
                view.showProgress(false);
                MyResponse<ArrayList<MailBoxResponse>> myResponse = response.body();
                if ((myResponse != null)) {
                    if (myResponse.getCode() == 200) {
                        GlobalInfo.mailBoxResponses.clear();
                        GlobalInfo.mailBoxResponses.addAll(myResponse.getData());
                        view.updateDrawer();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse<ArrayList<MailBoxResponse>>> call, Throwable t) {
                t.printStackTrace();
                view.showMessage("网络连接错误");
                view.showProgress(false);
            }
        });
    }

    // 删除账号
    public void delete(final int id) {
        view.showProgress(true);
        List<Integer> list = new ArrayList<>();
        list.add(id);
        Call<MyResponse> call = accountModel.deleteAcount(list);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                view.showProgress(false);
                try {
                    Log.i(SignActivity.TAG, "delete server response: " + response.code());
                    if (response.body().getCode() == 200) {
                        view.showMessage("删除成功");
                        for (MailBoxResponse mailBoxResponse : GlobalInfo.mailBoxResponses) {
                            if (mailBoxResponse.getId() == id) {
                                GlobalInfo.mailBoxResponses.remove(mailBoxResponse);
                                break;
                            }
                        }
                        view.updateDrawer();
                    } else {
                        view.showMessage("抱歉，发生错误");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showMessage("抱歉，发生错误");
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                view.showProgress(false);
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });
    }

    // 得到数据库邮件
    public void getEmail(final int id, final String boxType) {
        view.showProgress(true);
        Call<MyResponse<List<FolderResponse>>> accountCall = emailModel.getFolder(id);
        accountCall.enqueue(new Callback<MyResponse<List<FolderResponse>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<FolderResponse>>> call, Response<MyResponse<List<FolderResponse>>> response) {
                view.showProgress(false);
                Log.i(SignActivity.TAG, "get Folder server code :" + response.code());
                MyResponse<List<FolderResponse>> myResponse = response.body();
                if ((myResponse != null)) {
                    if (myResponse.getCode() == 200) {
                        GlobalInfo.allMail = myResponse.getData();
                        view.setData(GlobalInfo.getMailsByBox(boxType));
                        for (FolderResponse folderResponse : GlobalInfo.allMail) {
                            updateEmail(id, folderResponse.getId(), boxType);
                        }
                    } else {
                        view.showMessage("抱歉，发生错误");
                    }
                } else {
                    view.showMessage("抱歉，发生错误");
                }
            }

            @Override
            public void onFailure(Call<MyResponse<List<FolderResponse>>> call, Throwable t) {
                t.printStackTrace();
                view.showMessage("网络连接错误");
                view.showProgress(false);
            }
        });
    }

    // 更新数据库邮件
    public void updateEmail(final int boxId, final int folderId, final String boxType) {
        Log.i(SignActivity.TAG, "update boxId:" + boxId);
        Log.i(SignActivity.TAG, "update folderId:" + folderId);
        Log.i(SignActivity.TAG, "update boxType:" + boxType);
        final Call<MyResponse<List<MailPreviewResponse>>> folder = emailModel.updateFolder(boxId, folderId);
        folder.enqueue(new Callback<MyResponse<List<MailPreviewResponse>>>() {
            @Override
            public void onResponse(Call<MyResponse<List<MailPreviewResponse>>> call, Response<MyResponse<List<MailPreviewResponse>>> response) {
                view.showProgress(false);
                Log.i(SignActivity.TAG, "update " + boxId + "_" + folderId + "_" + boxType + " server code :" + response.code());
                MyResponse<List<MailPreviewResponse>> myResponse = response.body();
                if ((myResponse != null)) {
                    if (myResponse.getCode() == 200) {
                        GlobalInfo.updateMail(folderId, (ArrayList<MailPreviewResponse>) myResponse.getData());
                        if (GlobalInfo.getFolderName(folderId).equals(boxType)) {
                            view.setData(GlobalInfo.getMailsByBox(boxType));
                        }
                    } else {
                        view.showMessage("抱歉，发生错误");
                    }
                } else {
                    view.showMessage("抱歉，发生错误");
                }
            }

            @Override
            public void onFailure(Call<MyResponse<List<MailPreviewResponse>>> call, Throwable t) {
                t.printStackTrace();
                view.showMessage("网络连接错误");
                view.showProgress(false);
            }
        });
    }

    // 删除邮件
    public void deleteEmails(ArrayList<MailPreviewResponse> mails, int folderId) {
        view.showProgress(true);
        List<Integer> list = new ArrayList<>();
        for (MailPreviewResponse email : mails) {
            list.add(email.getId());
        }
        Call<MyResponse> call = emailModel.deleteEmails(GlobalInfo.activeId, folderId, list);
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                view.showProgress(false);
                try {
                    Log.i(SignActivity.TAG, "response: " + response.code());
                    if (response.body().getCode() == 200) {
                        view.showMessage("删除成功");
                        view.deleteSuccess();
                    } else {
                        view.showMessage("抱歉，发生错误");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    view.showMessage("抱歉，发生错误");
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                view.showProgress(false);
                view.showMessage("网络错误，请稍后再试");
                t.printStackTrace();
            }
        });

    }
}
