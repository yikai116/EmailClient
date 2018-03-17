package com.exercise.p.emailclient.activity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.R;
import com.exercise.p.emailclient.databinding.ActivitySendBinding;
import com.exercise.p.emailclient.dto.data.MailBoxResponse;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.dto.param.Mail;
import com.exercise.p.emailclient.presenter.SendPresenter;
import com.exercise.p.emailclient.utils.SimpleAccount;
import com.exercise.p.emailclient.view.SendView;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import io.github.mthli.knife.KnifeText;

public class SendActivity extends AppCompatActivity implements SendView {

    @BindView(R.id.send_toolbar)
    Toolbar sendToolbar;

    ActivitySendBinding binding;

    MaterialDialog materialDialog;

    SendPresenter presenter;

    Mail mail;
    @BindView(R.id.knife)
    KnifeText knife;
    MailPreviewResponse temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        String folderType = getIntent().getStringExtra("folderType");
        int position = getIntent().getIntExtra("position", -1);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_send);
        mail = new Mail();
        ButterKnife.bind(this);
        binding.setShow(false);
        presenter = new SendPresenter(this);
        initToolBar();
        initTool();

        if (folderType != null && position != -1) {
            int action = getIntent().getIntExtra("action", DetailActivity.FORWARD);
            temp = GlobalInfo.getMailsByBox(folderType).get(position);
            if (action == DetailActivity.REPLY) {
                mail.setSubject("Re:" + temp.getSubject());
                ArrayList<SimpleAccount> accounts = SimpleAccount.toList(temp.getFrom());
                StringBuilder stringBuilder = new StringBuilder("");
                for (int i = 0; i < accounts.size(); i++) {
                    stringBuilder.append(accounts.get(i).getEmailAddr());
                    stringBuilder.append(",");
                }
                mail.setTo(stringBuilder.toString());
            } else {
                mail.setSubject(temp.getSubject());
                mail.setTo(temp.getTo());
                if (temp.getTextBody() == null || temp.getTextBody().equals(""))
                    knife.fromHtml(temp.getHtmlBody());
                else
                    knife.setText(temp.getTextBody());
            }
        }
        Log.i("SEND",mail.getTo());
        binding.setMail(mail);
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
                onBackPressed();
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
                    mail.setHtmlBody(knife.toHtml());
                    presenter.send(mail);
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
    public void finish() {
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Log.i(SignActivity.TAG, "back pressed");
        new MaterialDialog.Builder(SendActivity.this)
                .backgroundColor(Color.WHITE)
                .contentColor(getResources().getColor(R.color.colorTextBlack))
                .titleColor(getResources().getColor(R.color.colorTextBlack))
                .itemsColor(getResources().getColor(R.color.colorTextBlack))
                .widgetColor(getResources().getColor(R.color.colorTextBlack))
                .title("提示")
                .content("是否进行保存？")
                .positiveText("保存")
                .negativeText("不")
                .onAny(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (DialogAction.POSITIVE == which) {
                            if (!mail.getTo().equals("") || !mail.getSubject().equals("") || !mail.getHtmlBody().equals("")) {

                                if (temp == null) {
                                    temp = new MailPreviewResponse();
                                }
                                temp.setContentType(mail.getContentType());
                                temp.setFrom(mail.getFrom());
                                temp.setTo(mail.getTo().trim());
                                temp.setSubject(mail.getSubject().trim());
                                temp.setHtmlBody(knife.toHtml());
                                temp.setSendDate(new Date().getTime());
                                GlobalInfo.getMailsByBox("DRAFT").add(temp);
                                temp.save();
                            }
                        }
                        SendActivity.super.onBackPressed();
                    }
                }).show();
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

    @OnFocusChange(R.id.knife)
    public void OnTextFocusChange(boolean hasFocus) {
        binding.setShow(hasFocus);
    }

    @OnClick(R.id.send_from)
    public void OnSendFromClick() {
        ArrayList<String> accounts = new ArrayList<>();
        for (MailBoxResponse box : GlobalInfo.mailBoxResponses) {
            accounts.add(box.getAccount());
        }
        new MaterialDialog.Builder(this)
                .title("选择邮箱")
                .items(accounts)
                .backgroundColor(Color.WHITE)
                .contentColor(getResources().getColor(R.color.colorTextBlack))
                .titleColor(getResources().getColor(R.color.colorTextBlack))
                .itemsColor(getResources().getColor(R.color.colorTextBlack))
                .widgetColor(getResources().getColor(R.color.colorTextBlack))
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        if (text != null && text.length() > 0) {
                            mail.setFrom((String) text);
                            binding.setMail(mail);
                        }
                        return true;
                    }
                })
                .positiveText("确定")
                .show();
    }

    private void initTool() {
        setupBold();
        setupItalic();
        setupUnderline();
        setupStrikethrough();
        setupBullet();
        setupQuote();
        setupLink();
        setupClear();
    }

    private void setupBold() {
        ImageButton bold = findViewById(R.id.bold);

        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bold(!knife.contains(KnifeText.FORMAT_BOLD));
            }
        });

        bold.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_bold, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupItalic() {
        ImageButton italic = findViewById(R.id.italic);

        italic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.italic(!knife.contains(KnifeText.FORMAT_ITALIC));
            }
        });

        italic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_italic, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupUnderline() {
        ImageButton underline = findViewById(R.id.underline);

        underline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.underline(!knife.contains(KnifeText.FORMAT_UNDERLINED));
            }
        });

        underline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_underline, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupStrikethrough() {
        ImageButton strikethrough = findViewById(R.id.strikethrough);

        strikethrough.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.strikethrough(!knife.contains(KnifeText.FORMAT_STRIKETHROUGH));
            }
        });

        strikethrough.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_strikethrough, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupBullet() {
        ImageButton bullet = findViewById(R.id.bullet);

        bullet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.bullet(!knife.contains(KnifeText.FORMAT_BULLET));
            }
        });


        bullet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_bullet, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupQuote() {
        ImageButton quote = findViewById(R.id.quote);

        quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                knife.quote(!knife.contains(KnifeText.FORMAT_QUOTE));
            }
        });

        quote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_quote, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupLink() {
        ImageButton link = findViewById(R.id.link);

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLinkDialog();
            }
        });

        link.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_insert_link, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void setupClear() {
        ImageButton clear = findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(SendActivity.this)
                        .title("提示")
                        .content("确定清除所有格式？")
                        .positiveText("确定")
                        .negativeText("返回")
                        .onAny(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if (DialogAction.POSITIVE == which) {
                                    knife.clearFormats();
                                }
                            }
                        }).show();
            }
        });

        clear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SendActivity.this, R.string.toast_format_clear, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void showLinkDialog() {
        final int start = knife.getSelectionStart();
        final int end = knife.getSelectionEnd();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog_link, null, false);
        final EditText editText = view.findViewById(R.id.edit);
        builder.setView(view);
        builder.setTitle(R.string.dialog_title);

        builder.setPositiveButton(R.string.dialog_button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String link = editText.getText().toString().trim();
                if (TextUtils.isEmpty(link)) {
                    return;
                }

                // When KnifeText lose focus, use this method
                knife.link(link, start, end);
            }
        });

        builder.setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // DO NOTHING HERE
            }
        });

        builder.create().show();
    }
}
