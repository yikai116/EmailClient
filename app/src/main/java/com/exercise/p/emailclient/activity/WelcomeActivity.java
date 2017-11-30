package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.dto.User;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, SignActivity.class);
            startActivity(intent);
            handler.removeCallbacks(this);
            WelcomeActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        /** todo
         *  验证登录状态
         **/
        handler.postDelayed(runnable, 1000);
    }
}
