package com.codepath.apps.tastebuds.fragments;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.Restaurant;

public class RestaurantDetailFragment extends Fragment {

	private String placeId;
	private TextView tvGoogleRating;
	private TextView tvFriendsRating;
	private TextView tvPrice;
	private TextView tvPhone;
	private TextView tvAddress;
	private TextView tvStatus;
	private TextView tvWebsite;
	private WebView wvGoogleMap;
	public Restaurant restaurant;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		placeId = getArguments().getString("placeId");
		restaurant = (Restaurant) getArguments().getSerializable("restaurant");
		//restaurantDetailFromGooglePlacesApi();	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
		tvGoogleRating = (TextView) v.findViewById(R.id.tvGoogleRating);
		tvPrice = (TextView) v.findViewById(R.id.tvPrice);
		tvPhone = (TextView) v.findViewById(R.id.tvPhone);
		tvAddress = (TextView) v.findViewById(R.id.tvAddress);
		tvStatus = (TextView) v.findViewById(R.id.tvStatus);
		tvWebsite = (TextView) v.findViewById(R.id.tvWebsite);
		tvFriendsRating= (TextView) v.findViewById(R.id.tvFriendRating);
		wvGoogleMap = (WebView) v.findViewById(R.id.wvGoogleMap);
		updateDetailFragmentView();
		return v;		
	}
	
	/*private void restaurantDetailFromGooglePlacesApi() {
		
		GooglePlacesApiClient placesApi = new GooglePlacesApiClient();		
		placesApi.getRestaurantDetailfromGooglePlaces(placeId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
        		JSONObject restaurantDetailJson = null;
        		Log.d("Debug", "Response: " + response.toString());
        		try {
        			restaurantDetailJson = response.getJSONObject("result");
        			restaurant = Restaurant.fromJSONDetail(restaurantDetailJson);
        			Log.d("Debug", "Name Detail: " + restaurant.getName());
        			updateDetailFragmentView();
				} catch (JSONException e) {
					e.printStackTrace();
				}
        	}
			@Override
    		public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.e("Error", e.toString());
    		}		
		});		
	}*/
	
	public void updateDetailFragmentView() {
		tvGoogleRating.setText(Double.toString(restaurant.getGoogle_rating()));	
		switch (restaurant.getPrice_level()) {
			case 0: tvPrice.setText(""); break;
			case 1: tvPrice.setText("$"); break;
			case 2: tvPrice.setText("$$"); break;
			case 3: tvPrice.setText("$$$"); break;
			case 4: tvPrice.setText("$$$$"); break;
			default: tvPrice.setText("");
		}
		tvPhone.setText(restaurant.getPhone());
		tvAddress.setText(restaurant.getAddress());
		tvWebsite.setText(restaurant.getWebsite());
		tvFriendsRating.setText(Double.toString(restaurant.getFriendRating()));
		wvGoogleMap.loadUrl(restaurant.getWeb_map());
		wvGoogleMap.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
			          view.loadUrl(url);
			          return true;
			           }});
		if (restaurant.isOpen_now()) {
			tvStatus.setText("Open");
			tvStatus.setTextColor(Color.GREEN);
		} else {
			tvStatus.setText("Closed");
			tvStatus.setTextColor(Color.RED);
		}
		wvGoogleMap.getSettings().setLoadsImagesAutomatically(true);
		//wvGoogleMap.getSettings().setJavaScriptEnabled(true);
		wvGoogleMap.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
	      // Configure the client to use when opening URLs
		//wvGoogleMap.setWebViewClient(new MyBrowser());
	      // Load the initial URL
//		wvGoogleMap.loadUrl(restaurant.getWeb_map()); // 
	}
	
	   // Manages the behavior when URLs are loaded
	   private class MyBrowser extends WebViewClient {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	      }
	   }
	
	
}
