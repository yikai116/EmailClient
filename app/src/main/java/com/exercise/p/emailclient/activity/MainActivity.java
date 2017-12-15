package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.dto.data.Email;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.presenter.MainPresenter;
import com.exercise.p.emailclient.utils.AccountAdapter;
import com.exercise.p.emailclient.utils.MailItemAdapter;
import com.exercise.p.emailclient.view.MainView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView {


    public static final int CODE = 101;

    private static final int SIDE_ALL = 1;
    private static final int SIDE_SENT = 2;
    private static final int SIDE_DRAFT = 3;
    private static final int SIDE_GARBAGE = 4;
    private static final int SIDE_DELETE = 5;
    private static final int SIDE_SETTING = 6;
    private static final int SIDE_FEEDBACK = 7;

    private static final int SIDE_ADD_COUNT = 8;

    @BindView(R.id.main_recycler_view)
    RecyclerView mainRecyclerView;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;


    MailItemAdapter adapter;

    ActionBar actionBar;
    Drawer result;
    AccountHeader headerResult;
    ActionMode actionMode = null;

    MainPresenter mainPresenter;

    // 提示框
    MaterialDialog materialDialog;
    // 当前选择邮箱
    int activeId = 0;
    // 邮箱文件夹当前选择
    private int side_select = SIDE_ALL;

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //加载actions
            actionMode = mode;
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_action, menu);
            actionMode.setTitle("123");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            //当actions的item被点击时回掉
            mode.finish();
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //当action mode销毁时的回掉
            actionMode = null;
        }
    };

    private Drawer.OnDrawerListener mDrawerListener = new Drawer.OnDrawerListener() {
        @Override
        public void onDrawerOpened(View drawerView) {
        }

        @Override
        public void onDrawerClosed(View drawerView) {
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            if (actionMode != null) {
                actionMode.finish();
                actionMode = null;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mainPresenter = new MainPresenter(this);
        initToolBar();
        initDrawer();
        initRecyclerView();
        mainPresenter.getAccounts();
    }

    private void initToolBar() {
        //设置toolbar属性
        setSupportActionBar(mainToolbar);
        actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("所有邮箱");
    }

    private void initRecyclerView() {
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mainRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void initDrawer() {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                .withOnlyMainProfileImageVisible(true)
                .withProfileImagesClickable(false)
                .withOnAccountHeaderItemLongClickListener(new AccountHeader.OnAccountHeaderItemLongClickListener() {
                    @Override
                    public boolean onProfileLongClick(View view, IProfile profile, boolean current) {
                        mainPresenter.delete((int) profile.getIdentifier());
                        return false;
                    }
                })
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (!current) {
                            int temp = (int) profile.getIdentifier();
                            if (temp == SIDE_ADD_COUNT) {
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, AddAccountActivity.class);
                                intent.putExtra("code", 1);
                                startActivityForResult(intent, CODE);
                            }
                            else {
                                activeId = temp;
                                mainPresenter.getEmail(activeId, (String) result.getDrawerItem(side_select).getTag());
                            }
                        }
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
                                .withTag("INBOX")
                                .withIcon(R.drawable.icon_side_all),
                        new PrimaryDrawerItem().withName("已发邮件").withIdentifier(SIDE_SENT)
                                .withTag("SENT")
                                .withIcon(R.drawable.icon_side_sent),
                        new PrimaryDrawerItem().withName("草稿箱").withIdentifier(SIDE_DRAFT)
                                .withTag("DRAFT")
                                .withIcon(R.drawable.icon_side_draft),
                        new PrimaryDrawerItem().withName("垃圾邮件").withIdentifier(SIDE_GARBAGE)
                                .withTag("JUNK")
                                .withIcon(R.drawable.icon_side_garbage),
                        new PrimaryDrawerItem().withName("已删除").withIdentifier(SIDE_DELETE)
                                .withTag("TRASH")
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
                        int temp = (int) drawerItem.getIdentifier();
                        switch ((int) drawerItem.getIdentifier()) {
                            case SIDE_SETTING:
                                temp = side_select;
                                result.setSelection(side_select, false);
                                break;
                            case SIDE_FEEDBACK:
                                temp = side_select;
                                result.setSelection(side_select, false);
                                Intent intent = new Intent();
                                intent.setClass(MainActivity.this, FeedbackActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                Log.i(SignActivity.TAG,"click tag： " + (String) drawerItem.getTag());
                                setData(GlobalInfo.getMailsByBox((String) drawerItem.getTag()));
                        }
                        side_select = temp;
                        return false;
                    }
                })
                .withOnDrawerListener(mDrawerListener)
                .build();
        result.setSelection(side_select, false);
    }

    @Override
    public void updateDrawer() {
        if (headerResult.getActiveProfile() != null) {
            activeId = (int) headerResult.getActiveProfile().getIdentifier();
        }
        headerResult.clear();
        if (GlobalInfo.accounts.size() == 0) {
            toAddAccountActivity();
            return;
        }
        headerResult.addProfiles(
                new ProfileSettingDrawerItem().withName("添加账号")
                        .withIcon(R.drawable.icon_side_add_account)
                        .withIdentifier(SIDE_ADD_COUNT)
        );
        for (int i = 0; i < GlobalInfo.accounts.size(); i++) {
            Email email = GlobalInfo.accounts.get(i);
            headerResult.addProfile(new ProfileDrawerItem().withName(GlobalInfo.user.getUserName())
                    .withEmail(email.getAccount())
                    .withIcon(R.drawable.icon_side_avatar)
                    .withIdentifier(email.getId()), 0);
        }

        if (headerResult.getActiveProfile().getIdentifier() != activeId) {
            // 更新邮箱数据
            activeId = (int) headerResult.getActiveProfile().getIdentifier();
            mainPresenter.getEmail(activeId, (String) result.getDrawerItem(side_select).getTag());
        }
    }

    @Override
    public void toAddAccountActivity() {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AddAccountActivity.class);
        startActivity(intent);
        MainActivity.this.finish();
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setData(ArrayList<MailPreviewResponse> mails) {
        adapter = new MailItemAdapter(mails, MainActivity.this);
        adapter.setOnItemClickListener(mOnItemClickListener);
        mainRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE && (GlobalInfo.Main2AddIschange || GlobalInfo.Main2ManageIschange)) {
            mainPresenter.getAccounts();
            GlobalInfo.Main2AddIschange = false;
            GlobalInfo.Main2ManageIschange = false;
        }
    }


    private MailItemAdapter.OnItemClickListener mOnItemClickListener = new MailItemAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Log.i(SignActivity.TAG, "click " + position);
        }

        @Override
        public void onItemLongClick(View view, int position) {
            Log.i(SignActivity.TAG, "long click " + position);
        }
    };
}
