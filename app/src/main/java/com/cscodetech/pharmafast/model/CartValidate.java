package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartValidate {
    @SerializedName("ResponseCode")
    @Expose
    private String responseCode;
    @SerializedName("Result")
    @Expose
    private String result;
    @SerializedName("ResponseMsg")
    @Expose
    private String responseMsg;
    @SerializedName("CartValidateData")
    @Expose
    private List<Validate> cartValidateData = null;
    @SerializedName("AddressList")
    @Expose
    private List<AddressList> addressList = null;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public List<Validate> getCartValidateData() {
        return cartValidateData;
    }

    public void setCartValidateData(List<Validate> cartValidateData) {
        this.cartValidateData = cartValidateData;
    }

    public List<AddressList> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressList> addressList) {
        this.addressList = addressList;
    }

}
