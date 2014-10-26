package com.codepath.apps.tastebuds.fragments;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.ui.CircleButton;

public class RestaurantDetailFragment extends Fragment {

	private String placeId;
	private RatingBar rbRating;
	private TextView tvFriendsRating;
	private TextView tvPrice;
	private TextView tvPhone;
	private TextView tvAddress;
	private TextView tvStatus;
	private TextView tvWebsite;
	private TextView tvName;
	private ListView lvPhotos;
	private RelativeLayout rlDetail;
	private CircleButton call;
	private CircleButton rate;
	private CircleButton dish;
	private CircleButton map;
	private CircleButton bookmark;
	public Restaurant restaurant;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		placeId = getArguments().getString("placeId");
		restaurant = (Restaurant) getArguments().getParcelable("restaurant");
		//restaurantDetailFromGooglePlacesApi();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
		rbRating = (RatingBar) v.findViewById(R.id.rbRatingRestaurantDetail);
		tvPrice = (TextView) v.findViewById(R.id.tvDollarsRestaurantDetail);
		tvPhone = (TextView) v.findViewById(R.id.tvPhoneRestaurantDetail);
		tvAddress = (TextView) v.findViewById(R.id.tvAddressRestaurantDetail);
		tvName = (TextView) v.findViewById(R.id.tvNameRestaurantDetail);
		rlDetail = (RelativeLayout) v.findViewById(R.id.rldetailImage);
		call = (CircleButton) v.findViewById(R.id.btnCallRestaurantDetail);
		rate = (CircleButton) v.findViewById(R.id.btnReviewRestaurantDetail);
		dish = (CircleButton) v.findViewById(R.id.btnDishRestaurantDetail);
		map = (CircleButton) v.findViewById(R.id.btnDirectionsRestaurantDetail);
		bookmark = (CircleButton) v.findViewById(R.id.btnBookmarkRestaurantDetail);
		
		call.setOnClickListener(new OnClickListener() {
			
	        public void onClick(View v) {
	            try {
	                    Intent callIntent = new Intent(Intent.ACTION_CALL);
	                    callIntent.setData(Uri.parse("tel:"+ tvPhone.getText().toString()));
	                    startActivity(callIntent);
	                } catch (ActivityNotFoundException activityException) {
	                    Log.e("Calling a Phone Number", "Call failed", activityException);
	                }
	        }
	    });
		//tvStatus = (TextView) v.findViewById(R.id.tvStatus);
		//tvWebsite = (TextView) v.findViewById(R.id.tvWebsite);
		//tvFriendsRating= (TextView) v.findViewById(R.id.tvFriendRating);

		/*lvPhotos = (ListView) v.findViewById(R.id.lvPhotos);
		//wvGoogleMap = (WebView) v.findViewById(R.id.wvGoogleMap);
		updateDetailFragmentView();
		
		wvGoogleMap = (WebView) v.findViewById(R.id.wvGoogleMap);*/
		updateDetailFragmentView();

		return v;		
	}

	
	@SuppressLint("NewApi")
	public void updateDetailFragmentView() {
		//tvGoogleRating.setText(Double.toString(restaurant.getGoogle_rating()));	
		switch (restaurant.getPrice_level()) {
			case 0: tvPrice.setText(""); break;
			case 1: tvPrice.setText("$"); break;
			case 2: tvPrice.setText("$$"); break;
			case 3: tvPrice.setText("$$$"); break;
			case 4: tvPrice.setText("$$$$"); break;
			default: tvPrice.setText("");
		}

		tvPhone.setText(restaurant.getPhone());
		tvAddress.setText(restaurant.getAddress().replace(",", "\n"));
		//tvWebsite.setText(restaurant.getWebsite());
		//rbRating.setRating((float) restaurant.getGoogle_rating());
		rbRating.setRating(5);
		tvName.setText(restaurant.getName());
		/*int imageResource = getResources().getIdentifier("res1", "drawable",
				"com.codepath.apps.tastebuds.fragments");
		Drawable res = getResources().getDrawable(imageResource);
		rlDetail.setBackground(res);*/
		//tvFriendsRating.setText(Double.toString(restaurant.getFriendRating()));
		
//		wvGoogleMap.loadUrl(restaurant.getWeb_map());
//		wvGoogleMap.setWebViewClient(new WebViewClient() {
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//			          view.loadUrl(url);
//			          return true;
//			           }});
//		if (restaurant.isOpen_now()) {
//			tvStatus.setText("Open");
//			tvStatus.setTextColor(Color.GREEN);
//		} else {
//			tvStatus.setText("Closed");
//			tvStatus.setTextColor(Color.RED);
//		}
//		wvGoogleMap.getSettings().setLoadsImagesAutomatically(true);
//
//		wvGoogleMap.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

	}
//	
//	   // Manages the behavior when URLs are loaded
//	   private class MyBrowser extends WebViewClient {
//	      @Override
//	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
//	         view.loadUrl(url);
//	         return true;
//	      }
//	   }
	
	
}