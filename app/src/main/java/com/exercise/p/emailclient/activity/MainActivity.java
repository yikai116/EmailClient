package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int SIDE_ALL = 1;
    private static final int SIDE_SENT = 2;
    private static final int SIDE_DRAFT = 3;
    private static final int SIDE_GARBAGE = 4;
    private static final int SIDE_DELETE = 5;
    private static final int SIDE_SETTING = 6;
    private static final int SIDE_FEEDBACK = 7;

    private static final int SIDE_ADD_COUNT = 8;
    private static final int SIDE_MANAGE_COUNT = 9;

    private static final int EMAIL_COUNT = 100;

    private int side_select = SIDE_ALL;

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_pager)
    FrameLayout mainPager;
    @BindView(R.id.main_drawerLayout)
    DrawerLayout mainDrawerLayout;
    ActionBar actionBar;
    Drawer result;
    AccountHeader headerResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SharedPreferences preferences = getSharedPreferences("Info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", GlobalInfo.user.getAccessToken());
        editor.apply();
        initToolBar();
        initDrawer();
    }

    private void initToolBar() {
        //设置toolbar属性
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("所有邮箱");
    }

    private void initDrawer() {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .addProfiles(
                        new ProfileDrawerItem().withName(GlobalInfo.user.getUserName())
                                .withEmail(GlobalInfo.user.getEmail())
                                .withIcon(R.drawable.icon_side_avatar)
                                .withIdentifier(EMAIL_COUNT),
                        new ProfileSettingDrawerItem().withName("添加账号")
                                .withIcon(R.drawable.icon_side_add_count)
                                .withIdentifier(SIDE_ADD_COUNT),
                        new ProfileSettingDrawerItem().withName("管理账号")
                                .withIcon(R.drawable.icon_side_manage_count)
                                .withIdentifier(SIDE_MANAGE_COUNT)
                )
                .withCurrentProfileHiddenInList(true)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (!current)
                            Log.i(SignActivity.TAG, "profile changed");
                        return true;
                    }
                })
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        return true;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return true;
                    }
                })
                .build();

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mainToolbar)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("所有邮件").withIdentifier(SIDE_ALL)
                                .withIcon(R.drawable.icon_side_all),
                        new PrimaryDrawerItem().withName("已发邮件").withIdentifier(SIDE_SENT)
                                .withIcon(R.drawable.icon_side_sent),
                        new PrimaryDrawerItem().withName("草稿箱").withIdentifier(SIDE_DRAFT)
                                .withIcon(R.drawable.icon_side_draft),
                        new PrimaryDrawerItem().withName("垃圾邮件").withIdentifier(SIDE_GARBAGE)
                                .withIcon(R.drawable.icon_side_garbage),
                        new PrimaryDrawerItem().withName("已删除").withIdentifier(SIDE_DELETE)
                                .withIcon(R.drawable.icon_side_delete),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("设置").withIdentifier(SIDE_SETTING)
                                .withIcon(R.drawable.icon_side_setting),
                        new PrimaryDrawerItem().withName("帮助和反馈").withIdentifier(SIDE_FEEDBACK)
                                .withIcon(R.drawable.icon_side_feedback)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D
                        int temp = (int) drawerItem.getIdentifier();
                        Log.i(SignActivity.TAG, "item click :" + temp);
                        switch ((int) drawerItem.getIdentifier()){
                            case SIDE_ALL:
                                Log.i(SignActivity.TAG,"click 1 " + ((PrimaryDrawerItem)drawerItem).getName().toString());
                                break;
                            case SIDE_SENT:
                                Log.i(SignActivity.TAG,"click 2 " + ((PrimaryDrawerItem)drawerItem).getName().toString());
                                break;
                            case SIDE_DRAFT:
                                Log.i(SignActivity.TAG,"click 3 " + ((PrimaryDrawerItem)drawerItem).getName().toString());
                                break;
                            case SIDE_GARBAGE:
                                Log.i(SignActivity.TAG,"click 4 " + ((PrimaryDrawerItem)drawerItem).getName().toString());
                                break;
                            case SIDE_DELETE:
                                Log.i(SignActivity.TAG,"click 5 " + ((PrimaryDrawerItem)drawerItem).getName().toString());
                                break;
                            case SIDE_SETTING:
                                Log.i(SignActivity.TAG,"click 6 " + ((PrimaryDrawerItem)drawerItem).getName().toString());
                                temp = side_select;
                                result.setSelection(side_select,false);
                                break;
                            case SIDE_FEEDBACK:
                                Log.i(SignActivity.TAG,"click 7 " + ((PrimaryDrawerItem)drawerItem).getName().toString());
                                temp = side_select;
                                result.setSelection(side_select,false);
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this,FeedbackActivity.class);
                                startActivity(intent);
                                break;
                        }
                        side_select = temp;
                        return false;
                    }
                })
                .build();
        result.setSelection(side_select,false);
    }
}
