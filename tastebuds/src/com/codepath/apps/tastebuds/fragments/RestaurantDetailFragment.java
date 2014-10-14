package com.codepath.apps.tastebuds.fragments;


import com.codepath.apps.tastebuds.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RestaurantDetailFragment extends Fragment {

	private String placeId;
	private TextView tvPlaceId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		placeId = getArguments().getString("placeId");
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
		tvPlaceId = (TextView) v.findViewById(R.id.tvPlaceId);
		tvPlaceId.setText(placeId);
		
		return v;		
	}
	
	
}
