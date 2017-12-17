package com.exercise.p.emailclient.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.exercise.p.emailclient.R;

import java.util.ArrayList;

/**
 * Created by p on 2017/12/17.
 */

public class SimpleAccount {
    private String name;
    private String emailAddr;

    public SimpleAccount(String name, String emailAddr) {
        this.name = name;
        this.emailAddr = emailAddr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddr() {
        return emailAddr;
    }

    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }

    public static ArrayList<SimpleAccount> toList(String str) {
        ArrayList<SimpleAccount> accounts = new ArrayList<>();
        String[] tempStrAccounts = str.split(",");
        for (int i = 0; i < tempStrAccounts.length; i++) {
            String temp = tempStrAccounts[i];
            String[] tempStrInfo = temp.split(" +");
            accounts.add(new SimpleAccount(tempStrInfo[0] == null || tempStrInfo[0].equals("null")
                    ? null : tempStrInfo[0], tempStrInfo[1]));
        }
        return accounts;
    }

    public static SpannableStringBuilder toBlueSpanBuilder(String str, Context context) {
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(
                ContextCompat.getColor(context, R.color.colorTextBlue));
        SpannableStringBuilder builder = new SpannableStringBuilder("");
        String[] tempStrAccounts = str.split(",");
        for (int i = 0; i < tempStrAccounts.length; i++) {
            String temp = tempStrAccounts[i];
            String[] tempStrInfo = temp.split(" +");
            builder.append(tempStrInfo[0] == null || tempStrInfo[0].equals("null")
                    ? "" : tempStrInfo[0] + "\n");
            int start = builder.length();
            builder.append(tempStrInfo[1]);
            int end = builder.length();
            builder.setSpan(blueSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (i != tempStrAccounts.length - 1) {
                builder.append("\n");
            }
        }
        return builder;
    }
}
