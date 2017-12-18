package com.exercise.p.emailclient.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.presenter.DetailPresenter;
import com.exercise.p.emailclient.utils.SimpleAccount;
import com.exercise.p.emailclient.view.DetailView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity implements DetailView {

    @BindView(R.id.detail_toolbar)
    Toolbar detailToolbar;
    @BindView(R.id.detail_subject)
    TextView detailSubject;
    @BindView(R.id.mail_detail_from)
    TextView mailDetailFrom;
    @BindView(R.id.mail_detail_to)
    TextView mailDetailTo;
    @BindView(R.id.mail_detail_date)
    TextView mailDetailDate;
    @BindView(R.id.info_detail_from)
    TextView infoDetailFrom;
    @BindView(R.id.info_detail_to)
    TextView infoDetailTo;
    @BindView(R.id.info_detail_date)
    TextView infoDetailDate;
    @BindView(R.id.mail_detail_content)
    WebView mailDetailContent;
    @BindView(R.id.detail)
    LinearLayout detail;

    public static final int REPLY = 0;
    public static final int FORWARD = 1;

    MailPreviewResponse mail;

    MaterialDialog materialDialog;
    String folderType;
    int position;
    @BindView(R.id.processBar)
    ProgressBar processBar;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            detailActionLayout.setVisibility(View.VISIBLE);
            processBar.setVisibility(View.GONE);
            mailDetailContent.setVisibility(View.VISIBLE);
            return false;
        }
    });
    @BindView(R.id.detail_action_layout)
    LinearLayout detailActionLayout;
    @BindView(R.id.detail_folder_name)
    TextView detailFolderName;

    DetailPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        folderType = getIntent().getStringExtra("folderType");
        position = getIntent().getIntExtra("position", -1);
        Log.i(SignActivity.TAG, "detail folderType: " + folderType + "  position" + position);
        try {
            mail = GlobalInfo.getMailsByBox(folderType).get(position);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
            DetailActivity.this.finish();
            return;
        }
        if (mail == null) {
            Log.i(SignActivity.TAG, "get the mail null");
            Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
            DetailActivity.this.finish();
            return;
        }
        initToolBar();
        initView();
        presenter = new DetailPresenter(this);
        if (!mail.isSeen())
            presenter.markAsSeen(GlobalInfo.getFolderId(folderType), mail.getId(), false);
    }

    private void initToolBar() {
        setSupportActionBar(detailToolbar);
        if (GlobalInfo.mailBoxResponses.size() != 0) {
            detailToolbar.setNavigationIcon(R.drawable.icon_back);
            detailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailActivity.this.finish();
                }
            });
        }
        assert getSupportActionBar() != null;
        detailToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_delete) {
                    Log.i(SignActivity.TAG, "detail activity click delete");
                    ArrayList<MailPreviewResponse> list = new ArrayList<>();
                    list.add(mail);
                    presenter.deleteEmails(list, GlobalInfo.getFolderId(folderType));
                }
                else {
                    presenter.markAsSeen(GlobalInfo.getFolderId(folderType), mail.getId(), true);
                    if (item.getItemId() == R.id.action_read) {
                        item.setVisible(false);
                        if (item.getItemId() == R.id.action_read) {
                            detailToolbar.getMenu().getItem(2).setVisible(true);
                        }
                        else {
                            detailToolbar.getMenu().getItem(1).setVisible(true);
                        }
                    }
                }
                return false;
            }
        });
    }

    private void initView() {
        detailSubject.setText(mail.getSubject());
        if (folderType.equals("INBOX"))
            detailFolderName.setText(">>收件箱");
        if (folderType.equals("SENT"))
            detailFolderName.setText(">>发件箱");
        if (folderType.equals("DRAFT"))
            detailFolderName.setText(">>草稿箱");
        if (folderType.equals("JUNK"))
            detailFolderName.setText(">>垃圾邮件");
        if (folderType.equals("TRASH"))
            detailFolderName.setText(">>已删除");
        setSimpleDetail();
        infoDetailFrom.setText(
                SimpleAccount.toBlueSpanBuilder(mail.getFrom(), DetailActivity.this));
        infoDetailTo.setText(
                SimpleAccount.toBlueSpanBuilder(mail.getTo(), DetailActivity.this));
        infoDetailDate.setText(new SimpleDateFormat("yyyy年MM月dd日 HH:mm",
                Locale.getDefault()).format(mail.getSendDate()));
        mailDetailContent.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        mailDetailContent.getSettings().setSupportZoom(true);
        mailDetailContent.getSettings().setBuiltInZoomControls(true);
        mailDetailContent.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i(SignActivity.TAG, "page finished");
                handler.sendEmptyMessageDelayed(0, 500);
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(SignActivity.TAG, "page started");
                processBar.setVisibility(View.VISIBLE);
                mailDetailContent.setVisibility(View.GONE);
                super.onPageStarted(view, url, favicon);
            }
        });
        if (mail.getHtmlBody() == null || mail.getHtmlBody().equals("")) {
            mailDetailContent.loadData(mail.getTextBody(), "text/html; charset=UTF-8", null);
        } else {
            mailDetailContent.loadData(mail.getHtmlBody(), "text/html; charset=UTF-8", null);
        }
    }

    private void setSimpleDetail() {
        ArrayList<SimpleAccount> fromList = SimpleAccount.toList(mail.getFrom());
        StringBuilder fromStr = new StringBuilder();
        for (int i = 0; i < fromList.size(); i++) {
            fromStr.append(fromList.get(i).getName() == null
                    ? fromList.get(i).getEmailAddr() : fromList.get(i).getName());
            if (i != fromList.size() - 1)
                fromStr.append(",");
        }
        mailDetailFrom.setText(fromStr);

        ArrayList<SimpleAccount> toList = SimpleAccount.toList(mail.getTo());
        StringBuilder toStr = new StringBuilder();
        for (int i = 0; i < toList.size(); i++) {
            toStr.append(toList.get(i).getName() == null
                    ? toList.get(i).getEmailAddr() : toList.get(i).getName());
            if (i != toList.size() - 1)
                toStr.append(",");
        }
        mailDetailTo.setText(toStr);

        //设置字体颜色
        final SpannableStringBuilder dateBuilder1 = new SpannableStringBuilder(
                new SimpleDateFormat("MM月dd日", Locale.getDefault())
                        .format(mail.getSendDate()) + " 查看详情");
        final SpannableStringBuilder dateBuilder2 = new SpannableStringBuilder("隐藏详情");
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(
                ContextCompat.getColor(this, R.color.colorTextBlue));
        dateBuilder1.setSpan(blueSpan, 6, dateBuilder1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dateBuilder2.setSpan(blueSpan, 0, dateBuilder2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mailDetailDate.setText(dateBuilder1);
        mailDetailDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (detail.getVisibility() == View.VISIBLE) {
                    detail.setVisibility(View.GONE);
                    mailDetailDate.setText(dateBuilder1);
                } else {
                    detail.setVisibility(View.VISIBLE);
                    mailDetailDate.setText(dateBuilder2);
                }
            }
        });
    }

    @Override
    public void finish() {
        mailDetailContent.clearCache(false);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action, menu);
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

    @OnClick({R.id.detail_reply, R.id.detail_reply_all})
    public void reply() {
        Intent intent = new Intent(DetailActivity.this, SendActivity.class);
        intent.putExtra("action", REPLY);
        intent.putExtra("folderType", folderType);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @OnClick(R.id.detail_forward)
    public void forward() {
        Intent intent = new Intent(DetailActivity.this, SendActivity.class);
        intent.putExtra("action", FORWARD);
        intent.putExtra("folderType", folderType);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}
