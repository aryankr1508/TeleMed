
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Home {

    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;
    @SerializedName("ResultData")
    private HomeData mResultData;

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

    public HomeData getResultData() {
        return mResultData;
    }

    public void setResultData(HomeData resultData) {
        mResultData = resultData;
    }

}
