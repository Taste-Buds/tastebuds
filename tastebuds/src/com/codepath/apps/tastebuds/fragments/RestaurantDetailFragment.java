package com.codepath.apps.tastebuds.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.fragments.UserRestaurantReviewsListFragment.UserRestaurantReviewListListener;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.ui.CircleButton;

public class RestaurantDetailFragment extends Fragment {

	private String placeId;
	private RatingBar rbRating;
	private TextView tvFriendsRating;
	private TextView tvPrice;
	private TextView tvPhone;
	private TextView tvHours;
	private TextView tvOpenNow;
	private TextView tvAddress;
	private TextView tvStatus;
	private TextView tvWebsite;
	private TextView tvName;
	private ListView lvPhotos;
	private RelativeLayout rlDetail;
	private CircleButton btnCall;
	private CircleButton btnReview;
	private CircleButton btnDish;
	private CircleButton btnMap;
	private CircleButton btnBookmark;
	private RestaurantDetailListener listener;
	private Bitmap bgImage;
	public Restaurant restaurant;
	
	public interface RestaurantDetailListener {
		public void onCallRestaurant(String phoneNumber);
		public void onDirections(String address);
		public void onAddReview(Restaurant restaurant);
		public void onAddDish(Restaurant restaurant);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		placeId = getArguments().getString("placeId");
		restaurant = (Restaurant) getArguments().getParcelable("restaurant");
		bgImage = (Bitmap) getArguments().getParcelable("bgImage");
		//restaurantDetailFromGooglePlacesApi();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
		rbRating = (RatingBar) v.findViewById(R.id.rbRatingRestaurantDetail);
		tvPrice = (TextView) v.findViewById(R.id.tvDollarsRestaurantDetail);
		tvHours = (TextView) v.findViewById(R.id.tvHoursRestaurantDetail);
		tvOpenNow = (TextView) v.findViewById(R.id.tvOpenRestaurantDetail);
		tvWebsite = (TextView) v.findViewById(R.id.tvWebsiteRestaurantDetail);
		tvAddress = (TextView) v.findViewById(R.id.tvAddressRestaurantDetail);
		tvPhone = (TextView) v.findViewById(R.id.tvPhoneRestaurantDetail);
		tvName = (TextView) v.findViewById(R.id.tvNameRestaurantDetail);
		rlDetail = (RelativeLayout) v.findViewById(R.id.rldetailImage);
		btnCall = (CircleButton) v.findViewById(R.id.btnCallRestaurantDetail);
		btnReview = (CircleButton) v.findViewById(R.id.btnReviewRestaurantDetail);
		btnDish = (CircleButton) v.findViewById(R.id.btnDishRestaurantDetail);
		btnMap = (CircleButton) v.findViewById(R.id.btnDirectionsRestaurantDetail);
		btnBookmark = (CircleButton) v.findViewById(R.id.btnBookmarkRestaurantDetail);
		
		btnCall.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	listener.onCallRestaurant(restaurant.getPhone());
	        }
	    });

		btnMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onDirections(restaurant.getAddress());
			}
		});

		btnReview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onAddReview(restaurant);
			}
		});

		btnDish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onAddDish(restaurant);
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
			case 0: tvPrice.setText("$"); break;
			case 1: tvPrice.setText("$"); break;
			case 2: tvPrice.setText("$$"); break;
			case 3: tvPrice.setText("$$$"); break;
			case 4: tvPrice.setText("$$$$"); break;
			default: tvPrice.setText("$");
		}

		tvName.setText(Html.fromHtml("<b>" + restaurant.getName() + "</b>"));
		tvAddress.setText(restaurant.getAddress().replace(",", "\n"));
		rbRating.setRating(5);
		if (restaurant.getWebsite() != null) {
			tvWebsite.setText(Html.fromHtml("<b>Website: </b><span>" +
					"<font color=\"blue\">" + restaurant.getWebsite() + "</font>"));
		} else {
			tvWebsite.setVisibility(TextView.GONE);
		}

		if (restaurant.getOpenHours() != null) {
			tvHours.setText(Html.fromHtml("<b>Hours: </b><span>" + restaurant.getOpenHours()));
		} else {
			tvWebsite.setVisibility(TextView.GONE);
		}		
		
		if (restaurant.isOpen_now()) {
			tvOpenNow.setText(Html.fromHtml("<font color=\"green\"> Open Now </font>"));
		} else {
			tvOpenNow.setText(Html.fromHtml("<font color=\"red\"> Closed </font>"));
		}
		if (restaurant.getPhone() != null) {
			tvPhone.setText(Html.fromHtml("<b>Phone: </b><span>" + restaurant.getPhone()));
		} else {
			tvWebsite.setVisibility(TextView.GONE);
		}
		if (bgImage != null) {
			rlDetail.setBackground(new BitmapDrawable(getResources(), bgImage));
		}
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
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof RestaurantDetailListener) {
			listener = (RestaurantDetailListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement RestaurantReviewDialog.UserRestaurantReviewListListener");
		}
	}	
}