package com.codepath.apps.tastebuds.activities;

import java.util.ArrayList;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.FriendsListFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog.RestaurantReviewDialogListener;
import com.codepath.apps.tastebuds.fragments.RestaurantListFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantMapListFragment;
import com.codepath.apps.tastebuds.listeners.FragmentTabListener;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class HomeActivity extends FragmentActivity {
	
	private ParseUser user;
	ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		Log.d("Debug", "Home Activity OnCreate");
		user = ParseUser.getCurrentUser();
		if (user == null) {
			user = new ParseUser();
			user.setUsername("efgh12345");
			user.setPassword("secret12345");
			user.setEmail("efgh12345@mail.com");
		}
		setupTabs();
		
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Restaurants")
			.setIcon(R.drawable.ic_launcher)
			.setTag("RestaurantListFragment")
			.setTabListener(
				new FragmentTabListener<RestaurantListFragment>(R.id.ctRestaurantsLists, this, "first",
								RestaurantListFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Friends")
			.setIcon(R.drawable.ic_launcher)
			.setTag("FriendsListFragment")
			.setTabListener(
			    new FragmentTabListener<FriendsListFragment>(R.id.ctRestaurantsLists, this, "second",
								FriendsListFragment.class));

		actionBar.addTab(tab2);
	}

	public void onReview(View view) {
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
		RestaurantReviewDialog dialog = RestaurantReviewDialog.newInstance(
				"Shree Datta", 12345678);
		dialog.show(ft, "compose");
		dialog.listener = new RestaurantReviewDialogListener() {
			@Override
			public void onFinishReviewComposeDialog(RestaurantReview review) {
				review.setUser(user);
				review.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException arg0) {
						if (arg0 == null) {
							Toast.makeText(HomeActivity.this, "Saved Review", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(HomeActivity.this, "Saved Review FAiled" + arg0.toString(), Toast.LENGTH_LONG).show();
						}
					}
					
					
				});
			}
		};
	}
}
