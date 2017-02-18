package com.glam.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.glam.AppPrefs;
import com.glam.ApplicationController;
import com.glam.MainActivity;
import com.glam.ProductDetail;
import com.glam.R;
import com.glam.adapter.ProductListAdapter;
import com.glam.custom.CustomFragment;
import com.glam.custom.WishListManager;
import com.glam.model.Data;
import com.glam.ui.productdetail.ProductDetailsTheme;
import com.shopify.buy.model.Product;
import com.shopify.buy.utils.DeviceUtils;

import org.w3c.dom.Text;

/**
 * The Class OnSale is the fragment that shows the products in GridView.
 */
public class OnSale extends CustomFragment
{

	/** The product list. */
	private ArrayList<Data> iList;
	private ProductDetailsTheme theme;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState)
	{
		View v = inflater.inflate(R.layout.on_sale, null);

		((MainActivity) getActivity()).toolbar.setTitle("On Sale");
		((MainActivity) getActivity()).toolbar.findViewById(
				R.id.spinner_toolbar).setVisibility(View.GONE);

		/*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
		{
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}*/

		setHasOptionsMenu(true);
		setupView(v);
		return v;
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

//		loadDummyData();
		TextView lbl1 = (TextView)v.findViewById(R.id.lbl);
		if (WishListManager.getInstance().getWishlist()!=null && WishListManager.getInstance().getWishlist().size()>0)
			lbl1.setVisibility(View.VISIBLE);
		else lbl1.setVisibility(View.INVISIBLE);

		RecyclerView recList = (RecyclerView) v.findViewById(R.id.cardList);
		recList.setHasFixedSize(true);
		StaggeredGridLayoutManager llm = new StaggeredGridLayoutManager(2,
				StaggeredGridLayoutManager.VERTICAL);
		recList.setLayoutManager(llm);
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

		int maxHeightInPx = screenHeight - minDetailsHeightInPx;
		final int imageAreaHeight = Math.min(screenWidth, maxHeightInPx);
		ProductListAdapter pa = new ProductListAdapter(WishListManager.getInstance().getWishlist(),getActivity(),screenWidth,imageAreaHeight);
		recList.setAdapter(pa);
		pa.setOnItemClickListener(new ProductListAdapter.MyClickListener() {
			@Override
			public void onItemClick(int position, View v) {
				if (v.getId() == R.id.img) {
					ApplicationController.getInstance().launchProductDetailsActivity(getActivity(), WishListManager.getInstance().getWishlist().get(position), theme);
				}
				if (v.getId() == R.id.btnAdd){
					Product pd = WishListManager.getInstance().getWishlist().get(position);
					if (v instanceof ImageView)
						WishListManager.getInstance().toogleFavourite(pd,((ImageView) v));
				}
			}
		});
	}



	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.search_exp, menu);
		menu.findItem(R.id.menu_grid).setVisible(false);
		super.onCreateOptionsMenu(menu, inflater);

	}
}
