package com.exercise.p.emailclient.dto.data;

import java.util.List;

public class FolderResponse {

    private Integer id;

    private String alias;

    private String folderType;

    private List<MailPreviewResponse> mailList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFolderType() {
        return folderType;
    }

    public void setFolderType(String folderType) {
        this.folderType = folderType;
    }

    public List<MailPreviewResponse> getMailList() {
        return mailList;
    }

    public void setMailList(List<MailPreviewResponse> mailList) {
        this.mailList = mailList;
    }
}