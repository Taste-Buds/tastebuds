package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.tastebuds.GooglePlacesApiClient;
import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.activities.RestaurantDetailActivity;
import com.codepath.apps.tastebuds.adapters.RestaurantAdapter;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.codepath.apps.tastebuds.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class RestaurantListFragment extends Fragment {

	List<Restaurant> restaurants;
	List<String> placeIds;
	ArrayAdapter<Restaurant> restaurantAdapter;
	ListView lvRestaurants;
	List<ParseObject> friends;
	List<RestaurantReview> reviews;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		restaurants = new ArrayList<Restaurant>();
		restaurantAdapter = new RestaurantAdapter(getActivity(), restaurants);
		placeIds = new ArrayList<String>();
		friendsFromFacebook();
		restaurantsFromGooglePlacesApi();	
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
		lvRestaurants = (ListView) v.findViewById(R.id.lvRestaurants);
		lvRestaurants.setAdapter(restaurantAdapter);
		lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
			   public void onItemClick(AdapterView parentView, View childView, int position, long id) 
			   {  
				   showRestaurantDetail(position);
			   } 
			});		
		return v;		
	}
	
	private void friendsFromFacebook() {
		// Get array of friends from Facebook API
		Log.d("Debug", "Start Friends Query");
		friends = ParseUser.getCurrentUser().getList("userFriends");
		Log.d("Debug", "Friends Query Complete");

	}
	
	private void restaurantsFromGooglePlacesApi() {
		
		GooglePlacesApiClient placesApi = new GooglePlacesApiClient();
		// 700 Illinois Street, SF = 37.764046, -122.387863
		double latitude = 37.764046;
		double longitude = -122.387863;
		
		placesApi.getRestaurantListfromGooglePlaces(latitude, longitude, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
        		JSONArray placesApiResultsJson = null;
        		try {
        			placesApiResultsJson = response.getJSONArray("results");
        			//Log.d("Debug", "Places:" + placesApiResultsJson.toString());
        			restaurants.addAll(Restaurant.fromJSONArray(placesApiResultsJson));
        			//adapterplacesApiResultsJson.addAll(Restaurant.fromJSONArray(placesApiResultsJson));
        			//Restaurant first = restaurants.get(0);
        			//Log.d("Debug", "PlaceId: " + first.getPlace_id());
        			
        			for(int i=0; i<restaurants.size(); i++) {
        				Restaurant restaurant = restaurants.get(i);
        				String placeId = restaurant.getPlace_id();
        				placeIds.add(placeId);
        			}     			       			
        			restaurantReviewsWithGoogleAndFacebookData();       			
        			
				} catch (JSONException e) {
					e.printStackTrace();
				}
            	restaurantAdapter.notifyDataSetChanged();
        	}
			@Override
    		public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.e("Error", e.toString());
    		}
			
		});
		
		
	}
	
	private void restaurantReviewsWithGoogleAndFacebookData() {
		if (placeIds == null) {Log.d("Debug", "PlaceIds Null");}
		if (friends == null) {Log.d("Debug", "Friends Null");}
		
		Log.d("Debug", "Getting Reviews");
		ParseQuery<RestaurantReview> query = RestaurantReview.getQuery(placeIds, friends);
		query.findInBackground(new FindCallback<RestaurantReview>() {
			@Override
			  public void done(List<RestaurantReview> results, ParseException e) {
				    // results has the list of user reviews from Parse
				  	reviews = results;
				  	parseReviews();
				  }
			});
	}
	
	private void parseReviews() {
		Log.d("Debug", "Parsing Reviews");
		for(int i=0; i<placeIds.size();i++) {
			String placeId = placeIds.get(i);
			Restaurant restaurant = restaurants.get(i);
			int numberOfReviews = 0;
			int sumOfRatings = 0;
			for(int j=0; j<reviews.size();j++) {
				RestaurantReview review = reviews.get(j);
				String placeIdReview = review.getGooglePlacesId();
				if (placeId.equals(placeIdReview)) {
					int rating = review.getRating();
					numberOfReviews++;
					sumOfRatings = sumOfRatings + rating;
				}
				//Log.d("Debug", "Rating: " + Integer.toString(rating));
			}
			long friendRating = 0;
			if (numberOfReviews > 0) {
				friendRating = sumOfRatings/numberOfReviews;
			}
			else {
				friendRating = 0;
				numberOfReviews = 0;
			}
			restaurant.setFriendRating(friendRating);
			restaurant.setNumOfReviews(numberOfReviews);		
		}
		restaurantAdapter.notifyDataSetChanged();
	}
	
	public void showRestaurantDetail(int position) {
		Log.d("Debug", "P: " + position);
		Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
		i.putExtra("place_id", restaurants.get(position).getPlace_id());
		startActivity(i);
	}

}