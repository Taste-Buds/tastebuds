package com.codepath.apps.tastebuds.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Restaurant {
	
	private String place_id;
	private String name;
	
	public String getPlace_id() {
		return place_id;
	}

	public String getName() {
		return name;
	}	
	
	public static Restaurant fromJSON(JSONObject jsonObject) {
		Restaurant restaurant = new Restaurant();
		try {
			restaurant.place_id = jsonObject.getString("place_id");
			restaurant.name = jsonObject.getString("name");
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}		
		return restaurant;
	}
	
	public static ArrayList<Restaurant> fromJSONArray(JSONArray jsonArray) {
		ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>(jsonArray.length());
		
		for (int i=0; i<jsonArray.length(); i++) {
			JSONObject restaurantJson = null;
			try {
				restaurantJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			Restaurant restaurant = Restaurant.fromJSON(restaurantJson);
			if (restaurant != null ) {
				restaurants.add(restaurant);
			}
		}
		return restaurants;
	}

}
