package com.exercise.p.emailclient.utils;

/**
 * Created by p on 2017/12/13.
 */

public class FormatUtils {
    public static boolean emailFormat(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }
}
