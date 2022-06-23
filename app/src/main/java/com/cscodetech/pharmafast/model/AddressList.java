
package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class AddressList {

    @SerializedName("address")
    private String mAddress;
    @SerializedName("delivery_charge")
    private String mDeliveryCharge;
    @SerializedName("hno")
    private String mHno;
    @SerializedName("IS_UPDATE_NEED")
    private Boolean mISUPDATENEED;
    @SerializedName("id")
    private String mId;
    @SerializedName("landmark")
    private String mLandmark;
    @SerializedName("lat_map")
    private double mLatMap;
    @SerializedName("long_map")
    private double mLongMap;

    @SerializedName("type")
    private String mType;
    @SerializedName("uid")
    private String mUid;
    @SerializedName("address_image")
    private String addressImage;



    public String getAddressImage() {
        return addressImage;
    }

    public void setAddressImage(String addressImage) {
        this.addressImage = addressImage;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getDeliveryCharge() {
        return mDeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        mDeliveryCharge = deliveryCharge;
    }

    public String getHno() {
        return mHno;
    }

    public void setHno(String hno) {
        mHno = hno;
    }

    public Boolean getISUPDATENEED() {
        return mISUPDATENEED;
    }

    public void setISUPDATENEED(Boolean iSUPDATENEED) {
        mISUPDATENEED = iSUPDATENEED;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getLandmark() {
        return mLandmark;
    }

    public void setLandmark(String landmark) {
        mLandmark = landmark;
    }

    public double getLatMap() {
        return mLatMap;
    }

    public void setLatMap(double latMap) {
        mLatMap = latMap;
    }

    public double getLongMap() {
        return mLongMap;
    }

    public void setLongMap(double longMap) {
        mLongMap = longMap;
    }



    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

}
