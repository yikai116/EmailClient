package com.exercise.p.emailclient;

import android.app.Application;
import android.content.SharedPreferences;

import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.Sign;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.model.WelcomeModel;

import retrofit2.Call;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by p on 2017/12/5.
 */

public class GlobalInfo {
    public static Sign user = null;
    public static String cookie = "";
}
