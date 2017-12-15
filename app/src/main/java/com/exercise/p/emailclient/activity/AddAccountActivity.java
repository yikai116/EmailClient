package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.databinding.ActivityAddAccountBinding;
import com.exercise.p.emailclient.dto.param.Email;
import com.exercise.p.emailclient.presenter.AccountPresenter;
import com.exercise.p.emailclient.view.AddAccountView;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAccountActivity extends AppCompatActivity implements AddAccountView {


    ActivityAddAccountBinding binding;
    Email email;

    int code = 0;

    AccountPresenter presenter;

    MaterialDialog materialDialog;
    @BindView(R.id.add_account_toolbar)
    Toolbar addAccountToolbar;
    @BindView(R.id.add_account_edit_email)
    EditText addAccountEditEmail;
    @BindView(R.id.add_account_edit_psw)
    EditText addAccountEditPsw;
    @BindView(R.id.add_account_button_submit)
    Button addAccountButtonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        email = new Email();
        email.setSmtpServer("smtp.163.com");
        email.setSmtpPort("465");
        email.setPop3Server("pop.163.com");
        email.setPop3Port("995");
        email.setAccount("15196673448@163.com");
        email.setPassword("yk123456");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_account);
        binding.setEmail(email);
        ButterKnife.bind(this);
        initToolBar();
        presenter = new AccountPresenter(this);
        addAccountButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(SignActivity.TAG, "Click");
                email.setAccount(email.getAccount().trim());
                email.setSmtpServer(email.getSmtpServer().trim());
                email.setPop3Server(email.getPop3Server().trim());
                binding.setEmail(email);
                presenter.submitAccount(email);
            }
        });
    }

    /**
     * 初始化标题栏
     */
    private void initToolBar() {
        setSupportActionBar(addAccountToolbar);
        if (GlobalInfo.accounts.size() != 0) {
            addAccountToolbar.setNavigationIcon(R.drawable.icon_back);
            addAccountToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddAccountActivity.this.finish();
                }
            });
        }
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @OnClick(R.id.add_account_button_submit)
    public void submit() {
        Log.i(SignActivity.TAG, "Click");
        email.setAccount(email.getAccount().trim());
        binding.setEmail(email);
        presenter.submitAccount(email);
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
        if (GlobalInfo.accounts.size() == 0) {
            Intent intent = new Intent();
            intent.setClass(AddAccountActivity.this, MainActivity.class);
            startActivity(intent);
        }
        this.finish();
    }


}
