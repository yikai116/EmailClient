package com.exercise.p.emailclient.dto.data;

import com.google.gson.Gson;

public class MailPreviewResponse {
    private Integer id;

    // 主题
    private String subject;

    private String from;

    // 纯文本 有可能其中一个为null
    private String textBody;

    // html
    private String htmlBody;

    private String to;

    private String contentType;

    private long sendDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public String getHtmlBody() {
        return htmlBody;
    }

    public void setHtmlBody(String htmlBody) {
        this.htmlBody = htmlBody;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSendDate() {
        return sendDate;
    }

    public void setSendDate(long sendDate) {
        this.sendDate = sendDate;
    }


    @Override
    public String toString() {
        this.setTextBody("text");
        this.setHtmlBody("html");
        return new Gson().toJson(this);
    }
}
