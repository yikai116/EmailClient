package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.databinding.ActivitySignUpBinding;
import com.exercise.p.emailclient.dto.param.UserSignUp;
import com.exercise.p.emailclient.presenter.SignPresenter;
import com.exercise.p.emailclient.view.SignView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignView {

    public static String TAG = "Sign";

    ActivitySignUpBinding binding;

    @BindView(R.id.sign_root)
    LinearLayout signRoot;

    SignPresenter presenter;
    UserSignUp user;

    MaterialDialog materialDialog;
    @BindView(R.id.sign_img_check_code)
    ImageView signImgCheckCode;
    @BindView(R.id.sign_up_toolbar)
    Toolbar signUpToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        ButterKnife.bind(this);
        initToolBar();
        presenter = new SignPresenter(this);
        user = new UserSignUp();
        binding.setUser(user);
        initCheckCode();
    }

    private void initToolBar() {
        setSupportActionBar(signUpToolbar);
        signUpToolbar.setNavigationIcon(R.drawable.icon_back);
        signUpToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpActivity.this.finish();

            }
        });

        assert getSupportActionBar() != null;
    }

    @OnClick(R.id.sign_button_sign_in)
    public void signUp() {
        user.setEmail(user.getEmail().trim());
        user.setCheckCode(user.getCheckCode().trim());
        user.setUserName(user.getUserName().trim());
        binding.setUser(user);
        presenter.signUp(user);
    }

    @OnClick(R.id.sign_button_reget_code)
    public void initCheckCode() {
        presenter.getCheckImg();
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void toMainActivity() {
        SignUpActivity.this.finish();
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
    public void showCheckImg(Bitmap bitmap) {
        signImgCheckCode.setImageBitmap(bitmap);
    }

}
