package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
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
	int pageToken;
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
		pageToken = 1;
		parsingPageToken = 1;
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
	    		//Log.d("Debug", "pageToken:" + Integer.toString(pageToken));
	    		//Log.d("Debug", "Page:" + Integer.toString(page));
	    		//Log.d("Debug", "TotalItemCount:" + Integer.toString(totalItemsCount));
	    		restaurantsFromGooglePlacesApi(nextPageToken);
	    		
                // or customLoadMoreDataFromApi(totalItemsCount); 
	    }
        });        
		return v;
	}

	private void friendsFromFacebook() {
		// Get array of friends from Facebook API
		//Log.d("Debug", "Start Friends Query");
		friends = ParseUser.getCurrentUser().getList("userFriends");
		//Log.d("Debug", "Friends Query Complete");
	}

	private void restaurantsFromGooglePlacesApi(String nextPageToken) {
		
		GooglePlacesApiClient placesApi = new GooglePlacesApiClient();
		// 700 Illinois Street, SF = 37.764046, -122.387863
		//double latitude = 37.764046; // Static for 700 Illinois
		//double longitude = -122.387863; // Static for 700 Illinois
		
		double latitude = mCurrentLocation.getLatitude();
		double longitude = mCurrentLocation.getLongitude();	
		
		placesApi.getRestaurantListfromGooglePlaces(nextPageToken, latitude, longitude, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
        		JSONArray placesApiResultsJson = null;
        		try {
        			placesApiResultsJson = response.getJSONArray("results");
        			String newNextPageToken = response.getString("next_page_token");
        			updateNextPageToken(newNextPageToken);
        			//Log.d("Debug", "Next Page Token" + newNextPageToken);
        			List<Restaurant> newRestaurants = Restaurant.fromJSONArray(placesApiResultsJson);
        			//Log.d("Debug", "Places:" + placesApiResultsJson.toString());
        			//restaurants.addAll(Restaurant.fromJSONArray(placesApiResultsJson));
        			//adapterplacesApiResultsJson.addAll(Restaurant.fromJSONArray(placesApiResultsJson));
        			Restaurant first = newRestaurants.get(0);
        			Log.d("Debug", "Name: " + first.getName());
        			//ArrayList<String> newPlaceIds = new ArrayList<String>();
        			
        			newPlaceIds.clear();
        			for(int i=0; i<newRestaurants.size(); i++) {
        				Restaurant restaurant = newRestaurants.get(i);
        				String placeId = restaurant.getPlace_id();    				
        				newPlaceIds.add(placeId);
        			}
        			placeIds.addAll(newPlaceIds);
        			
        			
        			restaurants.addAll(newRestaurants);
        			//restaurantReviewsWithGoogleAndFacebookData(newRestaurants);       			
        			
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
		pageToken++;	
	}
	
	private void updateNextPageToken (String newNextPageToken) {
		setNextPageToken(newNextPageToken);
	}
	
	private void setNextPageToken (String nextPageToken) {
		this.nextPageToken = nextPageToken;
	}

	private void restaurantReviewsWithGoogleAndFacebookData(List<Restaurant> newRestaurants) {
		//if (placeIds == null) {Log.d("Debug", "PlaceIds Null");}
		//if (friends == null) {Log.d("Debug", "Friends Null");}
		
		Log.d("Debug", "Getting Reviews");
		ParseQuery<RestaurantReview> query = RestaurantReview.getQuery(newPlaceIds, friends);
		query.findInBackground(new FindCallback<RestaurantReview>() {
			@Override
			  public void done(List<RestaurantReview> results, ParseException e) {
				    // results has the list of user reviews from Parse
					List<RestaurantReview> newReviews = new ArrayList<RestaurantReview>();
				  	//reviews = results;
				  	newReviews = results;
				  	parseReviews(newReviews);
				  }
			});
	}
	
	private void parseReviews(List<RestaurantReview> newReviews) {
		Log.d("Debug", "Parsing Reviews");
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
		parsingPageToken++;
		restaurantAdapter.notifyDataSetChanged();
	}
	
	public void showRestaurantDetail(int position) {
		Log.d("Debug", "P: " + position);
		Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
		i.putExtra("place_id", restaurants.get(position).getPlace_id());
		startActivity(i);
	}

}