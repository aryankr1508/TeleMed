package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Validate {
    @SerializedName("aid")
    @Expose
    private String aid;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

}
