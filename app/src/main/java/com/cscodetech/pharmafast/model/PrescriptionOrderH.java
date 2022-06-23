
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class PrescriptionOrderH {

    @SerializedName("PrescriptionOrderProductList")
    private PrescriptionOrderProductList mPrescriptionOrderProductList;
    @SerializedName("ResponseCode")
    private String mResponseCode;
    @SerializedName("ResponseMsg")
    private String mResponseMsg;
    @SerializedName("Result")
    private String mResult;

    public PrescriptionOrderProductList getPrescriptionOrderProductList() {
        return mPrescriptionOrderProductList;
    }

    public void setPrescriptionOrderProductList(PrescriptionOrderProductList prescriptionOrderProductList) {
        mPrescriptionOrderProductList = prescriptionOrderProductList;
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
