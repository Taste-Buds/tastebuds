package com.codepath.apps.tastebuds.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.tastebuds.GooglePlacesApiClient;
import com.codepath.apps.tastebuds.R;
import com.codepath.apps.tastebuds.activities.RestaurantDetailActivity;
import com.codepath.apps.tastebuds.adapters.RestaurantAdapter;
import com.codepath.apps.tastebuds.models.Restaurant;
import com.loopj.android.http.JsonHttpResponseHandler;

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

	ArrayList<Restaurant> restaurants;
	ArrayAdapter<Restaurant> restaurantAdapter;
	ListView lvRestaurants;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		restaurants = new ArrayList<Restaurant>();
		restaurantAdapter = new RestaurantAdapter(getActivity(), restaurants);
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
	
	public void showRestaurantDetail(int position) {
		Log.d("Debug", "P: " + position);
		Intent i = new Intent(getActivity(), RestaurantDetailActivity.class);
		i.putExtra("place_id", restaurants.get(position).getPlace_id());
		startActivity(i);
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
        			Restaurant first = restaurants.get(0);
        			Log.d("Debug", "Name: " + first.getName());
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

}