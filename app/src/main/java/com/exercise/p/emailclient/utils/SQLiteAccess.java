package com.exercise.p.emailclient.utils;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by p on 2017/1/25.
 */

public class SQLiteAccess {
    public static List<MailPreviewResponse> readDraft() {
        StringBuilder condition = new StringBuilder("from = ");
        int tempLength = GlobalInfo.mailBoxResponses.size();
        for (int i = 0; i < tempLength; i++) {
            condition.append("'" + GlobalInfo.mailBoxResponses.get(i).getAccount() + "'");
            if (i != GlobalInfo.mailBoxResponses.size() - 1) {
                condition.append(" or from = ");
            }
        }

        List<MailPreviewResponse> list = DataSupport.where(
                condition.toString())
                .order("sendDate").find(MailPreviewResponse.class);
        return list;
    }
}
