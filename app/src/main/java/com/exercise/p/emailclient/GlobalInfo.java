package com.exercise.p.emailclient;

import android.util.Log;

import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.data.Email;
import com.exercise.p.emailclient.dto.data.FolderResponse;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.dto.data.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p on 2017/12/5.
 */

public class GlobalInfo {
    public static UserInfo user = null;
    public static String authorization = "";
    public static ArrayList<Email> accounts = new ArrayList<>();
    public static boolean Main2AddIschange = false;
    public static boolean Manage2AddIschange = false;
    public static boolean Main2ManageIschange = false;
    public static List<FolderResponse> allMail = new ArrayList<>();

    public static ArrayList<MailPreviewResponse> getMailsByBox(String boxType){
        Log.i(SignActivity.TAG,"get box type: " + boxType);
        for (FolderResponse folder : GlobalInfo.allMail) {
            Log.i(SignActivity.TAG,"all type: " + folder.getFolderType());
            if (folder.getFolderType().equals(boxType)) {
                return (ArrayList<MailPreviewResponse>) folder.getMailList();
            }
        }
        return new ArrayList<MailPreviewResponse>();
    }
}
