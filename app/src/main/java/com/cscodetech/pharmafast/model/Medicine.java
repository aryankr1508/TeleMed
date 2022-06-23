
package com.cscodetech.pharmafast.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Medicine implements Parcelable {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private ArrayList<String> productImage = null;
    @SerializedName("Brand_name")
    @Expose
    private String brandName;
    @SerializedName("short_desc")
    @Expose
    private String shortDesc;

    @SerializedName("product_info")
    @Expose
    private ArrayList<ProductPrice> productInfo = null;

    protected Medicine(Parcel in) {
        id = in.readString();
        productName = in.readString();
        productImage = in.createStringArrayList();
        brandName = in.readString();
        shortDesc = in.readString();

    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ArrayList<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(ArrayList<String> productImage) {
        this.productImage = productImage;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }




    public ArrayList<ProductPrice> getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ArrayList<ProductPrice> productInfo) {
        this.productInfo = productInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(productName);
        dest.writeStringList(productImage);
        dest.writeString(brandName);
        dest.writeString(shortDesc);

    }
}
