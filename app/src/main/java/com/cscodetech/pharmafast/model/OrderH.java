
package com.cscodetech.pharmafast.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrderH {

    @SerializedName("OrderHistory")
    private List<OrderHistory> mOrderHistory;
    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;

    public List<OrderHistory> getOrderHistory() {
        return mOrderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        mOrderHistory = orderHistory;
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
