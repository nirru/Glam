package com.glam.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.glam.AppPrefs;
import com.glam.ApplicationController;
import com.glam.MainActivity;
import com.glam.ProductDetail;
import com.glam.R;
import com.glam.adapter.LotcheFragmentPagerAdapter;
import com.glam.adapter.ProductListAdapter;
import com.glam.custom.CartManager;
import com.glam.custom.CollectionManager;
import com.glam.custom.CustomCollection;
import com.glam.custom.CustomFragment;
import com.glam.custom.StggeredEndlessRecyclerOnScrollListener;
import com.glam.custom.WishListManager;
import com.glam.model.Data;
import com.glam.test.CategoryFragment;
import com.glam.ui.productdetail.ProductDetailsTheme;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;


/**
 * The Class MainFragment is the base fragment that shows the list of various
 * products. You can add your code to do whatever you want related to products
 * for your app.
 */
public class MainFragment extends CustomFragment
{

	/** The product list. */
	private ProductDetailsTheme theme;
	private ArrayList<Data> iList;
	private ArrayList<CustomCollection> customCollectionArrayList;
//	private RecyclerView recList;
	private View mProgressView;
	ViewPager pager;

	LotcheFragmentPagerAdapter lotcheFragmentPagerAdapter;


	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private View mProgressView1;

	private MainFragment.OnFragmentInteractionListener mListener;

	public MainFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment FL.
	 */
	// TODO: Rename and change types and number of parameters
	public static MainFragment newInstance(ArrayList<CustomCollection> param1, String param2) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putParcelableArrayList(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			customCollectionArrayList = getArguments().getParcelableArrayList(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.main_container, null);
		((MainActivity) getActivity()).toolbar.findViewById(
				R.id.spinner_toolbar).setVisibility(View.VISIBLE);

		setHasOptionsMenu(true);
		setupView(v);
		return v;
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
		if (context instanceof MainFragment.OnFragmentInteractionListener) {
			mListener = (MainFragment.OnFragmentInteractionListener) context;
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

	/* (non-Javadoc)
	 * @see com.whatshere.custom.CustomFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		super.onClick(v);
	}

	/**
	 * Setup the view components for this fragment. You write your code for
	 * initializing the views, setting the adapters, touch and click listeners
	 * etc.
	 *
	 * @param v
	 *            the base view of fragment
	 */
	private void setupView(View v)
	{
		theme = new ProductDetailsTheme(getResources());
		mProgressView1  =v.findViewById(R.id.login_progress);

		initPager(v);
	}

	/**
	 * Inits the pager view.
	 *
	 * @param v
	 *            the root view
	 */
	private void initPager(View v)
	{
		pager = (ViewPager) v.findViewById(R.id.pager);
//		pager.setPageMargin(10);
//
//		pager.setAdapter(new PageAdapter());



		final List<Fragment> fragments = new Vector<Fragment>();
		Bundle page = new Bundle();
		page.putString("url", "d");
		showProgress(true);
		new CollectionManager(new CollectionManager.OnSucess() {
			@Override
			public void OnResutFetch(ArrayList<CustomCollection> collectionList, CustomCollection parent) {
				   showProgress(false);
				   parent.setPager(pager);
				   for (int i = 0;i<collectionList.size();i++){
				        fragments.add(i, CategoryFragment.newInstance(collectionList.get(i).getCollectioId(),parent));
				   }
				   lotcheFragmentPagerAdapter  = new LotcheFragmentPagerAdapter(getActivity(),((MainFragment.this)).getChildFragmentManager(), fragments,collectionList);
			       pager.setAdapter(lotcheFragmentPagerAdapter);
			}
		}).getCollection();

	}


	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public  void showProgress(final boolean show,final RecyclerView recList) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		try{
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
				int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

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
				int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

				pager.setVisibility(show ? View.GONE : View.VISIBLE);
				pager.animate().setDuration(shortAnimTime).alpha(
						show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						pager.setVisibility(show ? View.GONE : View.VISIBLE);
					}
				});

				mProgressView1.setVisibility(show ? View.VISIBLE : View.GONE);
				mProgressView1.animate().setDuration(shortAnimTime).alpha(
						show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						mProgressView1.setVisibility(show ? View.VISIBLE : View.GONE);
					}
				});
			} else {
				// The ViewPropertyAnimator APIs are not available, so simply show
				// and hide the relevant UI components.
				pager.setVisibility(show ? View.VISIBLE : View.GONE);
				pager.setVisibility(show ? View.GONE : View.VISIBLE);
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}

	}


	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.search_exp, menu);
		super.onCreateOptionsMenu(menu, inflater);

	}
}
