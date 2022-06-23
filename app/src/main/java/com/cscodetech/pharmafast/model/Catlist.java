
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Catlist {

    @SerializedName("catimg")
    private String mCatimg;
    @SerializedName("catname")
    private String mCatname;
    @SerializedName("count")
    private int mCount;
    @SerializedName("id")
    private String mId;

    public String getCatimg() {
        return mCatimg;
    }

    public void setCatimg(String catimg) {
        mCatimg = catimg;
    }

    public String getCatname() {
        return mCatname;
    }

    public void setCatname(String catname) {
        mCatname = catname;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

}
