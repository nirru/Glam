package com.glam.adapter;

/**
 * Created by nikk on 12/2/17.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.glam.custom.CustomCollection;
import com.shopify.buy.model.Product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sonal on 7/2/2015.
 */
public class LotcheFragmentPagerAdapter extends FragmentStatePagerAdapter {

    public static int pos = 0;

    private List<Fragment> myFragments;
    private Context context;
    int oldPosition = -1;
    private ArrayList<CustomCollection> customCollectionArrayList;
    public LotcheFragmentPagerAdapter(Context context, FragmentManager fm, List<Fragment> myFrags,ArrayList<CustomCollection>customCollectionArrayList) {
        super(fm);
        this.myFragments = myFrags;
        this.context = context;
        this.customCollectionArrayList = customCollectionArrayList;
    }

    public void updateData(){
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {

        return myFragments.get(position);

    }

    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    @Override
    public int getCount() {

        return myFragments.size();
    }



    @Override
    public CharSequence getPageTitle(int position) {

        setPos(position);

        String PageTitle = "";


        return customCollectionArrayList.get(position).getTitle();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }




    public static int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        LotcheFragmentPagerAdapter.pos = pos;
    }


}

