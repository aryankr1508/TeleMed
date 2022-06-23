package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("ccode")
    private String mCcode;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("fname")
    private String mFname;
    @SerializedName("id")
    private String mId;
    @SerializedName("lname")
    private String mLname;
    @SerializedName("mobile")
    private String mMobile;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("rdate")
    private String mRdate;
    @SerializedName("status")
    private String mStatus;

    public String getCcode() {
        return mCcode;
    }

    public void setCcode(String ccode) {
        mCcode = ccode;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFname() {
        return mFname;
    }

    public void setFname(String fname) {
        mFname = fname;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLname() {
        return mLname;
    }

    public void setLname(String lname) {
        mLname = lname;
    }

    public String getMobile() {
        return mMobile;
    }

    public void setMobile(String mobile) {
        mMobile = mobile;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getRdate() {
        return mRdate;
    }

    public void setRdate(String rdate) {
        mRdate = rdate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}


