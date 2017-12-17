package com.exercise.p.emailclient.dto.param;


import com.exercise.p.emailclient.GlobalInfo;

import java.util.Date;


public class Mail {

    private String subject;
    private String from;
    private String htmlBody;
    private String to;
    private String contentType;
    private Date sendDate;

    public Mail() {
        this.subject = "";
        this.from = GlobalInfo.getCurrent().getAccount();
        this.htmlBody = "";
        this.to = "";
        // html ： text/html;charset=utf-8
        // text ： text/plain;charset=utf-8
        this.contentType = "text/html;charset=utf-8";
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

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}