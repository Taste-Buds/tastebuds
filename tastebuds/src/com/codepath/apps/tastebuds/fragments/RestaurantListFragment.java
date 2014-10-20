package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
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

import com.codepath.apps.tastebuds.GooglePlacesApiClient;
import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.activities.RestaurantDetailActivity;
import com.codepath.apps.tastebuds.adapters.RestaurantAdapter;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.codepath.apps.tastebuds.models.RestaurantReview;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.codepath.apps.tastebuds.listeners.EndlessScrollListener;




public class RestaurantListFragment extends Fragment {
	
	List<Restaurant> restaurants;
	List<String> placeIds;
	List<String> newPlaceIds;
	ArrayAdapter<Restaurant> restaurantAdapter;
	ListView lvRestaurants;
	List<ParseObject> friends;
	List<RestaurantReview> reviews;
	Location mCurrentLocation;
	int parsingPageToken;
	String nextPageToken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCurrentLocation = getArguments().getParcelable("mCurrentLocation");
		restaurants = new ArrayList<Restaurant>();
		restaurantAdapter = new RestaurantAdapter(getActivity(), restaurants);
		placeIds = new ArrayList<String>();
		newPlaceIds = new ArrayList<String>();
		friendsFromFacebook();
		parsingPageToken = 0;
		nextPageToken = "None";
		restaurantsFromGooglePlacesApi(nextPageToken);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		//return super.onCreateView(inflater, container, savedInstanceState);
		
		View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
		lvRestaurants = (ListView) v.findViewById(R.id.lvRestaurants);
		lvRestaurants.setAdapter(restaurantAdapter);
		lvRestaurants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			   public void onItemClick(AdapterView parentView, View childView, int position, long id) {
				   showRestaurantDetail(position);
			   } 
			});		
        lvRestaurants.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
	    		restaurantsFromGooglePlacesApi(nextPageToken);    		 
	    }
        });        
		return v;
	}

	private void friendsFromFacebook() {
		// Get array of friends from Facebook API
		friends = ParseUser.getCurrentUser().getList("userFriends");
	}

	private void restaurantsFromGooglePlacesApi(String nextPageToken) {
		
		GooglePlacesApiClient placesApi = new GooglePlacesApiClient();
		// 700 Illinois Street, SF = 37.764046, -122.387863
		//double latitude = 37.764046; // Static for 700 Illinois
		//double longitude = -122.387863; // Static for 700 Illinois
		
		double latitude = mCurrentLocation.getLatitude();
		double longitude = mCurrentLocation.getLongitude();
		//userLocation = new Location("");
	    //userLocation.setLatitude(latitude);
	    //userLocation.setLongitude(longitude); 
		
		placesApi.getRestaurantListfromGooglePlaces(nextPageToken, latitude, longitude, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
        		JSONArray placesApiResultsJson = null;
        		try {
        			placesApiResultsJson = response.getJSONArray("results");
        			String newNextPageToken = "";
        			try {
        			newNextPageToken = response.getString("next_page_token");
        			} catch (Exception e) {
        				newNextPageToken = "QueryLimitReached";
        			}
        			updateNextPageToken(newNextPageToken);
        			List<Restaurant> newRestaurants = Restaurant.fromJSONArray(placesApiResultsJson);
        			
        			newPlaceIds.clear();
        			for(int i=0; i<newRestaurants.size(); i++) {
        				Restaurant restaurant = newRestaurants.get(i);
        				String placeId = restaurant.getPlace_id();    				
        				newPlaceIds.add(placeId);
        				calcuateDistancetoUser(restaurant);
        			}
        			placeIds.addAll(newPlaceIds);     			     			
        			restaurants.addAll(newRestaurants);
        			//Log.d("Debug", "Added more Restaurants");
        			restaurantReviewsWithGoogleAndFacebookData(newRestaurants);       			
        			
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
	
	private void calcuateDistancetoUser(Restaurant restaurant) {
		Location locationRestaurant = new Location("");
		try {locationRestaurant = restaurant.getLocation();}
			catch (Exception e) { Log.d("Debug", "No Rest Location"); }
		if (mCurrentLocation == null) {
			Log.d("Debug", "No User Location");
		}
		if (restaurant.getLocation() == null) {
			//Log.d("Debug", "No Restaurant Location");
		}
		float distance = 0;
		try { distance = mCurrentLocation.distanceTo(locationRestaurant); }
			catch (Exception e){Log.d("Debug", "Can't get Location B"); }
		//restaurant.setCurrentDistancetoUser(distance);
	}
	
	private void updateNextPageToken (String newNextPageToken) {
		setNextPageToken(newNextPageToken);
	}
	

	public String getNextPageToken() {
		return nextPageToken;
	}

	public void setNextPageToken(String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	private void restaurantReviewsWithGoogleAndFacebookData(List<Restaurant> newRestaurants) {

		ParseQuery<RestaurantReview> query = RestaurantReview.getQuery(newPlaceIds, friends);
		query.findInBackground(new FindCallback<RestaurantReview>() {
			@Override
			  public void done(List<RestaurantReview> results, ParseException e) {
				    // results has the list of user reviews from Parse
					List<RestaurantReview> newReviews = new ArrayList<RestaurantReview>();
				  	newReviews = results;
				  	parseReviews(newReviews);
				  }
			});
	}
	
	private void parseReviews(List<RestaurantReview> newReviews) {
		for(int i=0; i<newPlaceIds.size();i++) {
			String placeId = newPlaceIds.get(i);
			int restaurantArrayPosition = (parsingPageToken*20)+i;
			Restaurant restaurant = restaurants.get(restaurantArrayPosition);
			int numberOfReviews = 0;
			int sumOfRatings = 0;
			for(int j=0; j<newReviews.size();j++) {
				RestaurantReview review = newReviews.get(j);
				String placeIdReview = review.getGooglePlacesId();
				if (placeId.equals(placeIdReview)) {
					int rating = review.getRating();
					numberOfReviews++;
					sumOfRatings = sumOfRatings + rating;
				}
			}
			float friendRating = 0;
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
		parsingPageToken++;
		//Restaurant theRamp = restaurants.get(4);
    	//Log.d("Debug", "Name: " + theRamp.getName());
    	//Log.d("Debug", "PlaceId: " + theRamp.getPlace_id());
    	//Log.d("Debug", "Number of Reviews: " + Integer.toString(theRamp.getNumOfReviews()));
		
		restaurantAdapter.notifyDataSetChanged();
	}
	
	public void showRestaurantDetail(int position) {
		//Log.d("Debug", "P: " + position);
		Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
		i.putExtra("place_id", restaurants.get(position).getPlace_id());
		startActivity(i);
	}

}