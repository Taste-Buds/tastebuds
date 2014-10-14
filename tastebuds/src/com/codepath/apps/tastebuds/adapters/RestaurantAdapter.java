package com.codepath.apps.tastebuds.adapters;

import java.util.List;


import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.Restaurant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
		
		return v;
	}
}
