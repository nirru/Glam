package com.lotche.custom;

import com.lotche.AppPrefs;
import com.lotche.ApplicationController;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.Product;

import java.util.ArrayList;

/**
 * Created by nikk on 9/2/17.
 */
public class CartManager {
    private static CartManager ourInstance = new CartManager();
    private static Cart cart;
    private static ArrayList<Product> products;

    public static CartManager getInstance() {
        AppPrefs appPrefs = ApplicationController.getInstance().getAppPrefs();
        if (appPrefs.getCartObjects("C")!=null){
            cart =  appPrefs.getCartObjects("C");
        }else{
            cart = new Cart();
        }
        if (appPrefs.getObject("D")!=null){
            products =  appPrefs.getObject("D");
        }else{
            products = new ArrayList();
        }
        return ourInstance;
    }

    private CartManager() {
    }

    public  Cart getCart() {
        return cart;
    }

    public ArrayList<Product> getProducts(){
        return products;
    }

    public Product findProduct(long productId){
        int index = 0;
        for (int i= 0; i<products.size(); i++){
            if (products.get(i).getProductId()==productId){
                index = i;
                break;
            }
        }
        if (index != -1)
            return  products.get(index);
        else return null;
    }



}
