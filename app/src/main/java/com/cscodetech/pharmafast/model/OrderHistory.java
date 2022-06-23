
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrderHistory {

    @SerializedName("id")
    private String mId;
    @SerializedName("order_date")
    private String mOrderDate;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("total")
    private String mTotal;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        mOrderDate = orderDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTotal() {
        return mTotal;
    }

    public void setTotal(String total) {
        mTotal = total;
    }

}
