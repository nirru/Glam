package com.glam.custom;

import android.os.Parcel;
import android.os.Parcelable;

import com.shopify.buy.model.Product;

/**
 * Created by nikk on 12/2/17.
 */

public class LotcheProducts extends Product implements Parcelable{
    public Product product;


    protected LotcheProducts(Parcel in) {
        product = (Product) in.readValue(getClass().getClassLoader());
    }

    public static final Creator<LotcheProducts> CREATOR = new Creator<LotcheProducts>() {
        @Override
        public LotcheProducts createFromParcel(Parcel in) {
            return new LotcheProducts(in);
        }

        @Override
        public LotcheProducts[] newArray(int size) {
            return new LotcheProducts[size];
        }
    };

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(product);
    }
}
