package com.lotche.ui;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lotche.ApplicationController;
import com.lotche.CheckoutActivity;
import com.lotche.MainActivity;
import com.lotche.R;
import com.lotche.adapter.CartListAdapter;
import com.lotche.custom.CartManager;
import com.lotche.custom.CustomFragment;
import com.lotche.custom.WishListManager;
import com.lotche.model.Data;
import com.lotche.ui.productdetail.ProductDetailsConstants;
import com.lotche.ui.productdetail.ProductDetailsListener;
import com.lotche.ui.productdetail.ProductDetailsTheme;
import com.lotche.ui.productdetail.ProductDetailsVariantOptionView;
import com.lotche.ui.productdetail.VariantSelectionController;
import com.lotche.utils.CurrencyConverter;
import com.shopify.buy.customTabs.CustomTabActivityHelper;
import com.shopify.buy.dataprovider.BuyClientError;
import com.shopify.buy.dataprovider.Callback;
import com.shopify.buy.model.Cart;
import com.shopify.buy.model.LineItem;
import com.shopify.buy.model.OptionValue;
import com.shopify.buy.model.Product;
import com.shopify.buy.model.ProductVariant;
import com.shopify.buy.utils.CurrencyFormatter;
import com.shopify.buy.utils.DeviceUtils;


/**
 * The Class Checkout is the fragment that shows the list products for checkout
 * and show the credit card details as well. You need to load and display actual
 * contents.
 */
public class Checkout extends CustomFragment
{

	/** The product list. */
	private ArrayList<Data> iList;
	private TextView price_total;
	private TextView currecny;
	private ProductDetailsTheme theme;
	private LinearLayout linearLayout;
	private Button btnDone;
	private LinearLayout product_details_container;
	private TextView product_title,product_price,product_compare_at_price,product_description;
	private RecyclerView recList;
	TextView cart_Count_Text;
	int lastVisiblePosition = 0;

	private ProgressDialog progressDialog;
	private ProductDetailsListener productDetailsListener;
	private final AtomicBoolean cancelledCheckout = new AtomicBoolean(false);
	private View myView,divider1,divider2;

	ProductVariant selectedVariant;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@SuppressLint({ "InflateParams", "InlinedApi" })
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		final View v = inflater.inflate(R.layout.my_checkout, null);
		myView = v;

		if (getActivity() instanceof MainActivity)
		{
			((MainActivity) getActivity()).toolbar.setTitle("Checkout");
			((MainActivity) getActivity()).toolbar.findViewById(
					R.id.spinner_toolbar).setVisibility(View.GONE);
		}
		else
			((CheckoutActivity) getActivity()).getSupportActionBar().setTitle(
					"Checkout");

		/*if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
		{
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}*/

		setTouchNClick(v.findViewById(R.id.btnDone));
		setHasOptionsMenu(true);
		setupView(v);
		initializeProgressDialog();
		configureCheckoutButton();
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
		divider1 = (View)v.findViewById(R.id.divider1);
		divider2 = (View)v.findViewById(R.id.divider2);
		product_details_container = (LinearLayout) v.findViewById(R.id.product_details_container);
		product_title = (TextView) v.findViewById(R.id.product_title);
		product_price = (TextView) v.findViewById(R.id.product_price);
		product_compare_at_price = (TextView)v.findViewById(R.id.product_compare_at_price);
		product_description  = (TextView)v.findViewById(R.id.product_description) ;
		btnDone = (Button)v.findViewById(R.id.btnDone);
		linearLayout = (LinearLayout)v.findViewById(R.id.price_layout);
		currecny = (TextView)v.findViewById(R.id.currency);
		price_total = (TextView)v.findViewById(R.id.price_total);
		recList = (RecyclerView) v.findViewById(R.id.cardList);
		recList.setHasFixedSize(true);
		final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		recList.setLayoutManager(llm);
		double salesPrice = CartManager.getInstance().getCart().getSubtotal() * (CurrencyConverter.globalCurrencyChangeRate);
        currecny.setText(""+ CurrencyConverter.globalCurrency);
		price_total.setText(new Double(salesPrice).intValue()+"");

		final ArrayList<ProductVariant> pd = new ArrayList<>();

		for (int i =0; i<CartManager.getInstance().getCart().getSize();i++){
			LineItem lineItem = CartManager.getInstance().getCart().getLineItems().get(i);
			ProductVariant productVariant = CartManager.getInstance().getCart().getProductVariant(lineItem);
			pd.add(productVariant);
		}
		final ArrayList<Product> pdLIst1 = ApplicationController.getInstance().getAppPrefs().getObject("D");

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

		final CartListAdapter cardAdapter = new CartListAdapter(pd,pdLIst1,getActivity(),screenWidth,imageAreaHeight);
		recList.setAdapter(cardAdapter);
		cardAdapter.setOnItemClickListener(new CartListAdapter.MyListener() {
			@Override
			public void onItemClick(int position, View v) {
				long productID = pd.get(position).getProductId();
				Product product = pdLIst1.get(position);
				if (v.getId() == R.id.img){
					if (product!=null)
					ApplicationController.getInstance().launchProductDetailsActivity(getActivity(), product, theme);

				}
				if (v.getId() == R.id.btnAdd){
					if (v instanceof ImageView)
						WishListManager.getInstance().toogleFavourite(product,((ImageView) v));
				}
				if (v.getId() == R.id.delete_cart){
					Cart cart  = CartManager.getInstance().getCart();
					LineItem lineItem = cart.getLineItems().get(position);
					ProductVariant productVariant = cart.getProductVariant(lineItem);
					cart.decrementVariant(productVariant);

					cardAdapter.removeItem(position);
					cardAdapter.removeItem1(position);
					updateHotCount(cart.getSize(),cart_Count_Text);
					double salesPrice =cart.getSubtotal() * (CurrencyConverter.globalCurrencyChangeRate);
					price_total.setText(new Double(salesPrice).intValue()+"");
					ArrayList<Product> pdLIst = ApplicationController.getInstance().getAppPrefs().getObject("D");
					pdLIst.remove(position);
					ApplicationController.getInstance().getAppPrefs().putObject("C",cart);
					ApplicationController.getInstance().getAppPrefs().putObject("D",pdLIst);
					ApplicationController.getInstance().getAppPrefs().commit();

				}

			}
		});
	}


	private void configureCheckoutButton() {
		btnDone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				btnDone.setEnabled(false);
				createWebCheckout();

				showProgressDialog(getString(R.string.loading), getString(R.string.loading_checkout_page), new Runnable() {
					@Override
					public void run() {
						btnDone.setEnabled(true);
					}
				});
			}
		});
	}

	/**
	 * Creates a checkout for use with the web checkout flow
	 */
	private void createWebCheckout() {
		ApplicationController.getInstance().createCheckout(CartManager.getInstance().getCart(), new Callback<com.shopify.buy.model.Checkout>() {
			@Override
			public void success(com.shopify.buy.model.Checkout checkout) {
				dismissProgressDialog();
				launchWebCheckout(checkout);
//				Intent intent = new Intent(Intent.ACTION_VIEW);
//				intent.setData(Uri.parse(checkout.getWebUrl()));
//				intent.setPackage("com.android.chrome");
//				startActivity(intent);
			}

			@Override
			public void failure(BuyClientError error) {
				error.printStackTrace();
				System.out.println(error.getRetrofitErrorBody());
				dismissProgressDialog();
				btnDone.setEnabled(true);
				Toast.makeText(getActivity(),R.string.default_checkout_error,Toast.LENGTH_SHORT).show();
			}
		});
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu, android.view.MenuInflater)
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		inflater.inflate(R.menu.cart, menu);
		final View cartView = menu.findItem(R.id.cart_menu_icon).getActionView();
		cart_Count_Text = (TextView) cartView.findViewById(R.id.txtCount);
		updateHotCount(CartManager.getInstance().getCart().getSize(),cart_Count_Text);
		super.onCreateOptionsMenu(menu, inflater);

	}

	public void updateHotCount(int count, final TextView txtViewCount) {
		if (count < 0) return;
		final int finalCount = count;
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (finalCount == 0){
					txtViewCount.setVisibility(View.GONE);
					linearLayout.setVisibility(View.INVISIBLE);
					btnDone.setVisibility(View.INVISIBLE);
				}
				else {
					btnDone.setVisibility(View.VISIBLE);
					linearLayout.setVisibility(View.VISIBLE);
					txtViewCount.setVisibility(View.VISIBLE);
					txtViewCount.setText("" + Integer.toString(finalCount));
					// supportInvalidateOptionsMenu();
				}
			}
		});
	}

	private void initializeProgressDialog() {
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setIndeterminate(true);
		progressDialog.setCancelable(true);
	}

	private void showProgressDialog(final String title, final String message, final Runnable onCancel) {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}

				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setTitle(title);
				progressDialog.setMessage(message);
				progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						onCancel.run();
					}
				});
				progressDialog.show();
			}
		});
	}

	void dismissProgressDialog() {
		progressDialog.dismiss();
	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String sds = data.getData().toString();
		String dc = "NAME";
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			productDetailsListener = (ProductDetailsListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnProductDetailsCompletedListener");
		}
	}

	/**
	 * Launch Chrome, and open the correct url for our {@code Checkout}
	 *
	 * @param checkout
	 */
	private void launchWebCheckout(com.shopify.buy.model.Checkout checkout) {
		// if the user dismissed the progress dialog before we got here, abort
		if (cancelledCheckout.getAndSet(false)) {
			return;
		}

		dismissProgressDialog();

		String uri = checkout.getWebUrl();

		CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
				.setToolbarColor(theme.getAppBarBackgroundColor(getResources()))
				.setShowTitle(true)
				.build();
		com.lotche.utils.CustomTabActivityHelper.openCustomTab(
				(AppCompatActivity)getActivity(), customTabsIntent, Uri.parse(uri), new Checkout.BrowserFallback());
//        CustomTabActivityHelper.openCustomTab(
//                getActivity(), customTabsIntent, Uri.parse(uri), new BrowserFallback());


		// The checkout was successfully started, let the listener know.
		Bundle bundle = new Bundle();
		bundle.putString(ProductDetailsConstants.EXTRA_CHECKOUT, checkout.toJsonString());
		productDetailsListener.onSuccess(bundle);
	}

	/**
	 * Show the error message in a {@link Snackbar}
	 */
	private void onCheckoutFailure() {
		dismissProgressDialog();
		Toast.makeText(getActivity(),"" + R.string.default_checkout_error,Toast.LENGTH_SHORT).show();
        btnDone.setEnabled(true);

	}


	/**
	 * A Fallback that opens any available Browser when Custom Tabs is not available
	 */
	private class BrowserFallback implements CustomTabActivityHelper.CustomTabFallback {

		@Override
		public void openUri(Activity activity, Uri uri) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.setData(uri);

			try {
				intent.setPackage("com.android.chrome");
				startActivity(intent);

			} catch (Exception launchChromeException) {
				try {
					// Chrome could not be opened, attempt to us other launcher
					intent.setPackage(null);
					startActivity(intent);

				} catch (Exception launchOtherException) {
					onCheckoutFailure();
				}
			}

		}
	}

	/**
	 * Fills in the views with all the {@link Product} details.
	 */
	private void updateProductDetails(Product product,ProductVariant variant,int pos) {
		 final LongSparseArray<ProductDetailsVariantOptionView> visibleOptionViews = new LongSparseArray<>();

		Resources res = getResources();


		// Setup the option views for product variant selection
		for (int i = 2; i >= 0; i--) {
			ProductDetailsVariantOptionView optionView = new ProductDetailsVariantOptionView(myView, i, res, theme);

			if (i >= product.getOptions().size()) {
				optionView.hide();
			} else {
				visibleOptionViews.put(product.getOptions().get(i).getId(), optionView);
				optionView.setTheme(theme);
			}
		}
		divider1.setBackgroundColor(theme.getDividerColor(res));
		divider2.setBackgroundColor(theme.getDividerColor(res));

		product_details_container.setBackgroundColor(theme.getBackgroundColor(res));


		NumberFormat currencyFormat = CurrencyFormatter.getFormatter(Locale.getDefault(), ApplicationController.getShop().getCurrency());

		// Create a VariantSelectionController which will manage the dialogs that allow the user to pick a product variant
		VariantSelectionController variantSelectionController = new VariantSelectionController(getActivity(), myView, product, variant, theme, currencyFormat);
		variantSelectionController.setListener(onVariantSelectedListener);

		// Product title
		product_title.setText(product.getTitle());
		product_title.setTextColor(theme.getProductTitleColor(res));

		// Product price
		double salesPrice = Double.parseDouble(variant.getPrice()) * (CurrencyConverter.globalCurrencyChangeRate);
		product_price.setText(CurrencyConverter.globalCurrency + " " + new Double(salesPrice).intValue());
		product_price.setTextColor(theme.getAccentColor());

		// Product "compare at" price (appears below the actual price with a strikethrough style)
		if (!variant.isAvailable()) {
			product_compare_at_price.setVisibility(View.VISIBLE);
			product_compare_at_price.setText(getResources().getString(R.string.sold_out));
			product_compare_at_price.setTextColor(getResources().getColor(R.color.error_background));
			product_compare_at_price.setPaintFlags(0);
		} else if (!TextUtils.isEmpty(variant.getCompareAtPrice())) {
			product_compare_at_price.setVisibility(View.VISIBLE);
			double comparePrice = Double.parseDouble(variant.getCompareAtPrice()) * (CurrencyConverter.globalCurrencyChangeRate);
			product_compare_at_price.setText(CurrencyConverter.globalCurrency + " " + new Double(comparePrice).intValue());
			product_compare_at_price.setTextColor(theme.getCompareAtPriceColor(res));
			product_compare_at_price.setPaintFlags(product_compare_at_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			product_compare_at_price.setVisibility(View.GONE);
		}

		// Set the correct values on the ProductDetailsVariantOptionViews
		List<OptionValue> optionValues = variant.getOptionValues();
		for (OptionValue optionValue : optionValues) {
			ProductDetailsVariantOptionView optionView = visibleOptionViews.get(Long.valueOf(optionValue.getOptionId()));
			if (optionView != null) {
				optionView.setOptionValue(optionValue);
			}
		}

		// Product description
		product_description.setText(Html.fromHtml(product.getBodyHtml()), TextView.BufferType.SPANNABLE);
		product_description.setTextColor(theme.getProductDescriptionColor(res));

		// Make the links clickable in the description
		// http://stackoverflow.com/questions/2734270/how-do-i-make-links-in-a-textview-clickable
		product_description.setMovementMethod(LinkMovementMethod.getInstance());
	}
	private final VariantSelectionController.OnVariantSelectedListener onVariantSelectedListener = new VariantSelectionController.OnVariantSelectedListener() {
		@Override
		public void onVariantSelected(ProductVariant variant) {
			Cart cart = CartManager.getInstance().getCart();
			LineItem lineItem = cart.getLineItems().get(lastVisiblePosition);
			selectedVariant = cart.getProductVariant(lineItem);
			selectedVariant = variant;
			CartManager.getInstance().getCart().setVariantQuantity(selectedVariant,1);
			ApplicationController.getInstance().getAppPrefs().putObject("C",cart);
			ApplicationController.getInstance().getAppPrefs().commit();
		}
	};



	/**
	 * Get the Action Bar height.
	 * @return the action bar height in pixels.
	 */
	private int getActionBarHeightPixels() {
		TypedValue tv = new TypedValue();
		if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			return TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
		}
		return 0;
	}
}
