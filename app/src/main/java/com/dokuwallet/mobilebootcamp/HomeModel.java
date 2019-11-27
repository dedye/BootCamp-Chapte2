package com.dokuwallet.mobilebootcamp;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeModel implements Serializable {

    @SerializedName("responseCode")
    @Expose
    private String responseCode;
    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;
    @SerializedName("listData")
    @Expose
    private List<HomeDetilModel> listData = null;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<HomeDetilModel> getListData() {
        return listData;
    }

    public void setListData(List<HomeDetilModel> listData) {
        this.listData = listData;
    }

}