package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.dto.MyResponse;
import com.exercise.p.emailclient.dto.data.Sign;
import com.exercise.p.emailclient.model.RetrofitInstance;
import com.exercise.p.emailclient.model.WelcomeModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    private boolean tag = false;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            if (tag){
                intent.setClass(WelcomeActivity.this, MainActivity.class);
            }
            else {
                intent.setClass(WelcomeActivity.this, SignActivity.class);
            }
            startActivity(intent);
            handler.removeCallbacks(this);
            WelcomeActivity.this.supportFinishAfterTransition();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences preferences = getSharedPreferences("Info",MODE_PRIVATE);
        String token = preferences.getString("token","");
        GlobalInfo.cookie = "accessToken=" + token + ";";
        WelcomeModel model = RetrofitInstance.getRetrofitWithToken().create(WelcomeModel.class);
        Call<MyResponse<Sign>> call = model.verToken();
        call.enqueue(new Callback<MyResponse<Sign>>() {
            @Override
            public void onResponse(Call<MyResponse<Sign>> call, Response<MyResponse<Sign>> response) {
                MyResponse<Sign> myResponse = response.body();
                if (myResponse.getCode() != 200) {
                    Toast.makeText(WelcomeActivity.this, myResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    tag = false;
                } else {
                    GlobalInfo.user = myResponse.getData();
                    tag = true;
                }
                handler.postDelayed(runnable, 1000);
            }

            @Override
            public void onFailure(Call<MyResponse<Sign>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(WelcomeActivity.this, "网络错误，请稍后再试", Toast.LENGTH_SHORT).show();
                tag = false;
                handler.postDelayed(runnable, 1000);
            }
        });
    }
}
