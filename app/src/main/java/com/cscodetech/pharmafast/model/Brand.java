
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Brand {

    @SerializedName("bname")
    private String mBname;
    @SerializedName("id")
    private String mId;
    @SerializedName("img")
    private String mImg;
    @SerializedName("popular")
    private String mPopular;
    @SerializedName("status")
    private String mStatus;

    public String getBname() {
        return mBname;
    }

    public void setBname(String bname) {
        mBname = bname;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImg() {
        return mImg;
    }

    public void setImg(String img) {
        mImg = img;
    }

    public String getPopular() {
        return mPopular;
    }

    public void setPopular(String popular) {
        mPopular = popular;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
