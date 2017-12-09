package com.exercise.p.emailclient.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = getSharedPreferences("Info",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", GlobalInfo.user.getAccessToken());
        editor.apply();
    }
}
