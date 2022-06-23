package com.cscodetech.pharmafast.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryProduct {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_img")
    @Expose
    private String categoryImg;
    @SerializedName("productlist")
    @Expose
    private List<Medicine> productlist = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }

    public List<Medicine> getProductlist() {
        return productlist;
    }

    public void setProductlist(List<Medicine> productlist) {
        this.productlist = productlist;
    }

}
