
package com.cscodetech.pharmafast.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Noti {

    @SerializedName("NotificationData")
    private List<NotificationDatum> mNotificationData;
    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;

    public List<NotificationDatum> getNotificationData() {
        return mNotificationData;
    }

    public void setNotificationData(List<NotificationDatum> notificationData) {
        mNotificationData = notificationData;
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

}
