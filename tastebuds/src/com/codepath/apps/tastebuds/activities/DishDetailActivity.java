package com.codepath.apps.tastebuds.activities;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.adapters.DishReviewListAdapter;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class DishDetailActivity extends Activity {

	private String restaurantName;
	private String dishName;
	private String placesId;
	private DishReviewListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dish_detail);
		restaurantName = getIntent().getStringExtra("restaurant_name");
		dishName = getIntent().getStringExtra("dish_name");
		placesId = getIntent().getStringExtra("placesId");
		ListView lvReviews = (ListView) findViewById(R.id.lvDishReview);
		List<ParseObject> friends = ParseUser.getCurrentUser().getList("userFriends");
		adapter = new DishReviewListAdapter(this, placesId, friends, dishName);
		lvReviews.setAdapter(adapter);
	}
}
