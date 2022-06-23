
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Login {

    @SerializedName("AddressExist")
    private Boolean mAddressExist;
    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;
    @SerializedName("UserLogin")
    private User mUserLogin;

    public Boolean getAddressExist() {
        return mAddressExist;
    }

    public void setAddressExist(Boolean addressExist) {
        mAddressExist = addressExist;
    }

    public String getResponseCode() {
        return mResponseCode;
    }

    public void setResponseCode(String responseCode) {
        mResponseCode = responseCode;
    }

    public String getResponseMsg() {
        return mResponseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        mResponseMsg = responseMsg;
    }

    public String getResult() {
        return mResult;
    }

    public void setResult(String result) {
        mResult = result;
    }

    public User getUserLogin() {
        return mUserLogin;
    }

    public void setUserLogin(User userLogin) {
        mUserLogin = userLogin;
    }

}
