package com.glam.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.glam.ApplicationController;
import com.glam.MainActivity;
import com.glam.R;
import com.glam.adapter.ProductListAdapter;
import com.glam.custom.CollectionManager;
import com.glam.custom.CustomCollection;
import com.glam.custom.LotcheProducts;
import com.glam.custom.StggeredEndlessRecyclerOnScrollListener;
import com.glam.custom.WishListManager;
import com.glam.ui.productdetail.ProductDetailsBuilder;
import com.glam.ui.productdetail.ProductDetailsTheme;
import com.glam.utils.ImageUtility;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Product;
import com.shopify.buy.utils.DeviceUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private Long collectionID;
    private CustomCollection customCollection;

    private OnFragmentInteractionListener mListener;
    private View mProgressView;
    private RecyclerView recList;
    private ProductDetailsTheme theme;
    int pos;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(Long param1, CustomCollection param2) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, param1);
        args.putParcelable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            collectionID = getArguments().getLong(ARG_PARAM1);
            customCollection = getArguments().getParcelable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pager_card_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUIView(view);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    
    private void initUIView(View v){
        theme = new ProductDetailsTheme(getResources());
        mProgressView = v.findViewById(R.id.login_progress);
        recList = (RecyclerView) v.findViewById(R.id.cardList);

        recList.setHasFixedSize(true);
        recList.setItemViewCacheSize(1024);
        recList.setDrawingCacheEnabled(true);
        recList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
       final ArrayList<Product> pdList = customCollection.getLotcheProductsHashMap().get(collectionID);
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);

        int screenWidth = metrics.widthPixels;
        int screenHeight = metrics.heightPixels;

        if (DeviceUtils.isTablet(getResources())) {
            screenHeight = getActivity().getWindow().getAttributes().height;
            screenWidth = getActivity().getWindow().getAttributes().width;
        }

        // Make sure the description area below the image is at least 40% of the screen height
        int minDetailsHeightInPx = Math.round(screenHeight * 0.4f);

        int maxHeightInPx = screenHeight - minDetailsHeightInPx ;
        final int imageAreaHeight = Math.min(screenWidth, maxHeightInPx);
       final ProductListAdapter  pa = new ProductListAdapter(pdList,getActivity(),screenWidth,imageAreaHeight);
        recList.setAdapter(pa);
        customCollection.getPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int selectedPage, float positionOffset, int positionOffsetPixels) {
                pa.setOnItemClickListener(new ProductListAdapter.MyClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        pos = position;
                        long id  = customCollection.getCollectionList().get(selectedPage).getCollectioId();
                        Product pd = customCollection.getLotcheProductsHashMap().get(id).get(position);
                        if (v.getId()==R.id.img){
//                            ApplicationController.getInstance().launchProductDetailsActivity(getActivity(), pd, theme);
                            ProductDetailsBuilder builder = new ProductDetailsBuilder(getActivity(), ApplicationController.getBuyClient());
                            Intent intent = builder.setShopDomain(ApplicationController.getBuyClient().getShopDomain())
                                    .setProduct(pd)
                                    .setTheme(theme)
                                    .setShop(ApplicationController.getShop())
                                    .build();
                            startActivityForResult(intent, 1);
                        }
                        if (v.getId() == R.id.btnAdd){
                            if (v instanceof ImageView)
                                WishListManager.getInstance().toogleFavourite(pd,((ImageView) v));
                        }
                    }
                });
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final Callback<List<Product>> callback = new Callback<List<Product>>() {
            @Override
            public void success(List<Product> products) {
//					((MainActivity)getActivity()).dismissLoadingDialog();
                  showProgress(false);

                for (Product item:products){
                    pa.addItem(item);
                }
            }

            @Override
            public void failure(BuyClientError error) {
                ((MainActivity)getActivity()).onError(error);
            }
        };

        recList.addOnScrollListener(new StggeredEndlessRecyclerOnScrollListener(llm) {
            @Override
            public void onLoadMore(int current_page) {
                try {
                    ApplicationController.getInstance().getProducts(current_page,collectionID, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (pdList.size()==0){
            showProgress(true);
            ApplicationController.getInstance().getProducts(1,collectionID, callback);
        }


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public  void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = 200;

                recList.setVisibility(show ? View.GONE : View.VISIBLE);
                recList.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        recList.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                recList.setVisibility(show ? View.VISIBLE : View.GONE);
                recList.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.e("POS=="," =" + pos);
//        int pi = pos;
//
//       View v = recList.getChildAt(0);
//       ImageView wish = (ImageView)v.findViewById(R.id.btnAdd);
//       Product product = (Product) ((ProductListAdapter)recList.getAdapter()).getItem(0);
//        if (WishListManager.getInstance().isFavourite(product)){
//            wish.setImageResource(R.drawable.heart_fill);
//        }else{
//            wish.setImageResource(R.drawable.heart_blank);
//        }
//        ((ProductListAdapter)recList.getAdapter()).notifyItemChanged(0);
    }
}
