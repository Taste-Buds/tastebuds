package com.codepath.apps.tastebuds.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Restaurant {
	
	private String place_id; 		// place_id
	private String name; 			// name
	private String address;			// formatted_address
	private String phone;			// formatted_phone_number
	private double latitude;		// geometry:location:lat
	private double longitude;		// geometry:location:lng
	private double google_rating;	// rating
	private int price_level;		// price_level
	private String website;			// website
	private boolean open_now; 		// opening_hours:open_now
	private String web_map;			// URI for Restaurant Map on Google
	private int numOfReviews;		// Number of Friend Reviews
	private long friendRating;		// average Rating by Friends
	
	
	public int getNumOfReviews() {
		return numOfReviews;
	}

	public void setNumOfReviews(int numOfReviews) {
		this.numOfReviews = numOfReviews;
	}

	public long getFriendRating() {
		return friendRating;
	}

	public void setFriendRating(long friendRating) {
		this.friendRating = friendRating;
	}

	public String getWeb_map() {
		return web_map;
	}

	public String getAddress() {
		return address;
	}

	public String getPhone() {
		return phone;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getGoogle_rating() {
		return google_rating;
	}

	public int getPrice_level() {
		return price_level;
	}

	public String getWebsite() {
		return website;
	}

	public boolean isOpen_now() {
		return open_now;
	}

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
	
	public static Restaurant fromJSONDetail(JSONObject jsonObject) {
		Restaurant restaurant = new Restaurant();
		try {
			restaurant.place_id = jsonObject.getString("place_id"); // place_id
			restaurant.name = jsonObject.getString("name");			// name
			restaurant.address = jsonObject.getString("formatted_address");		// formatted_address
			restaurant.phone = jsonObject.getString("formatted_phone_number");	// formatted_phone_number
			//restaurant.latitude = Double.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lat"));		// geometry:location:lat
			//restaurant.longitude = Double.parseDouble(jsonObject.getJSONObject("geometry").getJSONObject("location").getString("lng"));	// geometry:location:lng
			restaurant.google_rating = Double.parseDouble(jsonObject.getString("rating"));	// rating
			restaurant.price_level  = Integer.parseInt(jsonObject.getString("price_level"));		// price_level
			restaurant.website = jsonObject.getString("website");		// website
			restaurant.web_map = jsonObject.getString("url");		// web_map
			restaurant.open_now = Boolean.parseBoolean(jsonObject.getJSONObject("opening_hours").getString("open_now"));		// opening_hours:open_now
			
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
