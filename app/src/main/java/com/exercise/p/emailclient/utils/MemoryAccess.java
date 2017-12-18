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
    public static void saveCourseToSD(ArrayList<Mail> mails) throws IOException {
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

        for (Mail mail : mails) {
//            Log.i("Save",c.toString());
            writer.write(mail.toString() + "\n");
        }
        writer.close();
    }

    public static ArrayList<MailPreviewResponse> readCourseFromSD() throws IOException {
        Log.i(SignActivity.TAG,"read from SD id" + GlobalInfo.activeId);
        ArrayList<MailPreviewResponse> courses = new ArrayList<>();
        File sdDir = Environment.getExternalStorageDirectory();
        String myPath = sdDir.getPath() + "/Email/" + GlobalInfo.activeId + ".txt";
        File myDir = new File(myPath);
        BufferedReader reader = new BufferedReader(new FileReader(myDir.getPath()));

        for (String line = reader.readLine(); line != null && (!line.equals("")); ) {
            courses.add(MailPreviewResponse.toObject(line));
            line = reader.readLine();
        }
        reader.close();
        return courses;
    }
}
