package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.databinding.ActivitySendBinding;
import com.exercise.p.emailclient.dto.param.Mail;
import com.exercise.p.emailclient.presenter.SendPresenter;
import com.exercise.p.emailclient.view.SendView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendActivity extends AppCompatActivity implements SendView {

    @BindView(R.id.send_toolbar)
    Toolbar sendToolbar;

    ActivitySendBinding binding;

    MaterialDialog materialDialog;

    SendPresenter presenter;

    Mail mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        mail = new Mail();
        binding.setMail(mail);
        ButterKnife.bind(this);
        presenter = new SendPresenter(this);
        initToolBar();
    }

    /**
     * 初始化标题栏
     */
    private void initToolBar() {
        setSupportActionBar(sendToolbar);
        sendToolbar.setNavigationIcon(R.drawable.icon_back);
        sendToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendActivity.this.finish();
            }
        });
        assert getSupportActionBar() != null;
        sendToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_send) {
                    mail.setTo(mail.getTo().trim());
                    mail.setSubject(mail.getSubject().trim());
                    binding.setMail(mail);
                    presenter.send(mail);
                    Log.i(SignActivity.TAG, "发送");
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void showProgress(boolean show) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(this)
                    .title("请稍后")
                    .content("正在提交")
                    .progress(true, 0)
                    .show();
        }

        if (show)
            materialDialog.show();
        else
            materialDialog.dismiss();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void finishActivity() {
        this.finish();
    }
}
