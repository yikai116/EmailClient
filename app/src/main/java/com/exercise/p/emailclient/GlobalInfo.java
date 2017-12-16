package com.exercise.p.emailclient;

import android.accounts.Account;
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
    public static ArrayList<Email> emails = new ArrayList<>();
    public static boolean Main2AddIsChange = false;
    public static boolean Manage2AddIsChange = false;
    public static boolean Main2ManageIsChange = false;
    public static boolean Main2SendIsChange = false;
    public static List<FolderResponse> allMail = new ArrayList<>();

    // 当前邮箱账户id
    public static int activeId = 0;

    public static ArrayList<MailPreviewResponse> getMailsByBox(String boxType) {
        for (FolderResponse folder : GlobalInfo.allMail) {
            if (folder.getFolderType().equals(boxType)) {
                return (ArrayList<MailPreviewResponse>) folder.getMailList();
            }
        }
        Log.i(SignActivity.TAG, "get null");
        return new ArrayList<MailPreviewResponse>();
    }

    public static void updateMail(int folderId, ArrayList<MailPreviewResponse> mails) {
        for (FolderResponse folder : GlobalInfo.allMail) {
            if (folder.getId().equals(folderId)) {
                folder.getMailList().clear();
                folder.getMailList().addAll(mails);
            }
        }
    }

    public static String getFolderName(int folderId) {
        for (FolderResponse folder : GlobalInfo.allMail) {
            if (folder.getId().equals(folderId)) {
                return folder.getFolderType();
            }
        }
        return "";
    }

    public static int getFolderId(String name) {
        for (FolderResponse folder : GlobalInfo.allMail) {
            if (folder.getFolderType().equals(name)) {
                return folder.getId();
            }
        }
        return 0;
    }

    public static Email getCurrent() {
        for (Email email : emails) {
            if (email.getId() == activeId)
                return email;
        }
        return null;
    }
}
