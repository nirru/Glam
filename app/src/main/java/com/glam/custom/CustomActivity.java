package com.glam.custom;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.glam.R;
import com.glam.utils.TouchEffect;
import com.shopify.buy.dataprovider.BuyClientError;

/**
 * This is a common activity that all other activities of the app can extend to
 * inherit the common behaviors like setting a Theme to activity.
 */
public class CustomActivity extends AppCompatActivity implements
		OnClickListener
{

	private static final String LOG_TAG = CustomActivity.class.getSimpleName();

	private ProgressDialog progressDialog;

	/**
	 * Apply this Constant as touch listener for views to provide alpha touch
	 * effect. The view must have a Non-Transparent background.
	 */
	public static final TouchEffect TOUCH = new TouchEffect();

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(Bundle arg0)
	{
		super.onCreate(arg0);
		setupActionBar();
		initializeProgressDialog();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			getWindow()
					.addFlags(
							WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			// getWindow().setStatusBarColor(getResources().getColor(R.color.main_color_dk));
		}
	}

	/**
	 * Initializes a simple progress dialog that gets presented while the app is communicating with the server.
	 */
	private void initializeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}

		progressDialog = new ProgressDialog(this);
		progressDialog.setIndeterminate(true);
		progressDialog.setTitle(getString(R.string.please_wait));
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				CustomActivity.this.finish();
			}
		});
	}

	/**
	 * Present the progress dialog.
	 *
	 * @param messageId The identifier (R.string value) of the string to display in the dialog.
	 */
	public void showLoadingDialog(final int messageId) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.setMessage(getString(messageId));
				progressDialog.show();
			}
		});
	}

	public void dismissLoadingDialog() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				progressDialog.dismiss();
			}
		});
	}

	public void onError(BuyClientError error) {
		onError(error);
	}

	/* (non-Jav-adoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
			finish();
		return super.onOptionsItemSelected(item);
	}

	/**
	 * This method will setup the top title bar (Action bar) content and display
	 * values. It will also setup the custom background theme for ActionBar. You
	 * can override this method to change the behavior of ActionBar for
	 * particular Activity
	 */
	protected void setupActionBar()
	{
		final ActionBar actionBar = getSupportActionBar();
		if (actionBar == null)
			return;
		actionBar.setDisplayShowTitleEnabled(true);
		// actionBar.setDisplayUseLogoEnabled(true);
		// actionBar.setLogo(R.drawable.icon);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setTitle(null);

	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the touch and click listeners for a view..
	 * 
	 * @param id
	 *            the id of View
	 * @return the view
	 */
	public View setTouchNClick(int id)
	{

		View v = setClick(id);
		v.setOnTouchListener(TOUCH);
		return v;
	}

	/**
	 * Sets the click listener for a view.
	 * 
	 * @param id
	 *            the id of View
	 * @return the view
	 */
	public View setClick(int id)
	{

		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}

}
