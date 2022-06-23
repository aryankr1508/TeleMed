
package com.cscodetech.pharmafast.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PrescriptionOrderProductList implements Parcelable {

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





    @SerializedName("Cart_is_set")
    private String CartIsSet;



    @SerializedName("Prescription_image_list")
    private List<String> mPrescriptionImageList;

    @SerializedName("Order_Product_Data")
    private ArrayList<OrderProductDatum> mOrderProductData;


    protected PrescriptionOrderProductList(Parcel in) {
        mAdditionalNote = in.readString();
        mCouponAmount = in.readString();
        mCustomerAddress = in.readString();
        mDeliveryCharge = in.readString();
        mOrderDate = in.readString();
        mOrderStatus = in.readString();
        mOrderSubTotal = in.readString();
        mOrderTotal = in.readString();
        mOrderTransactionId = in.readString();
        mPMethodName = in.readString();
        CartIsSet = in.readString();
        mPrescriptionImageList = in.createStringArrayList();
        mOrderProductData = in.createTypedArrayList(OrderProductDatum.CREATOR);
    }

    public static final Creator<PrescriptionOrderProductList> CREATOR = new Creator<PrescriptionOrderProductList>() {
        @Override
        public PrescriptionOrderProductList createFromParcel(Parcel in) {
            return new PrescriptionOrderProductList(in);
        }

        @Override
        public PrescriptionOrderProductList[] newArray(int size) {
            return new PrescriptionOrderProductList[size];
        }
    };

    public String getCustomerAddress() {
        return mCustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        mCustomerAddress = customerAddress;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        mOrderDate = orderDate;
    }

    public String getOrderStatus() {
        return mOrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        mOrderStatus = orderStatus;
    }

    public String getCartIsSet() {
        return CartIsSet;
    }

    public void setCartIsSet(String cartIsSet) {
        CartIsSet = cartIsSet;
    }

    public List<String> getPrescriptionImageList() {
        return mPrescriptionImageList;
    }

    public void setPrescriptionImageList(List<String> prescriptionImageList) {
        mPrescriptionImageList = prescriptionImageList;
    }

    public ArrayList<OrderProductDatum> getmOrderProductData() {
        return mOrderProductData;
    }

    public void setmOrderProductData(ArrayList<OrderProductDatum> mOrderProductData) {
        this.mOrderProductData = mOrderProductData;
    }

    public String getmCouponAmount() {
        return mCouponAmount;
    }

    public void setmCouponAmount(String mCouponAmount) {
        this.mCouponAmount = mCouponAmount;
    }

    public String getmDeliveryCharge() {
        return mDeliveryCharge;
    }

    public void setmDeliveryCharge(String mDeliveryCharge) {
        this.mDeliveryCharge = mDeliveryCharge;
    }

    public String getmOrderSubTotal() {
        return mOrderSubTotal;
    }

    public void setmOrderSubTotal(String mOrderSubTotal) {
        this.mOrderSubTotal = mOrderSubTotal;
    }

    public String getmOrderTotal() {
        return mOrderTotal;
    }

    public void setmOrderTotal(String mOrderTotal) {
        this.mOrderTotal = mOrderTotal;
    }

    public String getmOrderTransactionId() {
        return mOrderTransactionId;
    }

    public void setmOrderTransactionId(String mOrderTransactionId) {
        this.mOrderTransactionId = mOrderTransactionId;
    }

    public String getmPMethodName() {
        return mPMethodName;
    }

    public void setmPMethodName(String mPMethodName) {
        this.mPMethodName = mPMethodName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAdditionalNote);
        parcel.writeString(mCouponAmount);
        parcel.writeString(mCustomerAddress);
        parcel.writeString(mDeliveryCharge);
        parcel.writeString(mOrderDate);
        parcel.writeString(mOrderStatus);
        parcel.writeString(mOrderSubTotal);
        parcel.writeString(mOrderTotal);
        parcel.writeString(mOrderTransactionId);
        parcel.writeString(mPMethodName);
        parcel.writeString(CartIsSet);
        parcel.writeStringList(mPrescriptionImageList);
        parcel.writeTypedList(mOrderProductData);
    }
}
