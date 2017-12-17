package com.exercise.p.emailclient.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.exercise.p.emailclient.R;

/**
 * Created by p on 2017/12/13.
 */

public class FormatUtils {
    public static boolean emailFormat(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }
}
