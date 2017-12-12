package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.exercise.p.emailclient.R;
import com.mikepenz.materialize.color.Material;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackActivity extends AppCompatActivity {


    // 相册请求码
    protected static final int ALBUM_CODE = 101;
    ArrayList<Uri> pics = new ArrayList<>();
    ArrayList<ImageView> images = new ArrayList<>();

    Timer timer = null;
    TimerTask task = null;

    @BindView(R.id.feedback_toolbar)
    Toolbar feedbackToolbar;
    @BindView(R.id.fb_question_des)
    EditText fbQuestionDes;
    @BindView(R.id.fd_pic1)
    ImageView fdPic1;
    @BindView(R.id.fd_pic2)
    ImageView fdPic2;
    @BindView(R.id.fd_pic3)
    ImageView fdPic3;
    @BindView(R.id.fd_pic4)
    ImageView fdPic4;
    @BindView(R.id.add)
    ImageView add;
    @BindView(R.id.fd_phone)
    EditText fdPhone;
    @BindView(R.id.submit)
    Button submit;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String[] strs = {"正在提交", "正在提交.", "正在提交..", "正在提交..."};
            submit.setText(strs[msg.what]);
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initToolBar();
        initView();
    }

    /**
     * 初始化标题栏
     */
    private void initToolBar() {
        setSupportActionBar(feedbackToolbar);
        feedbackToolbar.setNavigationIcon(R.drawable.icon_back);
        feedbackToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.this.finish();
            }
        });
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initView() {
        images.add(fdPic1);
        images.add(fdPic2);
        images.add(fdPic3);
        images.add(fdPic4);
    }

    @OnClick(R.id.add)
    public void addPic() {
        Intent intent = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALBUM_CODE);
    }

    @OnClick(R.id.submit)
    public void submit() {
        if (fbQuestionDes.getText().toString().equals("") || fbQuestionDes.getText().toString() == null) {
            Toast.makeText(FeedbackActivity.this, "请填写问题描述~", Toast.LENGTH_SHORT).show();
            return;
        }

        String content = fbQuestionDes.getText().toString();
        if (fdPhone.getText().toString().equals("") || fbQuestionDes.getText().toString() == null) {
            String temp = "\n联系电话：" + fdPhone.getText().toString();
            content += temp;
        }
        /*
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:way.ping.li@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "CitiCup反馈");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        for (int i = 0; i < pics.size(); i++) {
            intent.putExtra(Intent.EXTRA_STREAM, pics.get(i));
        }
        startActivity(intent);
        */
        new MyAsyncTask().execute();
    }

    public void showProgress(boolean show) {
        submit.setText("提交");
        submit.setClickable(!show);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (show) {
            timer = new Timer();
            task = new TimerTask() {
                int i = 0;
                @Override
                public void run() {
                    handler.sendEmptyMessage(i % 4);
                    i++;
                }
            };
            timer.schedule(task, 0, 500);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM_CODE && resultCode == RESULT_OK) {
            //相册回来进入裁剪activity
            //获取选择图片的uri
            Uri uri = data.getData();
            if (uri != null) {
                pics.add(uri);
                images.get(pics.size() - 1).setImageURI(uri);
                images.get(pics.size() - 1).setVisibility(View.VISIBLE);
                if (pics.size() == 4) {
                    add.setVisibility(View.GONE);
                }
            }
        }
    }

    class MyAsyncTask extends AsyncTask{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            SystemClock.sleep(3000);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            showProgress(false);
            Toast.makeText(FeedbackActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
        }
    }
}
