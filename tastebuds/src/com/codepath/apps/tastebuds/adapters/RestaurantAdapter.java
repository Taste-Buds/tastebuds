package com.codepath.apps.tastebuds.adapters;

import java.util.List;


import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class RestaurantAdapter extends ArrayAdapter<Restaurant> {
	
	public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
		super(context, 0, restaurants);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Restaurant restaurant = getItem(position);
		View v;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			v = inflater.inflate(R.layout.restaurant_list_item, parent, false);
		} else {
			v = convertView;
		}
		TextView tvRestaurantName = (TextView) v.findViewById(R.id.tvRestaurantName);
		tvRestaurantName.setText(restaurant.getName());
		RatingBar rbTasteBudsRating = (RatingBar) v.findViewById(R.id.rbTasteBudsRating);
		String numberOfReviewString = "Reviews: " + Integer.toString(restaurant.getNumOfReviews());
		float friendRating = (restaurant.getFriendRating());
		rbTasteBudsRating.setRating(friendRating);
		TextView tvNumberofReviews = (TextView) v.findViewById(R.id.tvNumberofReviews);
		tvNumberofReviews.setText(numberOfReviewString);
		TextView tvDistance = (TextView) v.findViewById(R.id.tvDistance);
		float distance = (float) (restaurant.getCurrentDistancetoUser() * 0.00062137);
		String distanceString = String.format("%.1f", distance);
		tvDistance.setText(distanceString);
		ImageView ivRestaurantImage = (ImageView) v.findViewById(R.id.ivRestaurantImage);
		if (restaurant.getIcon() != null) {
			Picasso.with(getContext()).load(restaurant.getIcon()).into(ivRestaurantImage);
		}
		return v;
	}
}
