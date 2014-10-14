package com.codepath.apps.tastebuds.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.DishListFragment;
import com.codepath.apps.tastebuds.fragments.DishReviewDialog;
import com.codepath.apps.tastebuds.fragments.DishReviewDialog.DishReviewDialogListener;
import com.codepath.apps.tastebuds.fragments.RestaurantDetailFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog.RestaurantReviewDialogListener;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewListFragment;
import com.codepath.apps.tastebuds.listeners.FragmentTabListener;
import com.codepath.apps.tastebuds.models.DishReview;
import com.codepath.apps.tastebuds.models.RestaurantReview;

public class RestaurantDetailActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_detail);
		setupTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.review, menu);
		return true;
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Details")
			.setTag("RestaurantDetailFragment")
			.setTabListener(
				new FragmentTabListener<RestaurantDetailFragment>(R.id.ctRestaurantsLists, this, "first",
								RestaurantDetailFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Dishes")
			.setTag("DishListFragment")
			.setTabListener(
			    new FragmentTabListener<DishListFragment>(R.id.ctRestaurantsLists, this, "second",
								DishListFragment.class));

		actionBar.addTab(tab2);

		Tab tab3 = actionBar
				.newTab()
				.setText("Reviews")
				.setTag("RestaurantReviewListFragment")
				.setTabListener(
					new FragmentTabListener<RestaurantReviewListFragment>(R.id.ctRestaurantsLists, this, "third",
									RestaurantReviewListFragment.class));
		actionBar.addTab(tab3);
	}


	public void onReviewComposeAction(MenuItem mi) {
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    if (getActionBar().getSelectedTab().getTag() != "DishListFragment") {
			RestaurantReviewDialog dialog = RestaurantReviewDialog.newInstance("Shree Datta", 1234567);
			dialog.show(ft, "compose");
			dialog.listener = new RestaurantReviewDialogListener() {
				@Override
				public void onFinishReviewComposeDialog(RestaurantReview review) {
					if (review != null) {
						Toast.makeText(RestaurantDetailActivity.this, "Review Saved", Toast.LENGTH_LONG).show();
					}
				}
			};
	    } else {
			DishReviewDialog dialog = DishReviewDialog.newInstance("Shree Datta", 1234567);
			dialog.show(ft, "compose");
			dialog.listener = new DishReviewDialogListener() {
				@Override
				public void onFinishReviewComposeDialog(DishReview review) {
					// TODO Auto-generated method stub
					if (review != null) {
						Toast.makeText(RestaurantDetailActivity.this, "Review Saved", Toast.LENGTH_LONG).show();
					}
				}
			};
	    }
	}
}
