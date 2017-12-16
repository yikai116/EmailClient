package com.exercise.p.emailclient.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        initToolBar();
    }

    private void initToolBar() {
        setSupportActionBar(detailToolbar);
        if (GlobalInfo.emails.size() != 0) {
            detailToolbar.setNavigationIcon(R.drawable.icon_back);
            detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailActivity.this.finish();
                }
            });
        }
        assert getSupportActionBar() != null;
    }
}
