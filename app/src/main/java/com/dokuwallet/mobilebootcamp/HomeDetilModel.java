package com.dokuwallet.mobilebootcamp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeDetilModel {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("dataContent")
    @Expose
    private String dataContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }
}