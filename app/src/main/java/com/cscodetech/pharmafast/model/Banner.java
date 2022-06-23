
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Banner {

    @SerializedName("id")
    private String mId;
    @SerializedName("img")
    private String mImg;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("position")
    private int positions;

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

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public int getPositions() {
        return positions;
    }

    public void setPositions(int positions) {
        this.positions = positions;
    }
}
