package com.glam.custom;

import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.glam.AppPrefs;
import com.glam.ApplicationController;
import com.glam.R;
import com.shopify.buy.model.Product;

import java.util.ArrayList;

/**
 * Created by nikk on 9/2/17.
 */
public class WishListManager {
    private static WishListManager ourInstance = new WishListManager();

    private static ArrayList<Product>wishlist;

    public static WishListManager getInstance() {
//        wishlist = new ArrayList<>();
        AppPrefs appPrefs = ApplicationController.getInstance().getAppPrefs();
        if (appPrefs.getObject("A")!=null){
            wishlist =  appPrefs.getObject("A");
        }else{
            wishlist = new ArrayList<>();
        }
        return ourInstance;
    }

    private WishListManager() {
    }

    public  ArrayList<Product> getWishlist() {
        return wishlist;
    }

    public void findProducts(){

    }

    public void addProducts(Product product){
        wishlist.add(product);
    }

    public void removeProduct(long productId){
        int index = 0;
        for (int i= 0; i<wishlist.size(); i++){
            if (wishlist.get(i).getProductId()==productId){
                index = i;
                break;
            }
        }
        if (index != -1)
            wishlist.remove(index);
    }

    public boolean isFavourite(long productId){
        int index = 0;
        for (int i= 0; i<wishlist.size(); i++){
            if (wishlist.get(i).getProductId()==productId){
                index = i;
                break;
            }
        }
        if (index != -1)
            return true;
        return false;
    }

    public boolean isFavourite(Product product){
       if (wishlist.contains(product))
           return true;
        else return false;
    }

    public void toogleFavourite(Product pd , ImageView v){
        WishListManager wishListManager = WishListManager.getInstance();

        if (wishListManager.isFavourite(pd)){
            wishListManager.removeProduct(pd.getProductId());
            if (v instanceof ImageView)
                ((ImageView) v).setImageResource(R.drawable.heart_blank);
        }else{
            wishListManager.addProducts(pd);
            if (v instanceof ImageView)
                ((ImageView) v).setImageResource(R.drawable.heart_fill);
        }

        ApplicationController.getInstance().getAppPrefs().putObject("A",wishListManager.getWishlist());
        ApplicationController.getInstance().getAppPrefs().commit();

    }

    public void toogleFavourite(Product pd , MenuItem v){
        WishListManager wishListManager = WishListManager.getInstance();

        if (wishListManager.isFavourite(pd)){
            wishListManager.removeProduct(pd.getProductId());
            if (v instanceof MenuItem)
                ((MenuItem) v).setIcon(R.drawable.ic_heart_outline);
        }else{
            wishListManager.addProducts(pd);
            if (v instanceof MenuItem)
                ((MenuItem) v).setIcon(R.drawable.ic_heart);
        }

        ApplicationController.getInstance().getAppPrefs().putObject("A",wishListManager.getWishlist());
        ApplicationController.getInstance().getAppPrefs().commit();

    }
}
