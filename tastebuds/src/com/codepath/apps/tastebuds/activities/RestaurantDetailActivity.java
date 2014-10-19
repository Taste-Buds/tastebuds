package com.codepath.apps.tastebuds.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.tastebuds.GooglePlacesApiClient;
import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.DishListFragment;
import com.codepath.apps.tastebuds.fragments.DishListFragment.DishListListener;
import com.codepath.apps.tastebuds.fragments.DishReviewDialog;
import com.codepath.apps.tastebuds.fragments.DishReviewDialog.DishReviewDialogListener;
import com.codepath.apps.tastebuds.fragments.RestaurantDetailFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDetailDialog;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDetailDialog.RestaurantReviewDetailDialogListener;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog.RestaurantReviewDialogListener;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewListFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewListFragment.RestaurantReviewListListener;
import com.codepath.apps.tastebuds.listeners.FragmentTabListener;
import com.codepath.apps.tastebuds.models.DishReview;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.loopj.android.http.JsonHttpResponseHandler;

public class RestaurantDetailActivity extends FragmentActivity 
	implements RestaurantReviewListListener, DishListListener {
	
	private String placeId;
	private Restaurant restaurant;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_detail);
		placeId = getIntent().getStringExtra("place_id");
		restaurantDetailFromGooglePlacesApi();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.review, menu);
		return true;
	}

	private void restaurantDetailFromGooglePlacesApi() {
		
		GooglePlacesApiClient placesApi = new GooglePlacesApiClient();		
		placesApi.getRestaurantDetailfromGooglePlaces(placeId, 
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
        		JSONObject restaurantDetailJson = null;
        		try {
        			restaurantDetailJson = response.getJSONObject("result");
        			restaurant = Restaurant.fromJSONDetail(restaurantDetailJson);
        			setupTabs();
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
			@Override
    		public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.e("Error", e.toString());
    		}		
		});		
	}

	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		//RestaurantDetailFragment restaurantDetailFragment = new RestaurantDetailFragment();
		Bundle args = new Bundle();
		args.putString("placeId", placeId);
		if (restaurant.getName() != null) {
			args.putString("restaurantName", restaurant.getName());
		} else {
			args.putString("restaurantName", "ABCD");
		}
		args.putSerializable("restaurant", restaurant);
		Tab tab1 = actionBar
			.newTab()
			.setText("Details")
			.setTag("RestaurantDetailFragment")
			.setTabListener(
				new FragmentTabListener<RestaurantDetailFragment>(R.id.ctRestaurantsLists, this, "first",
								RestaurantDetailFragment.class, args));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);
		
		Tab tab2 = actionBar
			.newTab()
			.setText("Dishes")
			.setTag("DishListFragment")
			.setTabListener(
			    new FragmentTabListener<DishListFragment>(R.id.ctRestaurantsLists, this, "second",
								DishListFragment.class, args));

		actionBar.addTab(tab2);

		Tab tab3 = actionBar
				.newTab()
				.setText("Reviews")
				.setTag("RestaurantReviewListFragment")
				.setTabListener(
					new FragmentTabListener<RestaurantReviewListFragment>(R.id.ctRestaurantsLists, this, "third",
									RestaurantReviewListFragment.class, args));
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
			RestaurantReviewDialog dialog = RestaurantReviewDialog.newInstance(
					restaurant.getName(), restaurant.getPlace_id());
			dialog.show(ft, "compose");
			dialog.listener = new RestaurantReviewDialogListener() {
				@Override
				public void onFinishReviewComposeDialog(RestaurantReview review) {
					if (review != null) {
						review.saveInBackground();
					}
				}
			};
	    } else {
			DishReviewDialog dialog = DishReviewDialog.newInstance(
					restaurant.getName(), restaurant.getPlace_id());
			dialog.show(ft, "compose");
			dialog.listener = new DishReviewDialogListener() {
				@Override
				public void onFinishReviewComposeDialog(DishReview review) {
					if (review != null) {
						review.saveInBackground();
					}
				}
			};
	    }
	}

	@Override
	public void onReviewSelected(String reviewId, String restaurantName) {
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
		RestaurantReviewDetailDialog dialog = 
				RestaurantReviewDetailDialog.newInstance(reviewId,
				restaurantName);
		dialog.show(ft, "detail");
		dialog.listener = new RestaurantReviewDetailDialogListener() {
			@Override
			public void onFinishReviewComposeDialog(RestaurantReview review) {}
		};
	}

	@Override
	public void onDishSelected(String googlePlacesId, String dishName) {
		Intent i = new Intent(RestaurantDetailActivity.this, DishDetailActivity.class);
		i.putExtra("placesId", placeId);
		i.putExtra("restaurant_name", restaurant.getName());
		i.putExtra("dish_name", dishName);
		startActivity(i);
	}
}
