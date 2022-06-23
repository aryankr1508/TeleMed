
package com.cscodetech.pharmafast.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class OrderProductDatum implements Parcelable {

    @SerializedName("Product_discount")
    private String mProductDiscount;
    @SerializedName("Product_image")
    private String mProductImage;
    @SerializedName("Product_name")
    private String mProductName;
    @SerializedName("Product_price")
    private String mProductPrice;
    @SerializedName("Product_quantity")
    private String mProductQuantity;
    @SerializedName("Product_total")
    private String mProductTotal;
    @SerializedName("Product_variation")
    private String mProductVariation;

    protected OrderProductDatum(Parcel in) {
        mProductDiscount = in.readString();
        mProductImage = in.readString();
        mProductName = in.readString();
        mProductPrice = in.readString();
        mProductQuantity = in.readString();
        mProductTotal = in.readString();
        mProductVariation = in.readString();
    }

    public static final Creator<OrderProductDatum> CREATOR = new Creator<OrderProductDatum>() {
        @Override
        public OrderProductDatum createFromParcel(Parcel in) {
            return new OrderProductDatum(in);
        }

        @Override
        public OrderProductDatum[] newArray(int size) {
            return new OrderProductDatum[size];
        }
    };

    public String getProductDiscount() {
        return mProductDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        mProductDiscount = productDiscount;
    }

    public String getProductImage() {
        return mProductImage;
    }

    public void setProductImage(String productImage) {
        mProductImage = productImage;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public String getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(String productPrice) {
        mProductPrice = productPrice;
    }

    public String getProductQuantity() {
        return mProductQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        mProductQuantity = productQuantity;
    }

    public String getProductTotal() {
        return mProductTotal;
    }

    public void setProductTotal(String productTotal) {
        mProductTotal = productTotal;
    }

    public String getProductVariation() {
        return mProductVariation;
    }

    public void setProductVariation(String productVariation) {
        mProductVariation = productVariation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mProductDiscount);
        parcel.writeString(mProductImage);
        parcel.writeString(mProductName);
        parcel.writeString(mProductPrice);
        parcel.writeString(mProductQuantity);
        parcel.writeString(mProductTotal);
        parcel.writeString(mProductVariation);
    }
}
