package com.glam.custom;

import com.glam.ApplicationController;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Collection;
import com.shopify.buy.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by nikk on 12/2/17.
 */
public class CollectionManager {
    private static CollectionManager ourInstance = new CollectionManager();

    public CollectionManager() {

    }

    public static CollectionManager getInstance() {
        return ourInstance;
    }

    OnSucess onSucess;

    public CollectionManager(OnSucess onSucess) {
        this.onSucess = onSucess;
    }

    public void getCollection(){
        ApplicationController.getInstance().getCollections(new Callback<List<Collection>>() {
            @Override
            public void success(List<Collection> collections) {
                  ArrayList<CustomCollection> collectionList = new ArrayList<CustomCollection>();
                  HashMap<Long, ArrayList<Product>> hashMap= new HashMap();
                  CustomCollection parent = new CustomCollection();
                for (int i =0;i<collections.size();i++){
                    CustomCollection cus = new CustomCollection();
                    ArrayList<Product>lp = new ArrayList<>();
                    hashMap.put(collections.get(i).getCollectionId(),lp);
                    cus.setCollectioId(collections.get(i).getCollectionId());
                    cus.setTitle(collections.get(i).getTitle());
                    collectionList.add(cus);
                }
                parent.setLotcheProductsHashMap(hashMap);
                parent.setCollectionList(collectionList);
                onSucess.OnResutFetch(collectionList,parent);
            }

            @Override
            public void failure(BuyClientError error) {
                error.printStackTrace();
            }
        });
    }
   public interface OnSucess {
       public void OnResutFetch(ArrayList<CustomCollection> collectionList,CustomCollection parent);
   }
}


