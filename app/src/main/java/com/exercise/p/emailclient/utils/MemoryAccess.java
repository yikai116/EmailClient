package com.exercise.p.emailclient.utils;

import android.os.Environment;
import android.util.Log;

import com.exercise.p.emailclient.GlobalInfo;
import com.exercise.p.emailclient.activity.SignActivity;
import com.exercise.p.emailclient.dto.data.MailPreviewResponse;
import com.exercise.p.emailclient.dto.param.Mail;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by p on 2017/1/25.
 */

public class MemoryAccess {
    public static void saveDraftToSD() throws IOException {
        File sdDir = Environment.getExternalStorageDirectory();
        String myPath = sdDir.getPath() + "/Email";
        File myDir = new File(myPath);
        if (!myDir.exists()) {

            myDir.mkdirs();
        }
        myPath = myDir.getPath() + "/" + GlobalInfo.activeId + ".txt";
        myDir = new File(myPath);
        if (!myDir.exists())
            myDir.createNewFile();
        BufferedWriter writer = new BufferedWriter(new FileWriter(myDir.getPath()));

        for (MailPreviewResponse mail : GlobalInfo.getMailsByBox("DRAFT")) {
            writer.write(mail.toString() + "\n");
        }
        writer.close();
    }

    public static ArrayList<MailPreviewResponse> readDraftFromSD() throws IOException {
        ArrayList<MailPreviewResponse> mails = new ArrayList<>();
        File sdDir = Environment.getExternalStorageDirectory();

        String myPath = sdDir.getPath() + "/Email";
        File myDir = new File(myPath);
        if (!myDir.exists()) {
            boolean x = myDir.mkdirs();
            Log.i(SignActivity.TAG,"mdir reslut " + x);
        }
        myPath = myDir.getPath() + "/" + GlobalInfo.activeId + ".txt";
        File file = new File(myPath);
        if (!file.exists())
            file.createNewFile();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        for (String line = reader.readLine(); line != null && (!line.equals("")); ) {
            mails.add(MailPreviewResponse.toObject(line));
            line = reader.readLine();
        }
        reader.close();
        return mails;
    }
}
