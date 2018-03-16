package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.databinding.ActivityAddAccountBinding;
import com.exercise.p.emailclient.dto.param.MailBox;
import com.exercise.p.emailclient.presenter.AddAccountPresenter;
import com.exercise.p.emailclient.view.AddAccountView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAccountActivity extends AppCompatActivity implements AddAccountView {

    ActivityAddAccountBinding binding;
    MailBox mailBox;

    AddAccountPresenter presenter;

    MaterialDialog materialDialog;
    @BindView(R.id.add_account_toolbar)
    Toolbar addAccountToolbar;

    private int code = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        Intent intent = getIntent();
        code = intent.getIntExtra("code", 0);
        mailBox = new MailBox();
        mailBox.setSmtpPort("465");
        mailBox.setPop3Port("995");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_account);
        binding.setMailBox(mailBox);
        ButterKnife.bind(this);
        initToolBar();
        presenter = new AddAccountPresenter(this);
    }

    /**
     * 初始化标题栏
     */
    private void initToolBar() {
        setSupportActionBar(addAccountToolbar);
        addAccountToolbar.setNavigationIcon(R.drawable.icon_back);
        addAccountToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (code == 1) {
                    AddAccountActivity.this.finish();
                }
                else {
                    if (GlobalInfo.mailBoxResponses.size() == 0){
                        Intent intent = new Intent();
                        intent.setClass(AddAccountActivity.this, SignActivity.class);
                        startActivity(intent);
                        AddAccountActivity.this.finish();
                    }
                }
            }
        });

        assert getSupportActionBar() != null;
    }

    @OnClick(R.id.add_account_button_submit)
    public void submit() {
        mailBox.setAccount(mailBox.getAccount().trim());
        mailBox.setSmtpServer(mailBox.getSmtpServer().trim());
        mailBox.setPop3Server(mailBox.getPop3Server().trim());
        binding.setMailBox(mailBox);
        presenter.submitAccount(mailBox);
    }

    @Override
    public void showProgress(boolean show) {
        if (materialDialog == null) {
            materialDialog = new MaterialDialog.Builder(this)
                    .backgroundColor(Color.WHITE)
                    .contentColor(getResources().getColor(R.color.colorTextBlack))
                    .titleColor(getResources().getColor(R.color.colorTextBlack))
                    .itemsColor(getResources().getColor(R.color.colorTextBlack))
                    .widgetColor(getResources().getColor(R.color.colorTextBlack))
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
        //刚刚登录的状态
        if (code == 0) {
            Intent intent = new Intent();
            intent.setClass(AddAccountActivity.this, MainActivity.class);
            startActivity(intent);
            this.finish();
            return;
        }
        //主界面通过按钮进入管理界面返回
        if (code == 1) {
            GlobalInfo.Main2AddIsChange = true;
            this.finish();
            return;
        }
    }


}
