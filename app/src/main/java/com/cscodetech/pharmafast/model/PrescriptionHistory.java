
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PrescriptionHistory {

    @SerializedName("id")
    private String mId;
    @SerializedName("order_date")
    private Object mOrderDate;
    @SerializedName("status")
    private String mStatus;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public Object getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(Object orderDate) {
        mOrderDate = orderDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
