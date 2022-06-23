package com.cscodetech.pharmafast.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductPrice implements Parcelable {
    @SerializedName("product_type")
    @Expose
    private String productType;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("Product_Out_Stock")
    @Expose
    private String productInStock;
    @SerializedName("product_discount")
    @Expose
    private double productDiscount;


    @SerializedName("attribute_id")
    @Expose
    private String attributeId;


    protected ProductPrice(Parcel in) {
        productType = in.readString();
        productPrice = in.readString();
        productInStock = in.readString();
        attributeId = in.readString();

        productDiscount = in.readDouble();
    }



    public static final Creator<ProductPrice> CREATOR = new Creator<ProductPrice>() {
        @Override
        public ProductPrice createFromParcel(Parcel in) {
            return new ProductPrice(in);
        }

        @Override
        public ProductPrice[] newArray(int size) {
            return new ProductPrice[size];
        }
    };

    public String getProductInStock() {
        return productInStock;
    }

    public void setProductInStock(String productInStock) {
        this.productInStock = productInStock;
    }

    public double getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(productType);
        dest.writeString(productPrice);
        dest.writeString(productInStock);
        dest.writeString(attributeId);
        dest.writeDouble(productDiscount);
    }


}
