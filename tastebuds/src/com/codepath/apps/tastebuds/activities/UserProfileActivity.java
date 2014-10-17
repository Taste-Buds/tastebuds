package com.codepath.apps.tastebuds.activities;

import java.util.ArrayList;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.FriendsListFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog;
import com.codepath.apps.tastebuds.fragments.RestaurantReviewDialog.RestaurantReviewDialogListener;
import com.codepath.apps.tastebuds.fragments.RestaurantListFragment;
import com.codepath.apps.tastebuds.fragments.RestaurantMapListFragment;
import com.codepath.apps.tastebuds.fragments.UserRestaurantReviewsListFragment;
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

public class UserProfileActivity extends FragmentActivity {
	
	private ParseUser user;
	private String userId;
	ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_user_profile);
		userId = getIntent().getStringExtra("userId");
		if(userId == null){
			user = ParseUser.getCurrentUser();
			userId = (String) user.get("fbId");
		}
		setupTabs();
		
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		Bundle args = new Bundle();
		args.putString("userId", userId);
		Tab tab1 = actionBar
			.newTab()
			.setText("Restaurant Reviews")
			.setIcon(R.drawable.ic_launcher)
			.setTag("UserRestaurantReviewsListFragment")
			.setTabListener(
				new FragmentTabListener<UserRestaurantReviewsListFragment>(R.id.flProfile, this, "first",
						UserRestaurantReviewsListFragment.class, args));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);
//
//		Tab tab2 = actionBar
//			.newTab()
//			.setText("Dish Reviews")
//			.setIcon(R.drawable.ic_launcher)
//			.setTag("UserDishReviewsListFragment")
//			.setTabListener(
//			    new FragmentTabListener<UserDishReviewsListFragment>(R.id.flProfile, this, "second",
//			    		UserDishReviewsListFragment.class));
//
//		actionBar.addTab(tab2);
	}

//	public void onReview(View view) {
//	    FragmentTransaction ft = getFragmentManager().beginTransaction();
//	    Fragment prev = getFragmentManager().findFragmentByTag("dialog");
//	    if (prev != null) {
//	        ft.remove(prev);
//	    }
//	    ft.addToBackStack(null);
//		RestaurantReviewDialog dialog = RestaurantReviewDialog.newInstance(
//				"Shree Datta", 12345678);
//		dialog.show(ft, "compose");
//		dialog.listener = new RestaurantReviewDialogListener() {
//			@Override
//			public void onFinishReviewComposeDialog(RestaurantReview review) {
//				review.setUser(user);
//				review.saveInBackground(new SaveCallback() {
//					@Override
//					public void done(ParseException arg0) {
//						if (arg0 == null) {
//							Toast.makeText(UserProfileActivity.this, "Saved Review", Toast.LENGTH_LONG).show();
//						} else {
//							Toast.makeText(UserProfileActivity.this, "Saved Review FAiled" + arg0.toString(), Toast.LENGTH_LONG).show();
//						}
//					}
//					
//					
//				});
//			}
//		};
//	}
}
