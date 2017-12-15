package com.exercise.p.emailclient.dto.data;

import java.util.List;

public class FolderResponse {

    /**
     * contentType : multipart/alternative; boundary="001a11498fd0a17be205605ec148"
     * from : 毛昌越 <scumchy@gmail.com>,
     * htmlBody : html
     * id : 43098
     * sendDate : 1513334934000
     * subject : TESTTESTETST
     * textBody : text
     * to : null <15196673448@163.com>,
     */

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