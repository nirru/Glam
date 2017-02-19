package com.lotche.custom;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;

import com.shopify.buy.model.Product;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nikk on 31/1/17.
 */

public class CustomCollection implements Parcelable{
    private long collectioId;
    private String title;
    private Product lotcheProducts;
    private ViewPager viewPager;
    ArrayList<CustomCollection> collectionList;
    private HashMap<Long,ArrayList<Product>> lotcheProductsHashMap;

    public CustomCollection(){

    }

    protected CustomCollection(Parcel in) {
        try {
            title = in.readString();
            lotcheProducts = (Product) in.readValue(getClass().getClassLoader());
            collectioId = in.readLong();
//            viewPager = (ViewPager) in.readValue(getClass().getClassLoader());
//            lotcheProductsHashMap = in.readHashMap(getClass().getClassLoader());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static final Creator<CustomCollection> CREATOR = new Creator<CustomCollection>() {
        @Override
        public CustomCollection createFromParcel(Parcel in) {
            return new CustomCollection(in);
        }

        @Override
        public CustomCollection[] newArray(int size) {
            return new CustomCollection[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCollectioId() {
        return collectioId;
    }

    public void setCollectioId(Long collectioId) {
        this.collectioId = collectioId;
    }

    public Product getLotcheProducts() {
        return lotcheProducts;
    }

    public void setLotcheProducts(LotcheProducts lotcheProducts) {
        this.lotcheProducts = lotcheProducts;
    }

    public HashMap<Long, ArrayList<Product>> getLotcheProductsHashMap() {
        return lotcheProductsHashMap;
    }

    public void setLotcheProductsHashMap(HashMap<Long, ArrayList<Product>> lotcheProductsHashMap) {
        this.lotcheProductsHashMap = lotcheProductsHashMap;
    }

    public ViewPager getPager(){
        return viewPager;
    }

    public void setPager(ViewPager pager){
        this.viewPager = pager;
    }

    public ArrayList<CustomCollection> getCollectionList() {
        return collectionList;
    }

    public void setCollectionList(ArrayList<CustomCollection> collectionList) {
        this.collectionList = collectionList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        try {
            dest.writeString(title);
            dest.writeValue(lotcheProducts);
            dest.writeLong(collectioId);
//            dest.writeValue(viewPager);
//            dest.writeMap(lotcheProductsHashMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
