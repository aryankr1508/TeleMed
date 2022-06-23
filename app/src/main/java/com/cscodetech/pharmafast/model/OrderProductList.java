
package com.cscodetech.pharmafast.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrderProductList {

    @SerializedName("Additional_Note")
    private String mAdditionalNote;
    @SerializedName("Coupon_Amount")
    private String mCouponAmount;
    @SerializedName("customer_address")
    private String mCustomerAddress;
    @SerializedName("Delivery_charge")
    private String mDeliveryCharge;
    @SerializedName("order_date")
    private String mOrderDate;
    @SerializedName("Order_Product_Data")
    private List<OrderProductDatum> mOrderProductData;
    @SerializedName("Order_Status")
    private String mOrderStatus;
    @SerializedName("Order_SubTotal")
    private String mOrderSubTotal;
    @SerializedName("Order_Total")
    private String mOrderTotal;
    @SerializedName("Order_Transaction_id")
    private String mOrderTransactionId;
    @SerializedName("p_method_name")
    private String mPMethodName;

    public String getAdditionalNote() {
        return mAdditionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        mAdditionalNote = additionalNote;
    }

    public String getCouponAmount() {
        return mCouponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        mCouponAmount = couponAmount;
    }

    public String getCustomerAddress() {
        return mCustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        mCustomerAddress = customerAddress;
    }

    public String getDeliveryCharge() {
        return mDeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        mDeliveryCharge = deliveryCharge;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        mOrderDate = orderDate;
    }

    public List<OrderProductDatum> getOrderProductData() {
        return mOrderProductData;
    }

    public void setOrderProductData(List<OrderProductDatum> orderProductData) {
        mOrderProductData = orderProductData;
    }

    public String getOrderStatus() {
        return mOrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        mOrderStatus = orderStatus;
    }

    public String getOrderSubTotal() {
        return mOrderSubTotal;
    }

    public void setOrderSubTotal(String orderSubTotal) {
        mOrderSubTotal = orderSubTotal;
    }

    public String getOrderTotal() {
        return mOrderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        mOrderTotal = orderTotal;
    }

    public String getOrderTransactionId() {
        return mOrderTransactionId;
    }

    public void setOrderTransactionId(String orderTransactionId) {
        mOrderTransactionId = orderTransactionId;
    }

    public String getPMethodName() {
        return mPMethodName;
    }

    public void setPMethodName(String pMethodName) {
        mPMethodName = pMethodName;
    }

}
