package com.exercise.p.emailclient;

import com.exercise.p.emailclient.dto.data.Email;
import com.exercise.p.emailclient.dto.data.UserInfo;

import java.util.ArrayList;

/**
 * Created by p on 2017/12/5.
 */

public class GlobalInfo {
    public static UserInfo user = null;
    public static String authorization = "";
    public static ArrayList<Email> accounts = new ArrayList<>();
    public static Email currentEmail = null;
    public static boolean Main2AddIschange = false;
    public static boolean Manage2AddIschange = false;
    public static boolean Main2ManageIschange = false;
}
