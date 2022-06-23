
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class PaymentItem implements Serializable {


    @SerializedName("id")
    private String mId;
    @SerializedName("img")
    private String mImg;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("subtitle")
    private String subtitle;
    @SerializedName("attributes")
    private String mAttributes;

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmImg() {
        return mImg;
    }

    public void setmImg(String mImg) {
        this.mImg = mImg;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmAttributes() {
        return mAttributes;
    }

    public void setmAttributes(String mAttributes) {
        this.mAttributes = mAttributes;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
