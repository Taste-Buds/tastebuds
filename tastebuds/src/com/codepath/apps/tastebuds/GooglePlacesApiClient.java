package com.codepath.apps.tastebuds;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GooglePlacesApiClient {
	
	public void getRestaurantListfromGooglePlaces(double latitude, double longitude, JsonHttpResponseHandler handler) {
		// change lat, long from double to String
		Log.d("Debug", "Client Called");
		String latString = String.valueOf(latitude);
		String longString = String.valueOf(longitude);
		String baseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
		String apiKey = "AIzaSyCMD74MgiALP22wpGggAAf4QXYzutiKRQg";
		String location = latString+","+longString;
		//String types = "&types=bakery|bar|cafe|convenience_store|food|grocery_or_supermarket|liquor_store|meal_delivery|meal_takeaway|restaurant";
		String types = "restaurant";
		RequestParams params = new RequestParams();
		params.put("key", apiKey);
		params.put("location", location);
		params.put("rankby", "distance");
		params.put("types", types);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(baseUrl, params, handler);
	}

}
