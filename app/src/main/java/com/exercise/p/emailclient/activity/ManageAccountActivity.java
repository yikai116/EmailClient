package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.dto.data.MailBoxResponse;
import com.exercise.p.emailclient.presenter.ManageAccountPresenter;
import com.exercise.p.emailclient.utils.AccountAdapter;
import com.exercise.p.emailclient.view.ManageAccountView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageAccountActivity extends AppCompatActivity implements ManageAccountView {

    @BindView(R.id.manage_account_toolbar)
    Toolbar manageAccountToolbar;
    @BindView(R.id.manage_account_recycler_view)
    RecyclerView manageAccountRecyclerView;
    ArrayList<MailBoxResponse> accounts = GlobalInfo.mailBoxResponses;

    ArrayList<MailBoxResponse> account_del = new ArrayList<>();
    ArrayList<View> views = new ArrayList<>();

    ActionMode actionMode;
    AccountAdapter adapter;

    MaterialDialog materialDialog;
    ManageAccountPresenter presenter;

    public static final int CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);
        ButterKnife.bind(this);
        initToolBar();
        initRecyclerView();
        presenter = new ManageAccountPresenter(this);
    }

    /**
     * 初始化标题栏
     */
    private void initToolBar() {
        setSupportActionBar(manageAccountToolbar);
        manageAccountToolbar.setNavigationIcon(R.drawable.icon_back);
        manageAccountToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageAccountActivity.this.finish();
            }
        });
        assert getSupportActionBar() != null;
        manageAccountToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_add) {
                    Intent intent = new Intent();
                    intent.setClass(ManageAccountActivity.this, AddAccountActivity.class);
                    intent.putExtra("code",2);
                    startActivityForResult(intent, CODE);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manage_account, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE && resultCode == RESULT_OK && GlobalInfo.Manage2AddIsChange) {
            GlobalInfo.Manage2AddIsChange = false;
            GlobalInfo.Main2ManageIsChange = true;
            ManageAccountActivity.this.finish();
        }
    }

    private void initRecyclerView() {
        manageAccountRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AccountAdapter(accounts, ManageAccountActivity.this);
        adapter.setOnItemClickListener(mOnItemClickListener);
        manageAccountRecyclerView.setItemAnimator(new DefaultItemAnimator());
        manageAccountRecyclerView.setAdapter(adapter);
    }

    private void add2Del(View view, int position) {
        account_del.add(accounts.get(position));
        adapter.setChoose(position, true);
        view.setBackgroundColor(getResources().getColor(R.color.colorSelected));
        views.add(view);
    }

    private void removeFromDel(View view, int position) {
        account_del.remove(accounts.get(position));
        adapter.setChoose(position, false);
        views.remove(view);
        view.setBackgroundColor(0);
    }

    @Override
    public void deleteSuccess() {
        adapter.removeAll(account_del);
        accounts.removeAll(account_del);
        account_del.clear();
        views.clear();
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

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //加载actions
            actionMode = mode;
            MenuInflater inflater = actionMode.getMenuInflater();
            inflater.inflate(R.menu.menu_action, menu);
            actionMode.setTitle("请选择");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            presenter.delete(account_del);
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            //当action mode销毁时的回掉
            actionMode = null;
            for (View view : views) {
                view.setBackgroundColor(0);
            }
            adapter.setAllChoose(false);
            account_del.clear();
            views.clear();
        }
    };

    private AccountAdapter.OnItemClickListener mOnItemClickListener = new AccountAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (actionMode != null) {
                if (account_del.contains(accounts.get(position))) {
                    removeFromDel(view, position);
                } else {
                    add2Del(view, position);
                }
            }
        }

        @Override
        public void onItemLongClick(View view, int position) {
            if (actionMode == null)
                startSupportActionMode(mActionModeCallback);
            add2Del(view, position);
        }
    };

}
