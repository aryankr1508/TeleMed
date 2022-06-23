
package com.cscodetech.pharmafast.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class HomeData {

    @SerializedName("Banner")
    private List<Banner> mBanner;
    @SerializedName("Brand")
    private List<Brand> mBrand;
    @SerializedName("Catlist")
    private List<Catlist> mCatlist;
    @SerializedName("Main_Data")
    private MainData mMainData;
    @SerializedName("Medicine")
    private List<Medicine> mMedicine;
    @SerializedName("testimonial")
    private List<Testimonial> mTestimonial;

    public List<Banner> getBanner() {
        return mBanner;
    }

    public void setBanner(List<Banner> banner) {
        mBanner = banner;
    }

    public List<Brand> getBrand() {
        return mBrand;
    }

    public void setBrand(List<Brand> brand) {
        mBrand = brand;
    }

    public List<Catlist> getCatlist() {
        return mCatlist;
    }

    public void setCatlist(List<Catlist> catlist) {
        mCatlist = catlist;
    }

    public MainData getMainData() {
        return mMainData;
    }

    public void setMainData(MainData mainData) {
        mMainData = mainData;
    }

    public List<Medicine> getMedicine() {
        return mMedicine;
    }

    public void setMedicine(List<Medicine> medicine) {
        mMedicine = medicine;
    }

    public List<Testimonial> getTestimonial() {
        return mTestimonial;
    }

    public void setTestimonial(List<Testimonial> testimonial) {
        mTestimonial = testimonial;
    }

}
